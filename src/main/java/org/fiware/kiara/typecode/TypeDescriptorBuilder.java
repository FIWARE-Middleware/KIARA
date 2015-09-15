package org.fiware.kiara.typecode;

import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.EnumTypeDescriptor;
import org.fiware.kiara.typecode.data.ExceptionTypeDescriptor;
import org.fiware.kiara.typecode.data.ListTypeDescriptor;
import org.fiware.kiara.typecode.data.MapTypeDescriptor;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.data.SetTypeDescriptor;
import org.fiware.kiara.typecode.data.StructTypeDescriptor;
import org.fiware.kiara.typecode.data.UnionTypeDescriptor;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;
import org.fiware.kiara.typecode.services.ServiceTypeDescriptor;

/**
 * This interface defines the operations used to create type-describing objects.
 * It allows the users to create every supported data type inside Advanced
 * Middleware by acting as a single access builder.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface TypeDescriptorBuilder {

    /**
     * This function creates a new {@link DataTypeDescriptor} representing a
     * void data type..
     *
     * @return void type descriptor
     * @see DataTypeDescriptor
     */
    public DataTypeDescriptor createVoidType();

    /**
     * This function returns a new {@link PrimitiveTypeDescriptor} whose kind is
     * the same specified as a parameter.
     *
     * @param kind type kind
     * @return primitive type descriptor
     * @see PrimitiveTypeDescriptor
     */
    public PrimitiveTypeDescriptor createPrimitiveType(TypeKind kind);

    /**
     * Function that creates a new {@link ArrayTypeDescriptor} object
     * representing an array.
     *
     * @param contentDescriptor array element type descriptor
     * @param dimensions array dimensions
     * @return array type descriptor
     * @see ArrayTypeDescriptor
     */
    public ArrayTypeDescriptor createArrayType(DataTypeDescriptor contentDescriptor, int... dimensions);

    /**
     * This function creates a new {@link ListTypeDescriptor} object
     * representing a list of objects.
     *
     * @param contentDescriptor list element type descriptor
     * @param maxSize maximal list size
     * @return list type descriptor
     * @see ListTypeDescriptor
     */
    public ListTypeDescriptor createListType(DataTypeDescriptor contentDescriptor, int maxSize);

    /**
     * Function that creates a new {@link SetTypeDescriptor} object representing
     * a set. A set is defined as a list with no repeated objects.
     *
     * @param contentDescriptor set element type descriptor
     * @param maxSize maximal set size
     * @return set type descriptor
     * @see SetTypeDescriptor
     */
    public SetTypeDescriptor createSetType(DataTypeDescriptor contentDescriptor, int maxSize);

    /**
     * This function is used to create a {@link MapTypeDescriptor} object that
     * represents a map data type.
     *
     * @param keyDescriptor map key type descriptor
     * @param valueDescriptor map value type descriptor
     * @param maxSize maximal map size
     * @return map type descriptor
     * @see MapTypeDescriptor
     */
    public MapTypeDescriptor createMapType(DataTypeDescriptor keyDescriptor, DataTypeDescriptor valueDescriptor, int maxSize);

    /**
     * This function creates a new {@link StructTypeDescriptor} object
     * representing a struct data type.
     *
     * @param name name of the struct
     * @return struct type descriptor
     * @see StructTypeDescriptor
     */
    public StructTypeDescriptor createStructType(String name);

    /**
     * Function that creates a new EnumTypeDescriptor object representing an
     * enumeration.
     *
     * @param name name of the enum
     * @param values enum value names
     * @return enum type descriptor
     * @see EnumTypeDescriptor
     */
    public EnumTypeDescriptor createEnumType(String name, String... values);

    /**
     * This function can be used to create a new UnionTypeDescriptor
     * that represents a union data type.
     * @param name name of the union
     * @param discriminatorDescriptor union discriminator type descriptor
     * @return union type descriptor
     * @see UnionTypeDescriptor
     */
    public UnionTypeDescriptor createUnionType(String name, DataTypeDescriptor discriminatorDescriptor);

    /**
     * Function that creates a new ExceptionTypeDescriptor used to represent
     * an exception data type.
     * @param name name of the exception
     * @return exception type descriptor
     * @see ExceptionTypeDescriptor
     */
    public ExceptionTypeDescriptor createExceptionType(String name);

    /**
     * This function can be used to create a new FunctionTypeDescriptor
     * representing a Remote Procedure Call (RPC).
     * @param name name of the function
     * @return function type descriptor
     * @see FunctionTypeDescriptor
     */
    public FunctionTypeDescriptor createFunctionType(String name);

    /**
     * Function that creates a new ServiceTypeDescriptor object used to
     * represent a service defined in the server?s side.
     * @param name short name of the service type
     * @param scopedName full name of the service type
     * @return service type descriptor
     * @see ServiceTypeDescriptor
     */
    public ServiceTypeDescriptor createServiceType(String name, String scopedName);

}
