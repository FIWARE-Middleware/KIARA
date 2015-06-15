package org.fiware.kiara.ps.rtps.resources;

import java.net.InetAddress;

public class AsioEndpoint {
	
	public int port;
	public InetAddress address;
	
	public AsioEndpoint() {
		
	}
	
	@Override
	public String toString() {
	    return this.address.toString() + ":" + this.port;
	}

}
