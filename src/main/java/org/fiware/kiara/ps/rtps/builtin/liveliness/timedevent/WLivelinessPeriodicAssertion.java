/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
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
package org.fiware.kiara.ps.rtps.builtin.liveliness.timedevent;

import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind;
import static org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
import static org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind.MANUAL_BY_PARTICIPANT_LIVELINESS_QOS;
import org.fiware.kiara.ps.rtps.builtin.liveliness.WLP;
import org.fiware.kiara.ps.rtps.common.EncapsulationKind;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import static org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind.ALIVE;
import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import static org.fiware.kiara.ps.rtps.resources.TimedEvent.EventCode.EVENT_ABORT;
import static org.fiware.kiara.ps.rtps.resources.TimedEvent.EventCode.EVENT_SUCCESS;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class WLivelinessPeriodicAssertion, used to assert the liveliness of the
 * writers in a RTPSParticipant.
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class WLivelinessPeriodicAssertion extends TimedEvent {

    private static final Logger logger = LoggerFactory.getLogger(WLP.class);

    /**
     * Kind that is being asserted by this object.
     */
    private final LivelinessQosPolicyKind m_livelinessKind;
    /**
     * WLP object.
     */
    private final WLP m_WLP;
    /**
     * Instance Handle
     */
    private final InstanceHandle m_iHandle;

    /**
     * GUIDPrefix
     */
    private final GUIDPrefix m_guidP;

    public WLivelinessPeriodicAssertion(WLP pwlp, LivelinessQosPolicyKind kind) {
        super(0);
        m_guidP = new GUIDPrefix();
        m_livelinessKind = kind;
        m_WLP = pwlp;
        m_iHandle = new InstanceHandle();

        m_guidP.copy(m_WLP.getRTPSParticipant().getGUID().getGUIDPrefix());
        for (int i = 0; i < 12; ++i) {
            m_iHandle.setValue(i, m_guidP.getValue(i));
        }
        m_iHandle.setValue(15, (byte) (m_livelinessKind.getValue() + 0x01));
    }
    
    public WLivelinessPeriodicAssertion(WLP pwlp, LivelinessQosPolicyKind kind, double milliseconds) {
        super(milliseconds);
        m_guidP = new GUIDPrefix();
        m_livelinessKind = kind;
        m_WLP = pwlp;
        m_iHandle = new InstanceHandle();

        m_guidP.copy(m_WLP.getRTPSParticipant().getGUID().getGUIDPrefix());
        for (int i = 0; i < 12; ++i) {
            m_iHandle.setValue(i, m_guidP.getValue(i));
        }
        m_iHandle.setValue(15, (byte) (m_livelinessKind.getValue() + 0x01));
    }

    /**
     * Method invoked when the event occurs
     *
     * @param code Code representing the status of the event
     * @param msg Message associated to the event
     */
    @Override
    public void event(EventCode code, String msg) {
        if (code == EVENT_SUCCESS) {
            logger.info("RTPS LIVELINESS: Period: {}", getIntervalMilliSec());
            if (m_WLP.getBuiltinWriter().getMatchedReadersSize() > 0) {
                if (m_livelinessKind == AUTOMATIC_LIVELINESS_QOS) {
                    automaticLivelinessAssertion();
                } else if (m_livelinessKind == MANUAL_BY_PARTICIPANT_LIVELINESS_QOS) {
                    manualByRTPSParticipantLivelinessAssertion();
                }
            }
            m_WLP.getBuiltinProtocols().getPDP().assertLocalWritersLiveliness(m_livelinessKind);
           // restartTimer();
        } else if (code == EVENT_ABORT) {
            logger.info("RTPS LIVELINESS: Liveliness Periodic Assertion aborted");
            stopSemaphorePost();
        } else {
            logger.info("RTPS LIVELINESS: Boost message: {}", msg);
        }
    }

    /**
     * Assert the liveliness of AUTOMATIC kind Writers.
     */
    public boolean automaticLivelinessAssertion() {
        final Lock mutex = m_WLP.getMutex();
        mutex.lock();
        try {
            if (m_WLP.getLivAutomaticWriters().size() > 0) {
                CacheChange change = m_WLP.getBuiltinWriter().newChange(ALIVE, new InstanceHandle());
                if (change != null) {
                    change.setInstanceHandle(m_iHandle);

                    SerializedPayload sp = change.getSerializedPayload();

                    sp.setEncapsulationKind(EncapsulationKind.CDR_BE);
                    byte[] buf = new byte[12 + 4 + 4 + 4];
                    System.arraycopy(m_guidP.getValue(), 0, buf, 0, 12);
                    for (int i = 12; i < 24; ++i) {
                        buf[i] = 0;
                    }
                    buf[15] = (byte) (m_livelinessKind.getValue() + 1);

                    sp.setBuffer(buf);
                    sp.setLength((short) buf.length);

                    Iterator<CacheChange> chi = m_WLP.getBuiltinWriterHistory().getChanges().iterator();
                    while (chi.hasNext()) {
                        CacheChange ch = chi.next();
                        if (ch.getInstanceHandle().equals(change.getInstanceHandle())) {
                            chi.remove();
                        }
                    }

                    m_WLP.getBuiltinWriterHistory().addChange(change);
                }
            }
            return true;
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Assert the liveliness of MANUAL_BY_PARTICIPANT kind writers.
     */
    public boolean manualByRTPSParticipantLivelinessAssertion() {
        final Lock mutex = m_WLP.getMutex();
        mutex.lock();
        try {

            boolean livelinessAsserted = false;
            for (RTPSWriter wit : m_WLP.getLivManRTPSParticipantWriters()) {
                if (wit.getLivelinessAsserted()) {
                    livelinessAsserted = true;
                }
                wit.setLivelinessAsserted(false);
            }

            if (livelinessAsserted) {
                final Lock mutex2 = m_WLP.getBuiltinWriter().getMutex();
                mutex2.lock();
                try {
                    CacheChange change = m_WLP.getBuiltinWriter().newChange(ALIVE, new InstanceHandle());
                    if (change != null) {
                        change.setInstanceHandle(m_iHandle);

                        SerializedPayload sp = change.getSerializedPayload();

                        sp.setEncapsulationKind(EncapsulationKind.CDR_BE);
                        byte[] buf = new byte[12 + 4 + 4 + 4];
                        System.arraycopy(m_guidP.getValue(), 0, buf, 0, 12);
                        for (int i = 12; i < 24; ++i) {
                            buf[i] = 0;
                        }
                        buf[15] = (byte) (m_livelinessKind.getValue() + 1);

                        sp.setBuffer(buf);
                        sp.setLength((short) buf.length);

                        Iterator<CacheChange> chi = m_WLP.getBuiltinWriterHistory().getChanges().iterator();
                        while (chi.hasNext()) {
                            CacheChange ch = chi.next();
                            if (ch.getInstanceHandle().equals(change.getInstanceHandle())) {
                                chi.remove();
                            }
                        }

                        m_WLP.getBuiltinWriterHistory().addChange(change);
                    }
                } finally {
                    mutex2.unlock();
                }
            }
            return false;
        } finally {
            mutex.unlock();
        }
    }

}
