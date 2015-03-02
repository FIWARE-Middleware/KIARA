package org.fiware.kiara.dynamic.impl.data;

import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;

public class DynamicConstructedImpl extends DynamicDataImpl {

    public DynamicConstructedImpl(DataTypeDescriptor dataDescriptor, String className) {
        super(dataDescriptor, className);
    }

}
