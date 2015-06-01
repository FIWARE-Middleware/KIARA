package org.fiware.kiara.ps.rtps.resources;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.DatagramChannel;
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
import org.fiware.kiara.ps.rtps.messages.RTPSMessageBuilder;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.utils.IPFinder;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;

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
	
	private DatagramChannel m_listenChannel;
	
	private final Lock m_mutex = new ReentrantLock(true);
	
	private ReceptionThread m_thread;
	
	//0private java.nio.channels.AsynchronousDatagramChannel channel = DatagramChannel.open().r
	
	//DatagramChannel channel = DatagramChannel.open().
	
	public ListenResource(RTPSParticipant participant, int ID, boolean isDefault) {
		this.m_assocReaders = new ArrayList<RTPSReader>();
		this.m_listenLocators = new LocatorList();
		this.m_RTPSParticipant = participant;
		this.m_ID = ID;
		this.m_isDefaultListenResource = isDefault;
		this.m_listenEndpoint = new AsioEndpoint();
		this.m_senderEndpoint = new AsioEndpoint();
		this.m_senderLocator = new Locator();
		
	}
	
	public Locator getSenderLocator() {
	    return this.m_senderLocator;
	}
	
	public AsioEndpoint getSenderEndpoint() {
	    return this.m_senderEndpoint;
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
        				System.out.println(endpoint.getGuid().getEntityId()+ " added to listemn locators list."); // Log this(info)
        				this.m_mutex.unlock();
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
        				System.out.println(endpoint.getGuid().getEntityId()+ " added to listemn locators list."); // Log this(info)
        				this.m_mutex.unlock();
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
        			for (RTPSWriter it : this.m_assocWriters) {
        				if (it.getGuid().getEntityId().equals(endpoint.getGuid().getEntityId())) {
        					this.m_assocWriters.remove(endpoint);
        					this.m_mutex.unlock();
        					return true;
        				}
        			}
        		} else if (endpoint.getAttributes().endpointKind == EndpointKind.READER) {
        			for (RTPSReader it : this.m_assocReaders) {
        				if (it.getGuid().getEntityId().equals(endpoint.getGuid().getEntityId())) {
        					this.m_assocReaders.remove(endpoint);
        					this.m_mutex.unlock();
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
			System.out.println("Defined Locastor IP with 0s (listen to all interfaces), listening to all interfaces"); // TODO Log this
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
		System.out.println("Creating ListenResource in " + loc + " with ID " + this.m_ID); // TODO Log this (info)
		this.m_RTPSParticipant = participant;
		if (!loc.isAddressDefined() && isMulticast) {
			System.out.println("MulticastAddresses need to have the IP defined, ignoring this address"); // TODO Log this (info)
			return false;
		}
		this.m_receiver = new MessageReceiver(listenSocketSize);
		this.m_receiver.setListenResource(this);
	
		this.getLocatorAdresses(loc);
		
		System.out.println("Initializing in : " + this.m_listenLocators); // TODO Log this
		
		InetAddress multicastAddress = null;
		
		try {
			
			if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv4) {
				this.m_listenChannel = DatagramChannel.open(StandardProtocolFamily.INET);
			} else if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv6) {
				this.m_listenChannel = DatagramChannel.open(StandardProtocolFamily.INET6);
			}
			
			//this.m_listenChannel.configureBlocking(false);
			this.m_listenChannel.setOption(StandardSocketOptions.SO_RCVBUF, listenSocketSize);
			this.m_listenChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			
			//this.m_listenSocket = new java.net.MulticastSocket(null/*this.m_listenEndpoint.port, this.m_listenEndpoint.address*/);
			//this.m_listenSocket.setReceiveBufferSize(listenSocketSize);
			//this.m_listenSocket.setReuseAddress(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
				this.m_listenChannel.socket().bind(sockAddr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} else {
			boolean binded = false;
			
			for (int i=0; i < 1000; ++i) {
				this.m_listenEndpoint.port += 1;
				InetSocketAddress sockAddr = new InetSocketAddress(this.m_listenEndpoint.address, this.m_listenEndpoint.port);
				try {
					this.m_listenChannel.socket().bind(sockAddr);
					binded = true;
					break;
				} catch (IOException e) {
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
			joinMulticastGroup(multicastAddress);
		}
		
		//this.m_listenSocket.r
		
		ReceptionThread runnable = new ReceptionThread(this.m_listenChannel, this);
		Thread thread = new Thread(runnable, "");
		thread.start();
		
		
		System.out.println("Finishing ListenResource thread");
		
		// TODO Thread stuff
		
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
					this.m_listenChannel.join(multicastAddress, netInt);
					
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
}
