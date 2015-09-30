/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
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
package org.fiware.kiara.ps.rtps.messages;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;





//import org.fiware.kiara.ps.rtps.BinaryInputStream;
//import org.fiware.kiara.ps.rtps.BinaryOutputStream;
//import org.fiware.kiara.ps.rtps.CDRSerializer;


import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;

/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class RTPSMessage {

    public static final short RTPS_MESSAGE_HEADER_SIZE = 20;
    public static final short RTPSMESSAGE_DEFAULT_SIZE = 10500;
    public static final short RTPSMESSAGE_COMMON_RTPS_PAYLOAD_SIZE = 500;
    public static final short OCTETSTOINLINEQOS_DATASUBMSG = 16;
    public static final short DATA_EXTRA_INLINEQOS_SIZE = 4;
    public static final short DATA_EXTRA_ENCODING_SIZE = 4;

    private RTPSMessageHeader m_header;

    private ArrayList<RTPSSubmessage> m_submessages;

    private byte[] m_buffer;
    private BinaryOutputStream m_bos;
    private BinaryInputStream m_bis;
    private CDRSerializer m_ser;
    //private int m_pos;
    private RTPSEndian m_endian;
    
    private int m_maxSize;
    
    private int initSubMsgPosition = -1;

    public RTPSMessage(int payloadSize, RTPSEndian endian) { // TODO Endian should be chosen from the user's side

        this.m_submessages = new ArrayList<RTPSSubmessage>();

        this.m_endian = endian; 

        this.m_buffer = new byte[payloadSize];

        this.m_bos = new BinaryOutputStream(payloadSize);
        //this.m_bos.setBuffer(this.m_buffer);

        this.m_bis = new BinaryInputStream(m_buffer);
        
        this.m_maxSize = payloadSize;

        this.m_ser = new CDRSerializer((this.m_endian == RTPSEndian.LITTLE_ENDIAN) ? true : false);

        //this.m_pos = 0;
    }

    public boolean setHeader(RTPSMessageHeader header) {
        if (this.m_header != null) {
            return false;
        }
        this.m_header = header;
        return true;
    }

    public void serialize() {
        /*try {
            this.m_header.serialize(this.m_ser, this.m_bos, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterator<RTPSSubmessage> it = this.m_submessages.iterator();
        while (it.hasNext()) {
            RTPSSubmessage subMsg = it.next();
            if (it.hasNext()) {
                subMsg.serialize(this.m_ser, this.m_bos, false);
            } else {
                subMsg.serialize(this.m_ser, this.m_bos, true);
            }
        }*/

        //this.m_pos = this.m_bos.getPosition();
        this.m_buffer = this.m_bos.toByteArray();
        this.m_bis.setBuffer(this.m_buffer);

    }
    
    // NEW
    public void checkPadding(/*CDRSerializer ser, BinaryOutputStream bos, boolean isLast*//*boolean dataMsg*/boolean isData) {

        try {

            int finalPos = this.m_bos.getPosition();
            short size = (short) (finalPos - (initSubMsgPosition + 4));

            if (/*!isLast && */(size % 4 != 0)) {
                short diff = (short) (4 - (size % 4));
                size = (short) (size + diff);
                this.m_ser.addPadding(this.m_bos, diff);
            } else if (isData && size < 24) {
                short diff = (short) (24 - size);
                size = 24;
                this.m_ser.addPadding(this.m_bos, diff); 
            }

            byte newBuffer[] = new byte[2];
            BinaryOutputStream newBos = new BinaryOutputStream();
            newBos.setBuffer(newBuffer);

            this.m_ser.serializeI16(newBos, "", size);
            System.arraycopy(newBuffer, 0, this.m_bos.getBuffer(), initSubMsgPosition + 2, 2);
            this.m_submessages.get(this.m_submessages.size()-1).getSubmessageHeader().setOctectsToNextHeader(size);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    // NEW

    @Override
    public boolean equals(Object other) {
        if (other instanceof RTPSMessage) {
            RTPSMessage instance = (RTPSMessage) other;
            boolean retVal = true;
            if (this.m_header != null) {
                retVal &= this.m_header.equals(instance.m_header);
            }
            retVal &= this.m_submessages.equals(instance.m_submessages);

            return retVal;
        }
        return false;
    }

    public CDRSerializer getSerializer() {
        return this.m_ser;
    }

    public BinaryInputStream getBinaryInputStream() {
        return this.m_bis;
    }
    
    public BinaryOutputStream getBinaryOutputStream() {
        return this.m_bos;
    }

    public void initBinaryOutputStream() {
        this.m_bis.setBuffer(this.m_buffer);
    }

    public byte[] getBuffer() {
        return this.m_buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.m_buffer = buffer;
    }

    public void setBuffer(byte[] buffer, int payloadSize) {
        this.m_buffer = new byte[payloadSize];
        System.arraycopy(buffer, 0, this.m_buffer, 0, payloadSize);
        //this.m_buffer = buffer;
    }

    public int getSize() {
        if (this.m_buffer != null) {
            return this.m_buffer.length;
        }
        return -1;
    }

    public RTPSEndian getEndiannes() {
        return this.m_endian;
    }

    public void setEndiannes(RTPSEndian endian) {
        if (this.m_endian != endian) {
            this.m_endian = endian;
            this.m_ser = new CDRSerializer((this.m_endian == RTPSEndian.LITTLE_ENDIAN) ? true : false);
        }
    }

    public void addSubmessage(RTPSSubmessage submessage) {
        this.m_submessages.add(submessage);
        initSubMsgPosition = this.m_bos.getPosition();
    }

    public void clearSubmessages() {
        this.m_submessages.clear();
    }
    
    public int getMaxSize() {
        return this.m_maxSize;
    }

}
