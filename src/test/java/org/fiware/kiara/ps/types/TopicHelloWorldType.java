package org.fiware.kiara.ps.types;

import java.io.IOException;

import org.fiware.kiara.ps.oldtests.LatencyType;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.topic.TopicDataType;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;

public class TopicHelloWorldType extends TopicDataType<HelloWorld> {
    
    public TopicHelloWorldType() {
        setName("HelloWorld");
        m_typeSize = 4 + (4 + 255);
        m_isGetKeyDefined = false;
    }

    @Override
    public boolean serialize(HelloWorld data, SerializedPayload payload) {
        BinaryOutputStream bos = new BinaryOutputStream(m_typeSize);
        try {
            data.serialize(payload.getSerializer(), bos, "");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*try {
            bos.writeIntLE(data.seqnum);
            if (data.data != null) {
                bos.writeIntLE(data.data.length);
                bos.write(data.data);
            } else {
                bos.writeIntLE(0);
            }
        } catch (IOException ex) {
            return false;
        }*/
        payload.setBuffer(bos.toByteArray());
        return true;
    }

    @Override
    public HelloWorld deserialize(SerializedPayload payload) throws InstantiationException, IllegalAccessException {
        HelloWorld retVal = this.createData();
        BinaryInputStream bis = new BinaryInputStream(payload.getBuffer(), 0, payload.getLength());
        try {
            retVal.deserialize(payload.getSerializer(), bis, "");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*try {
            LatencyType data = new LatencyType();
            data.seqnum = bis.readIntLE();
            final int siz = bis.readIntLE();
            if (siz == 0) {
                data.data = null;
            } else {
                data.data = new byte[siz];
                bis.readFully(data.data);
            }
            return data;
        } catch (IOException ex) {
            return null;
        }*/
        return retVal;
    }

    @Override
    public HelloWorld createData() {
        return new HelloWorld();
    }

}
