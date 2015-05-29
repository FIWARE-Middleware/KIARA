package org.fiware.kiara.ps.rtps.common;

import org.fiware.kiara.ps.rtps.messages.common.types.SubmessageId;

public enum EncapsulationKind {
	
	CDR_BE((byte) 0x00),
	CDR_LE((byte) 0x01),
	PL_CDR_BE((byte) 0x02),
	PL_CDR_LE((byte) 0x03);
	
	private final byte m_value;
	
	private EncapsulationKind(byte value) {
		this.m_value = value;
	}
	
	public byte getValue() {
		return this.m_value;
	}
	
	public static EncapsulationKind createFromValue(byte value) {
		switch (value) {
		case 0x00:
			return EncapsulationKind.CDR_BE;
		case 0x01:
			return EncapsulationKind.CDR_LE;
		case 0x02:
			return EncapsulationKind.PL_CDR_BE;
		case 0x03:
			return EncapsulationKind.PL_CDR_LE;
		default:
			return EncapsulationKind.CDR_BE;	
		}
	}

}
