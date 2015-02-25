package org.fiware.kiara.dynamic.impl.data;

import org.fiware.kiara.dynamic.DynamicData;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptor;

public class DynamicDataImpl extends DynamicTypeImpl implements DynamicData {
    
    public DynamicDataImpl(DataTypeDescriptor dataDescriptor, String className) {
        super(dataDescriptor, className);
    }

}
