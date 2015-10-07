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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a Listening point to receive data.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class ListenResource {

    /**
     * This object will take care of message reception
     */
    private MessageReceiver m_receiver;

    /**
     * List of associated {@link RTPSReader} references
     */
    private List<RTPSReader> m_assocReaders;

    /**
     * List of associated {@link RTPSWriter} references
     */
    private List<RTPSWriter> m_assocWriters;

    /**
     * List of {@link Locator} to listen to
     */
    private LocatorList m_listenLocators;

    /**
     * List of {@link Locator} to send to
     */
    private Locator m_senderLocator;

    /**
     * {@link RTPSParticipant} that created the {@link ListenResource}
     */
    private RTPSParticipant m_RTPSParticipant;

    /**
     * Identifier
     */
    private final int m_ID;

    /**
     * Indicates if it is a default listen resource
     */
    private boolean m_isDefaultListenResource;

    /**
     * Listening {@link AsioEndpoint}
     */
    private AsioEndpoint m_listenEndpoint;

    /**
     * Sending {@link AsioEndpoint}
     */
    private AsioEndpoint m_senderEndpoint;

    /**
     * Listening channel (where the data arrives)
     */
    private java.nio.channels.DatagramChannel m_listenChannel;

    /**
     * Mutex
     */
    private final Lock m_mutex = new ReentrantLock(true);

    /**
     * Reception thread
     */
    private Thread m_thread;
    
    /**
     * Reception thread handler
     */
    private ReceptionThread m_receptionThread;

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(ListenResource.class);

    /**
     * {@link ListenResource} constructor
     * 
     * @param participant {@link RTPSParticipant} that creates the {@link ListenResource}
     * @param ID Identifier
     * @param isDefault Indicates if it is a default {@link ListenResource}
     */
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

    /**
     * Destroys the {@link ListenResource}
     */
    public void destroy() {
        if (this.m_thread != null) {
            logger.debug("Removing listening thread {}", this.m_thread.getId());
            try {
                this.m_listenChannel.socket().close();
                this.m_listenChannel.disconnect();
                this.m_listenChannel.close();
            } catch (IOException e) {
                logger.error(e.getStackTrace().toString());
            }
            logger.debug("Joining thread {}", this.m_thread.getId());
            try {
                this.m_receptionThread.terminate();
                this.m_thread.join();
            } catch (InterruptedException e) {
                logger.error(e.getStackTrace().toString());
            }
            logger.debug("Listening thread {} closed successfully", this.m_thread.getId());
        }
    }

    /**
     * Adds an associated {@link Endpoint}
     * 
     * @param endpoint The {@link Endpoint} to be added
     * @return true on success; false otherwise
     */
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
                    logger.debug("Endpoint {} added to listen locators list", endpoint.getGuid().getEntityId()); 
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
                    logger.debug("Endpoint {} added to listen locators list", endpoint.getGuid().getEntityId());
                    return true;
                }
            }
        } finally {
            this.m_mutex.unlock();
        }

        return false;
    }

    /**
     * Removes an associated {@link Endpoint}
     * @param endpoint The {@link Endpoint} to be removed
     * @return true on success; false otherwise
     */
    public boolean removeAssociatedEndpoint(Endpoint endpoint) {

        this.m_mutex.lock();

        try {
            if (endpoint.getAttributes().endpointKind == EndpointKind.WRITER) {
                for (int i=0; i < this.m_assocWriters.size(); ++i) {
                    RTPSWriter it = this.m_assocWriters.get(i);
                    if (it.getGuid().getEntityId().equals(endpoint.getGuid().getEntityId())) {
                        this.m_assocWriters.remove(endpoint);
                        i--;
                        return true;
                    }
                }
            } else if (endpoint.getAttributes().endpointKind == EndpointKind.READER) {
                for (int i=0; i < this.m_assocReaders.size(); ++i) {
                    RTPSReader it = this.m_assocReaders.get(i);
                    if (it.getGuid().getEntityId().equals(endpoint.getGuid().getEntityId())) {
                        this.m_assocReaders.remove(endpoint);
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

    /**
     * Get the {@link Locator} address
     * 
     * @param loc A reference to the {@link Locator}
     */
    public void getLocatorAdresses(Locator loc) {
        if (!loc.isAddressDefined()) { // Listen in all interfaces
            logger.debug("Defined Locastor IP with 0s (listen to all interfaces), listening to all interfaces"); 
            LocatorList myIP = null;
            if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv4) {
                myIP = IPFinder.getIPv4Adress();
                //this.m_listenEndpoint.address = IPFinder.getFirstIPv4Adress();
                try {
                    this.m_listenEndpoint.address = Inet4Address.getByName("0.0.0.0");
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv6) {
                myIP = IPFinder.getIPv6Adress();
                //this.m_listenEndpoint.address = IPFinder.getFirstIPv6Adress();
                try {
                    this.m_listenEndpoint.address = Inet6Address.getByName("0");
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
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

    /**
     * Initializes the reception thread
     * 
     * @param participant {@link RTPSParticipant} that initializes the thread
     * @param loc {@link Locator} to listen
     * @param listenSocketSize Buffer size of the socket
     * @param isMulticast Indicates whether it is a multicast resource or not
     * @param isFixed Indicates whether it is a fixed resource or not
     * @return true on success; false otherwise
     */
    public boolean initThread(RTPSParticipant participant, Locator loc, int listenSocketSize, boolean isMulticast, boolean isFixed) {
        logger.debug("Creating ListenResource in " + loc + " with ID " + this.m_ID); 

        this.m_RTPSParticipant = participant;
        if (!loc.isAddressDefined() && isMulticast) {
            logger.warn("MulticastAddresses need to have the IP defined, ignoring this address"); 
            return false;
        }
        this.m_receiver = new MessageReceiver(listenSocketSize);
        this.m_receiver.setListenResource(this);

        this.getLocatorAdresses(loc);

        logger.debug("Initializing in : " + this.m_listenLocators); // TODO Some toString for ListenLocators
        
        InetAddress multicastAddress = null;

        try {

            if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv4) {
                //this.m_listenChannel = DatagramChannel.open(StandardProtocolFamily.INET);
                this.m_listenChannel = java.nio.channels.DatagramChannel.open(StandardProtocolFamily.INET);
            } else if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv6) {
                this.m_listenChannel = java.nio.channels.DatagramChannel.open(StandardProtocolFamily.INET6);
            }

            this.m_listenChannel.setOption(StandardSocketOptions.SO_RCVBUF, listenSocketSize);
            if (isMulticast) {
                this.m_listenChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            }
     
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
                //this.m_listenEndpoint.address = IPFinder.getFirstIPv4Adress();
                this.m_listenEndpoint.address = IPFinder.addressIPv4();
            } else if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv6) {
                //this.m_listenEndpoint.address = IPFinder.getFirstIPv6Adress();
                this.m_listenEndpoint.address = IPFinder.addressIPv6();
            }
        }

        InetSocketAddress sockAddr = new InetSocketAddress(this.m_listenEndpoint.address, this.m_listenEndpoint.port);
        if (isFixed) {
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
                
                try {
                    this.m_listenChannel.socket().bind(sockAddr);
                    binded = true;
                    break;
                   
                } catch (IOException e) {
                    logger.debug("Tried port {}, trying next...", this.m_listenEndpoint.port);
                }
                this.m_listenEndpoint.port += 2;
                sockAddr = new InetSocketAddress(this.m_listenEndpoint.address, this.m_listenEndpoint.port); // TODO: Delete addr
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
            joinMulticastGroup(multicastAddress/*, this.m_listenEndpoint.port++*/);
        }

        this.m_receptionThread =  new ReceptionThread(this.m_listenChannel, this);
        this.m_thread = new Thread(m_receptionThread, "");
        this.m_thread.start();

        this.m_RTPSParticipant.resourceSemaphoreWait();

        return true;

    }

    /**
     * Methos used to join to a specific multicast group
     * 
     * @param multicastAddress Multicast {@link InetAddress} to join to
     */
    private void joinMulticastGroup(InetAddress multicastAddress) {

        LocatorList loclist = new LocatorList();

        if (this.m_listenEndpoint.address instanceof Inet4Address) {
            loclist = IPFinder.getIPv4Adress();
            for (Locator it : loclist.getLocators()) {
                try {
                    NetworkInterface netInt = NetworkInterface.getByInetAddress(InetAddress.getByName(it.toIPv4String()));
                    this.m_listenChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, netInt);
                    MembershipKey key = this.m_listenChannel.join(multicastAddress, netInt);
                    
                    logger.debug("MulticastJoin: Address: {}, NetIf: {}, Key: {}", multicastAddress.toString(), netInt, key);
                    if (!key.isValid()) {
                        logger.error("Invalid membership key: {}", key);
                    }
                } catch (UnknownHostException e) {
                    logger.error(e.getStackTrace().toString());
                    //e.printStackTrace();
                } catch (IOException e) {
                    logger.error(e.getStackTrace().toString());
                    //e.printStackTrace();
                }
            }
        } else if (this.m_listenEndpoint.address instanceof Inet6Address) {
            loclist = IPFinder.getIPv6Adress();
            for (Locator it : loclist.getLocators()) {
                try {
                    NetworkInterface netInt = NetworkInterface.getByInetAddress(Inet6Address.getByAddress(it.getAddress()));
                    this.m_listenChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, netInt);
                    this.m_listenChannel.join(Inet6Address.getByAddress(it.getAddress()), netInt);
                } catch (UnknownHostException e) {
                    logger.error(e.getStackTrace().toString());
                    //e.printStackTrace();
                } catch (IOException e) {
                    logger.error(e.getStackTrace().toString());
                    //e.printStackTrace();
                }
            }
        }

    }

    /**
     * Get the list of associated {@link RTPSReader}
     * 
     * @return The list of associated {@link RTPSReader}
     */
    public List<RTPSReader> getAssocReaders() {
        return this.m_assocReaders;
    }
    
    /**
     * Get the list of associated {@link RTPSWriter}
     * 
     * @return The list of associated {@link RTPSWriter}
     */
    public List<RTPSWriter> getAssocWriters() {
        return this.m_assocWriters;
    }

    /**
     * Get the {@link LocatorList}
     * 
     * @return The {@link LocatorList}
     */
    public LocatorList getListenLocators() {
        return this.m_listenLocators;
    }

    /**
     * Get the sender {@link Locator}
     * 
     * @return The sender {@link Locator}
     */
    public Locator getSenderLocator() {
        return this.m_senderLocator;
    }

    /**
     * Get the sender {@link AsioEndpoint}
     * 
     * @return The sender {@link AsioEndpoint}
     */
    public AsioEndpoint getSenderEndpoint() {
        return this.m_senderEndpoint;
    }

    /**
     * Get the {@link MessageReceiver}
     * 
     * @return The {@link MessageReceiver}
     */
    public MessageReceiver getMessageReceiver() {
        return this.m_receiver;
    }

    /**
     * Get the {@link RTPSParticipant}
     * 
     * @return The {@link RTPSParticipant}
     */
    public RTPSParticipant getRTPSParticipant() {
        return this.m_RTPSParticipant;
    }

    /*
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
                InetSocketAddress sockAddr = new InetSocketAddress(this.m_listenEndpoint.port);
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
                logger.error("Tried 1000 ports and none was working, last tried: " + this.m_listenEndpoint.port);
            } else {
                for (Locator it : this.m_listenLocators.getLocators()) {
                    it.setPort(this.m_listenEndpoint.port);
                }
            }
        }

        if (isMulticast && multicastAddress != null) {
            joinMulticastGroupNetty(multicastAddress);
        }

        logger.info("Finishing ListenResource thread");

        // TODO Thread stuff

        return true;

    }*/

    /*private void joinMulticastGroupNetty(InetAddress multicastAddress) {

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

    }*/

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
