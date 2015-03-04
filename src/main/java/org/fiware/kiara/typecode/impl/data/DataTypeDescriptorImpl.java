package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.impl.TypeDescriptorImpl;

/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class DataTypeDescriptorImpl extends TypeDescriptorImpl implements DataTypeDescriptor {
    
    /*
     * Public Methods
     */
    
    public DataTypeDescriptorImpl(TypeKind kind/*, String name*/) {
        super(kind);
    }
    
    
}
