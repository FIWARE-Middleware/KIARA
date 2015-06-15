package org.fiware.kiara.ps.topic;

import java.io.IOException;

import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public abstract class TopicDataTypeOld implements Serializable {
    
    public int typeSize;
    
    public boolean isKeyDefined;
    
    private String m_topicDataTypeName;
    
    public TopicDataTypeOld() {
        this.typeSize = 0;
        this.isKeyDefined = false;
    }
    
    public abstract void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException;

    public abstract void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException;
    
    public abstract Object createData();
    
    public abstract void deleteData();
    
    public abstract InstanceHandle getKey();
    
    public void setName(String name) {
        this.m_topicDataTypeName = name;
    }
    
    public String getName() {
        return this.m_topicDataTypeName;
    }

}
