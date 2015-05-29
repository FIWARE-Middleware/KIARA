package org.fiware.kiara.ps.rtps.messages.common.types;

import java.util.BitSet;

import org.fiware.kiara.ps.rtps.messages.RTPSMessageHeader;

public class SubmessageFlags {
	
	BitSet m_bits;
	
	public SubmessageFlags() {
		this.m_bits = new BitSet(8);
		this.init();
	}
	
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
	
	public void setFlagValue(byte value) {
		int n = 8;
		final boolean[] set = new boolean[n];
		//this.m_bits.set(0, true);
		for (int i=0; i<8; i++) {
		    if ((value & (1 << i)) > 0) {
		    	this.m_bits.set(i);
		    }
		}
		
	}
	
	public byte getFlagByteValue(int position) {
		if (position >= 8 || position < 0) {
			throw new IndexOutOfBoundsException("Position must be within 0 and 7");
		}
		return this.m_bits.get(position) == true ? (byte) 1 : (byte) 0;
	}
	
	public boolean getFlagValue(int position) {
		if (position >= 8 || position < 0) {
			throw new IndexOutOfBoundsException("Position must be within 0 and 7");
		}
		return this.m_bits.get(position);
	}
	
	public void setBitValue(int pos, boolean value) {
		this.m_bits.set(pos, value);
	}
	
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
		
		//this.m_bits.
		//return this.m_bits.toByteArray()[0];
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof SubmessageFlags) {
			SubmessageFlags instance = (SubmessageFlags) other;
			return this.m_bits.equals(instance.m_bits);
		}
		return false;
	}

}
