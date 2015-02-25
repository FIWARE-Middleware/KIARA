/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fiware.kiara.serialization.impl;

import com.google.common.primitives.Bytes;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 * @param <T>
 */
public class Encryptor<T> implements Serializer<T> {

    private final Serializer<T> serializer;
    private final String keyName;
    private final byte[] keyNameBytes;
    private CipherProvider cipherProvider;

    public Encryptor(Serializer<T> serializer, String keyName, CipherProvider cipherProvider) {
        if (serializer == null) {
            throw new NullPointerException("serializer");
        }
        this.serializer = serializer;
        this.keyName = keyName;
        this.keyNameBytes = keyName != null ? keyName.getBytes(StandardCharsets.UTF_8) : null;
        this.cipherProvider = cipherProvider;

        // FIXME check if in keyNameBytes are no zeros ?
    }

    @Override
    public void write(SerializerImpl impl, BinaryOutputStream message, String name, T object) throws IOException {
        BinaryOutputStream encryptionStream = new BinaryOutputStream();
        serializer.write(impl, encryptionStream, name, object);
        encryptionStream.flush();

        byte[] encryptionBuffer = encryptionStream.getBuffer();
        int encryptionBufferOffset = encryptionStream.getBufferOffset();
        int encryptionBufferLength = encryptionStream.getBufferLength();

        if (cipherProvider != null && keyName != null) {
            final Cipher cipher;
            try {
                cipher = cipherProvider.getEncryptionCipher(keyName);
            } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
                throw new IOException(ex);
            }
            if (cipher != null) {
                try {
                    encryptionBuffer = cipher.doFinal(encryptionBuffer, encryptionBufferOffset, encryptionBufferLength);
                    encryptionBufferOffset = 0;
                    encryptionBufferLength = encryptionBuffer.length;
                } catch (IllegalBlockSizeException | BadPaddingException ex) {
                    throw new IOException(ex);
                }
            }
        }

        final BinaryOutputStream encryptedData = new BinaryOutputStream((this.keyNameBytes != null ? this.keyNameBytes.length : 0) + 1 + encryptionBufferLength);
        if (keyNameBytes != null) {
            encryptedData.write(keyNameBytes);
        }
        encryptedData.write(0x0);
        encryptedData.write(encryptionBuffer, encryptionBufferOffset, encryptionBufferLength);

        impl.serializeData(message, name, encryptedData.getBuffer(), encryptedData.getBufferOffset(), encryptedData.getBufferLength());
    }

    @Override
    public T read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        byte encryptionData[] = impl.deserializeData(message, name);

        // find end of keyName: zero
        final int keyEnd = Bytes.indexOf(encryptionData, (byte) 0x0);
        if (keyEnd == -1) {
            throw new IOException("Invalid encryption data");
        }

        final String decryptionKeyName = new String(encryptionData, 0, keyEnd, StandardCharsets.UTF_8);
        final Cipher cipher;
        try {
            cipher = cipherProvider != null ? cipherProvider.getDecryptionCipher(decryptionKeyName) : null;
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            throw new IOException(ex);
        }

        byte[] decryptionBuffer = encryptionData;
        int decryptionBufferOffset = keyEnd + 1;
        int decryptionBufferLength = encryptionData.length - keyEnd - 1;

        if (cipher != null) {
            try {
                decryptionBuffer = cipher.doFinal(encryptionData, decryptionBufferOffset, decryptionBufferLength);
                decryptionBufferOffset = 0;
                decryptionBufferLength = decryptionBuffer.length;
            } catch (IllegalBlockSizeException | BadPaddingException ex) {
                throw new IOException(ex);
            }
        }

        final BinaryInputStream bin = new BinaryInputStream(decryptionBuffer, decryptionBufferOffset, decryptionBufferLength);

        return serializer.read(impl, bin, name);
    }

}
