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

import java.nio.ByteBuffer;
import java.util.List;

import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.transport.impl.TransportMessage;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public interface SerializerImpl extends Serializer {

    public Object getNewMessageId();

    public void serializeMessageId(TransportMessage message, Object messageId);

    public Object deserializeMessageId(TransportMessage message);
    
    public void serializeMessageId(ByteBuffer buffer, Object messageId);

    public Object deserializeMessageId(ByteBuffer buffer);

    public boolean equalMessageIds(Object id1, Object id2);

    /*
     * Services
     */

    public void serializeService(TransportMessage message, String service);

    public String deserializeService(TransportMessage message);

    /*
     * Operations
     */

    public void serializeOperation(TransportMessage message, String operation);

    public String deserializeOperation(TransportMessage message);

    /*
     * Basic Types
     */

    public void serializeChar(TransportMessage message, String name, char value);

    public char deserializeChar(TransportMessage message, String name);

    public void serializeByte(TransportMessage message, String name, byte value);

    public byte deserializeByte(TransportMessage message, String name);


    public void serializeI16(TransportMessage message, String name, short value);

    public short deserializeI16(TransportMessage message, String name);

    public void serializeUI16(TransportMessage message, String name, short value);

    public short deserializeUI16(TransportMessage message, String name);


    public void serializeI32(TransportMessage message, String name, int value);

    public int deserializeI32(TransportMessage message, String name);

    public void serializeUI32(TransportMessage message, String name, int value);

    public int deserializeUI32(TransportMessage message, String name);


    public void serializeI64(TransportMessage message, String name, long value);

    public long deserializeI64(TransportMessage message, String name);

    public void serializeUI64(TransportMessage message, String name, long value);

    public long deserializeUI64(TransportMessage message, String name);


    public void serializeFloat32(TransportMessage message, String name, float value);

    public float deserializeFloat32(TransportMessage message, String name);

    public void serializeFloat64(TransportMessage message, String name, double value);

    public double deserializeFloat64(TransportMessage message, String name);


    public void serializeBoolean(TransportMessage message, String name, boolean value);

    public boolean deserializeBoolean(TransportMessage message, String name);
    
    
    public void serializeString(TransportMessage message, String name, String data);

    public String deserializeString(TransportMessage message, String name);
    
    /*
     * Generic types
     */

    public <T extends Serializable> void serialize(TransportMessage message, String name, T value);

    public <T extends Serializable> T deserialize(TransportMessage message, String name, Class<T> example) throws InstantiationException, IllegalAccessException;

    /*
     * Arrays
     */
    
    public <T> void serializeArrayChar(TransportMessage message, String name, List<T> array, int... dims);
    
    public <T, M> List<M> deserializeArrayChar(TransportMessage message, String name, int... dims);
    
    
    public <T> void serializeArrayByte(TransportMessage message, String name, List<T> array, int... dims);
    
    public <T, M> List<M> deserializeArrayByte(TransportMessage message, String name, int... dims);
    
    
    public <T> void serializeArrayI16(TransportMessage message, String name, List<T> array, int... dims);
    
    public <T, M> List<M> deserializeArrayI16(TransportMessage message, String name, int... dims);
    
    public <T> void serializeArrayUI16(TransportMessage message, String name, List<T> array, int... dims);
    
    public <T, M> List<M> deserializeArrayUI16(TransportMessage message, String name, int... dims);
    
    
    public <T> void serializeArrayI32(TransportMessage message, String name, List<T> array, int... dims);
    
    public <T, M> List<M> deserializeArrayI32(TransportMessage message, String name, int... dims);
    
    public <T> void serializeArrayUI32(TransportMessage message, String name, List<T> array, int... dims);
    
    public <T, M> List<M> deserializeArrayUI32(TransportMessage message, String name, int... dims);
    
    
    public <T> void serializeArrayI64(TransportMessage message, String name, List<T> array, int... dims);
    
    public <T, M> List<M> deserializeArrayI64(TransportMessage message, String name, int... dims);
    
    public <T> void serializeArrayUI64(TransportMessage message, String name, List<T> array, int... dims);
    
    public <T, M> List<M> deserializeArrayUI64(TransportMessage message, String name, int... dims);
    
    
    public <T> void serializeArrayFloat32(TransportMessage message, String name, List<T> array, int... dims);
    
    public <T, M> List<M> deserializeArrayFloat32(TransportMessage message, String name, int... dims);
    
    public <T> void serializeArrayFloat64(TransportMessage message, String name, List<T> array, int... dims);
    
    public <T, M> List<M> deserializeArrayFloat64(TransportMessage message, String name, int... dims);
    
    
    public <T> void serializeArrayBoolean(TransportMessage message, String name, List<T> array, int... dims);
    
    public <T, M> List<M> deserializeArrayBoolean(TransportMessage message, String name, int... dims);
    
    
    public <T> void serializeArrayString(TransportMessage message, String name, List<T> array, int... dims);
    
    public <T, M> List<M> deserializeArrayString(TransportMessage message, String name, int... dims);
    
    /*
     * Array of generic types
     */
    
    public <T> void serializeArray(TransportMessage message, String name, List<T> array, int... dims);
    
    public <T, M> List<M> deserializeArray(TransportMessage message, String name, Class<T> example, int... dims) throws InstantiationException, IllegalAccessException;
    
    /*
     * Sequences
     */
    
    public <T> void serializeSequenceChar(TransportMessage message, String name, List<T> sequence);
    
    public <T, M> List<M> deserializeSequenceChar(TransportMessage message, String name, int depth);
    
    
    public <T> void serializeSequenceByte(TransportMessage message, String name, List<T> sequence);
    
    public <T, M> List<M> deserializeSequenceByte(TransportMessage message, String name, int depth);
    
 
    public <T> void serializeSequenceI16(TransportMessage message, String name, List<T> sequence);
    
    public <T, M> List<M> deserializeSequenceI16(TransportMessage message, String name, int depth);
    
    public <T> void serializeSequenceUI16(TransportMessage message, String name, List<T> sequence);
    
    public <T, M> List<M> deserializeSequenceUI16(TransportMessage message, String name, int depth);
    
    
    public <T> void serializeSequenceI32(TransportMessage message, String name, List<T> sequence);
    
    public <T, M> List<M> deserializeSequenceI32(TransportMessage message, String name, int depth);
    
    public <T> void serializeSequenceUI32(TransportMessage message, String name, List<T> sequence);
    
    public <T, M> List<M> deserializeSequenceUI32(TransportMessage message, String name, int depth);
    
    
    public <T> void serializeSequenceI64(TransportMessage message, String name, List<T> sequence);
    
    public <T, M> List<M> deserializeSequenceI64(TransportMessage message, String name, int depth);
    
    public <T> void serializeSequenceUI64(TransportMessage message, String name, List<T> sequence);
    
    public <T, M> List<M> deserializeSequenceUI64(TransportMessage message, String name, int depth);
    
    
    public <T> void serializeSequenceFloat32(TransportMessage message, String name, List<T> sequence);
    
    public <T, M> List<M> deserializeSequenceFloat32(TransportMessage message, String name, int depth);
    
    public <T> void serializeSequenceFloat64(TransportMessage message, String name, List<T> sequence);
    
    public <T, M> List<M> deserializeSequenceFloat64(TransportMessage message, String name, int depth);
    

    public <T> void serializeSequenceBoolean(TransportMessage message, String name, List<T> sequence);
    
    public <T, M> List<M> deserializeSequenceBoolean(TransportMessage message, String name, int depth);
    
    
    public <T> void serializeSequenceString(TransportMessage message, String name, List<T> sequence);
    
    public <T, M> List<M> deserializeSequenceString(TransportMessage message, String name, int depth);
    
    /*
     * Sequences of generic types
     */
    
    public <T> void serializeSequence(TransportMessage message, String name, List<T> sequence);
    
    public <T, M> List<M> deserializeSequence(TransportMessage message, String name, Class<T> example, int depth) throws InstantiationException, IllegalAccessException;
    
    /*
     * Extra array functions
     */
    
    public void serializeArrayBegin(TransportMessage message, String name, int length);

    public void serializeArrayEnd(TransportMessage message, String name);

    public int deserializeArrayBegin(TransportMessage message, String name);

    public void deserializeArrayEnd(TransportMessage message, String name);
    
    /*
     * Extra struct functions
     */
    
    public void serializeStructBegin(TransportMessage message, String name);

    public void serializeStructEnd(TransportMessage message, String name);

    public int deserializeStructBegin(TransportMessage message, String name);

    public void deserializeStructEnd(TransportMessage message, String name);
    
    /*
     * Extra sequence functions
     */
    
    public void serializeSequenceBegin(TransportMessage message, String name);

    public void serializeSequenceEnd(TransportMessage message, String name);

    public int deserializeSequenceBegin(TransportMessage message, String name);

    public void deserializeSequenceEnd(TransportMessage message, String name);
    
    /*
     * Extra union functions
     */
    
    public void serializeUnionBegin(TransportMessage message, String name);

    public void serializeUnionEnd(TransportMessage message, String name);

    public int deserializeUnionBegin(TransportMessage message, String name);

    public void deserializeUnionEnd(TransportMessage message, String name);





}
