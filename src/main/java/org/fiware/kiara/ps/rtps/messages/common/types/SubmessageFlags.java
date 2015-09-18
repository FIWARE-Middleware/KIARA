/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
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
package org.fiware.kiara.ps.rtps.messages.common.types;

import java.util.BitSet;

/**
 * Flags of the RTPSSubmessage header
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class SubmessageFlags {

    /**
     * The {@link BitSet} representing the flag values
     */
    BitSet m_bits;

    public SubmessageFlags() {
        this.m_bits = new BitSet(8);
        this.init();
    }

    /**
     * Initializes the SubmessageFlags object
     */
    public void init() {
        for (int i=0; i < 8; ++i) {
            this.m_bits.set(i, false);
        }
    }

    public SubmessageFlags(byte value) {
        this.m_bits = new BitSet(8);
        this.init();
        setFlagValue(value);
    }

    /**
     * Set the flags value
     * 
     * @param value Byte value indicating the flags
     */
    public void setFlagValue(byte value) {
        int n = 8;
        for (int i=0; i<8; i++) {
            if ((value & (1 << i)) > 0) {
                this.m_bits.set(i);
            }
        }
    }

    /**
     * Get the value according to its position 
     * 
     * @param position Position of the flag
     * @return byte containing the flag value
     */
    public byte getFlagByteValue(int position) {
        if (position >= 8 || position < 0) {
            throw new IndexOutOfBoundsException("Position must be within 0 and 7");
        }
        return this.m_bits.get(position) == true ? (byte) 1 : (byte) 0;
    }

    /**
     * Get the value of a flag according to its position
     * 
     * @param position Position of the flag
     * @return boolean value of the flag
     */
    public boolean getFlagValue(int position) {
        if (position >= 8 || position < 0) {
            throw new IndexOutOfBoundsException("Position must be within 0 and 7");
        }
        return this.m_bits.get(position);
    }

    /**
     * Sets the value of the flag in a secific position
     * 
     * @param pos Position of the flag to be set
     * @param value boolean value of the flag
     */
    public void setBitValue(int pos, boolean value) {
        this.m_bits.set(pos, value);
    }

    /**
     * Get the byte value of the whole flag bitmask
     * 
     * @return The bye value of the entire bitmask
     */
    public byte getByteValue() {
        byte byteval = 0;   
        if (this.m_bits.length() == 0) {
            return (byte) 0;
        }
        for (int i = 0; i < this.m_bits.length(); i++) {
            if (this.m_bits.get(i)) {
                byteval |= 1 << (i % 8);
            }
        }
        return byteval;
    }

    /**
     *  Compares two SubmessageFlags objects
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof SubmessageFlags) {
            SubmessageFlags instance = (SubmessageFlags) other;
            return this.m_bits.equals(instance.m_bits);
        }
        return false;
    }

}
