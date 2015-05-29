package org.fiware.kiara.ps.rtps.attributes;

public class HistoryCacheAttributes {
	
	public int payloadMaxSize;
	public int initialeservedCaches;
	public int maximumReservedCaches;
	
	public HistoryCacheAttributes() {
		this.payloadMaxSize = 500;
		this.initialeservedCaches = 500;
		this.maximumReservedCaches = 0;
	}

	public HistoryCacheAttributes(int payloadMaxSize, int initialeservedCaches, int maximumReservedCaches) {
		super();
		this.payloadMaxSize = payloadMaxSize;
		this.initialeservedCaches = initialeservedCaches;
		this.maximumReservedCaches = maximumReservedCaches;
	}
	
	

}
