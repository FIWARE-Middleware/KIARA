/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
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
package org.fiware.kiara.serialization;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import org.fiware.kiara.serialization.impl.ArrayAsArraySerializer;
import org.fiware.kiara.serialization.impl.ArrayAsSequenceSerializer;
import org.fiware.kiara.serialization.impl.BasicSerializers;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.impl.CipherProvider;
import org.fiware.kiara.serialization.impl.Encryptor;
import org.fiware.kiara.serialization.impl.EnumSerializer;
import org.fiware.kiara.serialization.impl.ListAsArraySerializer;
import org.fiware.kiara.serialization.impl.ListAsSequenceSerializer;
import org.fiware.kiara.serialization.impl.MapAsMapSerializer;
import org.fiware.kiara.serialization.impl.SetAsSetSerializer;
import org.fiware.kiara.util.HexDump;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class NewSerializerTest {

    private final CDRSerializer ser;

    public NewSerializerTest() {
        this.ser = new CDRSerializer();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void arraySerializationTest() throws IOException {

        // serialize long[2][3] = { {1,2,3}, {3,2,1} }
        List<List<Integer>> array = new ArrayList<>();
        array.add(Arrays.asList(1, 2, 3));
        array.add(Arrays.asList(3, 2, 1));

        org.fiware.kiara.serialization.impl.Serializer<List<List<Integer>>> s1
                = new ListAsArraySerializer<>(2, new ListAsArraySerializer<>(3, new BasicSerializers.I32Serializer()));

        BinaryOutputStream bos = new BinaryOutputStream();

        s1.write(ser, bos, "", array);

        // deserialize long[2][3]
        org.fiware.kiara.serialization.impl.Serializer<int[][]> s2 = new ArrayAsArraySerializer<>(2, int[].class, new BasicSerializers.ArrayAsI32ArraySerializer(3));

        BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
        int[][] result = s2.read(ser, bis, "");

        Assert.assertArrayEquals(new int[][]{{1, 2, 3}, {3, 2, 1}}, result);
    }

    @Test
    public void sequenceSerializationTest() throws IOException {

        // serialize list<list<long>> = { {1,2,3}, {3,2,1} }
        List<List<Integer>> array = new ArrayList<>();
        array.add(Arrays.asList(1, 2, 3));
        array.add(Arrays.asList(3, 2, 1));

        org.fiware.kiara.serialization.impl.Serializer<List<List<Integer>>> s1
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new BasicSerializers.I32Serializer()));

        BinaryOutputStream bos = new BinaryOutputStream();

        s1.write(ser, bos, "", array);

        // deserialize list<list<int>>
        org.fiware.kiara.serialization.impl.Serializer<int[][]> s2
                = new ArrayAsSequenceSerializer<>(int[].class, new BasicSerializers.ArrayAsI32SequenceSerializer());

        BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
        int[][] result = s2.read(ser, bis, "");

        Assert.assertArrayEquals(new int[][]{{1, 2, 3}, {3, 2, 1}}, result);
    }

    @Test
    public void sequenceArraySerializationTest() throws IOException {

        // serialize list<long[3]> = { {1,2,3}, {3,2,1} }
        List<List<Integer>> array = new ArrayList<>();
        array.add(Arrays.asList(1, 2, 3));
        array.add(Arrays.asList(3, 2, 1));

        org.fiware.kiara.serialization.impl.Serializer<List<List<Integer>>> s1
                = new ListAsSequenceSerializer<>(new ListAsArraySerializer<>(3, new BasicSerializers.I32Serializer()));

        BinaryOutputStream bos = new BinaryOutputStream();

        s1.write(ser, bos, "", array);

        // deserialize list<long[3]>
        org.fiware.kiara.serialization.impl.Serializer<int[][]> s2
                = new ArrayAsSequenceSerializer<>(int[].class, new BasicSerializers.ArrayAsI32ArraySerializer(3));

        BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
        int[][] result = s2.read(ser, bis, "");

        Assert.assertArrayEquals(new int[][]{{1, 2, 3}, {3, 2, 1}}, result);
    }

    @Test
    public void setSerializationTest() throws IOException {

        // serialize set<int>> = {1,2,3,5,8}
        Set<Integer> array = new HashSet<>();
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(5);
        array.add(8);

        org.fiware.kiara.serialization.impl.Serializer<Set<Integer>> s1
                = new SetAsSetSerializer<>(new BasicSerializers.I32Serializer());

        BinaryOutputStream bos = new BinaryOutputStream();

        s1.write(ser, bos, "", array);

        BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
        Set<Integer> result = ser.deserializeSetI32(bis, "", 1);

        Assert.assertEquals(array, result);
    }

    @Test
    public void mapSerializationTest() throws IOException {
        // serialize map<int, string> = { (1, "a"), (2, "b") (5, "d") , (8, "x")}
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "a");
        map.put(2, "b");
        map.put(5, "d");
        map.put(8, "x");

        org.fiware.kiara.serialization.impl.Serializer<Map<Integer, String>> s1
                = new MapAsMapSerializer<>(new BasicSerializers.I32Serializer(), new BasicSerializers.StringSerializer());

        BinaryOutputStream bos = new BinaryOutputStream();

        s1.write(ser, bos, "", map);

        BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());

        Object result = s1.read(ser, bis, "");

        Assert.assertEquals(map, result);
    }

    private static enum TestEnum {

        ONE, TWO, THREE
    }

    @Test
    public void enumSerializationTest() throws IOException {
        List<TestEnum> enumList = new ArrayList<>();
        enumList.add(TestEnum.ONE);
        enumList.add(TestEnum.THREE);
        enumList.add(TestEnum.TWO);

        org.fiware.kiara.serialization.impl.Serializer<List<TestEnum>> s1
                = new ListAsSequenceSerializer<>(new EnumSerializer<>(TestEnum.class));

        BinaryOutputStream bos = new BinaryOutputStream();

        s1.write(ser, bos, "", enumList);

        BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());

        Object result = s1.read(ser, bis, "");

        Assert.assertEquals(enumList, result);
    }

    private static class TestCipherProvider implements CipherProvider {

        private static SecretKeyFactory factory = null;
        private static SecretKey defaultKey = null;
        private static IvParameterSpec defaultIV = null;
        private static SecureRandom sr = null;

        static {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                sr = SecureRandom.getInstance("SHA1PRNG");
                sr.setSeed("default".getBytes());
                kgen.init(128, sr);
                defaultKey = kgen.generateKey();
                defaultIV = new IvParameterSpec(sr.generateSeed(16));
            } catch (NoSuchAlgorithmException ex) {
            }
        }

        @Override
        public Cipher getEncryptionCipher(String keyName) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
            if ("default".equals(keyName)) {
                /* Encrypt the message. */
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, defaultKey, defaultIV);
                return cipher;
            }
            return null;
        }

        @Override
        public Cipher getDecryptionCipher(String keyName) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
            if ("default".equals(keyName)) {
                /* Encrypt the message. */
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, defaultKey, defaultIV);
                return cipher;
            }
            return null;
        }

    }

    @Test
    public void encryptionSerializationTest() throws IOException {
        CipherProvider cipherProvider = new TestCipherProvider();

        String[] keys = {null, "default"};
        for (String key : keys) {
            org.fiware.kiara.serialization.impl.Serializer<String> s1 = new Encryptor<>(new BasicSerializers.StringSerializer(), key, cipherProvider);
            BinaryOutputStream bos = new BinaryOutputStream();
            final String str = "TEST1234";
            s1.write(ser, bos, "", str);

            System.err.println(HexDump.dumpHexString(bos.getByteBuffer()));

            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());

            Object result = s1.read(ser, bis, "");

            Assert.assertEquals(str, result);
        }
    }

    @Test
    public void encryptionSerializationTest2() throws IOException {
        CipherProvider cipherProvider = new TestCipherProvider();

        String[] keys = {null, "default"};
        for (String key : keys) {

            Map<Integer, String> map = new HashMap<>();
            map.put(1, "a");
            map.put(2, "b");
            map.put(5, "d");
            map.put(8, "x");

            org.fiware.kiara.serialization.impl.Serializer<Map<Integer, String>> s1
                    = new Encryptor<>(new MapAsMapSerializer<>(
                                    new BasicSerializers.I32Serializer(),
                                    new BasicSerializers.StringSerializer()),
                            key, cipherProvider);

            BinaryOutputStream bos = new BinaryOutputStream();

            s1.write(ser, bos, "", map);

            System.err.println(HexDump.dumpHexString(bos.getByteBuffer()));

            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());

            Object result = s1.read(ser, bis, "");

            Assert.assertEquals(map, result);
        }
    }

}
