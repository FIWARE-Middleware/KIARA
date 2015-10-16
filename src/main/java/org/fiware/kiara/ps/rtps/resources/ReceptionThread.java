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
import java.nio.channels.DatagramChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageBuilder;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the message reception thread.
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class ReceptionThread implements Runnable {

    /**
     * {@link DatagramChannel} the thread will receive from
     */
    private DatagramChannel m_channel;

    /**
     * {@link ListenResource} that created this {@link ReceptionThread}
     */
    private ListenResource m_listenResource;

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(ReceptionThread.class);
    
    /**
     * Indicates if the thread is in execution
     */
    private volatile boolean running = true;
    
    /**
     * Mutex
     */
    private Lock m_mutex = new ReentrantLock(true);

    /**
     * {@link ReceptionThread} constructor
     * 
     * @param channel {@link DatagramChannel} associated to the thread
     * @param listenResource {@link ListenResource} that creates the thread
     */
    public ReceptionThread(DatagramChannel channel, ListenResource listenResource) {
        this.m_channel = channel;
        this.m_listenResource = listenResource;
    }

    /**
     * Main method
     */
    @Override
    public void run() {

        this.m_listenResource.getRTPSParticipant().resourceSemaphorePost();

        byte[] buf;
        try {

            buf = new byte[this.m_channel.socket().getReceiveBufferSize()];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);

            logger.debug("Thread {} listening in IP {}:{}", Thread.currentThread().getId(), this.m_channel.socket().getLocalAddress().getHostAddress(), this.m_channel.socket().getLocalPort());
            
            while(running && this.m_channel.isOpen()) {
                
                dp.setLength(buf.length);
                this.m_channel.socket().receive(dp);
                
                this.m_listenResource.getSenderEndpoint().port = dp.getPort();
                this.m_listenResource.getSenderEndpoint().address = dp.getAddress();

                RTPSMessage msg = RTPSMessageBuilder.createMessage(RTPSEndian.BIG_ENDIAN);

                msg.setBuffer(buf, dp.getLength());
                msg.initBinaryOutputStream();

                this.newRTPSMessage(msg);

            }
            
        } catch (java.nio.channels.AsynchronousCloseException ace) {
            // DO Nothing
            

        } catch (IOException e) {
            logger.error(e.toString());
            //e.printStackTrace();
        } 
        
    }

    /**
     * This method is executed when a new {@link RTPSMessage} has been received
     * 
     * @param msg Received {@link RTPSMessage}
     */
    private void newRTPSMessage(RTPSMessage msg) {
        this.m_mutex.lock();
        try {
            if (msg.getSize() == 0) {
                return;
            }

            try {
                if (this.m_channel.isOpen()) {
                    logger.debug("Received {} bytes FROM {} TO {}", msg.getSize(), this.m_listenResource.getSenderEndpoint().toString(), this.m_channel.getLocalAddress());
                }
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

        } finally {
            this.m_mutex.unlock();
        }
    }
    
    /**
     * Finishes the thread
     */
    public void terminate() {
        this.running = false;
    }
}
