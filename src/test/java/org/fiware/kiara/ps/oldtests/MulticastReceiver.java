package org.fiware.kiara.ps.oldtests;

import java.io.*;
import java.net.*;

/**
 *
 * @author lycog
 */
public class MulticastReceiver {

    public static void main(String[] args) {
        MulticastSocket socket = null;
        DatagramPacket inPacket = null;
        byte[] inBuf = new byte[256];
        try {
            //Prepare to join multicast group
            socket = new MulticastSocket(1024);
            InetAddress address = InetAddress.getByName("239.255.0.1");
            //socket.setInterface(address);
            socket.joinGroup(address);

            //SocketAddress socketAddress = new SocketAddress(groupIp, groupPort);
            //NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
            //socket.joinGroup(null, null);
            while (true) {
                inPacket = new DatagramPacket(inBuf, inBuf.length);
                socket.receive(inPacket);
                String msg = new String(inBuf, 0, inPacket.getLength());
                System.out.println("From " + inPacket.getAddress() + " Msg : " + msg);
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
