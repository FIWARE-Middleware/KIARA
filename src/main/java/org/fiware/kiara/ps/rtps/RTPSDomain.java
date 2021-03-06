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
package org.fiware.kiara.ps.rtps;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fiware.kiara.ps.rtps.attributes.RTPSParticipantAttributes;
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipantListener;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.ReaderListener;
import org.fiware.kiara.ps.rtps.utils.IPFinder;
import org.fiware.kiara.ps.rtps.utils.SystemUtils;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.fiware.kiara.ps.rtps.writer.WriterListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class RTPSDomain,it manages the creation and destruction of RTPSParticipant
 * RTPSWriter and RTPSReader. It stores a list of all created RTPSParticipant.
 * Is has only static methods.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class RTPSDomain {

    /**
     * Veod r specific identifier
     */
    private static final byte[] vendorId = new byte[]{0x01, 0x0F};

    /**
     * Maximum {@link RTPSParticipant} identifier
     */
    private static int m_maxRTPSParticipantID = -1;

    /**
     * List of {@link RTPSParticipant} references
     */
    private static final List<RTPSParticipant> m_rtpsParticipants = new ArrayList<>();

    /**
     * List of non repeated {@link RTPSParticipant} identifiers
     */
    private static final Set<Integer> m_rtpsParticipantsIDs = new HashSet<>();

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(RTPSDomain.class);

    /**
     * Method to shut down all RTPSParticipants, readers, writers, etc. It must
     * be called at the end of the process to avoid memory leaks. It also shut
     * downs the DomainRTPSParticipant.
     */
    public void stopAll() {
        while (m_rtpsParticipants.size() > 0) {
            RTPSDomain.removeRTPSParticipant(m_rtpsParticipants.get(0));
        }
        logger.info("RTPSParticipants deleted correctly");
    }

    /**
     * Create a RTPSParticipant.
     *
     * @param att RTPSParticipant Parameters.
     * @param listener reference to the {@link RTPSParticipantListener}.
     * @return reference to the RTPSParticipant.
     */
    public static RTPSParticipant createParticipant(RTPSParticipantAttributes att, RTPSParticipantListener listener) {
        logger.info("Creating RTPSParticipant");

        if (att.builtinAtt.leaseDuration.isLowerThan(new Timestamp().timeInfinite()) && att.builtinAtt.leaseDuration.isLowerThan(att.builtinAtt.leaseDurationAnnouncementPeriod)) {
            logger.error("RTPSParticipant Attributes: LeaseDuration should be >= leaseDuration announcement period");
            return null;
        }

        if (att.useIPv4ToSend == false && att.useIPv6ToSend == false) {
            logger.error("Use IP4 OR User IP6 to send must be set to true");
            return null;
        }

        int ID;
        if (att.participantID < 0) {
            ID = getNewId();
            while (m_rtpsParticipantsIDs.add(ID) == false) {
                ID = getNewId();
            }
        } else {
            ID = att.participantID;
            if (m_rtpsParticipantsIDs.add(ID) == false) {
                logger.error("RTPSParticipant with the same ID already exists");
                return null;
            }
        }

        if (!att.defaultUnicastLocatorList.isValid()) {
            logger.error("Default unicast Locator List contains invalid locator");
            m_rtpsParticipantsIDs.remove(ID);
            return null;
        }

        if (!att.defaultMulticastLocatorList.isValid()) {
            m_rtpsParticipantsIDs.remove(ID);
            logger.error("Default Multicast Locator List contains invalid Locator");
            return null;
        }

        att.participantID = ID;

        int pid;
        //pid = (int) Thread.currentThread().getId();
        pid = (int) SystemUtils.getPID();

        GUIDPrefix guidP = new GUIDPrefix();
        LocatorList loc = IPFinder.getIPv4Adress();
        if (loc.getLocators().size() > 0) {
            guidP.setValue(0, vendorId[0]);
            guidP.setValue(1, vendorId[1]);
            guidP.setValue(2, loc.begin().getAddress()[14]);
            guidP.setValue(3, loc.begin().getAddress()[15]);
        } else {
            guidP.setValue(0, vendorId[0]);
            guidP.setValue(1, vendorId[1]);
            guidP.setValue(2, (byte) 127);
            guidP.setValue(3, (byte) 1);
        }

        byte[] bytesPID = ByteBuffer.allocate(4).putInt(pid).array();
        byte[] bytesID = ByteBuffer.allocate(4).putInt(ID).array();
        for (int i = 0; i < 4; ++i) {
            guidP.setValue(4 + i, bytesPID[i]);
            guidP.setValue(8 + i, bytesID[i]);
        }

        RTPSParticipant participant = null;
        try {
            participant = new RTPSParticipant(att, guidP, listener);
            m_maxRTPSParticipantID = participant.getRTPSParticipantID();
            m_rtpsParticipants.add(participant);
        } catch (Exception e) {
            m_rtpsParticipantsIDs.remove(ID);
            logger.error(e.toString());
        }

        // Resets Participant ID in case the same parameters are used to create another one
        att.resetParticipantID();

        return participant;
    }

    /**
     * Remove a RTPSParticipant and delete all its associated Writers, Readers,
     * resources, etc.
     *
     * @param p Reference to the RTPSParticipant;
     * @return True if correct.
     */
    public static boolean removeRTPSParticipant(RTPSParticipant p) {
        if (p != null) {
            for (int i = 0; i < m_rtpsParticipants.size(); ++i) {
                RTPSParticipant it = m_rtpsParticipants.get(i);
                if (it.getGUID().getGUIDPrefix().equals(p.getGUID().getGUIDPrefix())) {
                    m_rtpsParticipantsIDs.remove(it.getRTPSParticipantID());
                    m_rtpsParticipants.remove(it);
                    i--;
                    return true;
                }
            }
        }
        logger.error("RTPSParticipant not valid or not recognized");
        return false;
    }

    /**
     * Create a RTPSWriter in a participant.
     *
     * @param p reference to the RTPSParticipant.
     * @param watt Writer Attributes.
     * @param history reference to the WriterHistory.
     * @param listener reference to the WriterListener.
     * @return reference to the created RTPSWriter.
     */
    public static RTPSWriter createRTPSWriter(RTPSParticipant p, WriterAttributes watt, WriterHistoryCache history, WriterListener listener) {
        for (RTPSParticipant it : m_rtpsParticipants) {
            if (it.getGUID().getGUIDPrefix().equals(p.getGUID().getGUIDPrefix())) {
                RTPSWriter writer = it.createWriter(watt, history, listener, new EntityId(), false);
                if (writer != null) {
                    return writer;
                }
                return null;
            }
        }
        return null;
    }

    /**
     * Remove a RTPSWriter.
     *
     * @param writer reference to the writer you want to remove.
     * @return True if correctly removed.
     */
    public static boolean removeRTPSWriter(RTPSWriter writer) {
        if (writer != null) {
            for (RTPSParticipant it : m_rtpsParticipants) {
                if (it.getGUID().getGUIDPrefix().equals(writer.getGuid().getGUIDPrefix())) {
                    return it.deleteUserEndpoint((Endpoint) writer);
                }
            }
        }
        return false;
    }

    /**
     * Create a RTPSReader in a participant.
     *
     * @param p reference to the RTPSParticipant.
     * @param ratt Reader Attributes.
     * @param history reference to the ReaderHistory.
     * @param listener reference to the ReaderListener.
     * @return reference to the created RTPSReader.
     */
    public static RTPSReader createRTPSReader(RTPSParticipant p, ReaderAttributes ratt, ReaderHistoryCache history, ReaderListener listener) {
        for (RTPSParticipant it : m_rtpsParticipants) {
            if (it.getGUID().getGUIDPrefix().equals(p.getGUID().getGUIDPrefix())) {
                return it.createReader(ratt, history, listener, new EntityId(), false);
            }
        }
        return null;
    }

    /**
     * Remove a RTPSReader.
     *
     * @param reader reference to the reader you want to remove.
     * @return True if correctly removed.
     */
    public static boolean removeRTPSReader(RTPSReader reader) {
        if (reader != null) {
            for (RTPSParticipant it : m_rtpsParticipants) {
                if (it.getGUID().getGUIDPrefix().equals(reader.getGuid().getGUIDPrefix())) {
                    return it.deleteUserEndpoint((Endpoint) reader);
                }
            }
        }
        return false;
    }

    /**
     * Get Id to create a RTPSParticipant.
     *
     * @return Different ID for each call.
     */
    private static int getNewId() {
        return ++m_maxRTPSParticipantID;
    }

}
