package org.fiware.kiara.ps.participant;

import java.io.IOException;

import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.topic.TopicDataTypeOld;
import org.fiware.kiara.ps.topic.TopicDataType;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class HelloWorldType extends TopicDataType<HelloWorld> {
    
    public HelloWorldType() {
        this.m_typeSize = HelloWorld.getMaxSize();
        this.m_isGetKeyDefined = false;
        super.setName("HelloWorld");
    }

    @Override
    public boolean serialize(HelloWorld data, SerializedPayload payload) {
        /*CDRSerializer ser = new CDRSerializer();
        
        BinaryOutputStream bos = new BinaryOutputStream();
        data.serialize(impl, message, name);*/
        
        return true;
    }

    @Override
    public boolean deserialize(SerializedPayload payload, HelloWorld data) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public HelloWorld createData() {
        // TODO Auto-generated method stub
        return null;
    }

    
}
