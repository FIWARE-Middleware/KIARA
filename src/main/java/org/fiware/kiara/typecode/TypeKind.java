package org.fiware.kiara.typecode;

public enum TypeKind {
    BOOLEAN_TYPE,
    BYTE_TYPE,
    INT_16_TYPE,
    UINT_16_TYPE,
    INT_32_TYPE,
    UINT_32_TYPE,
    INT_64_TYPE,
    UINT_64_TYPE,
    FLOAT_32_TYPE,
    FLOAT_64_TYPE,
    CHAR_8_TYPE,
    STRING_TYPE,
    
    ARRAY_TYPE,
    LIST_TYPE,
    MAP_TYPE,
    SET_TYPE,
    
    ENUM_TYPE,
    UNION_TYPE,
    STRUCT_TYPE,
    EXCEPTION_TYPE,
    
    NULL_TYPE,
    
    SERVICE_TYPE,
    FUNCTION_TYPE,
}
