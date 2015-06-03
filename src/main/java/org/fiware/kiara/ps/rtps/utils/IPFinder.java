package org.fiware.kiara.ps.rtps.utils;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorKind;
import org.fiware.kiara.ps.rtps.common.LocatorList;

public class IPFinder {
	
	public static List<InfoIP> getIPs() {
		ArrayList<InfoIP> retVal = new ArrayList<InfoIP>();

		try {
                    java.util.Enumeration<java.net.NetworkInterface> nets = java.net.NetworkInterface.getNetworkInterfaces();
                    for (java.net.NetworkInterface netint : java.util.Collections.list(nets)) {
                        java.util.Enumeration<java.net.InetAddress> inetAddresses = netint.getInetAddresses();
                        while (inetAddresses.hasMoreElements()) {
                            final InetAddress currentIP = inetAddresses.nextElement();
                            final InfoIP infoIP = new InfoIP();

                            if (currentIP.getHostAddress().indexOf(":") >= 0) { // IPv6
                                    System.out.println("IPv6");
                                    Inet6Address currentIPv6 = (Inet6Address) currentIP;
                                    infoIP.type = IPTYPE.IPv6;
                                    infoIP.name = currentIPv6.getHostAddress();
                                    if (!parseIPv6(infoIP)) {
                                            infoIP.type = IPTYPE.IPv6_LOCAL;
                                    }
                                    infoIP.scopeId = currentIPv6.getScopeId();
                                    retVal.add(infoIP);
                            } else if (currentIP.getHostAddress().matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$")) { // IPv4
                                    System.out.println("IPv4");
                                    infoIP.type = IPTYPE.IPv4;
                                    infoIP.name = currentIP.getHostAddress();
                                    if (!parseIPv4(infoIP)) {
                                            infoIP.type = IPTYPE.IPv4_LOCAL;
                                    }
                                    retVal.add(infoIP);
                            }

                            System.out.println(currentIP.getHostAddress());
                            //retVal.add(allMyIps[i]);
                        }
                    }
/*
			InetAddress localhost = InetAddress.getLocalHost();
			InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());

			if (allMyIps != null && allMyIps.length > 0) {

				for (int i = 0; i < allMyIps.length; ++i) {

					InetAddress currentIP = allMyIps[i];
					
					InfoIP infoIP = new InfoIP();
					
					if (currentIP.getHostAddress().indexOf(":") >= 0) { // IPv6
						System.out.println("IPv6");
						Inet6Address currentIPv6 = (Inet6Address) currentIP;
						infoIP.type = IPTYPE.IPv6;
						infoIP.name = currentIPv6.getHostAddress();
						if (!parseIPv6(infoIP)) {
							infoIP.type = IPTYPE.IPv6_LOCAL;
						}
						infoIP.scopeId = currentIPv6.getScopeId();
						retVal.add(infoIP);
					} else if (currentIP.getHostAddress().matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$")) { // IPv4
						System.out.println("IPv4");
						infoIP.type = IPTYPE.IPv4;
						infoIP.name = currentIP.getHostAddress();
						if (!parseIPv4(infoIP)) {
							infoIP.type = IPTYPE.IPv4_LOCAL;
						}
						retVal.add(infoIP);
					}
					
					System.out.println(allMyIps[i].getHostAddress());
					//retVal.add(allMyIps[i]);
				}

			}
                        */

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retVal;
	}
	
	public static LocatorList getIPv4Adress() {
		LocatorList locators = new LocatorList();
		List<InfoIP> ipNames = IPFinder.getIPs();
		
		if (ipNames.size() > 0) {
			locators.clear();
			for (InfoIP it : ipNames) {
				if (it.type == IPTYPE.IPv4) {
					locators.pushBack(it.locator);
				}
			}
		} 
		
		return locators;

	}

        public static Inet4Address getFirstIPv4Address() {
            try {
                java.util.Enumeration<java.net.NetworkInterface> nets = java.net.NetworkInterface.getNetworkInterfaces();
                for (java.net.NetworkInterface netint : java.util.Collections.list(nets)) {
                    java.util.Enumeration<java.net.InetAddress> inetAddresses = netint.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            return (Inet4Address)inetAddress;
                        }
                    }
                }
            } catch (SocketException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        public static Inet6Address getFirstIPv6Address() {
            try {
                java.util.Enumeration<java.net.NetworkInterface> nets = java.net.NetworkInterface.getNetworkInterfaces();
                for (java.net.NetworkInterface netint : java.util.Collections.list(nets)) {
                    java.util.Enumeration<java.net.InetAddress> inetAddresses = netint.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
                            return (Inet6Address)inetAddress;
                        }
                    }
                }
            } catch (SocketException ex) {
                ex.printStackTrace();
            }
            return null;
        }

	public static Inet4Address getFirstIPv4Adress() {
            return getFirstIPv4Address();
//		try {
//			InetAddress[] allMyIps = InetAddress.getAllByName(InetAddress.getLocalHost().getCanonicalHostName());
//			for (InetAddress it : allMyIps) {
//				if (!it.isLoopbackAddress() && it instanceof Inet4Address) {
//					System.out.println(it.getHostName());
//					return (Inet4Address) it;
//				}
//			}
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
	}

	public static LocatorList getIPv6Adress() {
		LocatorList locators = new LocatorList();
		List<InfoIP> ipNames = IPFinder.getIPs();
		
		if (ipNames.size() > 0) {
			locators.clear();
			for (InfoIP it : ipNames) {
				if (it.type == IPTYPE.IPv6) {
					locators.pushBack(it.locator);
				}
			}
		} 
		
		return locators;
		
	}

	public static Inet6Address getFirstIPv6Adress() {
            return getFirstIPv6Address();
//		try {
//			InetAddress[] allMyIps = InetAddress.getAllByName(InetAddress.getLocalHost().getCanonicalHostName());
//			for (InetAddress it : allMyIps) {
//				if (!it.isLoopbackAddress() && it instanceof Inet6Address) {
//					System.out.println(it.getHostName());
//					return (Inet6Address) it;
//				}
//			}
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
	}

	public static LocatorList getAllIPAdress() {
		LocatorList locators = new LocatorList();
		List<InfoIP> ipNames = IPFinder.getIPs();
		
		if (ipNames.size() > 0) {
			locators.clear();
			for (InfoIP it : ipNames) {
				if (it.type == IPTYPE.IPv4 || it.type == IPTYPE.IPv6) {
					locators.pushBack(it.locator);
				}
			}
		} 
		
		return locators;
		
	}

	private static boolean parseIPv4(InfoIP infoIP) {
		String ipStr = infoIP.name;
		
		
		byte[] addr = new byte[16];
		String ch = "\\.";
		
		String[] splitted = ipStr.split(ch);
		
		addr[12] = (byte) Integer.parseInt(splitted[0]);
		addr[13] = (byte) Integer.parseInt(splitted[1]);
		addr[14] = (byte) Integer.parseInt(splitted[2]);
		addr[15] = (byte) Integer.parseInt(splitted[3]);
		
		if (addr[12] == 127 && addr[13] == 0 && addr[14] == 0 && addr[15] == 1) {
			return false;
		}
		
		infoIP.locator.setKind(LocatorKind.LOCATOR_KIND_UDPv4);
		infoIP.locator.setPort(0);
		infoIP.locator.setAddress(addr);
		
		return true;
	}
	
	private static boolean parseIPv6(InfoIP infoIP) {
		String ipStr = infoIP.name;
		
		String[] scopeIdSplitted = ipStr.split("%");
		String[] splitted = scopeIdSplitted[0].split(":");
		
		if (splitted[0].length() == 0 && splitted[1].length() == 0) {
			return false;
		}
		
		if (splitted[splitted.length-1].contains(".")) { // Map do IPv4 address
			return false;
		}
		
		byte addr[] = new byte[16];
		
		infoIP.locator.setKind(LocatorKind.LOCATOR_KIND_UDPv6);
		infoIP.locator.setPort(0);
		
		int auxNumber = 0;
		byte index = 15;
		
		for (int i=splitted.length-1; i >=0; --i) {
			String it = splitted[i];
			if (it.length() != 0) {
				if (it.length() <= 2) {
					addr[index - 1] = 0;
					auxNumber = Integer.parseInt(it, 16);
					addr[index] = (byte) auxNumber;
				} else {
					//String subs = it.substring(it.length()-2);
					auxNumber = Integer.parseInt(it.substring(it.length()-2), 16);
					addr[index] = (byte) auxNumber;
					//subs = it.substring(0, it.length()-2);
					auxNumber = Integer.parseInt(it.substring(0, it.length()-2), 16);
					addr[index-1] = (byte) auxNumber;
				}
				index -= 2;
			} else {
				break;
			}
		}
		
		index = 0;
		for (String it : splitted) {
			if (it.length() != 0) {
				if (it.length() <= 2) {
					addr[index] = 0;
					auxNumber = Integer.parseInt(it, 16);
					addr[index+1] = (byte) auxNumber;
				} else {
					//String subs = it.substring(it.length()-2);
					auxNumber = Integer.parseInt(it.substring(it.length()-2), 16);
					addr[index+1] = (byte) auxNumber;
					//subs = it.substring(0, it.length()-2);
					auxNumber = Integer.parseInt(it.substring(0, it.length()-2), 16);
					addr[index] = (byte) auxNumber;
				}
				index += 2;
			} else {
				break;
			}
		}
		
		return true;
	}

	

}
