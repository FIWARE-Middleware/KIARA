package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

public class SequenceNumber extends RTPSSubmessageElement {
	
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
	
}
