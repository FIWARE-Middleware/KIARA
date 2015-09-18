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
import static java.lang.Math.pow;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

/**
* Class representing a timestamp
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class Timestamp extends RTPSSubmessageElement {

    /**
     * Seconds
     */
    private int m_seconds;
    
    /**
     * Fraction of timestamp
     */
    private int m_fraction;

    /**
     * Default constructor
     */
    public Timestamp() {
        this.m_seconds = 0;
        this.m_fraction =  0;
    }

    public Timestamp(Timestamp timestamp) {
        this.m_seconds = timestamp.m_seconds;
        this.m_fraction = timestamp.m_fraction;
    }

    public Timestamp(java.sql.Timestamp timestamp) {
        this.m_seconds = (int) (timestamp.getTime() / 1000);
        this.m_fraction =  (int) ((timestamp.getTime() % 1000) * (Math.pow(10, -6)) * (Math.pow(2, 32)));
    }

    public Timestamp(int seconds, int fraction) {
        this.m_seconds = seconds;
        this.m_fraction =  fraction;
    }

    /**
     * Copies the content of a Timestamp obejct
     * 
     * @param value The Timestamp to be copied
     */
    public void copy(Timestamp value) {
        m_seconds = value.m_seconds;
        m_fraction = value.m_fraction;
    }

    /**
     * Sets the time to infinite
     * 
     * @return The object in which the function has been invoked
     */
    public Timestamp timeInfinite() {
        this.m_seconds = 0x7fffffff;
        this.m_fraction = 0x7fffffff;
        return this;
    }

    /**
     * Sets the time to zero
     * 
     * @return The object in which the function has been invoked
     */
    public Timestamp timeZero() {
        this.m_seconds = 0;
        this.m_fraction = 0;
        return this;
    }

    /**
     * Sets the time to invalid
     * 
     * @return The object in which the function has been invoked
     */
    public Timestamp timeInvalid() {
        this.m_seconds = -1;
        this.m_fraction = 0xffffffff;
        return this;
    }

    /**
     * Get the serialized size of a Timestamp object
     */
    @Override
    public short getSerializedSize() {
        return 8;
    }

    /**
     * Serializes a Timestamp object
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        impl.serializeI32(message, "", this.m_seconds);
        impl.serializeUI32(message, "", this.m_fraction);
    }

    /**
     * Deserializes a Timestamp object
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_seconds = impl.deserializeI32(message, "");
        this.m_fraction = impl.deserializeUI32(message, "");
    }

    /**
     * Compares two Timestamp objects
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Timestamp) {
            return this.m_seconds == ((Timestamp) other).m_seconds && this.m_fraction == ((Timestamp) other).m_fraction;
        }
        return false;
    }
    
    /**
     * Checks whether a Timestamp object is lower than other
     * 
     * @param other The Timestamp object to compare
     * @return true if the current Timestamp object is lower than the other
     */
    public boolean isLowerThan(Timestamp other) {
        if (this.m_seconds < other.m_seconds) {
            return true;
        } else if (this.m_seconds == other.m_seconds) {
            if (this.m_fraction < other.m_fraction) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks whether a Timestamp object is lower than or equal to other
     * 
     * @param other The Timestamp object to compare
     * @return true if the current Timestamp object is lower than or equal to the other
     */
    public boolean isLowerOrEqualThan(Timestamp other) {
        if (this.m_seconds < other.m_seconds) {
            return true;
        } else if (this.m_seconds == other.m_seconds) {
            if (this.m_fraction <= other.m_fraction) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts the Timestamp into milliseconds
     * 
     * @return The Timestamp value in milliseconds
     */
    public double toMilliSecondsDouble() {
        double retVal = ((double) this.m_fraction / Math.pow(2.0, 32) * Math.pow(10.0, 3)) + (double) this.m_seconds * Math.pow(10.0, 3);
        if (retVal == 0) {
            return 1;
        }
        return retVal;
    }

    /**
     * Set the value of the Timestamp in milliseconds 
     * 
     * @param millisec The Timestamp value in milliseconds
     */
    public void setMilliSecondsDouble(double millisec) {
        m_seconds = (int)(millisec/pow(10.0,3));
	m_fraction = (int)((millisec-(double)m_seconds*pow(10.0,3))/pow(10.0,3)*pow(2.0,32));
    }

}
