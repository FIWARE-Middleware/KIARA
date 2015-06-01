package org.fiware.kiara.ps.rtps.resources;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
		
		ByteBuffer buf;
		try {
			System.out.println("Size: " + this.m_channel.socket().getReceiveBufferSize());
			buf = ByteBuffer.allocate(this.m_channel.socket().getReceiveBufferSize());
		
			DatagramPacket dp = new DatagramPacket(buf.array(), this.m_channel.socket().getReceiveBufferSize());
			//DatagramPacket dp = null;
			System.out.println(this.m_channel.socket().getLocalPort());
			this.m_channel.socket().receive(dp);

			System.out.println("Received");
			
			RTPSMessage msg = RTPSMessageBuilder.createMessage(RTPSEndian.BIG_ENDIAN);
			
			System.out.println("Message created");
			
			msg.setBuffer(buf.array());
			
			//MessageReceiver mrcv = new MessageReceiver(buf.array().length);
			//mrcv.setListenResource(listenResource);
			
			System.out.println("Buffer set");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	        
	    }
	}
	
}
