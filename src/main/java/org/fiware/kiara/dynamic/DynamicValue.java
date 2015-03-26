package org.fiware.kiara.dynamic;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.typecode.TypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public interface DynamicValue {
    
    public TypeDescriptor getTypeDescriptor();
    
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException;
    
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException;

}
