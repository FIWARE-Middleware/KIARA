package org.fiware.kiara.ps.rtps.common;

public enum LocatorKind {
	
	LOCATOR_KIND_RESERVED(0),
	LOCATOR_KIND_UDPv4(1),
	LOCATOR_KIND_UDPv6(2),
	LOCATOR_KIND_INVALID(-1);
	
	private int m_value;
	
	private LocatorKind(int value) {
		this.m_value = value;
	}
	
	public int getValue() {
		return this.m_value;
	}

}
