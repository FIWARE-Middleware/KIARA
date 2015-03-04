package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;

public class DynamicContainerImpl extends DynamicConstructedImpl {
    
    protected DynamicData m_contentType;
    protected ArrayList<DynamicData> m_members;
    
    public DynamicContainerImpl(DataTypeDescriptor dataDescriptor, String className) {
        super(dataDescriptor, className);
    }
    
    public DynamicData getContentType() {
        return this.m_contentType;
    }
    
    public void setContentType(DynamicData contentType) {
        this.m_contentType = contentType;
    }
    
    

}
