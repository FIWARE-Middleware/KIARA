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
package org.fiware.kiara.ps.rtps.builtin.liveliness;

import java.util.concurrent.locks.Lock;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.ReaderListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class WLPListener that receives the liveliness messages asserting the
 * liveliness of remote endpoints.
 *
 * @ingroup LIVELINESS_MODULE
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class WLPListener extends ReaderListener {

    private static final Logger logger = LoggerFactory.getLogger(WLPListener.class);

    private final WLP m_WLP;

    /**
     * Constructor
     *
     * @param pwlp WLP object.
     */
    public WLPListener(WLP pwlp) {
        m_WLP = pwlp;
    }

    public void destroy() {

    }

    /**
     *
     * @param reader
     * @param change
     */
    @Override
    public void onNewCacheChangeAdded(RTPSReader reader, CacheChange change) {

        final Lock mutex = reader.getMutex();
        mutex.lock();
        try {
            final Lock mutex2 = m_WLP.getMutex();
            mutex2.lock();
            try {

                logger.info("RTPS LIVELINESS");
                GUIDPrefix guidP = new GUIDPrefix();
                LivelinessQosPolicyKind livelinessKind;
                if (!computeKey(change)) {
                    logger.warn("RTPS LIVELINESS: Problem obtaining the Key");
                    return;
                }
                // Check the serializedPayload:
                for (CacheChange ch : m_WLP.m_builtinReaderHistory.getChanges()) {
                    if (ch.getInstanceHandle().equals(change.getInstanceHandle())) {
                        m_WLP.m_builtinReaderHistory.removeChange(ch);
                        break;
                    }
                }

                if (change.getSerializedPayload().getLength() > 0) {
                    for (int i = 0; i < 12; ++i) {
                        guidP.setValue(i, change.getSerializedPayload().getBuffer()[i]);
                    }
                    livelinessKind = LivelinessQosPolicyKind.fromValue((byte) (change.getSerializedPayload().getBuffer()[15] - 0x01));
                    logger.info("RTPS LIVELINESS: RTPSParticipant {} assert liveliness of {} {} writers",
                            guidP, ((livelinessKind.getValue() == 0x00) ? "AUTOMATIC" : ""),
                            ((livelinessKind.getValue() == 0x01) ? "MANUAL_BY_RTPSParticipant" : ""));
                } else {
                    livelinessKind = separateKey(change.getInstanceHandle(), guidP);
                    if (livelinessKind == null) {
                        return;
                    }
                }

                if (guidP.equals(reader.getGuid().getGUIDPrefix())) {
                    logger.info("RTPS LIVELINESS: Message from own RTPSParticipant, ignoring");
                    m_WLP.m_builtinReaderHistory.removeChange(change);
                    return;
                }
                m_WLP.getBuiltinProtocols().getPDP().assertRemoteWritersLiveliness(guidP, livelinessKind);
            } finally {
                mutex2.unlock();
            }
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Separate the Key between the GuidPrefix_t and the liveliness Kind
     *
     * @param key InstanceHandle_t to separate.
     * @param guidP GuidPrefix_t pointer to store the info.
     * @return True if correctly separated.
     */
    public LivelinessQosPolicyKind separateKey(InstanceHandle key, GUIDPrefix guidP) {
        for (int i = 0; i < 12; ++i) {
            guidP.setValue(i, key.getValue(i));
        }
        return LivelinessQosPolicyKind.fromValue(key.getValue(15));
    }

    /**
     * Compute the key from a CacheChange_t
     *
     * @param change
     * @return
     */
    public boolean computeKey(CacheChange change) {
        return false;
    }

    @Override
    public void onReaderMatched(RTPSReader reader, MatchingInfo info) {
    }

}
