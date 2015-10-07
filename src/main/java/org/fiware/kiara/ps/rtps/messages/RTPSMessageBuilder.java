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

import javax.sound.midi.Sequence;

import org.fiware.kiara.ps.rtps.common.EncapsulationKind;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.common.types.SubmessageFlags;
import org.fiware.kiara.ps.rtps.messages.common.types.SubmessageId;
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.ExtraFlags;
import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;
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
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterKey;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterSentinel;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to create new {@link RTPSMessage} entities.
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class RTPSMessageBuilder {
    
    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(RTPSMessageBuilder.class);

    /**
     * Creates a new {@link RTPSMessage} object using the specified {@link RTPSEndian}
     * 
     * @param endian The {@link RTPSEndian} to be set in the {@link RTPSMessage}
     * @return A new {@link RTPSMessage} object
     */
    public static RTPSMessage createMessage(RTPSEndian endian) {
        RTPSMessage retVal = new RTPSMessage(RTPSMessage.RTPS_MESSAGE_HEADER_SIZE + RTPSMessage.RTPSMESSAGE_DEFAULT_SIZE, endian); // TODO Change buffer size

        return retVal;
    }

    /**
     * Creates a new {@link RTPSMessage} object using the default {@link RTPSEndian} (LITTLE_ENDIAN)
     * 
     * @return A new {@link RTPSMessage} object
     */
    public static RTPSMessage createMessage() {
        return createMessage(RTPSEndian.LITTLE_ENDIAN);
    }

    /**
     * Creates a new {@link RTPSMessage} object using the specified {@link RTPSEndian} and
     * an initial buffer size
     * 
     * @param bufferSize The initial buffer size
     * @param endian The {@link RTPSEndian} to be set in the {@link RTPSMessage}
     * @return A new {@link RTPSMessage} object
     */
    public static RTPSMessage createMessage(int bufferSize, RTPSEndian endian) {
        RTPSMessage retVal = new RTPSMessage(RTPSMessage.RTPS_MESSAGE_HEADER_SIZE + bufferSize + RTPSMessage.RTPSMESSAGE_COMMON_RTPS_PAYLOAD_SIZE, endian);

        return retVal;
    }

    /**
     * Adds an {@link RTPSMessageHeader} to the provided {@link RTPSMessage}
     * 
     * @param message The {@link RTPSMessage} the new {@link RTPSMessageHeader} will be added to
     * @param prefix {@link GUIDPrefix} of the {@link EntityId} who creates the {@link RTPSMessage}
     * @return true on success; false otherwise
     */
    public static boolean addHeader(RTPSMessage message, GUIDPrefix prefix) {

        RTPSMessageHeader header = new RTPSMessageHeader();

        header.m_guidPrefix = prefix;

        message.setHeader(header);

        try {
            header.serialize(message.getSerializer(), message.getBinaryOutputStream(), "");
        } catch (IOException e) {
            logger.error(e.getStackTrace().toString());
        }
       
        return true;
    }

    /**
     * Adds a new {@link RTPSSubmessage} of type INFO_TS to the provided {@link RTPSMessage}
     * 
     * @param message The {@link RTPSMessage} 
     * @param timestamp The {@link Timestamp} to be used
     * @param invalidateFlag Flag indicating the message invalidation
     * @return true on success; false otherwise
     */
    public static boolean addSubmessageInfoTS(RTPSMessage message, java.sql.Timestamp timestamp, boolean invalidateFlag) {

        RTPSSubmessage infoTs = new RTPSSubmessage();
        RTPSSubmessageHeader subHeader = new RTPSSubmessageHeader();

        subHeader.setSubmessageId(SubmessageId.INFO_TS);
        
        // Flags
        SubmessageFlags flags = new SubmessageFlags((byte) 0x0);
        if (message.getEndiannes() == RTPSEndian.LITTLE_ENDIAN) {
            flags.setBitValue(0, true);
        } 
        if (invalidateFlag) {
            flags.setBitValue(1, true);
        }
        infoTs.setSubmessageEndian(message.getEndiannes());
        subHeader.setFlags(flags);

        // InfoTS message
        infoTs.setSubmessageHeader(subHeader);
        if (!invalidateFlag) {
            // Only present if !invalid
            infoTs.addSubmessageElement(new Timestamp(timestamp));
        }

        // Add submessage
        message.addSubmessage(infoTs);

        infoTs.serialize(message.getSerializer(), message.getBinaryOutputStream());

        return true;
    }

    /**
     *  Adds a new {@link RTPSSubmessage} of type INFO_TS to the provided {@link RTPSMessage}
     * @param message The {@link RTPSMessage}
     * @param invalidateFlag Flag indicating the message invalidation
     * @return true on success; false otherwise
     */
    public static boolean addSubmessageInfoTSNow(RTPSMessage message, boolean invalidateFlag) {
        java.sql.Timestamp t = new java.sql.Timestamp(System.currentTimeMillis());
        return addSubmessageInfoTS(message, t, invalidateFlag/*, isLast*/);
    }

    /**
     * Adds a new {@link RTPSSubmessage} of type INFO_DST to the provided {@link RTPSMessage}
     * 
     * @param message The {@link RTPSMessage}
     * @param guidPrefix The {@link GUIDPrefix} of the {@link EntityId} who created the {@link RTPSMessage}
     * @return true on success; false otherwise
     */
    public static boolean addSubmessageInfoDST(RTPSMessage message, GUIDPrefix guidPrefix) {

        RTPSSubmessage infoDst = new RTPSSubmessage();
        RTPSSubmessageHeader subHeader = new RTPSSubmessageHeader();

        // Submessage ID
        subHeader.setSubmessageId(SubmessageId.INFO_DST);
        
        // Flags
        SubmessageFlags flags = new SubmessageFlags((byte) 0x0);
        if (message.getEndiannes() == RTPSEndian.LITTLE_ENDIAN) {
            flags.setBitValue(0, true);
        } 
        infoDst.setSubmessageEndian(message.getEndiannes());
        subHeader.setFlags(flags);

        infoDst.setSubmessageHeader(subHeader);
        infoDst.addSubmessageElement(guidPrefix);

        message.addSubmessage(infoDst);
        
        infoDst.serialize(message.getSerializer(), message.getBinaryOutputStream());

        return true;
    }

    /**
     * Adds a new {@link RTPSSubmessage} of type INFO_SRC to the provided {@link RTPSMessage}
     * 
     * @param message The {@link RTPSMessage}
     * @param protocolVersion The {@link ProtocolVersion} of the RTPS implementation
     * @param vendorId The identified of the vendor
     * @param guidPrefix The {@link GUIDPrefix} of the {@link EntityId} who created the {@link RTPSMessage}
     * @return true on success; false otherwise
     */
    public static boolean addSubmessageInfoSRC(RTPSMessage message, ProtocolVersion protocolVersion, VendorId vendorId, GUIDPrefix guidPrefix) {

        RTPSSubmessage infoSrc = new RTPSSubmessage();
        RTPSSubmessageHeader subHeader = new RTPSSubmessageHeader();

        // Submessage ID
        subHeader.setSubmessageId(SubmessageId.INFO_SRC);
        //short size = 12;

        // Flags
        SubmessageFlags flags = new SubmessageFlags((byte) 0x0);
        if (message.getEndiannes() == RTPSEndian.LITTLE_ENDIAN) {
            flags.setBitValue(0, true);
        }
        infoSrc.setSubmessageEndian(message.getEndiannes());
        subHeader.setFlags(flags);

        // Bytes to next header
        //subHeader.setOctectsToNextHeader(size);

        infoSrc.setSubmessageHeader(subHeader);
        infoSrc.addSubmessageElement(new Unused(4));
        infoSrc.addSubmessageElement(protocolVersion);
        infoSrc.addSubmessageElement(vendorId);
        infoSrc.addSubmessageElement(guidPrefix);


        message.addSubmessage(infoSrc);

        return true;
    }

    /**
     *  Adds a new {@link RTPSSubmessage} of type HEARTEAT to the provided {@link RTPSMessage}
     * 
     * @param message The {@link RTPSMessage}
     * @param readerId The {@link EntityId} of the reader to which the {@link RTPSMessage} will be sent
     * @param writerId The {@link EntityId} of the writer that sends the {@link RTPSMessage}
     * @param firstSN First {@link SequenceNumber} available
     * @param lastSN Last {@link SequenceNumber} available
     * @param count Number of {@link Sequence} objects available
     * @param isFinal Indicates whether the {@link RTPSMessage} is final or not
     * @param livelinessFlag Indicates if the {@link RTPSMessage} asserts liveliness
     * @return true on success; false otherwise
     */
    public static boolean addSubmessageHeartbeat(RTPSMessage message, EntityId readerId, EntityId writerId, SequenceNumber firstSN, SequenceNumber lastSN, Count count, boolean isFinal, boolean livelinessFlag) {

        RTPSSubmessage submessageHeartbeat = new RTPSSubmessage();

        RTPSSubmessageHeader subHeader = new RTPSSubmessageHeader();

        // Submessage ID
        subHeader.setSubmessageId(SubmessageId.HEARTBEAT);
        //
        // Flags
        SubmessageFlags flags = new SubmessageFlags((byte) 0x0);
        if (message.getEndiannes() == RTPSEndian.LITTLE_ENDIAN) {
            flags.setBitValue(0, true);
        } 
        submessageHeartbeat.setSubmessageEndian(message.getEndiannes());

        if (isFinal) {
            flags.setBitValue(1, true);
        }

        if (livelinessFlag) {
            flags.setBitValue(2, true);
        }

        subHeader.setFlags(flags);

        submessageHeartbeat.addSubmessageElement(readerId);
        submessageHeartbeat.addSubmessageElement(writerId);

        submessageHeartbeat.addSubmessageElement(firstSN);
        submessageHeartbeat.addSubmessageElement(lastSN);

        submessageHeartbeat.addSubmessageElement(count);

        submessageHeartbeat.setSubmessageHeader(subHeader);

        message.addSubmessage(submessageHeartbeat);

        submessageHeartbeat.serialize(message.getSerializer(), message.getBinaryOutputStream());

        return true;
    }

    /**
     * Adds a new {@link RTPSSubmessage} of type ACKNACK to the provided {@link RTPSMessage}
     * 
     * @param message The {@link RTPSMessage}
     * @param readerId The {@link EntityId} of the reader to which the {@link RTPSMessage} will be sent
     * @param writerId The {@link EntityId} of the writer that sends the {@link RTPSMessage}
     * @param set {@link SequenceNumberSet} indicating the acknowledged messages
     * @param count Number of acknowledged messages
     * @param isFinal Indicates whether the {@link RTPSMessage} is final or not
     * @return true on success; false otherwise
     */
    public static boolean addSubmessageAckNack(RTPSMessage message, EntityId readerId, EntityId writerId, SequenceNumberSet set, Count count, boolean isFinal) {

        RTPSSubmessage submessageAckNack = new RTPSSubmessage();

        // Submessage ID
        RTPSSubmessageHeader subHeader = new RTPSSubmessageHeader();
        subHeader.setSubmessageId(SubmessageId.ACKNACK);

        // Endian flag
        SubmessageFlags flags = new SubmessageFlags((byte) 0x0);
        if (message.getEndiannes() == RTPSEndian.LITTLE_ENDIAN) {
            flags.setBitValue(0, true);
        } 
        submessageAckNack.setSubmessageEndian(message.getEndiannes());

        if (isFinal) {
            flags.setBitValue(1, true);
        }

        subHeader.setFlags(flags);

        submessageAckNack.addSubmessageElement(readerId);
        submessageAckNack.addSubmessageElement(writerId);
        submessageAckNack.addSubmessageElement(set);
        submessageAckNack.addSubmessageElement(count);

        submessageAckNack.setSubmessageHeader(subHeader);

        message.addSubmessage(submessageAckNack);

        submessageAckNack.serialize(message.getSerializer(), message.getBinaryOutputStream());

        return true;
    }

    /**
     * Adds a new {@link RTPSSubmessage} of type GAP to the provided {@link RTPSMessage}
     *  
     * @param message The {@link RTPSMessage}
     * @param gapStart {@link SequenceNumber} indicating the GAP start
     * @param gapList List of {@link SequenceNumber} objects requested
     * @param readerId The {@link EntityId} of the reader to which the {@link RTPSMessage} will be sent
     * @param writerId The {@link EntityId} of the writer that sends the {@link RTPSMessage}
     * @return true on success; false otherwise
     */
    public static boolean addSubmessageGap(RTPSMessage message, SequenceNumber gapStart, SequenceNumberSet gapList, EntityId readerId, EntityId writerId) {

        RTPSSubmessage submessageGap = new RTPSSubmessage();

        // Submessage ID
        RTPSSubmessageHeader subHeader = new RTPSSubmessageHeader();
        subHeader.setSubmessageId(SubmessageId.GAP);

        // Endian flag
        SubmessageFlags flags = new SubmessageFlags((byte) 0x0);
        if (message.getEndiannes() == RTPSEndian.LITTLE_ENDIAN) {
            flags.setBitValue(0, true);
        } 
        submessageGap.setSubmessageEndian(message.getEndiannes());

        subHeader.setFlags(flags);

        submessageGap.addSubmessageElement(readerId);
        submessageGap.addSubmessageElement(writerId);
        submessageGap.addSubmessageElement(gapStart);
        submessageGap.addSubmessageElement(gapList);

        submessageGap.setSubmessageHeader(subHeader);

        message.addSubmessage(submessageGap);

        submessageGap.serialize(message.getSerializer(), message.getBinaryOutputStream());

        return true;
    }

    /**
     * Adds a new {@link RTPSSubmessage} of type DATA to the provided {@link RTPSMessage}
     * 
     * @param message The {@link RTPSMessage}
     * @param change The {@link CacheChange} to be sent as DATA
     * @param topicKind The type of the Topic
     * @param readerId The {@link EntityId} of the reader to which the {@link RTPSMessage} will be sent
     * @param expectsInlineQos Indicates if the reader expects InlineQoS
     * @param inlineQos The {@link ParameterList} containing the InlineQoS
     * @return true on success; false otherwise
     */
    public static boolean addSubmessageData(RTPSMessage message, CacheChange change, TopicKind topicKind, EntityId readerId, boolean expectsInlineQos, ParameterList inlineQos) {

        // Submessage ID
        RTPSSubmessageHeader subHeader = new RTPSSubmessageHeader();
        subHeader.setSubmessageId(SubmessageId.DATA);

        RTPSSubmessage submessageData = new RTPSSubmessage();

        // Endian flag
        SubmessageFlags flags = new SubmessageFlags((byte) 0x0);
        if (message.getEndiannes() == RTPSEndian.LITTLE_ENDIAN) {
            flags.setBitValue(0, true);
        } 
        submessageData.setSubmessageEndian(message.getEndiannes());
        //submessageData.initSerializer();

        // Data flags
        boolean dataFlag = false;
        boolean keyFlag = false;
        boolean inlineQosFlag = false;
        boolean hasSerializedPayload = false;
        boolean isDataOrParamList = (change.getSerializedPayload().getLength() > 0 || change.getSerializedPayload().getParameterListLength() > 0); // FIXME
        boolean existsDataOrParamList = (change.getSerializedPayload().getData() != null || change.getSerializedPayload().getParameterList() != null);
        if (change.getSerializedPayload().getBuffer() != null) {
            hasSerializedPayload = change.getSerializedPayload().getBuffer().length != 0;
        }

        if (change.getKind() == ChangeKind.ALIVE && isDataOrParamList && (existsDataOrParamList || hasSerializedPayload)) {
            dataFlag = true;
            keyFlag = false;
        } else {
            dataFlag = false;
            keyFlag = true;
        }

        if (topicKind == TopicKind.NO_KEY) {
            keyFlag = false;
        }

        if (inlineQos != null || expectsInlineQos || change.getKind() != ChangeKind.ALIVE) {
            if (topicKind == TopicKind.WITH_KEY) {
                flags.setBitValue(1, true);
                inlineQosFlag = true;
                keyFlag = false;
            }
        }

        if (dataFlag) {
            flags.setBitValue(2, dataFlag);
        }

        if (keyFlag) {
            flags.setBitValue(3, keyFlag);
        }

        subHeader.setFlags(flags);

        // DATA Submessage creation

        ExtraFlags extraFlags = new ExtraFlags();
        submessageData.addSubmessageElement(extraFlags);

        short octectsToInlineQos = 16;
        OctectsToInlineQos octects2Qos = new OctectsToInlineQos(octectsToInlineQos);
        submessageData.addSubmessageElement(octects2Qos);

        submessageData.addSubmessageElement(readerId);

        submessageData.addSubmessageElement(change.getWriterGUID().getEntityId());

        submessageData.addSubmessageElement(change.getSequenceNumber());

        if (inlineQosFlag) {
            // TODO Insert in the following order: InstanceHandle, Status, inlineQos, Sentinel
            if (inlineQos != null) {
                if (inlineQos.getHasChanged()) {
                    submessageData.addSubmessageElement(inlineQos);
                }
            }
            if (topicKind == TopicKind.WITH_KEY) {

            }
            if (inlineQos != null) {

            }
        }

        // Add SerializedPayload
        if (dataFlag) {
            submessageData.addSubmessageElement(change.getSerializedPayload());
        } else {
            if (hasSerializedPayload) {
                submessageData.addSubmessageElement(change.getSerializedPayload());
            }
        }

        if (keyFlag) {


            SerializedPayload payload = new SerializedPayload();
            if (submessageData.getSubmessageEndian() == RTPSEndian.BIG_ENDIAN) {
                payload.setEncapsulationKind(EncapsulationKind.PL_CDR_BE);
            } else {
                payload.setEncapsulationKind(EncapsulationKind.PL_CDR_LE);
            }
            // payload.options are serialized automatically (no existing attribute)

            // Add ParameterKey, ParameterStatus and ParameterSentinel to payload
            payload.addParameter(new ParameterKey(change.getInstanceHandle()));
            payload.addParameter(new ParameterStatus(change.getKind()));
            payload.addParameter(new ParameterSentinel());
            submessageData.addSubmessageElement(payload);

        }

        short octectsToNextHeader = submessageData.getLength();
        if (octectsToNextHeader < 24) {
            octectsToNextHeader = 24;
        }

        subHeader.setOctectsToNextHeader(octectsToNextHeader);

        submessageData.setSubmessageHeader(subHeader);

        message.addSubmessage(submessageData);

        submessageData.serialize(message.getSerializer(), message.getBinaryOutputStream());

        return true;
    }

    /**
     * Adds a new {@link RTPSSubmessage} of type PAD to the provided {@link RTPSMessage}
     * 
     * @param message The {@link RTPSMessage}
     * @param nBytes The number of bytes in the PAD message
     * @return true on success; false otherwise
     */
    public static boolean addSubmessagePad(RTPSMessage message, short nBytes) {

        RTPSSubmessage submessagePad = new RTPSSubmessage();

        // Submessage ID
        RTPSSubmessageHeader subHeader = new RTPSSubmessageHeader();
        subHeader.setSubmessageId(SubmessageId.PAD);

        // Endian flag
        SubmessageFlags flags = new SubmessageFlags((byte) 0x0);
        if (message.getEndiannes() == RTPSEndian.LITTLE_ENDIAN) {
            flags.setBitValue(0, true);
        } 
        submessagePad.setSubmessageEndian(message.getEndiannes());

        subHeader.setFlags(flags);

        submessagePad.addSubmessageElement(new Pad(nBytes));
        submessagePad.setSubmessageHeader(subHeader);

        message.addSubmessage(submessagePad);

        return true;
    }

}
