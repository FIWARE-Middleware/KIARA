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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumberSet;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.fiware.kiara.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class RTPSMessageGroup {

    private static final Logger logger = LoggerFactory.getLogger(WriterHistoryCache.class);

    public static void sendChangesAsData(RTPSWriter rtpsWriter, List<CacheChange> changes, LocatorList unicastLocatorList, LocatorList multicastLocatorList, boolean expectsInlineQos, EntityId entityId) {

        short dataMsgSize = 0;
        short changeIndex = 1;

        RTPSMessage msg = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN);
        RTPSMessageBuilder.addHeader(msg, rtpsWriter.getGuid().getGUIDPrefix());
        RTPSMessageBuilder.addSubmessageInfoTSNow(msg, false/*, false*/);

        // TODO Check if this can be placed into RTPSMessageBuilder for every submessage
        msg.checkPadding(false); 

        Iterator<CacheChange> cit = changes.iterator();
        if (cit.hasNext()) {
            int initialPos = msg.getBinaryOutputStream().getPosition();
            RTPSMessageGroup.prepareSubmessageData(msg, rtpsWriter, cit.next(), expectsInlineQos, entityId);
            dataMsgSize = (short) (msg.getBinaryOutputStream().getPosition() - initialPos);
            if (dataMsgSize + RTPSMessage.RTPS_MESSAGE_HEADER_SIZE > msg.getMaxSize()) {
                logger.error("The Data messages are larger than max size");
                return;
            }
            msg.checkPadding(true);
        }
        boolean first = true;

        do {

            boolean added = false;

            if (first) {
                first = false;
                added = true;
            } else {
                msg = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN);
                RTPSMessageBuilder.addHeader(msg, rtpsWriter.getGuid().getGUIDPrefix());
                RTPSMessageBuilder.addSubmessageInfoTSNow(msg, false/*, false*/);
                msg.checkPadding(false); // TODO CHeck if this can be placed into RTPSMessageBuilder for every submessage
            }

            while (cit.hasNext()) {
                if (msg.getBinaryOutputStream().getPosition() + dataMsgSize < msg.getMaxSize()) {
                    added = true;
                    ++changeIndex;
                    RTPSMessageGroup.prepareSubmessageData(msg, rtpsWriter, cit.next(), expectsInlineQos, entityId);
                    msg.checkPadding(true);
                } else {
                    break;
                }
            }

            if (added) {
                msg.serialize();
                for (Locator unicastLoc : unicastLocatorList.getLocators()) {
                    rtpsWriter.getRTPSParticipant().sendSync(msg, unicastLoc);
                }
                for (Locator multicastLoc : multicastLocatorList.getLocators()) {
                    rtpsWriter.getRTPSParticipant().sendSync(msg, multicastLoc);
                }
            } else {
                logger.error("A problem occurred when adding a message");
            }

        } while (changeIndex < changes.size());
    }

    /*public static void sendChangesAsData_old(RTPSWriter rtpsWriter, List<CacheChange> changes, LocatorList unicastLocatorList, LocatorList multicastLocatorList, boolean expectsInlineQos, EntityId entityId) {

        RTPSMessage msg = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN);
        RTPSMessageBuilder.addHeader(msg, rtpsWriter.getGuid().getGUIDPrefix());
        boolean added = false;

        for (CacheChange cit : changes) {

            RTPSMessageBuilder.addSubmessageInfoTSNow(msg, false);

            RTPSMessageGroup.prepareSubmessageData(msg, rtpsWriter, cit, expectsInlineQos, entityId);

            added = true;

        }

        msg.serialize();

        if (added) {
            for (Locator unicastLoc : unicastLocatorList.getLocators()) {
                rtpsWriter.getRTPSParticipant().sendSync(msg, unicastLoc);
            }
            for (Locator multicastLoc : multicastLocatorList.getLocators()) {
                rtpsWriter.getRTPSParticipant().sendSync(msg, multicastLoc);
            }
        }


    }*/

    public static void sendChangesAsData(RTPSWriter rtpsWriter, List<CacheChange> changes, Locator locator, boolean expectsInlineQos, EntityId entityId) {
        short dataMsgSize = 0;
        short changeIndex = 1;

        RTPSMessage msg = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN);
        RTPSMessageBuilder.addHeader(msg, rtpsWriter.getGuid().getGUIDPrefix());
        RTPSMessageBuilder.addSubmessageInfoTSNow(msg, false/*, false*/);
        msg.checkPadding(false); // TODO CHeck if this can be placed into RTPSMessageBuilder for every submessage

        Iterator<CacheChange> cit = changes.iterator();
        if (cit.hasNext()) {
            int initialPos = msg.getBinaryOutputStream().getPosition();
            RTPSMessageGroup.prepareSubmessageData(msg, rtpsWriter, cit.next(), expectsInlineQos, entityId);
            dataMsgSize = (short) (msg.getBinaryOutputStream().getPosition() - initialPos);
            if (dataMsgSize + RTPSMessage.RTPS_MESSAGE_HEADER_SIZE > msg.getMaxSize()) {
                logger.error("The Data messages are larger than max size");
                return;
            }
            msg.checkPadding(true);
        }
        boolean first = true;

        do {

            boolean added = false;

            if (first) {
                first = false;
                added = true;
            } else {
                msg = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN);
                RTPSMessageBuilder.addHeader(msg, rtpsWriter.getGuid().getGUIDPrefix());
                RTPSMessageBuilder.addSubmessageInfoTSNow(msg, false/*, false*/);
                msg.checkPadding(false); // TODO CHeck if this can be placed into RTPSMessageBuilder for every submessage
            }

            while (cit.hasNext()) {
                if (msg.getBinaryOutputStream().getPosition() + dataMsgSize < msg.getMaxSize()) {
                    added = true;
                    ++changeIndex;
                    RTPSMessageGroup.prepareSubmessageData(msg, rtpsWriter, cit.next(), expectsInlineQos, entityId);
                    msg.checkPadding(true);
                } else {
                    break;
                }
            }

            if (added) {
                msg.serialize();
                rtpsWriter.getRTPSParticipant().sendSync(msg, locator);
            } else {
                logger.error("A problem occurred when adding a message");
            }

        } while (changeIndex < changes.size());

    }

    public static void sendChangesAsGap(RTPSWriter rtpsWriter, List<SequenceNumber> changesSeqNum, EntityId readerId, LocatorList unicastLocatorList, LocatorList multicastLocatorList) {

        short gapMsgSize = 0;
        short changeIndex = 1;

        RTPSMessage msg = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN);
        RTPSMessageBuilder.addHeader(msg, rtpsWriter.getGuid().getGUIDPrefix());
        RTPSMessageBuilder.addSubmessageInfoTSNow(msg, false/*, false*/);
        // TODO CHeck if this can be placed into RTPSMessageBuilder for every submessage
        msg.checkPadding(false); 

        List<Pair<SequenceNumber, SequenceNumberSet>> sequences = RTPSMessageGroup.prepareSequenceNumberSet(changesSeqNum);
        Iterator<Pair<SequenceNumber, SequenceNumberSet>> seqit = sequences.iterator();
        if (seqit.hasNext()) {
            Pair<SequenceNumber, SequenceNumberSet> pair = seqit.next();
            int initialPos = msg.getBinaryOutputStream().getPosition();
            RTPSMessageBuilder.addSubmessageGap(msg, pair.getFirst(), pair.getSecond(), readerId, rtpsWriter.getGuid().getEntityId());
            gapMsgSize = (short) (msg.getBinaryOutputStream().getPosition() - initialPos);
            if (gapMsgSize + RTPSMessage.RTPS_MESSAGE_HEADER_SIZE > msg.getMaxSize()) {
                logger.error("The Gap messages are larger than max size");
                return;
            }
            msg.checkPadding(false);
        }
        boolean first = true;

        do {

            boolean added = false;

            if (first) {
                first = false;
                added = true;
            } else {
                msg = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN);
                RTPSMessageBuilder.addHeader(msg, rtpsWriter.getGuid().getGUIDPrefix());
                // TODO Check if this can be placed into RTPSMessageBuilder for every submessage
                msg.checkPadding(false); 
            }

            while (seqit.hasNext()) {
                Pair<SequenceNumber, SequenceNumberSet> pair = seqit.next();
                if (msg.getBinaryOutputStream().getPosition() + gapMsgSize < msg.getMaxSize()) {
                    added = true;
                    ++changeIndex;
                    RTPSMessageBuilder.addSubmessageGap(msg, pair.getFirst(), pair.getSecond(), readerId, rtpsWriter.getGuid().getEntityId());
                    msg.checkPadding(false);
                } else {
                    break;
                }
            }

            if (added) {
                msg.serialize();
                for (Locator unicastLoc : unicastLocatorList.getLocators()) {
                    rtpsWriter.getRTPSParticipant().sendSync(msg, unicastLoc);
                }
                for (Locator multicastLoc : multicastLocatorList.getLocators()) {
                    rtpsWriter.getRTPSParticipant().sendSync(msg, multicastLoc);
                }
            } else {
                logger.error("A problem occurred when adding a message");
            }

        } while (changeIndex < sequences.size());

    }

    private static void prepareSubmessageData(RTPSMessage msg, RTPSWriter rtpsWriter, CacheChange change, boolean expectsInlineQos, EntityId entityId) {
        ParameterList inlineQos = null;
        if (expectsInlineQos) {
            // TODO Prepare inline QOS (Not supported yet)
        }

        RTPSMessageBuilder.addSubmessageData(msg, change, rtpsWriter.getAttributes().topicKind, entityId, expectsInlineQos, inlineQos); 

    }

    private static List<Pair<SequenceNumber, SequenceNumberSet>> prepareSequenceNumberSet(List<SequenceNumber> changesSeqNum) { // TODO Review this

        List<Pair<SequenceNumber, SequenceNumberSet>> sequences = new ArrayList<Pair<SequenceNumber, SequenceNumberSet>>();

        Collections.sort(changesSeqNum);

        boolean newPair = true;
        boolean seqNumSetInit = false;
        int count = 0;

        for (int i=0; i < changesSeqNum.size(); ++i) {
            SequenceNumber it = changesSeqNum.get(i);
            if (newPair) {
                SequenceNumberSet seqSet = new SequenceNumberSet();
                it.increment();
                seqSet.setBase(it);
                Pair<SequenceNumber, SequenceNumberSet> pair = new Pair<SequenceNumber, SequenceNumberSet>(it, seqSet);
                sequences.add(pair);
                newPair = false;
                seqNumSetInit = false;
                count = 1;
                continue;
            }
            if (it.toLong() - sequences.get(sequences.size()-1).getFirst().toLong() == count) {
                ++count;
                it.increment();
                sequences.get(sequences.size()-1).getSecond().setBase(it);
                continue;
            } else {
                if (!seqNumSetInit) {
                    it.decrement();
                    sequences.get(sequences.size()-1).getSecond().setBase(it);
                    seqNumSetInit = false;
                }
                if (sequences.get(sequences.size()-1).getSecond().add(it)) {
                    continue;
                } else {
                    --i;
                    newPair = true;
                }
            }
        }

        return null;
    }

}
