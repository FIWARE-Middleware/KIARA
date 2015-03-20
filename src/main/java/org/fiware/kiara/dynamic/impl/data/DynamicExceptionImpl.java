package org.fiware.kiara.dynamic.impl.data;

import java.io.IOException;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicException;
import org.fiware.kiara.dynamic.data.DynamicMember;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.typecode.data.ExceptionTypeDescriptor;

public class DynamicExceptionImpl extends DynamicMemberedImpl implements DynamicException {

    public DynamicExceptionImpl(ExceptionTypeDescriptor exceptionDescriptor) {
        super(exceptionDescriptor, "DynamicExceptionImpl");
    }
    
    @Override
    public DynamicData getMember(String name) {
        for (DynamicMember member : this.m_members) {
            if (member.getName().equals(name)) {
                return member.getDynamicData();
            }
        }
        return null;
    }
    
    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject instanceof DynamicException) {
            boolean isEquals = true;
            for (int i=0; i < m_members.size(); ++i) {
                isEquals = isEquals & ((DynamicExceptionImpl) anotherObject).m_members.get(i).equals(this.m_members.get(i));
                if (!isEquals) {
                    return isEquals;
                }
            }
            return isEquals;
        }
        return false;
    }
    
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        for (DynamicMember m : this.m_members) {
            m.getDynamicData().serialize(impl, message, name);
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        for (DynamicMember m : this.m_members) {
            m.getDynamicData().deserialize(impl, message, name);
        }
    }

}
