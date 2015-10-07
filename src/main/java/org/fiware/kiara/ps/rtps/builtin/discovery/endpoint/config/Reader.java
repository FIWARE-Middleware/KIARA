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
package org.fiware.kiara.ps.rtps.builtin.discovery.endpoint.config;

import java.nio.ByteBuffer;
import java.util.Set;

import org.fiware.kiara.ps.qos.policies.DurabilityQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.OwnershipQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.rtps.builtin.data.ReaderProxyData;
import org.fiware.kiara.ps.rtps.builtin.discovery.endpoint.EDPStaticXML;
import org.fiware.kiara.ps.rtps.builtin.discovery.endpoint.StaticRTPSParticipantInfo;
import org.fiware.kiara.ps.rtps.common.LocatorKind;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.slf4j.LoggerFactory;

/**
 * Represents an {@link RTPSReader}
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class Reader extends Endpoint {

    /**
     * Indicates if the {@link RTPSReader} expects inline QoS
     */
    public boolean expectsInlineQos = false;

    /**
     * Logging object
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EDPStaticXML.class);

    /**
     * Processes the XML information
     * 
     * @param pdata {@link StaticRTPSParticipantInfo} data
     * @param endpointIds List of {@link org.fiware.kiara.ps.rtps.Endpoint} identifiers
     * @param entityIds List of Entity identifiers
     * @return true on success; false otherwise
     */
    public boolean process(StaticRTPSParticipantInfo pdata, Set<Short> endpointIds, Set<Integer> entityIds) {
        ReaderProxyData rdata = new ReaderProxyData();
        if (userId <= 0 || endpointIds.add(userId) == false) {
            logger.error("RTPS EDP: Repeated or negative ID in XML file");
            return false;
        }
        rdata.setUserDefinedId(userId);

        if (entityId != null) {
            int id;
            try {
                id = Integer.parseInt(entityId);
            } catch (NumberFormatException ex) {
                logger.error("RTPS EDP: entityId is not an integer: {}", entityId);
                return false;
            }
            if (id <= 0 || entityIds.add(id) == false) {
                logger.error("RTPS EDP: Repeated or negative entityId in XML file");
                return false;
            }

            byte[] c = ByteBuffer.allocate(4).putInt(id).array();
            rdata.getGUID().getEntityId().setValue(2, c[0]);
            rdata.getGUID().getEntityId().setValue(1, c[1]);
            rdata.getGUID().getEntityId().setValue(0, c[2]);
        }

        rdata.setExpectsInlineQos(expectsInlineQos);
        if (topicName != null) {
            rdata.setTopicName(topicName);
        }
        if (topicDataType != null) {
            rdata.setTypeName(topicDataType);
        }
        if (topicKind != null) {
            rdata.setTopicKind(topicKind);
        }

        if (reliabilityQos != null) {
            if ("RELIABLE_RELIABILITY_QOS".equals(reliabilityQos)) {
                rdata.getQos().reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
            } else if ("BEST_EFFORT_RELIABILITY_QOS".equals(reliabilityQos)) {
                rdata.getQos().reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
            } else {
                logger.error("RTPS EDP: Bad XML file, endpoint of stateKind: {} is not valid", reliabilityQos);
                return false;
            }
        }

        for (Locator cfgLoc : unicastLocators) {
            org.fiware.kiara.ps.rtps.common.Locator loc = new org.fiware.kiara.ps.rtps.common.Locator();
            loc.setKind(LocatorKind.LOCATOR_KIND_UDPv4);
            loc.setIPv4Address(cfgLoc.address);
            loc.setPort(cfgLoc.port);
            rdata.getUnicastLocatorList().pushBack(loc);
        }

        for (Locator cfgLoc : multicastLocators) {
            org.fiware.kiara.ps.rtps.common.Locator loc = new org.fiware.kiara.ps.rtps.common.Locator();
            loc.setKind(LocatorKind.LOCATOR_KIND_UDPv4);
            loc.setIPv4Address(cfgLoc.address);
            loc.setPort(cfgLoc.port);
            rdata.getUnicastLocatorList().pushBack(loc);
        }

        if (topic != null) {
            if (topic.name != null) {
                rdata.setTopicName(topic.name);
            }
            if (topic.dataType != null) {
                rdata.setTypeName(topic.dataType);
            }
            if (topic.kind != null) {
                rdata.setTopicKind(topic.kind);
            }

            if ("EPROSIMA_UNKNOWN_STRING".equals(rdata.getTopicName()) || "EPROSIMA_UNKNOWN_STRING".equals(rdata.getTypeName())) {
                logger.error("RTPS EDP: Bad XML file, topic: {} or typeName: {} undefined", rdata.getTopicName(), rdata.getTopicName());
                return false;
            }
        }

        if (durabilityQos != null) {
            if ("TRANSIENT_LOCAL_DURABILITY_QOS".equals(durabilityQos)) {
                rdata.getQos().durability.kind = DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
            } else if ("VOLATILE_DURABILITY_QOS".equals(durabilityQos)) {
                rdata.getQos().durability.kind = DurabilityQosPolicyKind.VOLATILE_DURABILITY_QOS;
            } else {
                logger.error("RTPS EDP: Bad XML file, durability of kind: {} is not valid", durabilityQos);
                return false;
            }
        }

        if (ownershipQos != null) {
            if ("SHARED_OWNERSHIP_QOS".equals(ownershipQos.kind)) {
                rdata.getQos().ownership.kind = OwnershipQosPolicyKind.SHARED_OWNERSHIP_QOS;
            } else if ("EXCLUSIVE_OWNERSHIP_QOS".equals(ownershipQos.kind)) {
                rdata.getQos().ownership.kind = OwnershipQosPolicyKind.EXCLUSIVE_OWNERSHIP_QOS;
            } else {
                logger.error("RTPS EDP: Bad XML file, ownership of kind: {} is not valid", ownershipQos.kind);
                return false;
            }
        }
        for (String elem : partitionQos) {
            rdata.getQos().partition.pushBack(elem);
        }
        if (livelinessQos != null) {
            if ("AUTOMATIC_LIVELINESS_QOS".equals(livelinessQos.kind)) {
                rdata.getQos().liveliness.kind = LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
            } else if ("MANUAL_BY_PARTICIPANT_LIVELINESS_QOS".equals(livelinessQos.kind)) {
                rdata.getQos().liveliness.kind = LivelinessQosPolicyKind.MANUAL_BY_PARTICIPANT_LIVELINESS_QOS;
            } else if ("MANUAL_BY_TOPIC_LIVELINESS_QOS".equals(livelinessQos.kind)) {
                rdata.getQos().liveliness.kind = LivelinessQosPolicyKind.MANUAL_BY_TOPIC_LIVELINESS_QOS;
            } else {
                logger.error("RTPS EDP: Bad XML file, liveliness of kind: {} is not valid", livelinessQos.kind);
                return false;
            }
            if ("INF".equals(livelinessQos.leaseDuration_ms)) {
                rdata.getQos().liveliness.leaseDuration.timeInfinite();
            } else {
                if (livelinessQos.leaseDuration_ms != null) {
                    try {
                        rdata.getQos().liveliness.leaseDuration.setMilliSecondsDouble(Long.parseLong(livelinessQos.leaseDuration_ms));
                    } catch (NumberFormatException ex) {
                        logger.warn("RTPS EDP: BAD XML:livelinessQos leaseDuration is a bad number: {} setting to INF", livelinessQos.leaseDuration_ms);
                        rdata.getQos().liveliness.leaseDuration.timeInfinite();
                    }
                }
            }
        }
        if (rdata.getUserDefinedId() == 0) {
            logger.error("Reader XML endpoint with NO ID defined");
            return false;
        }

        pdata.readers.add(rdata);
        return true;
    }

}
