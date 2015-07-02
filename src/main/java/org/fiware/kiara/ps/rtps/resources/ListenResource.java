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

import org.fiware.kiara.netty.NioDatagramChannelFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.internal.StringUtil;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.channels.MembershipKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.Endpoint;
import org.fiware.kiara.ps.rtps.common.EndpointKind;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorKind;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.messages.MessageReceiver;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.utils.IPFinder;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.fiware.kiara.transport.impl.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class ListenResource {

    private MessageReceiver m_receiver;

    private List<RTPSReader> m_assocReaders;

    private List<RTPSWriter> m_assocWriters;

    private LocatorList m_listenLocators;

    private Locator m_senderLocator;

    private RTPSParticipant m_RTPSParticipant;

    private final int m_ID;

    private boolean m_isDefaultListenResource;

    //private java.net.Socket m_listenEndpoint;
    private AsioEndpoint m_listenEndpoint;

    private AsioEndpoint m_senderEndpoint;

    //private java.net.DatagramSocket m_listenSocket;

    //private DatagramChannel m_listenChannel;

    //private io.netty.channel.socket.DatagramChannel m_listenChannel;
    private java.nio.channels.DatagramChannel m_listenChannel;

    private io.netty.channel.socket.DatagramChannel m_listenChannelNetty;

    private final Lock m_mutex = new ReentrantLock(true);

    private Thread m_thread;

    //0private java.nio.channels.AsynchronousDatagramChannel channel = DatagramChannel.open().r

    //DatagramChannel channel = DatagramChannel.open().

    private static final Logger logger = LoggerFactory.getLogger(ListenResource.class);

    public ListenResource(RTPSParticipant participant, int ID, boolean isDefault) {
        this.m_assocReaders = new ArrayList<RTPSReader>();
        this.m_assocWriters = new ArrayList<RTPSWriter>();
        this.m_listenLocators = new LocatorList();
        this.m_RTPSParticipant = participant;
        this.m_ID = ID;
        this.m_isDefaultListenResource = isDefault;
        this.m_listenEndpoint = new AsioEndpoint();
        this.m_senderEndpoint = new AsioEndpoint();
        this.m_senderLocator = new Locator();
    }

    public void destroy() {
        if (this.m_thread != null) {
            logger.info("Removing listening thread " + this.m_thread.getId());
            try {
                this.m_listenChannel.socket().close();
                this.m_listenChannel.disconnect();
                this.m_listenChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            logger.info("Joining with thread");
            try {
                this.m_thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            logger.info("Listening thread closed successfully");
        }
    }

    public Locator getSenderLocator() {
        return this.m_senderLocator;
    }

    public AsioEndpoint getSenderEndpoint() {
        return this.m_senderEndpoint;
    }

    public MessageReceiver getMessageReceiver() {
        return this.m_receiver;
    }

    public RTPSParticipant getRTPSParticipant() {
        return this.m_RTPSParticipant;
    }

    public boolean addAssociatedEndpoint(Endpoint endpoint) {
        this.m_mutex.lock();
        try {
            boolean found = false;
            if (endpoint.getAttributes().endpointKind == EndpointKind.WRITER) {
                for (RTPSWriter it : this.m_assocWriters) {
                    if (it.getGuid().getEntityId().equals(endpoint.getGuid().getEntityId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    this.m_assocWriters.add((RTPSWriter) endpoint);
                    logger.info(endpoint.getGuid().getEntityId()+ " added to listen locators list"); 
                    //this.m_mutex.unlock();
                    return true;
                }
            } else if (endpoint.getAttributes().endpointKind == EndpointKind.READER) {
                for (RTPSReader it : this.m_assocReaders) {
                    if (it.getGuid().getEntityId().equals(endpoint.getGuid().getEntityId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    this.m_assocReaders.add((RTPSReader) endpoint);
                    logger.info("Endpoint " + endpoint.getGuid().getEntityId()+ " added to listemn locators list.");
                    //this.m_mutex.unlock();
                    return true;
                }
            }
        } finally {
            this.m_mutex.unlock();
        }

        return false;
    }

    public boolean removeAssociatedEndpoint(Endpoint endpoint) {

        this.m_mutex.lock();

        try {
            if (endpoint.getAttributes().endpointKind == EndpointKind.WRITER) {
                for (int i=0; i < this.m_assocWriters.size(); ++i) {
                    RTPSWriter it = this.m_assocWriters.get(i);
                    if (it.getGuid().getEntityId().equals(endpoint.getGuid().getEntityId())) {
                        this.m_assocWriters.remove(endpoint);
                        this.m_mutex.unlock();
                        i--;
                        return true;
                    }
                }
            } else if (endpoint.getAttributes().endpointKind == EndpointKind.READER) {
                for (int i=0; i < this.m_assocReaders.size(); ++i) {
                    RTPSReader it = this.m_assocReaders.get(i);
                    if (it.getGuid().getEntityId().equals(endpoint.getGuid().getEntityId())) {
                        this.m_assocReaders.remove(endpoint);
                        this.m_mutex.unlock();
                        i--;
                        return true;
                    }
                }
            }
        } finally {
            this.m_mutex.unlock();
        }

        return false;
    }

    public List<RTPSReader> getAssocReaders() {
        return this.m_assocReaders;
    }

    public LocatorList getListenLocators() {
        return this.m_listenLocators;
    }

    public void getLocatorAdresses(Locator loc) {
        if (!loc.isAddressDefined()) { // Listen in all interfaces
            logger.info("Defined Locastor IP with 0s (listen to all interfaces), listening to all interfaces"); 
            LocatorList myIP = null;
            if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv4) {
                myIP = IPFinder.getIPv4Adress();
                this.m_listenEndpoint.address = IPFinder.getFirstIPv4Adress();
            } else if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv6) {
                myIP = IPFinder.getIPv6Adress();
                this.m_listenEndpoint.address = IPFinder.getFirstIPv6Adress();
            }
            if (myIP != null) {
                for (Locator locIt : myIP.getLocators()) {
                    locIt.setPort(loc.getPort());
                    this.m_listenLocators.pushBack(locIt);
                }
            }
        } else {
            try {
                if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv4) {
                    this.m_listenEndpoint.address = Inet4Address.getByName(loc.toIPv4String());
                } else if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv6) {
                    this.m_listenEndpoint.address = Inet6Address.getByAddress(loc.getAddress());
                }
                this.m_listenLocators.pushBack(loc);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        this.m_listenEndpoint.port = loc.getPort();
    }

    public boolean initThread(RTPSParticipant participant, Locator loc, int listenSocketSize, boolean isMulticast, boolean isFixed) {
        logger.info("Creating ListenResource in " + loc + " with ID " + this.m_ID); 

        this.m_RTPSParticipant = participant;
        if (!loc.isAddressDefined() && isMulticast) {
            logger.warn("MulticastAddresses need to have the IP defined, ignoring this address"); 
            return false;
        }
        this.m_receiver = new MessageReceiver(listenSocketSize);
        this.m_receiver.setListenResource(this);

        this.getLocatorAdresses(loc);

        logger.info("Initializing in : " + this.m_listenLocators); 

        InetAddress multicastAddress = null;

        try {

            if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv4) {
                //this.m_listenChannel = DatagramChannel.open(StandardProtocolFamily.INET);
                this.m_listenChannel = java.nio.channels.DatagramChannel.open(StandardProtocolFamily.INET);
            } else if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv6) {
                this.m_listenChannel = java.nio.channels.DatagramChannel.open(StandardProtocolFamily.INET6);
            }

            //this.m_listenChannel.configureBlocking(false);
            this.m_listenChannel.setOption(StandardSocketOptions.SO_RCVBUF, listenSocketSize);
            this.m_listenChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

            //this.m_listenSocket = new java.net.MulticastSocket(null this.m_listenEndpoint.port, this.m_listenEndpoint.address);
            //this.m_listenSocket.setReceiveBufferSize(listenSocketSize);
            //this.m_listenSocket.setReuseAddress(true);
        } catch (SocketException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        if (isMulticast) {
            multicastAddress = this.m_listenEndpoint.address;
            if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv4) {
                //this.m_listenSocket.setOption(SocketOptions., value)
                this.m_listenEndpoint.address = IPFinder.getFirstIPv4Adress();
            } else if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv6) {
                this.m_listenEndpoint.address = IPFinder.getFirstIPv6Adress();
            }
        }

        if (isFixed) {
            InetSocketAddress sockAddr = new InetSocketAddress(this.m_listenEndpoint.address, this.m_listenEndpoint.port);
            try {
                this.m_listenChannel.socket().bind(sockAddr);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }


        } else {
            boolean binded = false;

            for (int i=0; i < 1000; ++i) {
                this.m_listenEndpoint.port += 1;
                InetSocketAddress sockAddr = new InetSocketAddress(/*this.m_listenEndpoint.address, */this.m_listenEndpoint.port); // TODO: Delete addr
                try {
                    this.m_listenChannel.socket().bind(sockAddr);
                    binded = true;
                    break;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }

            }

            if (!binded) {
                logger.error("Tried 1000 ports and none was working, last tried: " + this.m_listenEndpoint.port);
                Thread.currentThread().interrupt();
            } else {
                for (Locator it : this.m_listenLocators.getLocators()) {
                    it.setPort(this.m_listenEndpoint.port);
                }
            }
        }

        if (isMulticast && multicastAddress != null) {
            joinMulticastGroup(multicastAddress);
        }

        ReceptionThread runnable = new ReceptionThread(this.m_listenChannel, this);
        this.m_thread = new Thread(runnable, "");
        this.m_thread.start();

        this.m_RTPSParticipant.resourceSemaphoreWait();

        return true;

    }

    private void joinMulticastGroup(InetAddress multicastAddress) {

        LocatorList loclist = new LocatorList();

        if (this.m_listenEndpoint.address instanceof Inet4Address) {
            loclist = IPFinder.getIPv4Adress();
            for (Locator it : loclist.getLocators()) {
                try {
                    InetSocketAddress sockAddr = new InetSocketAddress(multicastAddress, 0);
                    NetworkInterface netInt = NetworkInterface.getByInetAddress(InetAddress.getByName(it.toIPv4String()));
                    this.m_listenChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, netInt);
                    MembershipKey key = this.m_listenChannel.join(multicastAddress, netInt);

                    System.err.printf("MulticastJoin: Address: %s, NetIf: %s, Key: %s%n", multicastAddress, netInt, key);
                    if (!key.isValid()) {
                        System.err.println("Invalid membership key: "+key);
                    }
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else if (this.m_listenEndpoint.address instanceof Inet6Address) {
            loclist = IPFinder.getIPv6Adress();
            //int index = 0;
            for (Locator it : loclist.getLocators()) {
                try {
                    //((MulticastSocket) this.m_listenSocket).joinGroup(Inet6Address.getByAddress(it.getAddress()));
                    NetworkInterface netInt = NetworkInterface.getByInetAddress(Inet6Address.getByAddress(it.getAddress()));
                    this.m_listenChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, netInt);
                    this.m_listenChannel.join(Inet6Address.getByAddress(it.getAddress()), netInt);
                    //++index;
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean initThreadNetty(RTPSParticipant participant, Locator loc, int listenSocketSize, boolean isMulticast, boolean isFixed) {
        logger.info("Creating ListenResource in " + loc + " with ID " + this.m_ID); 
        this.m_RTPSParticipant = participant;
        if (!loc.isAddressDefined() && isMulticast) {
            logger.warn("MulticastAddresses need to have the IP defined, ignoring this address"); 
            return false;
        }
        this.m_receiver = new MessageReceiver(listenSocketSize);
        this.m_receiver.setListenResource(this);

        this.getLocatorAdresses(loc);

        logger.info("Initializing in : " + this.m_listenLocators); 

        InetAddress multicastAddress = null;

        Bootstrap b = new Bootstrap();

        if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv4) {
            b.channelFactory(new NioDatagramChannelFactory(InternetProtocolFamily.IPv4));
        } else if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv6) {
            b.channelFactory(new NioDatagramChannelFactory(InternetProtocolFamily.IPv6));
        }

        EventLoopGroup group = Global.transportGroup;
        b.group(group)
        .handler(new ReceptionHandler(this))
        .option(ChannelOption.SO_RCVBUF, listenSocketSize)
        .option(ChannelOption.SO_REUSEADDR, true);


        /*try {
                    //System.out.println(this.m_listenSocket.getLocalAddress().toString());
            } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            }*/
        //java.net.ServerSocket recvSocket = new ServerSocket();
        //recvSocket.
        if (isMulticast) {
            multicastAddress = this.m_listenEndpoint.address;
            if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv4) {
                //this.m_listenSocket.setOption(SocketOptions., value)
                this.m_listenEndpoint.address = IPFinder.getFirstIPv4Adress();
            } else if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv6) {
                this.m_listenEndpoint.address = IPFinder.getFirstIPv6Adress();
            }
        }

        if (isFixed) {
            InetSocketAddress sockAddr = new InetSocketAddress(this.m_listenEndpoint.address, this.m_listenEndpoint.port);
            try {
                this.m_listenChannelNetty = (DatagramChannel)b.bind(sockAddr).sync().channel();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            boolean binded = false;

            for (int i=0; i < 1000; ++i) {
                this.m_listenEndpoint.port += 1;
                InetSocketAddress sockAddr = new InetSocketAddress(/*this.m_listenEndpoint.address, */this.m_listenEndpoint.port);
                try {
                    System.err.println(sockAddr); //???DEBUG

                    this.m_listenChannelNetty = (DatagramChannel)b.bind(sockAddr).sync().channel();
                    binded = true;
                    break;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            if (!binded) {
                System.out.println("Tried 1000 ports and none was working, last tried: " + this.m_listenEndpoint.port);
            } else {
                for (Locator it : this.m_listenLocators.getLocators()) {
                    it.setPort(this.m_listenEndpoint.port);
                }
            }
        }

        if (isMulticast && multicastAddress != null) {
            joinMulticastGroupNetty(multicastAddress);
        }

        System.out.println("Finishing ListenResource thread");

        // TODO Thread stuff

        return true;

    }

    private void joinMulticastGroupNetty(InetAddress multicastAddress) {

        LocatorList loclist;

        if (this.m_listenEndpoint.address instanceof Inet4Address) {
            loclist = IPFinder.getIPv4Adress();
            for (Locator it : loclist.getLocators()) {
                try {
                    InetSocketAddress sockAddr = new InetSocketAddress(multicastAddress, 0);
                    NetworkInterface netInt = NetworkInterface.getByInetAddress(InetAddress.getByName(it.toIPv4String()));
                    this.m_listenChannelNetty.config().setOption(ChannelOption.IP_MULTICAST_IF, netInt);
                    this.m_listenChannelNetty.joinGroup(multicastAddress, netInt, null).sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else if (this.m_listenEndpoint.address instanceof Inet6Address) {
            loclist = IPFinder.getIPv6Adress();
            //int index = 0;
            for (Locator it : loclist.getLocators()) {
                try {
                    //((MulticastSocket) this.m_listenSocket).joinGroup(Inet6Address.getByAddress(it.getAddress()));
                    NetworkInterface netInt = NetworkInterface.getByInetAddress(Inet6Address.getByAddress(it.getAddress()));
                    InetSocketAddress sockAddr = new InetSocketAddress(Inet6Address.getByAddress(it.getAddress()), 0);
                    this.m_listenChannelNetty.joinGroup(sockAddr, netInt);
                    //++index;
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean isListeningTo(Locator loc) {
        if (loc.isAddressDefined()) {
            LocatorList locList = this.m_listenLocators;
            return locList.contains(loc);
        } else {
            if (loc.getPort() == this.m_listenLocators.begin().getPort()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAssociatedEndpoints() {
        return !(this.m_assocWriters.isEmpty() && this.m_assocReaders.isEmpty());
    }

    public boolean isDefaultListenResource() {
        return this.m_isDefaultListenResource;
    }


}
