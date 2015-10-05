package org.fiware.kiara.dynamic;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.typecode.TypeDescriptor;

/**
 * Interface that acts as a supertype for every dynamic value that can be
 * managed. Every DynamicValue object is defined by using a TypeDescriptor which
 * is used to describe the data. It defines the common serialization functions
 * as well as a function to retrieve the TypeDescriptor object it was created
 * from.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface DynamicValue {

    /**
     * This function returns the TypeDescriptor used when creating the
     * DynamicValue object.
     *
     * @return type descriptor
     * @see TypeDescriptor
     */
    public TypeDescriptor getTypeDescriptor();

    /**
     * This function serializes the content of the DynamicValue object inside a
     * BinaryOutputStream message.
     *
     * @param impl serializer implementation
     * @param message message output stream
     * @param name
     * @throws IOException
     */
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException;

    /**
     * This function deserializes the content of a BinaryInputStream message
     * into a DynamicValue object.
     *
     * @param impl serializer implementation
     * @param message message input stream
     * @param name
     * @throws IOException
     */
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException;

}
