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
package com.kiara.serialization.impl;

import java.util.List;

import com.kiara.serialization.Serializer;
import com.kiara.transport.impl.TransportMessage;

/**
*
* @author Rafael Lara <rafaellara@eprosima.com>
*/
public interface SerializerImpl extends Serializer {

    public Object getNewMessageId();

    public void serializeMessageId(TransportMessage message, Object messageId);

    public Object deserializeMessageId(TransportMessage message);

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
    
    public void serializeArrayChar(TransportMessage message, String name, List<Character> array);
    
    public List<Character> deserializeArrayChar(TransportMessage message, String name, int length);
    
    public void serializeArrayByte(TransportMessage message, String name, List<Byte> array);
    
    public List<Byte> deserializeArrayByte(TransportMessage message, String name, int length);
    
    public void serializeArrayI16(TransportMessage message, String name, List<Short> array);
    
    public List<Short> deserializeArrayI16(TransportMessage message, String name, int length);
    
    public void serializeArrayUI16(TransportMessage message, String name, List<Short> array);
    
    public List<Short> deserializeArrayUI16(TransportMessage message, String name, int length);
    
    public void serializeArrayI32(TransportMessage message, String name, List<Integer> array);
    
    public List<Integer> deserializeArrayI32(TransportMessage message, String name, int length);
    
    public void serializeArrayUI32(TransportMessage message, String name, List<Integer> array);
    
    public List<Integer> deserializeArrayUI32(TransportMessage message, String name, int length);
    
    public void serializeArrayI64(TransportMessage message, String name, List<Long> array);
    
    public List<Long> deserializeArrayI64(TransportMessage message, String name, int length);
    
    public void serializeArrayUI64(TransportMessage message, String name, List<Long> array);
    
    public List<Long> deserializeArrayUI64(TransportMessage message, String name, int length);
    
    public void serializeArrayFloat32(TransportMessage message, String name, List<Float> array);
    
    public List<Float> deserializeArrayFloat32(TransportMessage message, String name, int length);
    
    public void serializeArrayFloat64(TransportMessage message, String name, List<Double> array);
    
    public List<Double> deserializeArrayFloat64(TransportMessage message, String name, int length);
    
    public void serializeArrayBoolean(TransportMessage message, String name, List<Boolean> array);
    
    public List<Boolean> deserializeArrayBoolean(TransportMessage message, String name, int length);
    
    public void serializeArrayString(TransportMessage message, String name, List<String> array);
    
    public List<String> deserializeArrayString(TransportMessage message, String name, int length);
    
    /*
     * Array of generic types
     */
    
    public <T extends Serializable> void serializeArray(TransportMessage message, String name, List<T> array);
    
    public <T extends Serializable> List<T> deserializeArray(TransportMessage message, String name, Class<T> example, int length) throws InstantiationException, IllegalAccessException;
    
    public void serializeArrayBegin(TransportMessage message, String name, int length);

    public void serializeArrayEnd(TransportMessage message, String name);

    public int deserializeArrayBegin(TransportMessage message, String name);

    public void deserializeArrayEnd(TransportMessage message, String name);





}
