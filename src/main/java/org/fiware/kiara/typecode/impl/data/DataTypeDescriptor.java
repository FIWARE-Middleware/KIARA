package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.impl.TypeDescriptorImpl;

/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class DataTypeDescriptor extends TypeDescriptorImpl{
    
    /*
     * Public Methods
     */
    
    public DataTypeDescriptor(TypeKind kind, String name) {
        super(kind, name);
    }
    
    
}
