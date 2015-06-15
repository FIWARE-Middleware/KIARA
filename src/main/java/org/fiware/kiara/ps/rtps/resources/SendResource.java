package org.fiware.kiara.ps.rtps.resources;

/*import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.InternetProtocolFamily;*/

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorKind;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.utils.IPFinder;
import org.fiware.kiara.ps.rtps.utils.IPTYPE;

import io.netty.channel.socket.nio.NioDatagramChannel;

import org.fiware.kiara.ps.rtps.utils.InfoIP;
import org.fiware.kiara.transport.impl.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eprosima.log.Log;

public class SendResource {
    
    public static final int MAX_BIND_TRIES = 100;
    
    private final Lock m_mutex;
    
    private boolean m_useIPv4;
    
    private boolean m_useIPv6;
    
    private List<Locator> m_sendLocatorIPv4;
    
    private List<Locator> m_sendLocatorIPv6;
    
    /*private List<io.netty.channel.socket.DatagramChannel> m_sendSocketIPv4;
    
    private List<io.netty.channel.socket.DatagramChannel> m_sendSocketIPv6;*/
    
    private List<java.nio.channels.DatagramChannel> m_sendSocketIPv4;
    
    private List<java.nio.channels.DatagramChannel> m_sendSocketIPv6;
    
    private int m_bytesSent;
    
    private boolean m_sendNext;
    
    private RTPSParticipant m_RTPSParticipant;
    
    private static final Logger logger = LoggerFactory.getLogger(RTPSParticipant.class);
    
    public SendResource() {
        
        this.m_sendLocatorIPv4 = new ArrayList<Locator>();
        this.m_sendLocatorIPv6 = new ArrayList<Locator>();
        
        this.m_useIPv4 = true;
        this.m_useIPv6 = true;
        
        this.m_bytesSent = 0;
        this.m_sendNext = true;
        
        this.m_mutex = new ReentrantLock(true);
        
    }
    
    public boolean initSend(RTPSParticipant participant, Locator loc, int sendSockBuffer, boolean useIPv4, boolean useIPv6) {
        
        this.m_useIPv4 = useIPv4;
        this.m_useIPv6 = useIPv6;
        
        List<InfoIP> locNames = IPFinder.getIPs();
        
        boolean notBind = true;
        boolean initialized = false;
        int bindTries = 0;
        for (InfoIP ipit : locNames) {
            if (ipit.type == IPTYPE.IPv4 && this.m_useIPv4) {

                this.m_sendLocatorIPv4.add(loc);

                DatagramChannel sendSocketV4;
                try {
                    sendSocketV4 = DatagramChannel.open(StandardProtocolFamily.INET6);
                    sendSocketV4.setOption(StandardSocketOptions.SO_SNDBUF, sendSockBuffer);
                } catch (IOException e) {
                    //e.printStackTrace();
                    logger.error("UDPv4 Error opening DatagramChannel");
                    return false;
                }

                bindTries = 0;
                AsioEndpoint endpoint = new AsioEndpoint();

                while (notBind && bindTries < SendResource.MAX_BIND_TRIES) {

                    try {
                        endpoint.address = InetAddress.getByName(ipit.name);
                        endpoint.port = loc.getPort();
                    } catch (UnknownHostException e) {
                        logger.error("UDPv4 Error obtaining addres from host: " + ipit.name);
                        return false;
                    }

                    InetSocketAddress sockAddr = new InetSocketAddress(endpoint.address, endpoint.port);
                    try {
                        sendSocketV4.bind(sockAddr);
                        notBind = false;
                    } catch (IOException e) {
                        logger.info("UDPv4 Error binding endpoint: " + endpoint.toString());
                        loc.incrementPort();
                    }

                    ++bindTries;
                }

                if (!notBind) {
                    try {
                        int size = sendSocketV4.getOption(StandardSocketOptions.SO_SNDBUF);
                        logger.info("UDPv4: " + sendSocketV4.getLocalAddress() + " || State: " + sendSocketV4.isOpen() + " || Buffer size: " + size);
                        initialized = true;
                    } catch (IOException e) {
                        logger.error("UDPv4: Maxmimum Number of tries while binding in this endpoint: " + endpoint.toString());
                        return false;
                    }
                } else {
                    logger.warn("UDPv6: Maxmimum Number of tries while binding in this interface: " + endpoint.toString());
                    this.m_sendLocatorIPv4.remove(loc);
                }

                notBind = true;

            } else if (ipit.type == IPTYPE.IPv6 && this.m_useIPv6) {
                this.m_sendLocatorIPv6.add(loc);

                DatagramChannel sendSocketV6;
                try {
                    sendSocketV6 = DatagramChannel.open(StandardProtocolFamily.INET);
                    sendSocketV6.setOption(StandardSocketOptions.SO_SNDBUF, sendSockBuffer);
                } catch (IOException e) {
                    //e.printStackTrace();
                    logger.error("UDPv6 Error opening DatagramChannel");
                    return false;
                }

                bindTries = 0;
                AsioEndpoint endpoint = new AsioEndpoint();

                while (notBind && bindTries < SendResource.MAX_BIND_TRIES) {

                    try {
                        endpoint.address = InetAddress.getByName(ipit.name);
                        endpoint.port = loc.getPort();
                    } catch (UnknownHostException e) {
                        logger.error("UDPv6 Error obtaining addres from host: " + ipit.name);
                        return false;
                    }

                    InetSocketAddress sockAddr = new InetSocketAddress(endpoint.address, endpoint.port);
                    try {
                        sendSocketV6.bind(sockAddr);
                        notBind = false;
                    } catch (IOException e) {
                        logger.info("UDPv6 Error binding endpoint: " + endpoint.toString());
                        loc.incrementPort();
                    }

                    ++bindTries;
                }

                if (!notBind) {
                    try {
                        int size = sendSocketV6.getOption(StandardSocketOptions.SO_SNDBUF);
                        logger.info("UDPv6: " + sendSocketV6.getLocalAddress() + " || State: " + sendSocketV6.isOpen() + " || Buffer size: " + size);
                        initialized = true;
                    } catch (IOException e) {
                        logger.error("UDPv6: Maxmimum Number of tries while binding in this endpoint: " + endpoint.toString());
                        return false;
                    }
                } else {
                    logger.warn("UDPv6: Maxmimum Number of tries while binding in this interface: " + endpoint.toString());
                    this.m_sendLocatorIPv6.remove(loc);
                }

                notBind = true;
            }
        }
        
        return initialized;
    }
    
    public Object sendSync(RTPSMessage msg, Locator loc) {
        // TODO Implement
        return null;
    }

    public void looseNextChange() {
        // TODO Implement
        
    }
    
}
