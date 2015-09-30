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
package org.fiware.kiara.ps.publisher;

import org.fiware.kiara.ps.attributes.PublisherAttributes;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.rtps.RTPSDomain;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.fiware.kiara.ps.rtps.writer.WriterListener;
import org.fiware.kiara.ps.topic.TopicDataType;
import org.fiware.kiara.serialization.impl.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Publisher, used to send data to associated subscribers.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class Publisher<T> {

    /**
     *
     * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
     */
    public class PublisherWriterListener extends WriterListener {

        private final Publisher<T> m_publisher;

        public PublisherWriterListener(Publisher<T> publisher) {
            this.m_publisher = publisher;
        }

        @Override
        public void onWriterMatched(RTPSWriter writer, MatchingInfo info) {
            if (this.m_publisher.m_listener != null) {
                this.m_publisher.m_listener.onPublicationMatched(this.m_publisher, info);
            }
        }

    }

    private Participant m_participant;

    private RTPSWriter m_writer;

    private TopicDataType<T> m_type;

    private PublisherAttributes m_att;

    private PublisherHistory m_history;

    private PublisherListener m_listener;

    private RTPSParticipant m_rtpsParticipant;

    private PublisherWriterListener m_writerListener;

    private static final Logger logger = LoggerFactory.getLogger(Publisher.class);

    public Publisher(Participant participant, TopicDataType<T> dataType, PublisherAttributes att, PublisherListener listener) {
        this.m_participant = participant;
        this.m_writer = null;
        this.m_type = dataType;
        this.m_att = att;
        this.m_history = new PublisherHistory(this, dataType.getTypeSize(), att.topic.historyQos, att.topic.resourceLimitQos);
        this.m_listener = listener;
        this.m_writerListener = new PublisherWriterListener(this);
        this.m_rtpsParticipant = null;
    }

    public void destroy() {
        logger.info("Destroying Publisher (Writer GUID: {})", this.getGuid());
        RTPSDomain.removeRTPSWriter(this.m_writer);
    }

    /**
     * Write data to the topic.
     *
     * @param data reference to the data
     * @return True if correct
     */
    public boolean write(T data) {
        logger.debug("Writing new data");
        return this.createNewChange(ChangeKind.ALIVE, data);
    }

    /**
     * Create new change.
     *
     * @param kind change kind
     * @param data data
     * @return true if operation was successful
     */
    public boolean createNewChange(ChangeKind kind, T data) {
        if (data == null) {
            logger.error("Data is null");
            return false;
        }

        if (kind == ChangeKind.NOT_ALIVE_UNREGISTERED || kind == ChangeKind.NOT_ALIVE_DISPOSED || kind == ChangeKind.NOT_ALIVE_DISPOSED_UNREGISTERED) {
            if (this.m_att.topic.topicKind == TopicKind.NO_KEY) {
                logger.error("Topic is NO_KEY, operation not permitted");
                return false;
            }
        }

        InstanceHandle handle = new InstanceHandle();
        if (this.m_att.topic.topicKind == TopicKind.WITH_KEY) {
            this.m_type.getKey(data, handle);
        }

        CacheChange ch = this.m_writer.newChange(kind, handle);
        if (ch != null) {
            if (kind == ChangeKind.ALIVE) {
                ch.getSerializedPayload().setData((Serializable) data);
                if (!this.m_type.serialize(data, ch.getSerializedPayload())) {
                    logger.warn("RTPSWriter: Serialization returns false");
                    this.m_history.releaseCache(ch);
                    return false;
                } else if (ch.getSerializedPayload().getLength() > this.m_type.getTypeSize()) {
                    logger.warn("Serialized Payload length larger than maximum type size");
                    this.m_history.releaseCache(ch);
                    return false;
                } else if (ch.getSerializedPayload().getLength() == 0) {
                    logger.warn("Serialized Payload length must be greater then zero");
                    this.m_history.releaseCache(ch);
                    return false;
                }
            }

            if (!this.m_history.addPubChange(ch)) {
                this.m_history.releaseCache(ch);
                return false;
            }
            return true;

        }

        return false;
    }

    /**
     * Dispose of a previously written data.
     *
     * @param data reference to the data.
     * @return True if correct.
     */
    public boolean dispose(T data) {
        logger.info("Disposing of data");
        return this.createNewChange(ChangeKind.NOT_ALIVE_DISPOSED, data);
    }

    /**
     * Unregister a previously written data.
     *
     * @param data Reference to the data.
     * @return True if correct.
     */
    public boolean unregister(T data) {
        logger.info("Unregistering of type");
        return this.createNewChange(ChangeKind.NOT_ALIVE_UNREGISTERED, data);
    }

    /**
     * Dispose and unregister a previously written data.
     *
     * @param data Reference to the data.
     * @return True if correct.
     */
    public boolean disposeAndUnregister(T data) {
        logger.info("Disposing and unregistering data");
        return this.createNewChange(ChangeKind.NOT_ALIVE_DISPOSED_UNREGISTERED, data);
    }

    /**
     * Remove all the Changes in the associated RTPSWriter.
     *
     * @param removed Number of elements removed
     * @return number of elements removed
     */
    public int removeAllChanges(int removed) {
        logger.info("Removing all data from hsitory");
        return this.m_history.removeAllChangesNum();
    }

    /**
     * Update the Attributes of the publisher;
     *
     * @param att Reference to a PublisherAttributes object to update the
     * parameters;
     * @return True if correctly updated, false if ANY of the updated parameters
     * cannot be updated
     */
    public boolean updateAttributes(PublisherAttributes att) {
        boolean updated = true;
        boolean missing = false;

        if (this.m_att.qos.reliability.kind == ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS) {
            if (att.unicastLocatorList.getLocators().size() != this.m_att.unicastLocatorList.getLocators().size()
                    || att.multicastLocatorList.getLocators().size() != this.m_att.multicastLocatorList.getLocators().size()) {
                logger.warn("Locator Lists cannot be changed or updated in this version");
                updated &= false;
            } else {
                for (Locator it : this.m_att.unicastLocatorList.getLocators()) {
                    missing = true;
                    for (Locator it2 : att.unicastLocatorList.getLocators()) {
                        if (it.equals(it2)) {
                            missing = false;
                            break;
                        }
                    }
                    if (missing) {
                        logger.warn("Locator: not present in new list");
                        logger.warn("Locator Lists cannot be changed or updated in this version");
                    }
                }
                for (Locator it : this.m_att.multicastLocatorList.getLocators()) {
                    missing = true;
                    for (Locator it2 : att.multicastLocatorList.getLocators()) {
                        if (it.equals(it2)) {
                            missing = false;
                            break;
                        }
                    }
                    if (missing) {
                        logger.warn("Locator: not present in new list");
                        logger.warn("Locator Lists cannot be changed or updated in this version");
                    }
                }
            }
        }

        if (!this.m_att.topic.equals(att.topic)) {
            logger.warn("Topic attributes cannot be updated");
            updated &= false;
        }

        if (!this.m_att.qos.canQosBeUpdated(att.qos)) {
            updated &= false;
        }

        if (updated) {
            if (this.m_att.qos.reliability.kind == ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS) {
                // TODO Not supported in this version (StatefulWriter)
            }
            this.m_att.qos.setQos(att.qos, false);
            this.m_att = att;
            this.m_rtpsParticipant.updateLocalWriter(this.m_writer, this.m_att.qos);
        }

        return updated;
    }

    /**
     * Get the Attributes of the Subscriber.
     *
     * @return Attributes of the Subscriber.
     */
    public PublisherAttributes getAttributes() {
        return this.m_att;
    }

    /**
     * Get publisher history.
     *
     * @return publisher history
     * @see PublisherHistory
     */
    public PublisherHistory getHistory() {
        return this.m_history;
    }

    /**
     * Get the {@link GUID} of the associated RTPSWriter.
     *
     * @return GUID.
     */
    public GUID getGuid() {
        return this.m_writer.getGuid();
    }

    /**
     * Get the RTPS participant.
     *
     * @return RTPS participant
     * @see RTPSParticipant
     */
    public RTPSParticipant getRTPSParticipant() {
        return this.m_rtpsParticipant;
    }

    /**
     * Set the RTPS participant.
     *
     * @see RTPSParticipant
     */
    public void setRTPSParticipant(RTPSParticipant participant) {
        this.m_rtpsParticipant = participant;
    }

    /**
     * Get the writer listener
     *
     * @return writer listener
     */
    public WriterListener getWriterListener() {
        return this.m_writerListener;
    }

    /**
     * Set the writer
     *
     * @param writer
     */
    public void setWriter(RTPSWriter writer) {
        this.m_writer = writer;
    }

}
