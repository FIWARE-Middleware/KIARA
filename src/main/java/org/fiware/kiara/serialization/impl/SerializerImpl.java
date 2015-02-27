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
package org.fiware.kiara.serialization.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Set;

import org.fiware.kiara.serialization.Serializer;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public interface SerializerImpl extends Serializer {

    public Object getNewMessageId();

    public void serializeMessageId(BinaryOutputStream message, Object messageId) throws IOException;

    public Object deserializeMessageId(BinaryInputStream message) throws IOException;

    public boolean equalMessageIds(Object id1, Object id2);

    /*
     * Services
     */

    public void serializeService(BinaryOutputStream message, String service) throws IOException;

    public String deserializeService(BinaryInputStream message) throws IOException;

    /*
     * Operations
     */

    public void serializeOperation(BinaryOutputStream message, String operation) throws IOException;

    public String deserializeOperation(BinaryInputStream message) throws IOException;

    /*
     * Basic Types
     */

    public void serializeChar(BinaryOutputStream message, String name, char value) throws IOException;

    public char deserializeChar(BinaryInputStream message, String name) throws IOException;

    public void serializeByte(BinaryOutputStream message, String name, byte value) throws IOException;

    public byte deserializeByte(BinaryInputStream message, String name) throws IOException;


    public void serializeI16(BinaryOutputStream message, String name, short value) throws IOException;

    public short deserializeI16(BinaryInputStream message, String name) throws IOException;

    public void serializeUI16(BinaryOutputStream message, String name, short value) throws IOException;

    public short deserializeUI16(BinaryInputStream message, String name) throws IOException;


    public void serializeI32(BinaryOutputStream message, String name, int value) throws IOException;

    public int deserializeI32(BinaryInputStream message, String name) throws IOException;

    public void serializeUI32(BinaryOutputStream message, String name, int value) throws IOException;

    public int deserializeUI32(BinaryInputStream message, String name) throws IOException;


    public void serializeI64(BinaryOutputStream message, String name, long value) throws IOException;

    public long deserializeI64(BinaryInputStream message, String name) throws IOException;

    public void serializeUI64(BinaryOutputStream message, String name, long value) throws IOException;

    public long deserializeUI64(BinaryInputStream message, String name) throws IOException;


    public void serializeFloat32(BinaryOutputStream message, String name, float value) throws IOException;

    public float deserializeFloat32(BinaryInputStream message, String name) throws IOException;

    public void serializeFloat64(BinaryOutputStream message, String name, double value) throws IOException;

    public double deserializeFloat64(BinaryInputStream message, String name) throws IOException;


    public void serializeBoolean(BinaryOutputStream message, String name, boolean value) throws IOException;

    public boolean deserializeBoolean(BinaryInputStream message, String name) throws IOException;


    public void serializeString(BinaryOutputStream message, String name, String data) throws IOException;

    public String deserializeString(BinaryInputStream message, String name) throws IOException;

    public void serializeData(BinaryOutputStream message, String name, byte [] data, int offset, int length) throws IOException;

    public byte[] deserializeData(BinaryInputStream message, String name) throws IOException;

    /*
     * Generic types
     */

    public <T extends Serializable> void serialize(BinaryOutputStream message, String name, T value) throws IOException;

    public <T extends Serializable> T deserialize(BinaryInputStream message, String name, Class<T> example) throws InstantiationException, IllegalAccessException,  IOException;

    /*
     * Arrays
     */

    @Deprecated
    public <T> void serializeArrayChar(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeArrayChar(BinaryInputStream message, String name, int... dims) throws IOException;

    @Deprecated
    public <T> void serializeArrayByte(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeArrayByte(BinaryInputStream message, String name, int... dims) throws IOException;

    @Deprecated
    public <T> void serializeArrayI16(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeArrayI16(BinaryInputStream message, String name, int... dims) throws IOException;

    @Deprecated
    public <T> void serializeArrayUI16(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeArrayUI16(BinaryInputStream message, String name, int... dims) throws IOException;

    @Deprecated
    public <T> void serializeArrayI32(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeArrayI32(BinaryInputStream message, String name, int... dims) throws IOException;

    @Deprecated
    public <T> void serializeArrayUI32(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeArrayUI32(BinaryInputStream message, String name, int... dims) throws IOException;

    @Deprecated
    public <T> void serializeArrayI64(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeArrayI64(BinaryInputStream message, String name, int... dims) throws IOException;

    @Deprecated
    public <T> void serializeArrayUI64(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeArrayUI64(BinaryInputStream message, String name, int... dims) throws IOException;

    @Deprecated
    public <T> void serializeArrayFloat32(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeArrayFloat32(BinaryInputStream message, String name, int... dims) throws IOException;

    @Deprecated
    public <T> void serializeArrayFloat64(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeArrayFloat64(BinaryInputStream message, String name, int... dims) throws IOException;

    @Deprecated
    public <T> void serializeArrayBoolean(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeArrayBoolean(BinaryInputStream message, String name, int... dims) throws IOException;

    @Deprecated
    public <T> void serializeArrayString(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeArrayString(BinaryInputStream message, String name, int... dims) throws IOException;

    /*
     * Array of generic types
     */

    @Deprecated
    public <T> void serializeArray(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeArray(BinaryInputStream message, String name, Class<T> example, int... dims) throws InstantiationException, IllegalAccessException, IOException;

    /*
     * Sequences
     */

    @Deprecated
    public <T> void serializeSequenceChar(BinaryOutputStream message, String name, List<T> sequence) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeSequenceChar(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSequenceByte(BinaryOutputStream message, String name, List<T> sequence) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeSequenceByte(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSequenceI16(BinaryOutputStream message, String name, List<T> sequence) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeSequenceI16(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSequenceUI16(BinaryOutputStream message, String name, List<T> sequence) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeSequenceUI16(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSequenceI32(BinaryOutputStream message, String name, List<T> sequence) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeSequenceI32(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSequenceUI32(BinaryOutputStream message, String name, List<T> sequence) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeSequenceUI32(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSequenceI64(BinaryOutputStream message, String name, List<T> sequence) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeSequenceI64(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSequenceUI64(BinaryOutputStream message, String name, List<T> sequence) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeSequenceUI64(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSequenceFloat32(BinaryOutputStream message, String name, List<T> sequence) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeSequenceFloat32(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSequenceFloat64(BinaryOutputStream message, String name, List<T> sequence) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeSequenceFloat64(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSequenceBoolean(BinaryOutputStream message, String name, List<T> sequence) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeSequenceBoolean(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSequenceString(BinaryOutputStream message, String name, List<T> sequence) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeSequenceString(BinaryInputStream message, String name, int depth) throws IOException;

    /*
     * Sequences of generic types
     */

    @Deprecated
    public <T> void serializeSequence(BinaryOutputStream message, String name, List<T> sequence) throws IOException;

    @Deprecated
    public <T, M> List<M> deserializeSequence(BinaryInputStream message, String name, Class<T> example, int depth) throws InstantiationException, IllegalAccessException, IOException;

    /*
     * Extra array functions
     */

    public void serializeArrayBegin(BinaryOutputStream message, String name, int length) throws IOException;

    public void serializeArrayEnd(BinaryOutputStream message, String name) throws IOException;

    public int deserializeArrayBegin(BinaryInputStream message, String name) throws IOException;

    public void deserializeArrayEnd(BinaryInputStream message, String name) throws IOException;

    /*
     * Extra struct functions
     */

    public void serializeStructBegin(BinaryOutputStream message, String name) throws IOException;

    public void serializeStructEnd(BinaryOutputStream message, String name) throws IOException;

    public int deserializeStructBegin(BinaryInputStream message, String name) throws IOException;

    public void deserializeStructEnd(BinaryInputStream message, String name) throws IOException;

    /*
     * Extra sequence functions
     */

    public void serializeSequenceBegin(BinaryOutputStream message, String name) throws IOException;

    public void serializeSequenceEnd(BinaryOutputStream message, String name) throws IOException;

    public int deserializeSequenceBegin(BinaryInputStream message, String name) throws IOException;

    public void deserializeSequenceEnd(BinaryInputStream message, String name) throws IOException;

    /*
     * Extra union functions
     */

    public void serializeUnionBegin(BinaryOutputStream message, String name) throws IOException;

    public void serializeUnionEnd(BinaryOutputStream message, String name) throws IOException;

    public int deserializeUnionBegin(BinaryInputStream message, String name) throws IOException;

    public void deserializeUnionEnd(BinaryInputStream message, String name) throws IOException;

    
    /*
     * Enum types
     */

    public <E extends Enum> void serializeEnum(BinaryOutputStream message, String name, E value) throws IOException;
    
    public <E extends Enum> E deserializeEnum(BinaryInputStream message,String name, Class<E> example) throws IOException;
    
    /*public <E extends Enum> void serializeArrayEnum(TransportMessage message, String name, List<E> array, int... dims);
    
    public <E, M> List<M> deserializeArrayEnum(TransportMessage message, String name, Class<E> example, int... dims);
    
    public <E extends Enum> void serializeSequenceEnum(TransportMessage message, String name, List<E> sequence);
    
    public <E, M> List<M> deserializeSequenceEnum(TransportMessage message, String name, Class<E> example, int depth);*/
    
    /*
     * Sets
     */

    @Deprecated
    public <T> void serializeSetChar(BinaryOutputStream message, String name, Set<T> set) throws IOException;

    @Deprecated
    public <T, M> Set<M> deserializeSetChar(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSetByte(BinaryOutputStream message, String name, Set<T> set) throws IOException;

    @Deprecated
    public <T, M> Set<M> deserializeSetByte(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSetI16(BinaryOutputStream message, String name, Set<T> set) throws IOException;

    @Deprecated
    public <T, M> Set<M> deserializeSetI16(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSetUI16(BinaryOutputStream message, String name, Set<T> set) throws IOException;

    @Deprecated
    public <T, M> Set<M> deserializeSetUI16(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSetI32(BinaryOutputStream message, String name, Set<T> set) throws IOException;

    @Deprecated
    public <T, M> Set<M> deserializeSetI32(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSetUI32(BinaryOutputStream message, String name, Set<T> set) throws IOException;

    @Deprecated
    public <T, M> Set<M> deserializeSetUI32(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSetI64(BinaryOutputStream message, String name, Set<T> set) throws IOException;

    @Deprecated
    public <T, M> Set<M> deserializeSetI64(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSetUI64(BinaryOutputStream message, String name, Set<T> set) throws IOException;

    @Deprecated
    public <T, M> Set<M> deserializeSetUI64(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSetFloat32(BinaryOutputStream message, String name, Set<T> set) throws IOException;

    @Deprecated
    public <T, M> Set<M> deserializeSetFloat32(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSetFloat64(BinaryOutputStream message, String name, Set<T> set) throws IOException;

    @Deprecated
    public <T, M> Set<M> deserializeSetFloat64(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSetBoolean(BinaryOutputStream message, String name, Set<T> set) throws IOException;

    @Deprecated
    public <T, M> Set<M> deserializeSetBoolean(BinaryInputStream message, String name, int depth) throws IOException;

    @Deprecated
    public <T> void serializeSetString(BinaryOutputStream message, String name, Set<T> set) throws IOException;

    @Deprecated
    public <T, M> Set<M> deserializeSetString(BinaryInputStream message, String name, int depth) throws IOException;

    /*
     * Sets of generic types
     */
    @Deprecated
    public <T> void serializeSet(BinaryOutputStream message, String name, Set<T> set) throws IOException;

    @Deprecated
    public <T, M> Set<M> deserializeSet(BinaryInputStream message, String name, Class<T> example, int depth) throws IOException, InstantiationException, IllegalAccessException;

}
