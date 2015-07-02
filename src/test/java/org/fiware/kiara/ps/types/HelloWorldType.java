package org.fiware.kiara.ps.types;

import java.io.IOException;

import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.topic.SerializableDataType;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;

public class HelloWorldType extends SerializableDataType<HelloWorld> {

    public HelloWorldType() {
        super(HelloWorld.class, "HelloWorld", HelloWorld.getMaxSize(), false);
    }

    /*@Override
    public boolean serialize(HelloWorld data, SerializedPayload payload) {
        CDRSerializer ser = new CDRSerializer();
        
        BinaryOutputStream bos = new BinaryOutputStream();
        payload.setData(data);
        try {
            payload.serialize(ser, bos, getName());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }*/
    
   /*@Override
    public boolean deserialize(SerializedPayload payload, HelloWorld data) {
        // TODO Auto-generated method stub
        return false;
    }*/

    @Override
    public HelloWorld createData() {
        return new HelloWorld();
    }

    
}
