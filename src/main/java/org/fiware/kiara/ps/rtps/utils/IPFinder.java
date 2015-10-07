/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fiware.kiara.ps.rtps.utils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.rtps.common.LocatorKind;
import org.fiware.kiara.ps.rtps.common.LocatorList;

/**
 * This class helps the middleware to find IP adresses
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class IPFinder {

    /**
     * Gets all the IP adresses associated to a specific machine
     * 
     * @return A list of {@link InfoIP} objects
     */
    public static List<InfoIP> getIPs() {
        ArrayList<InfoIP> retVal = new ArrayList<>();

        try {
            java.util.Enumeration<java.net.NetworkInterface> nets = java.net.NetworkInterface.getNetworkInterfaces();
            while (nets.hasMoreElements()) {
                java.net.NetworkInterface netint = nets.nextElement();

                if (netint.isUp()) {

                    final boolean loopback = netint.isLoopback();

                    java.util.Enumeration<java.net.InetAddress> inetAddresses = netint.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        final InetAddress currentIP = inetAddresses.nextElement();

                        if (currentIP instanceof Inet6Address) { // IPv6
                            final Inet6Address currentIPv6 = (Inet6Address) currentIP;
                            final InfoIP infoIP = new InfoIP(
                                    loopback ? IPTYPE.IPv6_LOCAL : IPTYPE.IPv6,
                                            currentIPv6.getScopeId(),
                                            currentIPv6.getHostAddress(),
                                            null
                                    );

                            if (!loopback) {
                                byte addr[] = new byte[16];
                                final byte[] ipAddress = currentIPv6.getAddress();
                                System.arraycopy(ipAddress, 0, addr, 0, 16);

                                infoIP.locator.setKind(LocatorKind.LOCATOR_KIND_UDPv6);
                                infoIP.locator.setPort(0);
                                infoIP.locator.setAddress(ipAddress);
                            }

                            retVal.add(infoIP);
                        } else if (currentIP instanceof Inet4Address) { // IPv4
                            final Inet4Address currentIPv4 = (Inet4Address) currentIP;
                            final InfoIP infoIP = new InfoIP(
                                    loopback ? IPTYPE.IPv4_LOCAL : IPTYPE.IPv4,
                                            0,
                                            currentIPv4.getHostAddress(),
                                            null
                                    );

                            if (!loopback) {
                                byte[] addr = new byte[16];
                                final byte[] ipAddress = currentIPv4.getAddress();

                                addr[12] = ipAddress[0];
                                addr[13] = ipAddress[1];
                                addr[14] = ipAddress[2];
                                addr[15] = ipAddress[3];

                                infoIP.locator.setKind(LocatorKind.LOCATOR_KIND_UDPv4);
                                infoIP.locator.setPort(0);
                                infoIP.locator.setAddress(addr);
                            }

                            retVal.add(infoIP);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    /**
     * Get a {@link LocatorList} with all the avaliable IPv4 addresses
     * 
     * @return A {@link LocatorList} with the avaliable IPv4 addresses
     */
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

    /**
     * Get an {@link Inet4Address} with the first avaliable IPv4 address
     * 
     * @return An {@link Inet4Address} with the first avaliable IPv4 address
     */
    public static Inet4Address getFirstIPv4Address() {
        try {
            java.util.Enumeration<java.net.NetworkInterface> nets = java.net.NetworkInterface.getNetworkInterfaces();
            for (java.net.NetworkInterface netint : java.util.Collections.list(nets)) {
                java.util.Enumeration<java.net.InetAddress> inetAddresses = netint.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return (Inet4Address) inetAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Get an {@link Inet6Address} with the first avaliable IPv6 address
     * 
     * @return An {@link Inet6Address} with the first avaliable IPv6 address
     */
    public static Inet6Address getFirstIPv6Address() {
        try {
            java.util.Enumeration<java.net.NetworkInterface> nets = java.net.NetworkInterface.getNetworkInterfaces();
            for (java.net.NetworkInterface netint : java.util.Collections.list(nets)) {
                java.util.Enumeration<java.net.InetAddress> inetAddresses = netint.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
                        return (Inet6Address) inetAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Creates an empty IPv4 address
     * 
     * @return An empty {@link Inet4Address}
     */
    public static Inet4Address addressIPv4() {
        try {
            return (Inet4Address) InetAddress.getByName("0.0.0.0");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the IPv6 addresses
     * 
     * @return A {@link LocatorList} containing the IPv6 addresses
     */
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
   
    /**
     * Creates an empty IPv6 address
     * 
     * @return An empty {@link Inet6Address}
     */
    public static Inet6Address addressIPv6() {
        try {
            return (Inet6Address) InetAddress.getByName("::");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all the IP adresses
     * 
     * @return A {@link LocatorList} containing all the IP adresses
     */
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


}
