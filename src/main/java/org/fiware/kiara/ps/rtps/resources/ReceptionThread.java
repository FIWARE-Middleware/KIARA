package org.fiware.kiara.ps.rtps.resources;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.Endpoint;
import org.fiware.kiara.ps.rtps.messages.MessageReceiver;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageBuilder;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;

public class ReceptionThread implements Runnable {

    private DatagramChannel m_channel;

    private ListenResource m_listenResource;

    //private final Lock m_mutex = new ReentrantLock(true);

    public ReceptionThread(DatagramChannel channel, ListenResource listenResource) {
        this.m_channel = channel;
        this.m_listenResource = listenResource;
    }

    @Override
    public void run() {

        System.out.println("Thread started");
        
        this.m_listenResource.getRTPSParticipant().resourceSemaphorePost();
        
        ByteBuffer buf;
        try {
            System.out.println("Size: " + this.m_channel.socket().getReceiveBufferSize());
            buf = ByteBuffer.allocate(this.m_channel.socket().getReceiveBufferSize());

            DatagramPacket dp = new DatagramPacket(buf.array(), this.m_channel.socket().getReceiveBufferSize());
            System.out.println(this.m_channel.socket().getLocalPort());
            
            while(true) {
            
                this.m_channel.socket().receive(dp);
                
                this.m_listenResource.getSenderEndpoint().port = dp.getPort();
                this.m_listenResource.getSenderEndpoint().address = dp.getAddress();
    
                /*InetAddress addr = dp.getAddress(); // <<<-------------------- AQUI
    			int port = dp.getPort();*/
    
                System.out.println("Received");
    
                RTPSMessage msg = RTPSMessageBuilder.createMessage(RTPSEndian.BIG_ENDIAN);
    
                System.out.println("Message created");
    
                msg.setBuffer(buf.array(), dp.getLength());
                msg.initBinaryOutputStream();
    
                this.newRTPSMessage(msg);
            
                System.out.println("Buffer set");
            
            }


        } catch (java.nio.channels.AsynchronousCloseException ace) {
            // DO Nothing
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("");
            e.printStackTrace();
        }

    }

    private void newRTPSMessage(RTPSMessage msg) {
        synchronized(this.m_listenResource) {
            if (msg.getSize() == 0) {
                return;
            }

            System.out.println(""); // TODO Log this

            this.m_listenResource.getSenderLocator().setPort(this.m_listenResource.getSenderEndpoint().port);

            if (this.m_listenResource.getSenderEndpoint().address instanceof Inet4Address) {
                for (int i=0; i < 4; ++i) {
                    this.m_listenResource.getSenderLocator().getAddress()[i+12] = this.m_listenResource.getSenderEndpoint().address.getAddress()[i];
                }
            } else {
                for (int i=0; i < 16; ++i) {
                    this.m_listenResource.getSenderLocator().getAddress()[i] = this.m_listenResource.getSenderEndpoint().address.getAddress()[i];
                }
            }

            this.m_listenResource.getMessageReceiver().processCDRMessage(this.m_listenResource.getRTPSParticipant().getGUID().getGUIDPrefix(), this.m_listenResource.getSenderLocator(), msg);
            
            System.out.println("");

        }
    }

}
