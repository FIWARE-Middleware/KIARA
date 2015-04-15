/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fiware.kiara.typecode;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
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
    VOID_TYPE
}
