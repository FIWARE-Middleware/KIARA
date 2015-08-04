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

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.utils.IPFinder;
import org.fiware.kiara.ps.rtps.utils.IPTYPE;

import io.netty.channel.socket.nio.NioDatagramChannel;

import org.fiware.kiara.netty.NioDatagramChannelFactory;
import org.fiware.kiara.ps.rtps.utils.InfoIP;
import org.fiware.kiara.transport.impl.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;

import java.net.Inet4Address;
import java.util.Arrays;
import java.util.logging.Level;

import org.fiware.kiara.ps.rtps.common.LocatorKind;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class SendResource {

    public static final int MAX_BIND_TRIES = 100;

    private final Lock m_mutex;

    private boolean m_useIPv4;

    private boolean m_useIPv6;

    private final List<Locator> m_sendLocatorIPv4;

    private final List<Locator> m_sendLocatorIPv6;

    private final List<DatagramChannel> m_sendSocketIPv4;

    private final List<DatagramChannel> m_sendSocketIPv6;

    private InetSocketAddress m_sendEndpointV4;
    private InetSocketAddress m_sendEndpointV6;

    private int m_bytesSent;

    private boolean m_sendNext;

    private RTPSParticipant m_RTPSParticipant;

    private static final Logger logger = LoggerFactory.getLogger(RTPSParticipant.class);

    public SendResource() {
        this.m_sendLocatorIPv4 = new ArrayList<>();
        this.m_sendLocatorIPv6 = new ArrayList<>();
        this.m_sendSocketIPv4 = new ArrayList<>();
        this.m_sendSocketIPv6 = new ArrayList<>();

        this.m_useIPv4 = true;
        this.m_useIPv6 = true;

        this.m_bytesSent = 0;
        this.m_sendNext = true;

        this.m_mutex = new ReentrantLock(true);

    }

    /**
     * Initialize the sending socket.
     *
     * @param participant
     * @param loc Locator of the address from where to start the sending socket.
     * @param sendSockBuffer
     * @param useIPv4 Boolean telling whether to use IPv4
     * @param useIPv6 Boolean telling whether to use IPv6
     * @return true on success
     */
    public boolean initSend(RTPSParticipant participant, Locator loc, int sendSockBuffer, boolean useIPv4, boolean useIPv6) {

        this.m_useIPv4 = useIPv4;
        this.m_useIPv6 = useIPv6;

        List<InfoIP> locNames = IPFinder.getIPs();

        boolean notBind = true;
        boolean initialized = false;
        int bindTries = 0;

        for (InfoIP ipit : locNames) {
            if (ipit.type == IPTYPE.IPv4 && this.m_useIPv4) {

                Locator sendLocV4 = new Locator(loc);

                DatagramChannel sendSocketV4 = null;
                Bootstrap b = new Bootstrap();
                b.group(Global.transportGroup)
                        .handler(new ChannelInitializer<NioDatagramChannel>() {

                            @Override
                            protected void initChannel(NioDatagramChannel ch) throws Exception {
                            }

                        });

                b.channelFactory(new NioDatagramChannelFactory(InternetProtocolFamily.IPv4));
                b.option(ChannelOption.SO_SNDBUF, sendSockBuffer);

                bindTries = 0;
                InetSocketAddress sendEndpoint = null;

                while (notBind && bindTries < SendResource.MAX_BIND_TRIES) {

                    try {
                        sendEndpoint = new InetSocketAddress(InetAddress.getByName(ipit.name), sendLocV4.getPort());
                    } catch (UnknownHostException e) {
                        logger.error("UDPv4 Error obtaining addres from host: " + ipit.name, e);
                        return false;
                    }

                    try {
                        sendSocketV4 = (DatagramChannel) b.bind(sendEndpoint).sync().channel();
                        notBind = false;
                    } catch (Exception e) {
                        logger.info("UDPv4 Error binding endpoint: ({}) error: {}", sendEndpoint.toString(), e);
                        sendLocV4.increasePort();
                    }

                    ++bindTries;
                }

                if (!notBind) {
                    assert sendSocketV4 != null;
                    try {
                        int size = sendSocketV4.config().getOption(ChannelOption.SO_SNDBUF);
                        logger.info("UDPv4: " + sendSocketV4.localAddress() + " || State: " + sendSocketV4.isOpen() + " || Buffer size: " + size);
                        m_sendLocatorIPv4.add(sendLocV4);
                        m_sendSocketIPv4.add(sendSocketV4);
                        initialized = true;
                    } catch (Exception e) {
                        logger.error("UDPv4: Maxmimum Number of tries while binding in this endpoint: " + sendEndpoint.toString());
                        return false;
                    }
                } else {
                    logger.warn("UDPv4: Maxmimum Number of tries while binding in this interface: " + sendEndpoint.toString());
                }

                notBind = true;

            } else if (ipit.type == IPTYPE.IPv6 && this.m_useIPv6) {

                Locator sendLocV6 = new Locator(loc);

                DatagramChannel sendSocketV6 = null;
                Bootstrap b = new Bootstrap();
                b.group(Global.transportGroup)
                        .handler(new ChannelInitializer<NioDatagramChannel>() {

                            @Override
                            protected void initChannel(NioDatagramChannel ch) throws Exception {
                            }

                        });

                b.channelFactory(new NioDatagramChannelFactory(InternetProtocolFamily.IPv6));
                b.option(ChannelOption.SO_SNDBUF, sendSockBuffer);

                bindTries = 0;
                InetSocketAddress sendEndpoint = null;

                while (notBind && bindTries < SendResource.MAX_BIND_TRIES) {

                    try {
                        sendEndpoint = new InetSocketAddress(InetAddress.getByName(ipit.name), sendLocV6.getPort());
                    } catch (UnknownHostException e) {
                        logger.error("UDPv6 Error obtaining addres from host: " + ipit.name, e);
                        return false;
                    }

                    try {
                        sendSocketV6 = (DatagramChannel) b.bind(sendEndpoint).sync().channel();
                        notBind = false;
                    } catch (Exception e) {
                        logger.info("UDPv6 Error binding endpoint: ({}) error: {}", sendEndpoint.toString(), e);
                        sendLocV6.increasePort();
                    }

                    ++bindTries;
                }

                if (!notBind) {
                    assert sendSocketV6 != null;
                    try {
                        int size = sendSocketV6.config().getOption(ChannelOption.SO_SNDBUF);
                        logger.info("UDPv6: {} || State: {} || Buffer size: {}", sendSocketV6.localAddress(), sendSocketV6.isOpen(), size);
                        this.m_sendLocatorIPv6.add(sendLocV6);
                        m_sendSocketIPv6.add(sendSocketV6);
                        initialized = true;
                    } catch (Exception e) {
                        logger.error("UDPv6: Maxmimum Number of tries while binding in this endpoint: {}", sendEndpoint.toString(), e);
                        return false;
                    }
                } else {
                    logger.warn("UDPv6: Maxmimum Number of tries while binding in this interface: {}", sendEndpoint.toString());
                }

                notBind = true;
            }
        }

        return initialized;
    }

    /**
     * Send a CDR message synchronously. No waiting is required.
     *
     * @param msg Pointer to the message.
     * @param loc Locator where to send the message.
     */
    public synchronized void sendSync(RTPSMessage msg, Locator loc) {
        m_mutex.lock();

        try {
            if (loc.getPort() == 0) {
                return;
            }

            // Should we call this or the caller ?
            //msg.serialize(); //??? FIXME

            if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv4 && m_useIPv4) {
                byte[] srcAddr = loc.getAddress();

                byte[] addr = new byte[4];
                for (int i = 0; i < 4; ++i) {
                    addr[i] = srcAddr[12 + i];
                }

                try {
                    //if (this.m_sendEndpointV4 == null) {
                        m_sendEndpointV4 = new InetSocketAddress(InetAddress.getByAddress(addr), loc.getPort());
                    //}
                } catch (UnknownHostException e) {
                    logger.error("UDPv4 Error obtaining address: {}", Arrays.toString(addr));
                    return;
                }

                for (DatagramChannel sockit : m_sendSocketIPv4) {
                    logger.info("UDPv4: {} bytes TO endpoint: {} FROM {}", msg.getSize(), m_sendEndpointV4, sockit.localAddress());
                    if (m_sendEndpointV4.getPort() > 0) {
                        m_bytesSent = 0;
                        if (m_sendNext) {
                            try {
                                if (!this.m_sendEndpointV4.getAddress().equals(InetAddress.getByAddress(new byte[] {0,0,0,0}))) { // TODO Fix 0.0.0.0 case
                                    sockit.writeAndFlush(new DatagramPacket(
                                            Unpooled.wrappedBuffer(msg.getBuffer()),
                                            m_sendEndpointV4)).syncUninterruptibly();
                                }
                            } catch (Exception error) {
                                // Should print the actual error message
                                logger.warn(error.toString());
                               // error.printStackTrace();
                            }

                        } else {
                            m_sendNext = true;
                        }
                        logger.info("SENT {}", msg.getBuffer().length);
                    } else if (m_sendEndpointV4.getPort() <= 0) {
                        logger.warn("Port invalid: {}", m_sendEndpointV4.getPort());
                    } else {
                        logger.error("Port error");
                    }
                }
            } else if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv6 && m_useIPv6) {
                byte[] srcAddr = loc.getAddress();

                byte[] addr = new byte[16];
                for (int i = 0; i < 16; ++i) {
                    addr[i] = srcAddr[i];
                }

                try {
                    if (this.m_sendEndpointV6 == null) {
                        m_sendEndpointV6 = new InetSocketAddress(InetAddress.getByAddress(addr), loc.getPort());
                    }
                } catch (UnknownHostException e) {
                    logger.error("UDPv6 Error obtaining address: " + Arrays.toString(addr), e);
                    return;
                }

                for (DatagramChannel sockit : m_sendSocketIPv6) {
                    logger.info("UDPv6: {} bytes TO endpoint: {} FROM {}", msg.getSize(), m_sendEndpointV6, sockit.localAddress());
                    if (m_sendEndpointV6.getPort() > 0) {
                        m_bytesSent = 0;
                        if (m_sendNext) {
                            try {

                                sockit.writeAndFlush(new DatagramPacket(
                                        Unpooled.wrappedBuffer(msg.getBuffer()),
                                        m_sendEndpointV6)).sync();

                            } catch (Exception error) {
                                // Should print the actual error message
                                logger.error("Thread V6: " + Thread.currentThread().getId() + " - " + error.toString());
                            }

                        } else {
                            m_sendNext = true;
                        }
                        logger.info("SENT {}", msg.getBuffer().length);
                    } else if (m_sendEndpointV6.getPort() <= 0) {
                        logger.warn("Port invalid: {}", m_sendEndpointV6.getPort());
                    } else {
                        logger.error("Port error");
                    }
                }
            } else {
                logger.info("Destination {} not valid for this ListenResource", loc);
            }

        } finally {
            m_mutex.unlock();
        }

    }

    //!FOR TESTING ONLY!!!!
    public void looseNextChange() {
        m_sendNext = false;
    }

    public Lock getMutex() {
        return m_mutex;
    }

    public void destroy() {
        this.m_mutex.lock();
        try {
            for (DatagramChannel channel : this.m_sendSocketIPv4) {
                channel.eventLoop().shutdownGracefully();
                channel.disconnect();
                channel.close();
            }
            for (DatagramChannel channel : this.m_sendSocketIPv6) {
                channel.eventLoop().shutdownGracefully();
                channel.disconnect();
                channel.close();
            }
        } finally {
            this.m_mutex.unlock();
        }
    }

}
