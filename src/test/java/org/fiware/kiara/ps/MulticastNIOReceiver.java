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
        NetworkInterface ni = NetworkInterface.getByName("wlp3s0");  // wlp3s0
        InetAddress group = InetAddress.getByName("239.255.0.1");

        DatagramChannel dc = DatagramChannel.open(StandardProtocolFamily.INET)
                .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                .bind(new InetSocketAddress(1024))
                .setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
        MembershipKey key = dc.join(group, ni);

        System.err.printf("MulticastJoin: Address: %s, NetIf: %s, Key: %s%n", group, ni, key);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1500);
        while (true) {
            if (key.isValid()) {
                byteBuffer.clear();
                InetSocketAddress sa = (InetSocketAddress) dc.receive(byteBuffer);
                byteBuffer.flip();

                System.out.println("Multicast received from " + sa.getHostString());
                System.out.println(HexDump.dumpHexString(byteBuffer));
            }
        }
    }
}
