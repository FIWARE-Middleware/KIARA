package org.fiware.kiara.dynamic.impl.data;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicException;
import org.fiware.kiara.dynamic.data.DynamicMember;
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

}
