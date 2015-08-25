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
package org.fiware.kiara.ps.rtps.builtin.discovery.participant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.publisher.WriterProxy;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind;
import org.fiware.kiara.ps.rtps.RTPSDomain;
import org.fiware.kiara.ps.rtps.attributes.BuiltinAttributes;
import org.fiware.kiara.ps.rtps.attributes.HistoryCacheAttributes;
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.builtin.BuiltinProtocols;
import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.ReaderProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.WriterProxyData;
import org.fiware.kiara.ps.rtps.builtin.discovery.endpoint.EDP;
import org.fiware.kiara.ps.rtps.builtin.discovery.endpoint.EDPSimple;
import org.fiware.kiara.ps.rtps.builtin.discovery.endpoint.EDPStatic;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.timedevent.ResendParticipantProxyDataPeriod;
import org.fiware.kiara.ps.rtps.common.DurabilityKind;
import org.fiware.kiara.ps.rtps.common.EncapsulationKind;
import org.fiware.kiara.ps.rtps.common.EndpointKind;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.common.ReliabilityKind;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId.EntityIdEnum;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.StatefulReader;
import org.fiware.kiara.ps.rtps.reader.StatelessReader;
import org.fiware.kiara.ps.rtps.utils.InfoEndianness;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.fiware.kiara.ps.rtps.writer.StatelessWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class PDPSimple {

    private BuiltinProtocols m_builtin;

    private RTPSParticipant m_RTPSParticipant;

    private BuiltinAttributes m_discovery;

    private StatelessWriter m_SPDPWriter;

    private StatelessReader m_SPDPReader;

    private EDP m_EDP;

    private List<ParticipantProxyData> m_participantProxies;

    private boolean m_hasChangedLocalPDP;

    private ResendParticipantProxyDataPeriod m_resendParticipantTimer;

    private PDPSimpleListener m_listener;

    private WriterHistoryCache m_SPDPWriterHistory;

    private ReaderHistoryCache m_SPDPReaderHistory;

    private final Lock m_mutex = new ReentrantLock(true);

    private static final Logger logger = LoggerFactory.getLogger(PDPSimple.class);

    private Object m_guardMutex = new Object();

    private Object m_guardMutexAlt = new Object();

    private Object m_guardW = new Object();

    private Object m_guardR = new Object();;

    public PDPSimple(BuiltinProtocols builtinProtocols) {
        this.m_builtin = builtinProtocols;
        this.m_hasChangedLocalPDP = true;
        /*this.m_guardMutex = new Object();
        this.m_guardMutexAlt = new Object();
        this.m_guardW = new Object();
        this.m_guardR = new Object();*/
        this.m_participantProxies = new ArrayList<ParticipantProxyData>();
        // TODO Create objects properly
    }

    public void destroy() {
        this.m_mutex.lock();
        try {

            if (this.m_EDP != null) {
                this.m_EDP.destroy();
            }

            if (this.m_resendParticipantTimer != null) {
                this.m_resendParticipantTimer.delete();
            }

            if (this.m_SPDPReader != null) {
                RTPSDomain.removeRTPSReader(this.m_SPDPReader);
            }

            if (this.m_SPDPWriter != null) {
                RTPSDomain.removeRTPSWriter(this.m_SPDPWriter);
            }

            /*if (this.m_listener != null) {
                this.m_listener.destroy();
            }*/

            while (this.m_participantProxies.size() > 0) {
                this.m_participantProxies.get(0).destroy();
                this.m_participantProxies.remove(0);
            }
        } finally {
            this.m_mutex.unlock();
        }
    }

    public boolean initPDP(RTPSParticipant participant) {
        logger.debug("Starting Participant Discovery Protocol (PDP)");
        this.m_RTPSParticipant = participant;
        this.m_discovery = this.m_RTPSParticipant.getAttributes().builtinAtt;
        this.m_mutex.lock();

        try {

            if (!createSPDPEndpoints()) {
                return false;
            }

            this.m_builtin.updateMetatrafficLocators(this.m_SPDPReader.getAttributes().unicastLocatorList);
            this.m_participantProxies.add(new ParticipantProxyData());
            this.m_participantProxies.get(0).initializeData(this.m_RTPSParticipant, this);

            // Init EDP 
            if (this.m_discovery.useStaticEDP) {
                this.m_EDP = new EDPStatic(this, this.m_RTPSParticipant);// TODO Uncomment
                if (!this.m_EDP.initEDP(this.m_discovery)) {
                    return false;
                }
            } else if (this.m_discovery.useSimpleEDP) {
                //this.m_EDP = new EDPSimple(this, this.m_RTPSParticipant);
                //this.m_EDP.initEDP(this.m_discovery);
            } else {
                logger.warn("No EndpointDiscoveryProtocol has been defined");
                return false;
            }

            this.m_resendParticipantTimer = new ResendParticipantProxyDataPeriod(this, this.m_discovery.leaseDurationAnnouncementPeriod.toMilliSecondsDouble());

            return true;

        } finally {
            this.m_mutex.unlock();
        }

    }

    public void stopParticipantAnnouncement() {
        this.m_resendParticipantTimer.stopTimer();
    }

    public void resetParticipantAnnouncement() {
        this.m_resendParticipantTimer.restartTimer();
    }

    public ParticipantProxyData getLocalParticipantProxyData() {
        return this.m_participantProxies.get(0);
    }

    public void announceParticipantState(boolean newChange) {
        this.m_mutex.lock();
        try {
            logger.debug("Announcing RTPSParticipant State (new change: {})", newChange);
            CacheChange change = null;

            if (newChange || this.m_hasChangedLocalPDP) {
                this.getLocalParticipantProxyData().increaseManualLivelinessCount();
                if (this.m_SPDPWriterHistory.getHistorySize() > 0) {
                    this.m_SPDPWriterHistory.removeMinChange();
                }
                change = this.m_SPDPWriter.newChange(ChangeKind.ALIVE, getLocalParticipantProxyData().getKey());
                ParticipantProxyData proxyData = getLocalParticipantProxyData();
                proxyData.setHasChanged(true);
                ParameterList paramList = proxyData.toParameterList();
                //ParameterList paramList = getLocalParticipantProxyData().toParameterList();
                if (paramList != null) {
                    change.getSerializedPayload().setEncapsulationKind(InfoEndianness.checkMachineEndianness() == RTPSEndian.BIG_ENDIAN ? EncapsulationKind.PL_CDR_BE : EncapsulationKind.PL_CDR_LE);
                    change.getSerializedPayload().deleteParameters();
                    change.getSerializedPayload().addParameters(paramList);
                    this.m_SPDPWriterHistory.addChange(change);
                } 
                this.m_hasChangedLocalPDP = false;
            } else {
                this.m_SPDPWriter.unsentChangesReset();
            }
        } finally {
            this.m_mutex.unlock();
        }
    }

    public ReaderProxyData lookupReaderProxyData(GUID reader) {
        this.m_mutex.lock();
        logger.debug("Lookup ReaderProxyData: " + reader);
        try {
            for (ParticipantProxyData pit : this.m_participantProxies) {
                synchronized (this.m_guardMutex) {
                    for (ReaderProxyData rit : pit.getReaders()) {
                        if (rit.getGUID().equals(reader)) {
                            return rit;
                        }
                    }
                }
            }
            return null;
        } finally {
            this.m_mutex.unlock();
        }
    }

    public WriterProxyData lookupWriterProxyData(GUID writer) {
        this.m_mutex.lock();
        logger.debug("Lookup WriterProxyData " + writer);
        try {
            for (ParticipantProxyData pit : this.m_participantProxies) {
                synchronized(this.m_guardMutex) {
                    for (WriterProxyData wit : pit.getWriters()) {
                        if (wit.getGUID().equals(writer)) {
                            return wit;
                        }
                    }
                }
            }
            return null;
        } finally {
            this.m_mutex.unlock();
        }
    }

    public boolean removeReaderProxyData(ReaderProxyData rdata) {
        this.m_mutex.lock();
        logger.debug("Removing ReaderProxyData " + rdata.getGUID());
        try {
            for (ParticipantProxyData pit : this.m_participantProxies) {
                synchronized(this.m_guardMutex) {
                    for (ReaderProxyData rit : pit.getReaders()) {
                        if (rit.getGUID().equals(rdata.getGUID())) {
                            return pit.getReaders().remove(rdata);
                        }
                    }
                }
            }
            return false;
        } finally {
            this.m_mutex.unlock();
        }
    }

    public boolean removeWriterProxyData(WriterProxyData wdata) {
        this.m_mutex.lock();
        logger.debug("Removing WriterProxyData " + wdata.getGUID());
        try {
            for (ParticipantProxyData pit : this.m_participantProxies) {
                synchronized(this.m_guardMutex) {
                    for (WriterProxyData wit : pit.getWriters()) {
                        if (wit.getGUID().equals(wdata.getGUID())) {
                            return pit.getWriters().remove(wdata);
                        }
                    }
                }
            }
            return false;
        } finally {
            this.m_mutex.unlock();
        }
    }

    public ParticipantProxyData lookupParticipantProxyData(GUID pguid) {
        logger.info("Lookup ParticipantProxyData " + pguid);
        this.m_mutex.lock();
        try {
            for (ParticipantProxyData pit : this.m_participantProxies) {
                if (pit.getGUID().equals(pguid)) {
                    return pit;
                }
            }
            return null;
        } finally {
            this.m_mutex.unlock();
        }
    }

    private boolean createSPDPEndpoints() {
        logger.debug("Beginning PDP Builtin Endpoints creation");

        HistoryCacheAttributes whatt = new HistoryCacheAttributes();
        whatt.payloadMaxSize = ParticipantProxyData.DISCOVERY_PARTICIPANT_DATA_MAX_SIZE;
        whatt.initialReservedCaches = 20;
        whatt.maximumReservedCaches = 100;

        this.m_SPDPWriterHistory = new WriterHistoryCache(whatt);

        WriterAttributes watt = new WriterAttributes();
        watt.endpointAtt.endpointKind = EndpointKind.WRITER;
        watt.endpointAtt.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;
        watt.endpointAtt.reliabilityKind = ReliabilityKind.BEST_EFFORT;
        watt.endpointAtt.topicKind = TopicKind.WITH_KEY;

        RTPSWriter wout = this.m_RTPSParticipant.createWriter(watt, this.m_SPDPWriterHistory, null, new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER), true);
        if (wout != null) {
            this.m_SPDPWriter = (StatelessWriter) wout;
            RemoteReaderAttributes ratt = new RemoteReaderAttributes();
            for (Locator lit : this.m_builtin.getMetatrafficMulticastLocatorList().getLocators()) {
                this.m_SPDPWriter.addLocator(ratt, lit);
            }
            if (this.m_builtin.getUseMandaory()) {
                this.m_SPDPWriter.addLocator(ratt, this.m_builtin.getMandatoryMulticastLocator());
            }
            logger.debug("Simple PDP Builtin Writer created with GUID {}", this.m_SPDPWriter.getGuid());
        } else {
            logger.error("Simple PDP Builtin Writer creation failed");
            this.m_SPDPWriterHistory = null;
            return false;
        }

        HistoryCacheAttributes rhatt = new HistoryCacheAttributes();
        rhatt.payloadMaxSize = ParticipantProxyData.DISCOVERY_PARTICIPANT_DATA_MAX_SIZE;
        rhatt.initialReservedCaches = 250;
        rhatt.maximumReservedCaches = 5000;

        this.m_SPDPReaderHistory = new ReaderHistoryCache(rhatt);

        ReaderAttributes ratt = new ReaderAttributes();
        ratt.endpointAtt.multicastLocatorList.copy(this.m_builtin.getMetatrafficMulticastLocatorList());
        ratt.endpointAtt.unicastLocatorList.copy(this.m_builtin.getMetatrafficUnicastLocatorList());
        //this.m_builtin.getMetatrafficMulticastLocatorList().getLocators().get(0).setPort(5555);
        ratt.endpointAtt.topicKind = TopicKind.WITH_KEY;
        ratt.endpointAtt.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;
        ratt.endpointAtt.reliabilityKind = ReliabilityKind.BEST_EFFORT;

        this.m_listener = new PDPSimpleListener(this);

        RTPSReader rout = this.m_RTPSParticipant.createReader(ratt, this.m_SPDPReaderHistory, this.m_listener, new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER), true);
        if (rout != null) {
            this.m_SPDPReader = (StatelessReader) rout;
            logger.debug("Simple PDP Builtin Reader created with GUID {}", this.m_SPDPReader.getGuid());
        } else {
            logger.error("Simple PDP builtin Reader creation failed");
            this.m_SPDPReaderHistory = null;
            this.m_listener = null;
            return false;
        }

        logger.debug("SPDP Builtin Endpoints creation finished");
        return true;
    }

    public boolean addReaderProxyData(ReaderProxyData rdata) {
        return addReaderProxyData(rdata, false, null, null);
    }

    public boolean addReaderProxyData(ReaderProxyData rdata, boolean copyData) {
        return addReaderProxyData(rdata, copyData, null, null);
    }

    public boolean addReaderProxyData(ReaderProxyData rdata, boolean copyData, ReaderProxyData returnReaderProxyData, ParticipantProxyData pdata) {
        logger.debug("Adding ReaderProxyData: " + rdata.getGUID());
        this.m_mutex.lock();
        try {
            for (ParticipantProxyData pit : this.m_participantProxies) {
                synchronized(this.m_guardMutex) {
                    if (pit.getGUID().getGUIDPrefix().equals(rdata.getGUID().getGUIDPrefix())) {
                        // Check that it is not already
                        for (ReaderProxyData rit : pit.getReaders()) {
                            if (rit.getGUID().getEntityId().equals(rdata.getGUID().getEntityId())) {
                                if (copyData) {
                                    returnReaderProxyData.copy(rit);
                                    pdata.copy(pit);
                                }
                                return false;
                            }
                        }
                        if (copyData) {
                            ReaderProxyData newRPD = new ReaderProxyData();
                            newRPD.copy(rdata);
                            pit.getReaders().add(newRPD);
                            returnReaderProxyData.copy(newRPD);
                            pdata.copy(pit);
                        } else {
                            pit.getReaders().add(rdata);
                        }
                        return true;
                    }
                }
            }
            return false;
        } finally {
            this.m_mutex.unlock();
        }
    }

    public boolean addWriterProxyData(WriterProxyData wdata) {
        return addWriterProxyData(wdata, false, null, null);
    }

    public boolean addWriterProxyData(WriterProxyData wdata, boolean copyData) {
        return addWriterProxyData(wdata, copyData, null, null);
    }

    public boolean addWriterProxyData(WriterProxyData wdata, boolean copyData, WriterProxyData returnWriterProxyData, ParticipantProxyData pdata) {
        logger.debug("Adding WriterProxyData: " + wdata.getGUID());
        this.m_mutex.lock();
        try {
            for (ParticipantProxyData pit : this.m_participantProxies) {
                synchronized(this.m_guardMutex) {
                    if (pit.getGUID().getGUIDPrefix().equals(wdata.getGUID().getGUIDPrefix())) {
                        // Check that it is not already
                        for (WriterProxyData rit : pit.getWriters()) {
                            if (rit.getGUID().getEntityId().equals(wdata.getGUID().getEntityId())) {
                                if (copyData) {
                                    returnWriterProxyData.copy(rit);
                                    pdata.copy(pit);
                                }
                                return false;
                            }
                        }
                        if (copyData) {
                            WriterProxyData newWPD = new WriterProxyData();
                            newWPD.copy(wdata);
                            pit.getWriters().add(newWPD);
                            returnWriterProxyData.copy(newWPD);
                            pdata.copy(pit);
                        } else {
                            pit.getWriters().add(wdata);
                        }
                        return true;
                    }
                }
            }
            return false;
        } finally {
            this.m_mutex.unlock();
        }
    }

    public void assignRemoteEndpoints(ParticipantProxyData pdata) {
        logger.debug("Assign remote Endpoints for RTPSParticipant {}", pdata.getGUID().getGUIDPrefix());
        int endp = pdata.getAvailableBuiltinEndpoints();
        this.m_mutex.lock();
        try {
            int auxEndp = endp;
            auxEndp &= ParticipantProxyData.DISC_BUILTIN_ENDPOINT_PARTICIPANT_ANNOUNCER;
            if (auxEndp != 0) {
                RemoteWriterAttributes watt = new RemoteWriterAttributes();
                watt.guid.setGUIDPrefix(pdata.getGUID().getGUIDPrefix());
                watt.guid.setEntityId(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER));
                watt.endpoint.unicastLocatorList.copy(pdata.getMetatrafficUnicastLocatorList());
                watt.endpoint.multicastLocatorList.copy(pdata.getMetatrafficMulticastLocatorList());
                watt.endpoint.reliabilityKind = ReliabilityKind.BEST_EFFORT;
                watt.endpoint.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;
                pdata.getBuiltinWriters().add(watt);
                this.m_SPDPReader.matchedWriterAdd(watt);
            }
            auxEndp = endp;
            auxEndp &= ParticipantProxyData.DISC_BUILTIN_ENDPOINT_PARTICIPANT_DETECTOR;
            if (auxEndp != 0) {
                RemoteReaderAttributes ratt = new RemoteReaderAttributes();
                ratt.expectsInlineQos = false;
                ratt.guid.setGUIDPrefix(pdata.getGUID().getGUIDPrefix());
                ratt.guid.setEntityId(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER));
                ratt.endpoint.unicastLocatorList.copy(pdata.getMetatrafficUnicastLocatorList());
                ratt.endpoint.multicastLocatorList.copy(pdata.getMetatrafficMulticastLocatorList());
                ratt.endpoint.reliabilityKind = ReliabilityKind.BEST_EFFORT;
                ratt.endpoint.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;
                pdata.getBuiltinReaders().add(ratt);
                this.m_SPDPWriter.matchedReaderAdd(ratt);
            }

            //Inform EDP of new RTPSParticipant data:

            if (this.m_EDP != null) {
                this.m_EDP.assignRemoteEndpoints(pdata);
            }

            if (this.m_builtin.getWLP() != null) {
                this.m_builtin.getWLP().assignRemoteEndpoints(pdata);
            }
        } finally {
            this.m_mutex.unlock();
        }
    }

    public void removeRemoteEndpoints(ParticipantProxyData pdata) {
        logger.info("For RTPSParticipant: " + pdata.getGUID());
        this.m_mutex.lock();
        try {
            for (RemoteReaderAttributes it : pdata.getBuiltinReaders()) {
                if (it.guid.getEntityId().equals(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER)) && this.m_SPDPWriter != null) {
                    this.m_SPDPWriter.matchedReaderRemove(it);
                }
            }
            for (RemoteWriterAttributes it : pdata.getBuiltinWriters()) {
                if (it.guid.getEntityId().equals(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER)) && this.m_SPDPReader != null) {
                    this.m_SPDPReader.matchedWriterRemove(it);
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
    }

    public boolean removeRemoteParticipant(GUID partGUID) {
        logger.info("Removing RemoteParticipant: " + partGUID);
        synchronized (this.m_guardW) {
            synchronized (this.m_guardR) {
                ParticipantProxyData pdata = null;
                this.m_mutex.lock();
                try {
                    for (ParticipantProxyData pit : this.m_participantProxies) {
                        synchronized(this.m_guardMutex) {
                            if (pit.getGUID().equals(partGUID)) {
                                pdata = pit;
                                this.m_participantProxies.remove(pit);
                                break;
                            }
                        }
                    }
                    if (pdata != null) {
                        pdata.getMutex().lock();
                        try {
                            if (this.m_EDP != null) {
                                for (ReaderProxyData rit : pdata.getReaders()) {
                                    this.m_EDP.unpairReaderProxy(rit);
                                }
                                for (WriterProxyData wit : pdata.getWriters()) {
                                    this.m_EDP.unpairWriterProxy(wit);
                                }
                            }
                            if (this.m_builtin.getWLP() != null) {
                                this.m_builtin.getWLP().removeRemoteEndpoints(pdata);
                            }
                            this.m_EDP.removeRemoteEndpoints(pdata);
                            this.removeRemoteEndpoints(pdata);
                            for (CacheChange it : this.m_SPDPReaderHistory.getChanges()) {
                                if (it.getInstanceHandle().equals(pdata.getKey())) {
                                    this.m_SPDPReaderHistory.removeChange(it);
                                    break;
                                }
                            }
                            return true;
                        } finally {
                            pdata.getMutex().unlock();
                        }
                    }
                } finally {
                    this.m_mutex.unlock();
                }
            }
        }
        return false;
    }

    public void assertRemoteParticipantLiveliness(GUIDPrefix guidPrefix) {
        this.m_mutex.lock();
        try {
            for (ParticipantProxyData it : this.m_participantProxies) {
                synchronized(this.m_guardMutex) {
                    if (it.getGUID().getGUIDPrefix().equals(guidPrefix)) {
                        logger.info("RTPSParticipant " + it.getGUID() + " is Alive");
                        it.setIsAlive(true);
                    }
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
    }

    public void assertLocalWritersLiveliness(LivelinessQosPolicyKind kind) {
        logger.info("Asserting liveliness of type " + (kind == LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS ? "AUTOMATIC" : "") + 
                (kind == LivelinessQosPolicyKind.MANUAL_BY_PARTICIPANT_LIVELINESS_QOS ? "MANUAL_BY_PARTICIPANT" : ""));
        this.m_mutex.lock();
        try {
            this.m_participantProxies.get(0).getMutex().lock();
            try {
                for (WriterProxyData wit : this.m_participantProxies.get(0).getWriters()) {
                    if (wit.getQos().liveliness.kind == kind) {
                        logger.info("Local writer " + wit.getGUID().getEntityId() + " marked as ALIVE");
                        wit.setIsAlive(true);
                    }
                }
            } finally {
                this.m_participantProxies.get(0).getMutex().unlock();
            }
        } finally {
            this.m_mutex.unlock();
        }
    }

    public void assertRemoteWritersLiveliness(GUIDPrefix guidP, LivelinessQosPolicyKind kind) {
        this.m_mutex.lock();
        try {
            logger.info("Asserting liveliness of type " + (kind == LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS ? "AUTOMATIC" : "") + 
                    (kind == LivelinessQosPolicyKind.MANUAL_BY_PARTICIPANT_LIVELINESS_QOS ? "MANUAL_BY_PARTICIPANT" : ""));
            for (ParticipantProxyData pit : this.m_participantProxies) {
                pit.getMutex().lock();
                try {
                    for (WriterProxyData wit : pit.getWriters()) {
                        if (wit.getQos().liveliness.kind == kind) {
                            wit.setIsAlive(true);
                            this.m_RTPSParticipant.getParticipantMutex().lock();
                            try {
                                for (RTPSReader rit : this.m_RTPSParticipant.getUserReaders()) {
                                    if (rit.getAttributes().reliabilityKind == ReliabilityKind.RELIABLE) {
                                        // Not supported in this version
                                        /*StatefulReader sfr = (StatefulReader) rit;
                                        WriterProxy wp = new WriterProxy();
                                        if (sfr.matchedWriterLookup(wit.getGUID(), wp)) {
                                            wp.assertLiveliness();
                                            continue;
                                        }*/
                                    }
                                }
                            } finally {
                                this.m_RTPSParticipant.getParticipantMutex().unlock();
                            }
                        }
                        break;
                    }
                } finally {
                    pit.getMutex().unlock();
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
    }

    public boolean newRemoteEndpointStaticallyDiscovered(GUID pguid, short userDefienedId, EndpointKind kind) {
        ParticipantProxyData pdata = lookupParticipantProxyData(pguid);
        if (pdata != null) {
            if (kind == EndpointKind.WRITER) {
                ((EDPStatic) this.m_EDP).newRemoteWriter(pdata, userDefienedId, new EntityId());
            } else {
                ((EDPStatic) this.m_EDP).newRemoteReader(pdata, userDefienedId, new EntityId());
            }
        }
        return false;
    }

    public EDP getEDP() {
        return this.m_EDP;
    }

    public BuiltinProtocols getBuiltinProtocols() {
        return this.m_builtin;
    }

    public Lock getMutex() {
        return m_mutex;
    }

    public List<ParticipantProxyData> getParticipantProxies() {
        return m_participantProxies;
    }

    public ReaderHistoryCache getSPDPReaderHistory() {
        return this.m_SPDPReaderHistory;
    }

    public RTPSParticipant getRTPSParticipant() {
        return this.m_RTPSParticipant;
    }

    public BuiltinAttributes getDiscovery() {
        return this.m_discovery;
    }




}
