package org.fiware.kiara.ps;


import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.resources.ListenResource;
import org.fiware.kiara.ps.rtps.utils.IPFinder;

public class NewMain {
	
	public static void main(String[] args) {
		
		//IPFinder.getIPs();
		
		ListenResource lr = new ListenResource(new RTPSParticipant(null, null, null), 1, true);
		
		byte[] addr = new byte[16];
		addr[12] = (byte) 239;
		addr[13] = (byte) 255;
		addr[14] = (byte) 0;
		addr[15] = (byte) 1;
		/*addr[12] = (byte) 127;
                addr[13] = (byte) 0;
                addr[14] = (byte) 0;
                addr[15] = (byte) 1;*/
		Locator loc = new Locator();
		loc.setAddress(addr);
		loc.setPort(7400);
		
		lr.initThread(new RTPSParticipant(null, new GUIDPrefix(), null), loc, 1024, true, false);
		//lr.initThread(new RTPSParticipant(null, null, null), loc, 1024, false, false);
		
	}

}
