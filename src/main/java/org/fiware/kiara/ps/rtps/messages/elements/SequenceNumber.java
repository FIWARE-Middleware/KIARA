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
package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

/**
 * Class representing a sequence number RTPS submessage element.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class SequenceNumber extends RTPSSubmessageElement implements Comparable<Object> {

    /**
     * High value
     */
    private int m_high;
    
    /**
     * Low value
     */
    private int m_low;

    /**
     * Default {@link SequenceNumber} constructor
     */
    public SequenceNumber() {
        this.m_high = 0;
        this.m_low = 0;
    }

    /**
     * Alternative {@link SequenceNumber} constructor
     * 
     * @param high High value of the {@link SequenceNumber}
     * @param low Low value of the {@link SequenceNumber}
     */
    public SequenceNumber(int high, int low) {
        this.m_high = high;
        this.m_low = low;
    }

    /**
     * Copy constructor
     * 
     * @param seq The SequenceNumber object to copy
     */
    public SequenceNumber(SequenceNumber seq) {
        this.m_low = seq.m_low;
        this.m_high = seq.m_high;
    }

    public SequenceNumber(SequenceNumber seq, long offset) {
        this.m_low = seq.m_low;
        this.m_high = seq.m_high;

        if (this.m_low + offset > Math.pow(2.0,  32)) {
            int module = (int) Math.floor((offset + this.m_low) / Math.pow(2.0, 32));
            this.m_high += module;
            this.m_low += (int) (offset - (Math.pow(2.0, 32) * module));
        } else {
            this.m_low += (int) (offset);
        }

    }

    /**
     * Converts a SequenceNumber to its long representation
     * 
     * @return The long representaton of the SequenceNumber
     */
    public long toLong() {
        return (long) ((this.m_high * Math.pow(2, 32)) + this.m_low);
    }

    /**
     * Get the SequenceNumber serialized size
     */
    @Override
    public short getSerializedSize() {
        return 8;
    }

    /**
     * Get the high value
     * 
     * @return The high value
     */
    public int getHigh() {
        return m_high;
    }

    /**
     * Set the high value
     * 
     * @param m_high The high value
     */
    public void setHigh(int m_high) {
        this.m_high = m_high;
    }

    /**
     * Get the low value
     * 
     * @return The low value
     */
    public int getLow() {
        return m_low;
    }

    /**
     * Set the low value
     * 
     * @param m_low The low value
     */
    public void setLow(int m_low) {
        this.m_low = m_low;
    }

    /**
     * Sets the high and low value to unknown
     * 
     * @return The object in which the method has been invoked 
     */
    public SequenceNumber setUnknown() {
        this.m_high = -1;
        this.m_low = 0;
        return this;
    }

    /**
     * Chechs whether the SequenceNumber is unknown
     * 
     * @return true if the SequenceNumber is unknown; false otherwise
     */
    public boolean isUnknown() {
        return this.m_high == -1 && this.m_low == 0;
    }

    /**
     * Serializes a SequenceNumber object
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        impl.serializeI32(message, "", this.m_high);
        impl.serializeI32(message, "", this.m_low);
    }

    /**
     * Deserializes a SequenceNumber object
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_high = impl.deserializeI32(message, "");
        this.m_low = impl.deserializeI32(message, "");
    }

    /**
     * Compares two SequenceNumber objects
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof SequenceNumber) {
            if (this.m_high == ((SequenceNumber) other).m_high && this.m_low == ((SequenceNumber) other).m_low) {
                return true;
            }
        }
        return false;
    }

    /**
     * Chacks whether the current SequenceNumber object is greater than the other
     * 
     * @param other The other SequenceNumber object to compare
     * @return true if the current SequenceNumber is greater than the other
     */
    public boolean isGreaterThan(SequenceNumber other) {
        if (this.m_high > other.m_high) {
            return true;
        } else if (this.m_high < other.m_high) {
            return false;
        } else {
            if (this.m_low > other.m_low) {
                return true;
            }
        }

        return false;
    }

    /**
     * Chacks whether the current SequenceNumber object is greater than 
     * or equal to the other
     * 
     * @param other The other SequenceNumber object to compare
     * @return true if the current SequenceNumber is greater than or equal 
     * to the other
     */
    public boolean isGreaterOrEqualThan(SequenceNumber other) {
        if (this.m_high > other.m_high) {
            return true;
        } else if (this.m_high < other.m_high) {
            return false;
        } else {
            if (this.m_low >= other.m_low) {
                return true;
            }
        }

        return false;
    }

    /**
     * Chacks whether the current SequenceNumber object is lower than the other
     * 
     * @param other The other SequenceNumber object to compare
     * @return true if the current SequenceNumber is lower than the other
     */
    public boolean isLowerThan(SequenceNumber other) {
        if (this.m_high < other.m_high) {
            return true;
        } else if (this.m_high > other.m_high) {
            return false;
        } else {
            if (this.m_low < other.m_low) {
                return true;
            }
        }

        return false;
    }

    /**
     * Chacks whether the current SequenceNumber object is lower than or equal 
     * to than the other
     * 
     * @param other The other SequenceNumber object to compare
     * @return true if the current SequenceNumber is lower than or equal to 
     * the other
     */
    public boolean isLowerOrEqualThan(SequenceNumber other) {
        if (this.m_high < other.m_high) {
            return true;
        } else if (this.m_high > other.m_high) {
            return false;
        } else {
            if (this.m_low <= other.m_low) {
                return true;
            }
        }

        return false;
    }

    /**
     * Increases the SequenceNumber
     */
    public void increment() {
        if (this.m_low == Math.pow(2.0, 32)) {
            this.m_high++;
            this.m_low = 0;
        } else {
            this.m_low++;
        }
    }

    /**
     * Decreases the SequenceNumber
     */
    public void decrement() {
        if ((long) this.m_low - (long) 1 < 0) {
            this.m_high--;
            this.m_low = (int) (Math.pow(2.0, 32) - (1 - this.m_low));
        } else {
            this.m_low -= (int) 1;
        }
    }

    /**
     * Substracts a quantity from the SequenceNumber
     * 
     * @param inc The value to be substracted
     * @return The object in which the method has been invoked
     */
    public SequenceNumber subtract(int inc) {
        SequenceNumber res = new SequenceNumber(this);
        if ((long) res.m_low - (long) inc < 0) {
            res.m_high--;
            res.m_low = (int) (Math.pow(2.0, 32) - (inc - this.m_low));
        } else {
            res.m_low -= (int) inc;
        }
        return res;
    }

    /**
     * Converts a SequenceNumber to its String representation
     */
    @Override
    public String toString() {
        return this.toLong() + " (H: " + this.m_high + ", L: " + this.m_low + ")";
    }

    /**
     * Copies the content of a SequenceNumber
     * @param seq SequenceNumber to copy the data from
     */
    public void copy(SequenceNumber seq) {
        m_high = seq.m_high;
        m_low = seq.m_low;
    }

    /**
     * Compares two SequenceNumber objects
     */
    @Override
    public int compareTo(Object other) { // TODO Review this
        SequenceNumber otherSeqNum = (SequenceNumber) other;
        if (this.isLowerThan(otherSeqNum)) {
            return -1;
        } else if (this.equals(otherSeqNum)) {
            return 0;
        } else {
            return 1;
        }
    }

}
