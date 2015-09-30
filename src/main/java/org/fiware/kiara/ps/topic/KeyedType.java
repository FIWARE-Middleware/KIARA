package org.fiware.kiara.ps.topic;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public interface KeyedType {
    
    public void serializeKey(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException;

}
