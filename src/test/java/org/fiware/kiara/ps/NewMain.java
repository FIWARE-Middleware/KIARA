package org.fiware.kiara.ps;


import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.resources.ListenResource;
import org.fiware.kiara.ps.rtps.utils.IPFinder;

public class NewMain {
	        static void displayInterfaceInformation(java.net.NetworkInterface netint) throws SocketException {
            System.out.printf("Display name: %s\n", netint.getDisplayName());
            System.out.printf("Name: %s\n", netint.getName());
            java.util.Enumeration<java.net.InetAddress> inetAddresses = netint.getInetAddresses();
            for (java.net.InetAddress inetAddress : java.util.Collections.list(inetAddresses)) {
                System.out.printf("InetAddress: %s\n", inetAddress);
                if (inetAddress.isLoopbackAddress()) {
                    System.out.printf("Loopback address%n");
                }
                if (inetAddress instanceof java.net.Inet6Address) {
                    System.out.printf("IPv6 address%n");
                }
                if (inetAddress instanceof java.net.Inet4Address) {
                    System.out.printf("IPv4 address%n");
                }
                System.out.printf("%n");
            }
            System.out.printf("%n");
        }

	public static void main(String[] args) {
                try {
                    java.util.Enumeration<java.net.NetworkInterface> nets = java.net.NetworkInterface.getNetworkInterfaces();
                    for (java.net.NetworkInterface netint : java.util.Collections.list(nets))
                        displayInterfaceInformation(netint);
                } catch (SocketException ex) {
                    Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
                }

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
