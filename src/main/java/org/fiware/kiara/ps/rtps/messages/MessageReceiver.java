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
package org.fiware.kiara.ps.rtps.messages;

import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;

import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorKind;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.common.types.SubmessageFlags;
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.OctectsToInlineQos;
import org.fiware.kiara.ps.rtps.messages.elements.Pad;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;
import org.fiware.kiara.ps.rtps.messages.elements.ProtocolVersion;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumberSet;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.messages.elements.Unused;
import org.fiware.kiara.ps.rtps.messages.elements.VendorId;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.resources.ListenResource;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class MessageReceiver {
	
	private CacheChange m_change;
	
	private ProtocolVersion m_sourceVersion;
	
	private VendorId m_sourceVendorId;
	
	private GUIDPrefix m_sourceGuidPrefix;
	
	private GUIDPrefix m_destGuidPrefix;
	
	private LocatorList m_unicastReplyLocatorList;
	
	private LocatorList m_multicastReplyLocatorList;
	
	private boolean m_haveTimestamp;
	
	private Timestamp m_timestamp;
	
	private ProtocolVersion m_destVersion;
	
	private Locator m_defUniLoc;
	
	
	
	private ListenResource m_listenResource;
	
	public MessageReceiver(int recBufferSize) {
		// this.m_rec_msg(recBufferSize); TODO Fix this
		this.m_destVersion = new ProtocolVersion();
		this.m_sourceVersion = new ProtocolVersion();
		this.m_sourceVendorId = new VendorId().setVendorUnknown();
		this.m_sourceGuidPrefix = new GUIDPrefix();
		this.m_destGuidPrefix = new GUIDPrefix();
		this.m_haveTimestamp = false;
		this.m_timestamp = new Timestamp().timeInvalid();
		
		this.m_unicastReplyLocatorList = new LocatorList();
		this.m_multicastReplyLocatorList = new LocatorList();
		
		this.m_defUniLoc = new Locator();
		
		this.m_listenResource = null;
		
		short maxPayload = (Short.MAX_VALUE < recBufferSize) ? Short.MAX_VALUE : (short) recBufferSize;
		this.m_change = new CacheChange(/*maxPayload*/);
	}
	
	public void reset() {
		this.m_destVersion = new ProtocolVersion();
		this.m_sourceVersion = new ProtocolVersion();
		this.m_sourceVendorId = new VendorId().setVendorUnknown();
		this.m_sourceGuidPrefix = new GUIDPrefix();
		this.m_destGuidPrefix = new GUIDPrefix();
		this.m_haveTimestamp = false;
		this.m_timestamp = new Timestamp().timeInvalid();
		
		this.m_unicastReplyLocatorList.clear();
		this.m_unicastReplyLocatorList.reserve(1);
		this.m_multicastReplyLocatorList.clear();
		
		Locator loc = new Locator();
		LocatorList locList = new LocatorList();
		locList.clear();
		locList.pushBack(loc);
		
		this.m_unicastReplyLocatorList.pushBack(loc);
		this.m_multicastReplyLocatorList.pushBack(this.m_defUniLoc);
		
		this.m_change.setKind(ChangeKind.ALIVE);
		this.m_change.getSequenceNumber().setHigh(0);
		this.m_change.getSequenceNumber().setLow(0);
		this.m_change.setWriterGUID(new GUID());
		//this.m_change.getSerializedPayload().setLength // TODO Check this
		//this.m_change.getSerializedPayload().setPos // TODO Check this
		this.m_change.setInstanceHandle(new InstanceHandle());
		this.m_change.setRead(false);
		this.m_change.setSourceTimestamp(new Timestamp(0, 0));
		
	}
	
	public void setListenResource(ListenResource listenResource) {
		this.m_listenResource = listenResource;
	}
	
	public void processCDRMessage(GUIDPrefix RTPSParticipantGuidPrefix, Locator loc, RTPSMessage msg) {
		if (msg.getBuffer().length < RTPSMessageHeader.RTPS_MESSAGE_HEADER_SIZE) {
			System.out.println("Received message too short, ignoring"); // TODO Log this
			return;
		}
		
		this.reset();
		this.m_destGuidPrefix = RTPSParticipantGuidPrefix;
		this.m_unicastReplyLocatorList.begin().setKind(loc.getKind());
		
		byte nStart = 0;
		
		if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv4) {
			nStart = 12;
		} else if (loc.getKind() == LocatorKind.LOCATOR_KIND_UDPv6) {
			nStart = 0;
		} else {
			System.out.println("Locator kind invalid"); // TODO Log this
			return;
		}
		
		this.m_unicastReplyLocatorList.begin().setAddress(loc.getAddress());
		
		if (!checkRTPSHeader(msg)) {
			return;
		}
		
		boolean lastSubmsg = false;
		boolean valid = false;
		int count = 0;
		
		while (msg.getBinaryInputStream().getPosition() < msg.getBuffer().length) {
			RTPSSubmessage subMsg = new RTPSSubmessage();
			RTPSSubmessageHeader header = new RTPSSubmessageHeader();
			try {
				header.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
			if (msg.getBinaryInputStream().getPosition() > msg.getBuffer().length) {
				System.out.println("Submessage of invalid length"); // TODO Log this
			}
			
			//if (header.getOctectsToNextHeader())
			
			valid = true;
			count++;
			subMsg.setSubmessageHeader(header);
			
			switch(header.getSubmessageId()) {
			
			case DATA:
				if (!this.m_destGuidPrefix.equals(RTPSParticipantGuidPrefix)) {
					System.out.println("Data Submsg ignored, DST is another RTPSParticipant"); // TODO Log this
				} else {
					valid = processSubmessageData(msg, subMsg);
				}
				break;
			
			case GAP:
				if (!this.m_destGuidPrefix.equals(RTPSParticipantGuidPrefix)) {
					System.out.println("Gap Submsg ignored, DST is another RTPSParticipant"); // TODO Log this
				} else {
					valid = processSubmessageGap(msg, subMsg);
					msg.addSubmessage(subMsg);
				}
				break;
			
			case ACKNACK:
				if (!this.m_destGuidPrefix.equals(RTPSParticipantGuidPrefix)) {
					System.out.println("Acknack Submsg ignored, DST is another RTPSParticipant"); // TODO Log this
				} else {
					valid = processSubmessageAcknack(msg, subMsg);
					msg.addSubmessage(subMsg);
				}
				break;
			
			case HEARTBEAT:
				if (!this.m_destGuidPrefix.equals(RTPSParticipantGuidPrefix)) {
					System.out.println("Heartbeat Submsg ignored, DST is another RTPSParticipant"); // TODO Log this
				} else {
					valid = processSubmessageHeartbeat(msg, subMsg);
					msg.addSubmessage(subMsg);
				}
				break;
			
			case PAD:
				if (!this.m_destGuidPrefix.equals(RTPSParticipantGuidPrefix)) {
					System.out.println("Pad Submsg ignored, DST is another RTPSParticipant"); // TODO Log this
				} else {
					valid = processSubmessagePad(msg, subMsg);
					msg.addSubmessage(subMsg);
				}
				break;
			
			case INFO_DST:
				if (!this.m_destGuidPrefix.equals(RTPSParticipantGuidPrefix)) {
					System.out.println("InfoDST Submsg ignored, DST is another RTPSParticipant"); // TODO Log this
				} else {
					valid = processSubmessageInfoDst(msg, subMsg);
					msg.addSubmessage(subMsg);
				}
				break;
				
			case INFO_SRC:
				if (!this.m_destGuidPrefix.equals(RTPSParticipantGuidPrefix)) {
					System.out.println("InfoSRC Submsg ignored, DST is another RTPSParticipant"); // TODO Log this
				} else {
					valid = processSubmessageInfoSrc(msg, subMsg);
					msg.addSubmessage(subMsg);
				}
				break;
			
			case INFO_TS:
				if (!this.m_destGuidPrefix.equals(RTPSParticipantGuidPrefix)) {
					System.out.println("InfoTSSubmsg ignored, DST is another RTPSParticipant"); // TODO Log this
				} else {
					valid = processSubmessageInfoTs(msg, subMsg);
					msg.addSubmessage(subMsg);
				}
				break;
			
			case INFO_REPLY:
			case INFO_REPLY_IP4:
			case NACK_FRAG:
			case HEARTBEAT_FRAG:
			case DATA_FRAG:
			default:
					System.out.println("Unsupported message, ignored.");
					System.out.println(header.getSubmessageId());
			}
		}
		
	}
	
	private boolean checkRTPSHeader(RTPSMessage msg) {
		
		RTPSMessageHeader header = new RTPSMessageHeader();
		try {
			header.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		if (!header.getProtocolName().equals("RTPS")) {
			return false;
		}
		
		if (header.getProtocolVersion().isLowerOrEqualThan(this.m_destVersion)) {
			this.m_sourceVersion = header.getProtocolVersion();
		} else {
			System.out.println("Major RTPS Version not supported"); // TODO Log this
			return false;
		}
		
		// Set source vendor ID
		this.m_sourceVendorId = header.getVendorId();
		
		// Set source GUIDPrefix
		this.m_sourceGuidPrefix = header.getGUIDPrefix();
		
		this.m_haveTimestamp = false;
		
		msg.setHeader(header);
		
		return true;
	}
	
	private boolean processSubmessageData(RTPSMessage msg, RTPSSubmessage subMsg) {
		
		SubmessageFlags flags = subMsg.m_submessageHeader.getFlags();
		
		boolean endiannessFlag = flags.getFlagValue(0);
		boolean inlineQosFlag = flags.getFlagValue(1);
		boolean dataFlag = flags.getFlagValue(2);
		boolean keyFlag = flags.getFlagValue(3);
		
		if (keyFlag && dataFlag) {
			System.out.println("Message received with Data and Key Flag set, ignoring"); // TODO Log this
			return false;
		}
		
		// Assign message endianness
		if (endiannessFlag) {
			msg.setEndiannes(RTPSEndian.LITTLE_ENDIAN);
		} else {
			msg.setEndiannes(RTPSEndian.BIG_ENDIAN);
		}
		
		try {
			
			// Extra flags don't matter for now
			msg.getBinaryInputStream().skipBytes(2);
			
			OctectsToInlineQos otiQos = new OctectsToInlineQos((short) 0);
			otiQos.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			
			// Reader and Writer ID
			EntityId readerId = new EntityId();
			readerId.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			
			// Look for the reader
			
			RTPSReader firstReader = null;
			// TODO: Delete this
			//firstReader = new RTPSReader();
			// TODO: Uncomment this
			/*if (this.m_listenResource.getAssocReaders().isEmpty()) {
				System.out.println("Data received in locator: " + this.m_listenResource.getListenLocators() +  ", when NO readers are listening"); // TODO Log this
				return false;
			}
			
			for (RTPSReader reader : this.m_listenResource.getAssocReaders()) {
				if (reader.acceptMsgDirectedTo(readerId)) {
					firstReader = reader;
					break;
				}
			}
			
			if (firstReader == null) { // Reader not found
				System.out.println("No Reader in this Locator"); // TODO Log this
				return false;
			}
			*/
			// Add readerId
			subMsg.addSubmessageElement(readerId);
			
			// Reader has been found
			CacheChange ch = this.m_change;
			GUID writerGUID = new GUID();
			writerGUID.setGUIDPrefix(this.m_sourceGuidPrefix);
			EntityId writerId = new EntityId();
			writerId.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			writerGUID.setEntityId(writerId);
			ch.setWriterGUID(writerGUID);
			
			// Add writerId
			subMsg.addSubmessageElement(writerId);
			
			// Get SequenceNumber
			SequenceNumber writerSN = new SequenceNumber();
			writerSN.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			
			if (writerSN.toLong() <= 0 || writerSN.getHigh() == -1 || writerSN.getLow() == 0) { // Message is invalid
				System.out.println("Invalid message received, bad sequence Number"); // TODO Log this
				return false;
			}
			
			subMsg.addSubmessageElement(writerSN);
			ch.setSequenceNumber(writerSN);
			
			if (otiQos.getSerializedSize() > RTPSMessage.OCTETSTOINLINEQOS_DATASUBMSG) {
				msg.getBinaryInputStream().skipBytes(otiQos.getSerializedSize() - RTPSMessage.OCTETSTOINLINEQOS_DATASUBMSG);
			}
			
			int inlineQosSize = 0;
			
			if (inlineQosFlag) {
				// Data MSG contains inline QOS
				ParameterList paramList = new ParameterList();
				paramList.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
				inlineQosSize = paramList.getListSize();
				
				if (inlineQosSize <= 0) {
					System.out.println("SubMessage Data ERROR, Inline Qos ParameterList error");
					return false;
				}
			}
			
			if (dataFlag || keyFlag) {
				
				int payloadSize;
				if (subMsg.m_submessageHeader.m_octectsToNextHeader > 0) {
					payloadSize = subMsg.m_submessageHeader.m_octectsToNextHeader - (RTPSMessage.DATA_EXTRA_INLINEQOS_SIZE + otiQos.getValue() + inlineQosSize);
				} else {
					payloadSize = subMsg.m_submessageHeader.m_submessageLengthLarger;
				}
				
				if (dataFlag) {
					SerializedPayload payload = new SerializedPayload();
					payload.setLength((short) (payloadSize-RTPSMessage.DATA_EXTRA_ENCODING_SIZE));
					payload.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
					ch.setSerializedPayload(payload);
					ch.setKind(ChangeKind.ALIVE);
					//payload.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
				} else if (keyFlag) {
					// TODO Complete this
				}
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	private boolean processSubmessageGap(RTPSMessage msg, RTPSSubmessage subMsg) {
		
		SubmessageFlags flags = subMsg.m_submessageHeader.getFlags();
		
		boolean endiannessFlag = flags.getFlagValue(0);
		
		if (endiannessFlag) {
			msg.setEndiannes(RTPSEndian.LITTLE_ENDIAN);
		} else {
			msg.setEndiannes(RTPSEndian.BIG_ENDIAN);
		}
		
		try {
			
			// Get reader ID
			GUID readerGUID = new GUID();
			EntityId readerId = new EntityId();
			readerId.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			readerGUID.setGUIDPrefix(this.m_destGuidPrefix);
			readerGUID.setEntityId(readerId);
			subMsg.addSubmessageElement(readerId);
			
			// Get writer ID
			GUID writerGUID = new GUID();
			EntityId writerId = new EntityId();
			writerId.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			writerGUID.setGUIDPrefix(this.m_sourceGuidPrefix);
			writerGUID.setEntityId(writerId);
			subMsg.addSubmessageElement(writerId);
			
			SequenceNumber gapStart = new SequenceNumber();
			gapStart.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			if (gapStart.toLong() <= 0) {
				System.out.println("Wrong gapStart value. It should be greater than zero."); // TODO Log this
				return false;
			}
			subMsg.addSubmessageElement(gapStart);
			
			SequenceNumberSet gapList = new SequenceNumberSet();
			gapList.setBase(gapStart);
			gapList.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			subMsg.addSubmessageElement(gapList);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	private boolean processSubmessageAcknack(RTPSMessage msg, RTPSSubmessage subMsg) {
		
		SubmessageFlags flags = subMsg.m_submessageHeader.getFlags();
		
		boolean endiannessFlag = flags.getFlagValue(0);
		boolean finalFlag = flags.getFlagValue(1);
		
		if (endiannessFlag) {
			msg.setEndiannes(RTPSEndian.LITTLE_ENDIAN);
		} else {
			msg.setEndiannes(RTPSEndian.BIG_ENDIAN);
		}
		
		try {
			// Get reader ID
			GUID readerGUID = new GUID();
			EntityId readerId = new EntityId();
			readerId.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			readerGUID.setGUIDPrefix(this.m_destGuidPrefix);
			readerGUID.setEntityId(readerId);
			subMsg.addSubmessageElement(readerId);
			
			// Get writer ID
			GUID writerGUID = new GUID();
			EntityId writerId = new EntityId();
			writerId.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			writerGUID.setGUIDPrefix(this.m_sourceGuidPrefix);
			writerGUID.setEntityId(writerId);
			subMsg.addSubmessageElement(writerId);
			
			// Get readerSeqNumState
			SequenceNumberSet readerSNState = new SequenceNumberSet();
			readerSNState.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			subMsg.addSubmessageElement(readerSNState);
			
			// Get Count
			Count count = new Count(0);
			count.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			subMsg.addSubmessageElement(count);
			
			// TODO Status changes
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private boolean processSubmessageHeartbeat(RTPSMessage msg, RTPSSubmessage subMsg) {
		
		SubmessageFlags flags = subMsg.m_submessageHeader.getFlags();
		
		boolean endiannessFlag = flags.getFlagValue(0);
		
		if (endiannessFlag) {
			msg.setEndiannes(RTPSEndian.LITTLE_ENDIAN);
		} else {
			msg.setEndiannes(RTPSEndian.BIG_ENDIAN);
		}
		
		try {
			// Get reader ID
			GUID readerGUID = new GUID();
			EntityId readerId = new EntityId();
			readerId.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			readerGUID.setGUIDPrefix(this.m_destGuidPrefix);
			readerGUID.setEntityId(readerId);
			subMsg.addSubmessageElement(readerId);
			
			// Get writer ID
			GUID writerGUID = new GUID();
			EntityId writerId = new EntityId();
			writerId.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			writerGUID.setGUIDPrefix(this.m_sourceGuidPrefix);
			writerGUID.setEntityId(readerId);
			subMsg.addSubmessageElement(writerId);

			// Sequence numbers
			SequenceNumber firstSN = new SequenceNumber();
			firstSN.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			subMsg.addSubmessageElement(firstSN);
			SequenceNumber lastSN = new SequenceNumber();
			lastSN.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			subMsg.addSubmessageElement(lastSN);
			
			if (lastSN.isLowerThan(firstSN)) {
				System.out.println("HB Received with lastSN < firstSN, ignoring"); // TODO Log this
				return false;
			}
			
			// Heartbeat count
			Count count = new Count(0);
			count.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			subMsg.addSubmessageElement(count);
			
			// TODO Status changes
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		// TODO Writer changes
		
		return true;
	}
	
	private boolean processSubmessagePad(RTPSMessage msg, RTPSSubmessage subMsg) {
		
		SubmessageFlags flags = subMsg.m_submessageHeader.getFlags();
		
		boolean endiannessFlag = flags.getFlagValue(0);
		
		if (endiannessFlag) {
			msg.setEndiannes(RTPSEndian.LITTLE_ENDIAN);
		} else {
			msg.setEndiannes(RTPSEndian.BIG_ENDIAN);
		}
		
		try {
			// Jump Pad
			Pad pad = new Pad((short) subMsg.m_submessageHeader.m_octectsToNextHeader);
			pad.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			subMsg.addSubmessageElement(pad);
			//System.out.println("");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		// TODO Writer changes
		
		return true;
	}
	
	private boolean processSubmessageInfoDst(RTPSMessage msg, RTPSSubmessage subMsg) {

		SubmessageFlags flags = subMsg.m_submessageHeader.getFlags();
		
		boolean endiannessFlag = flags.getFlagValue(0);
		
		if (endiannessFlag) {
			msg.setEndiannes(RTPSEndian.LITTLE_ENDIAN);
		} else {
			msg.setEndiannes(RTPSEndian.BIG_ENDIAN);
		}
		
		try {
			
			// Get guidPrefix
			GUIDPrefix guidPrefix = new GUIDPrefix();
			guidPrefix.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			subMsg.addSubmessageElement(guidPrefix);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		// TODO Writer changes
		
		return true;
	}
	
	private boolean processSubmessageInfoSrc(RTPSMessage msg, RTPSSubmessage subMsg) {

		SubmessageFlags flags = subMsg.m_submessageHeader.getFlags();
		
		boolean endiannessFlag = flags.getFlagValue(0);
		
		if (endiannessFlag) {
			msg.setEndiannes(RTPSEndian.LITTLE_ENDIAN);
		} else {
			msg.setEndiannes(RTPSEndian.BIG_ENDIAN);
		}
		
		try {
			
			// Get Unused
			Unused unused = new Unused(4);
			unused.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			subMsg.addSubmessageElement(unused);
			
			// Get version
			ProtocolVersion version = new ProtocolVersion();
			version.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			subMsg.addSubmessageElement(version);
			
			// Get vendorId
			VendorId vendorId = new VendorId();
			vendorId.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			subMsg.addSubmessageElement(vendorId);
			
			// Get guidPrefix
			GUIDPrefix guidPrefix = new GUIDPrefix();
			guidPrefix.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
			subMsg.addSubmessageElement(guidPrefix);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		// TODO Writer changes
		
		return true;
	}

	private boolean processSubmessageInfoTs(RTPSMessage msg, RTPSSubmessage subMsg) {
		
		SubmessageFlags flags = subMsg.m_submessageHeader.getFlags();
		
		boolean endiannessFlag = flags.getFlagValue(0);
		boolean invalidFlag = flags.getFlagValue(1);
		
		if (endiannessFlag) {
			msg.setEndiannes(RTPSEndian.LITTLE_ENDIAN);
		} else {
			msg.setEndiannes(RTPSEndian.BIG_ENDIAN);
		}
		
		try {
			
			if (!invalidFlag) {
				Timestamp timestamp = new Timestamp();
				timestamp.deserialize(msg.getSerializer(), msg.getBinaryInputStream(), "");
				subMsg.addSubmessageElement(timestamp);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		// TODO Writer changes
		
		return true;
	}

}
