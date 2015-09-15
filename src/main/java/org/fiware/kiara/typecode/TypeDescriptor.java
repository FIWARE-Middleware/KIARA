package org.fiware.kiara.typecode;

/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
*/
public interface TypeDescriptor {

    /*
     * Public Methods
     */

    /**
     * Function that returns the {@link TypeKind} of a {@link TypeDescriptor} object.
     *
     * @return type kind
     * @see TypeKind
     */
    public TypeKind getKind();

    /**
     * This function returns true if and only if the {@link TypeDescriptor} represented
     * by the object in which is invoked describes a data type. Functions and
     * services are not considered data types.
     *
     * @return true if this type is data type
     */
    public boolean isData();

    /**
     * Function used to know if a {@link TypeCode} object is a description of a
     * primitive data type.
     *
     * @return true if this type is primitive type
     */
    public boolean isPrimitive();

    /**
     * This function returns true if the {@link TypeDescriptor} object represents a void
     * data type.
     *
     * @return true if type is void type
     */
    public boolean isVoid();

    // ---------------------- Container -----------------------

    /**
     * This function can be used to check if a {@link TypeDescriptor} object is
     * representing a container type. The types considered as container data
     * types are arrays, lists, sets and maps.
     *
     * @return true if this type is container type
     */
    public boolean isContainer();

    /**
     * Function used to know if a {@link TypeDescriptor} object is a description of an
     * array data type.
     *
     * @return true if this type is array type
     */
    public boolean isArray();

    /**
     * Function used to know if a {@link TypeDescriptor} object is a description of a
     * list data type.
     *
     * @return true if this type is list type
     */
    public boolean isList();

    /**
     * Function used to know if a {@link TypeDescriptor} object is a description of a
     * map data type.
     *
     * @return true if this type is map type
     */
    public boolean isMap();

    /**
     * Function used to know if a {@link TypeDescriptor} object is a description of a
     * set data type.
     *
     * @return true if this type is set type
     */
    public boolean isSet();

    // ----------------------- Membered -----------------------

    /**
     * This function is used to know if a {@link TypeDescriptor} object is
     * a description of a membered data type.
     * Membered types are structs, enumerations, unions and exceptions.
     * @return true if this type is membered
     */
    public boolean isMembered();

    /**
     * Function used to know if a {@link TypeDescriptor} object is a description of
     * an enumeration data type.
     * @return true if this type is enum type
     */
    public boolean isEnum();

    /**
     * Function used to know if a {@link TypeDescriptor} object is a description of
     * a union data type.
     * @return true if this type is union type
     */
    public boolean isUnion();

    /**
     * Function used to know if a {@link TypeDescriptor} object is a description of
     * a struct data type.
     * @return true if this type is struct type
     */
    public boolean isStruct();

    /**
     * Function used to know if a {@link TypeDescriptor} object is a description of
     * an exception data type.
     * @return true if this type is exception type
     */
    public boolean isException();

    // --------------------- Service ------------------------------

    /**
     * Function used to know if a {@link TypeDescriptor} object is a description of
     * a service.
     * @return true if this type is service type
     */
    public boolean isService();

    // --------------------- Function ------------------------------

    /**
     * Function used to know if a TypeDescriptor object is a description of
     * a function.
     * @return true if this type is function type
     */
    public boolean isFunction();

}
