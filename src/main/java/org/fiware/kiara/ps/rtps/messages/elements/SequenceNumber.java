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
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class SequenceNumber extends RTPSSubmessageElement implements Comparable {
	
	private int m_high;
	private int m_low;
	
	public SequenceNumber() {
		this.m_high = 0;
		this.m_low = 0;
	}
	
	public SequenceNumber(int high, int low) {
		this.m_high = high;
		this.m_low = low;
	}

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
	
	public long toLong() {
		return (long) ((this.m_high * Math.pow(2, 32)) + this.m_low);
	}

	/*@Override
	public void serialize(CDRSerializer ser, BinaryOutputStream bos) {
		try {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	@Override
	public short getSerializedSize() {
		return 8;
	}
	
	public int getHigh() {
		return m_high;
	}

	public void setHigh(int m_high) {
		this.m_high = m_high;
	}

	public int getLow() {
		return m_low;
	}

	public void setLow(int m_low) {
		this.m_low = m_low;
	}

        public SequenceNumber setUnknown() {
            this.m_high = -1;
            this.m_low = 0;
            return this;
        }

        public boolean isUnknown() {
            return this.m_high == -1 && this.m_low == 0;
        }

	@Override
	public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
		impl.serializeI32(message, "", this.m_high);
		impl.serializeI32(message, "", this.m_low);
	}

	@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
		this.m_high = impl.deserializeI32(message, "");
		this.m_low = impl.deserializeI32(message, "");
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof SequenceNumber) {
			if (this.m_high == ((SequenceNumber) other).m_high && this.m_low == ((SequenceNumber) other).m_low) {
				return true;
			}
		}
		return false;
	}
	
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

	public void increment() {
		if (this.m_low == Math.pow(2.0, 32)) {
			 this.m_high++;
			 this.m_low = 0;
		} else {
			this.m_low++;
		}
	}
	
	public void decrement() {
            if ((long) this.m_low - (long) 1 < 0) {
                this.m_high--;
                this.m_low = (int) (Math.pow(2.0, 32) - (1 - this.m_low));
            } else {
                this.m_low -= (int) 1;
            }
        }

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

	@Override
	public String toString() {
	    return this.toLong() + " (H: " + this.m_high + ", L: " + this.m_low + ")";
	}

        /**
	 * Assignment operator
	 * @param seq SequenceNumber to copy the data from
	 */
        public void copy(SequenceNumber seq) {
            m_high = seq.m_high;
            m_low = seq.m_low;
        }

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
