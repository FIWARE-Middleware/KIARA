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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.channel.socket.nio.NioDatagramChannel;

import org.fiware.kiara.netty.NioDatagramChannelFactory;
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

import java.util.Arrays;

import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorKind;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.utils.IPFinder;
import org.fiware.kiara.ps.rtps.utils.IPTYPE;
import org.fiware.kiara.ps.rtps.utils.InfoIP;

/**
 * This class is the one in charge of sending messages over
 * the wire. It contains all the informacion about the endpoints
 * to which the data will be sent.
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class SendResource {

    /**
     * Maximum number of binding tries
     */
    public static final int MAX_BIND_TRIES = 100;

    /**
     * This value indicates whether to se IPv4 or not
     */
    private boolean m_useIPv4;

    /**
     * This value indicates whether to se IPv6 or not
     */
    private boolean m_useIPv6;

    /**
     * List of IPv4 {@link Locator}s
     */
    private final List<Locator> m_sendLocatorIPv4;

    /**
     * List of IPv6 {@link Locator}s
     */
    private final List<Locator> m_sendLocatorIPv6;

    /**
     * List of IPv4 {@link DatagramChannel}s to send data to
     */
    private final List<DatagramChannel> m_sendSocketIPv4;

    /**
     * List of IPv6 {@link DatagramChannel}s to send data to
     */
    private final List<DatagramChannel> m_sendSocketIPv6;

    /**
     * IPv4 sending endpoint
     */
    private InetSocketAddress m_sendEndpointV4;

    /**
     * IPv6 sending endpoint
     */
    private InetSocketAddress m_sendEndpointV6;

    /**
     * Tells whether to send next message or not
     */
    private boolean m_sendNext;

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(RTPSParticipant.class);

    /**
     * Mutex
     */
    private final Lock m_mutex;

    /**
     * Default {@link SendResource} constructor
     */
    public SendResource() {
        this.m_sendLocatorIPv4 = new ArrayList<>();
        this.m_sendLocatorIPv6 = new ArrayList<>();
        this.m_sendSocketIPv4 = new ArrayList<>();
        this.m_sendSocketIPv6 = new ArrayList<>();
        this.m_useIPv4 = true;
        this.m_useIPv6 = true;
        this.m_sendNext = true;
        this.m_mutex = new ReentrantLock(true);
    }

    /**
     * Initialize the sending socket.
     *
     * @param participant The {@link RTPSParticipant} that creates the {@link SendResource}
     * @param loc Locator of the address from where to start the sending socket.
     * @param sendSockBuffer Send buffer
     * @param useIPv4 Boolean telling whether to use IPv4
     * @param useIPv6 Boolean telling whether to use IPv6
     * @return true on success; false otherwise
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
                        logger.debug("UDPv4 Error binding endpoint: ({}) error: {}", sendEndpoint.toString(), e);
                        sendLocV4.increasePort();
                    }

                    ++bindTries;
                }

                if (!notBind) {
                    assert sendSocketV4 != null;
                    try {
                        int size = sendSocketV4.config().getOption(ChannelOption.SO_SNDBUF);
                        logger.debug("UDPv4: {} || State: {} || Buffer size: {}", sendSocketV4.isOpen(), size, sendSocketV4.localAddress());
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
                        logger.debug("UDPv6 Error binding endpoint: ({}) error: {}", sendEndpoint.toString(), e);
                        sendLocV6.increasePort();
                    }

                    ++bindTries;
                }

                if (!notBind) {
                    assert sendSocketV6 != null;
                    try {
                        int size = sendSocketV6.config().getOption(ChannelOption.SO_SNDBUF);
                        logger.debug("UDPv6: {} || State: {} || Buffer size: {}", sendSocketV6.localAddress(), sendSocketV6.isOpen(), size);
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
     * @param msg Reference to the message.
     * @param loc Locator where to send the message.
     */
    public synchronized void sendSync(RTPSMessage msg, Locator loc) {
        m_mutex.lock();

        try {
            if (loc.getPort() == 0) {
                return;
            }

            if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv4 && m_useIPv4) {
                byte[] srcAddr = loc.getAddress();

                byte[] addr = new byte[4];
                for (int i = 0; i < 4; ++i) {
                    addr[i] = srcAddr[12 + i];
                }

                try {
                    m_sendEndpointV4 = new InetSocketAddress(InetAddress.getByAddress(addr), loc.getPort());
                } catch (UnknownHostException e) {
                    logger.error("UDPv4 Error obtaining address: {}", Arrays.toString(addr));
                    return;
                }

                for (DatagramChannel sockit : m_sendSocketIPv4) {
                    logger.debug("UDPv4 Sending {} bytes TO {} FROM {}", msg.getSize(), m_sendEndpointV4, sockit.localAddress());
                    if (m_sendEndpointV4.getPort() > 0) {
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
                        logger.debug("UDPv4 Sent {} bytes TO endpoint {}", msg.getBuffer().length, m_sendEndpointV4);
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
                    logger.debug("UDPv6 Sending {} bytes TO {} FROM {}", msg.getSize(), m_sendEndpointV6, sockit.localAddress());
                    if (m_sendEndpointV6.getPort() > 0) {
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
                        logger.debug("UDPv6 Sent {} bytes TO endpoint {}", msg.getBuffer().length, m_sendEndpointV6);
                    } else if (m_sendEndpointV6.getPort() <= 0) {
                        logger.warn("Port invalid: {}", m_sendEndpointV6.getPort());
                    } else {
                        logger.error("Port error");
                    }
                }
            } else {
                logger.debug("Destination {} not valid for this ListenResource (Use IPv4: {}, Use IPv6: {})", loc, this.m_useIPv4, this.m_useIPv6);
            }

        } finally {
            m_mutex.unlock();
        }

    }

    /**
     * Looses next change (for testing purposes only)
     */
    public void looseNextChange() {
        m_sendNext = false;
    }

    /**
     * Get the {@link Lock} mutex
     * 
     * @return The {@link Lock} mutex
     */
    public Lock getMutex() {
        return m_mutex;
    }

    /**
     * Destroys the {@link SendResource} and all the associated objects
     */
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
