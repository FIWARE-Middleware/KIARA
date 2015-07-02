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
import java.util.Date;
import java.util.GregorianCalendar;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class Timestamp extends RTPSSubmessageElement {

    //private java.sql.Timestamp m_timestamp;
    private int m_seconds;
    private int m_fraction;

    public Timestamp() {
        this.m_seconds = 0;
        this.m_fraction =  0;
    }

    public Timestamp(java.sql.Timestamp timestamp) {
        this.m_seconds = (int) (timestamp.getTime() / 1000);
        this.m_fraction =  (int) ((timestamp.getTime() % 1000) * (Math.pow(10, -6)) * (Math.pow(2, 32)));
    }

    public Timestamp(int seconds, int fraction) {
        this.m_seconds = seconds;
        this.m_fraction =  fraction;
    }

    public Timestamp timeInfinite() {
        this.m_seconds = 0x7fffffff;
        this.m_fraction = 0x7fffffff;
        return this;
    }

    public Timestamp timeZero() {
        this.m_seconds = 0;
        this.m_fraction = 0;
        return this;
    }

    public Timestamp timeInvalid() {
        this.m_seconds = -1;
        this.m_fraction = 0xffffffff;
        return this;
    }

    /*public void serialize(CDRSerializer ser, BinaryOutputStream bos) {
		try {
			//ser.serializeUI64(bos, "", this.m_timestamp.getTime());
			//Date d = new Date(this.m_timestamp.getTime());


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

    @Override
    public short getSerializedSize() {
        return 8;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        impl.serializeI32(message, "", this.m_seconds);
        impl.serializeUI32(message, "", this.m_fraction);
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_seconds = impl.deserializeI32(message, "");
        this.m_fraction = impl.deserializeUI32(message, "");
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Timestamp) {
            return this.m_seconds == ((Timestamp) other).m_seconds && this.m_fraction == ((Timestamp) other).m_fraction;
        }
        return false;
    }
    
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

    public double toMilliSecondsDouble() {
        return ((double) this.m_fraction / Math.pow(2.0,32) * Math.pow(10.0,6)) + (double) this.m_seconds*Math.pow(10.0,6);
    }

}
