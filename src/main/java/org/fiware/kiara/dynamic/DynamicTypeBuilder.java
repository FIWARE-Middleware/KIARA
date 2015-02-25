package org.fiware.kiara.dynamic;

import org.fiware.kiara.dynamic.impl.services.DynamicFunction;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.impl.services.FunctionTypeDescriptor;

public interface DynamicTypeBuilder {
    
    public DynamicData createData(DataTypeDescriptor dataDescriptor);
    
    public DynamicFunction createFunction(FunctionTypeDescriptor functionDescriptor);

}
