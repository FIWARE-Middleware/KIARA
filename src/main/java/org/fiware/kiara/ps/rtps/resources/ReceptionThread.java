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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import com.eprosima.log.Log;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class ReceptionThread implements Runnable {

    private DatagramChannel m_channel;

    private ListenResource m_listenResource;
    
    private static final Logger logger = LoggerFactory.getLogger(ReceptionThread.class);

    //private final Lock m_mutex = new ReentrantLock(true);

    public ReceptionThread(DatagramChannel channel, ListenResource listenResource) {
        this.m_channel = channel;
        this.m_listenResource = listenResource;
    }

    @Override
    public void run() {

        this.m_listenResource.getRTPSParticipant().resourceSemaphorePost();
        
        ByteBuffer buf;
        try {
            //System.out.println("Size: " + this.m_channel.socket().getReceiveBufferSize());
            buf = ByteBuffer.allocate(this.m_channel.socket().getReceiveBufferSize());

            DatagramPacket dp = new DatagramPacket(buf.array(), this.m_channel.socket().getReceiveBufferSize());
            //System.out.println(this.m_channel.socket().getLocalPort());

            System.out.println("---");
            logger.info("Thread " + Thread.currentThread().getId() + " listening in IP " + this.m_channel.socket().getLocalAddress().getHostAddress() + ":" + this.m_channel.socket().getLocalPort());
            System.out.println("---");
            
            while(true) {
            
                this.m_channel.socket().receive(dp);
                
                System.out.println("Message received");
                
                this.m_listenResource.getSenderEndpoint().port = dp.getPort();
                this.m_listenResource.getSenderEndpoint().address = dp.getAddress();
    
                RTPSMessage msg = RTPSMessageBuilder.createMessage(RTPSEndian.BIG_ENDIAN);
    
                //System.out.println("Message created");
    
                msg.setBuffer(buf.array(), dp.getLength());
                msg.initBinaryOutputStream();
    
                this.newRTPSMessage(msg);
            
                //System.out.println("Buffer set");
            
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

            try {
                logger.info(msg.getSize() + " bytes FROM " + this.m_listenResource.getSenderEndpoint().toString() + " TO: " + this.m_channel.getLocalAddress());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

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
            
            //System.out.println("");

        }
    }

}
