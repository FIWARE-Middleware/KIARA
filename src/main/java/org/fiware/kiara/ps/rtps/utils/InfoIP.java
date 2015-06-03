package org.fiware.kiara.ps.rtps.utils;

import org.fiware.kiara.ps.rtps.common.Locator;

public class InfoIP {
	public IPTYPE type;
	public int scopeId;
	public String name;
	public final Locator locator;

	public InfoIP() {
            this(IPTYPE.IPv4, 0, "", null);
	}

    public InfoIP(IPTYPE type, int scopeId, String name, Locator locator) {
        this.type = type;
        this.scopeId = scopeId;
        this.name = name;
        this.locator = locator == null ? new Locator() : locator;
    }
}
