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
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class WLPListener extends ReaderListener {

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(WLPListener.class);

    /**
     * {@link WLP} reference
     */
    private final WLP m_WLP;

    /**
     * Constructor
     *
     * @param pwlp WLP object.
     */
    public WLPListener(WLP pwlp) {
        m_WLP = pwlp;
    }

    /**
     * Destroys the information related to the {@link WLPListener}
     */
    public void destroy() {
        // Do nothing
    }
    
    /**
     * Method to be called when a new {@link RTPSReader} is matched
     */
    @Override
    public void onReaderMatched(RTPSReader reader, MatchingInfo info) {
        // Do nothing
    }

    /**
     * Method to be called when a new {@link CacheChange} is added
     */
    @Override
    public void onNewCacheChangeAdded(RTPSReader reader, CacheChange change) {

        reader.getMutex().lock();
        try {
            m_WLP.getMutex().lock();
            try {

                logger.debug("RTPS LIVELINESS");
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
                    logger.debug("RTPS LIVELINESS: RTPSParticipant {} assert liveliness of {} {} writers",
                            guidP, ((livelinessKind.getValue() == 0x00) ? "AUTOMATIC" : ""),
                            ((livelinessKind.getValue() == 0x01) ? "MANUAL_BY_RTPSParticipant" : ""));
                } else {
                    livelinessKind = separateKey(change.getInstanceHandle(), guidP);
                    if (livelinessKind == null) {
                        return;
                    }
                }

                if (guidP.equals(reader.getGuid().getGUIDPrefix())) {
                    logger.debug("RTPS LIVELINESS: Message from own RTPSParticipant, ignoring");
                    m_WLP.m_builtinReaderHistory.removeChange(change);
                    return;
                }
                m_WLP.getBuiltinProtocols().getPDP().assertRemoteWritersLiveliness(guidP, livelinessKind);
            } finally {
                m_WLP.getMutex().unlock();
            }
        } finally {
            reader.getMutex().unlock();
        }
    }

    /**
     * Separate the Key between the {@link GUIDPrefix} and the liveliness Kind
     *
     * @param key {@link InstanceHandle} to separate.
     * @param guidP {@link GUIDPrefix} reference to store the info.
     * @return True if correctly separated.
     */
    public LivelinessQosPolicyKind separateKey(InstanceHandle key, GUIDPrefix guidP) {
        for (int i = 0; i < 12; ++i) {
            guidP.setValue(i, key.getValue(i));
        }
        return LivelinessQosPolicyKind.fromValue(key.getValue(15));
    }

    /**
     * Compute the key from a {@link CacheChange}
     *
     * @param change The {@link CacheChange} to be computed
     * @return true if operation was successful
     */
    public boolean computeKey(CacheChange change) {
        return true;
    }

}
