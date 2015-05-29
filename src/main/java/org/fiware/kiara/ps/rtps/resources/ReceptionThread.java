package org.fiware.kiara.ps.rtps.resources;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageBuilder;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;

public class ReceptionThread implements Runnable {
	
	private DatagramChannel m_channel;
	
	//private final Lock m_mutex = new ReentrantLock(true);
	
	public ReceptionThread(DatagramChannel channel) {
		//this.m_mutex.lock();
		this.m_channel = channel;
		//this.m_mutex.unlock();
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
			this.m_channel.socket().receive(dp);

			System.out.println("Received");
			
			RTPSMessage msg = RTPSMessageBuilder.createMessage(RTPSEndian.BIG_ENDIAN);
			
			System.out.println("Message created");
			
			msg.setBuffer(buf.array());
			
			System.out.println("Buffer set");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
