package org.fiware.kiara.ps.rtps.utils;

import org.fiware.kiara.ps.rtps.common.Locator;

public class InfoIP {
	public IPTYPE type;
	public int scopeId;
	public String name;
	public Locator locator;
	
	public InfoIP() {
		this.name = "";
		this.locator = new Locator();
	}
}
