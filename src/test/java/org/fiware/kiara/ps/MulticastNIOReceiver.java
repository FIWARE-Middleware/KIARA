package org.fiware.kiara.ps;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import org.fiware.kiara.util.HexDump;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rubinste
 */
public class MulticastNIOReceiver {

    public static void main(String[] args) throws Exception {
        java.util.Enumeration<java.net.NetworkInterface> nets = java.net.NetworkInterface.getNetworkInterfaces();
        java.net.NetworkInterface ni = null;
        for (java.net.NetworkInterface netint : java.util.Collections.list(nets)) {
            if (netint.isUp() && netint.supportsMulticast()) {
                ni = netint;
            }
        }

        InetAddress group = InetAddress.getByName("239.255.0.1");

        DatagramChannel dc = DatagramChannel.open(StandardProtocolFamily.INET)
                .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                .bind(new InetSocketAddress(1024))
                .setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);

        for (java.net.NetworkInterface netint : java.util.Collections.list(java.net.NetworkInterface.getNetworkInterfaces())) {
            if (netint.isUp() && netint.supportsMulticast()) {
                MembershipKey key = dc.join(group, netint);
                System.err.printf("MulticastJoin: Address: %s, NetIf: %s, Key: %s%n", group, ni, key);
            }
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(1500);
        while (true) {
            byteBuffer.clear();
            InetSocketAddress sa = (InetSocketAddress) dc.receive(byteBuffer);
            byteBuffer.flip();

            System.out.println("Multicast received from " + sa.getHostString());
            System.out.println(HexDump.dumpHexString(byteBuffer));
        }
    }
}
