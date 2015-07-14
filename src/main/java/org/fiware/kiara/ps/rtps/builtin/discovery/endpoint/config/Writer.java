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
import org.fiware.kiara.ps.rtps.builtin.data.WriterProxyData;
import org.fiware.kiara.ps.rtps.builtin.discovery.endpoint.EDPStaticXML;
import org.fiware.kiara.ps.rtps.builtin.discovery.endpoint.StaticRTPSParticipantInfo;
import org.fiware.kiara.ps.rtps.common.LocatorKind;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class Writer extends Endpoint {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EDPStaticXML.class);

    public boolean process(StaticRTPSParticipantInfo pdata, Set<Short> endpointIds, Set<Integer> entityIds) {
        WriterProxyData wdata = new WriterProxyData();
        if (userId <= 0 || endpointIds.add(userId) == false) {
            logger.error("RTPS EDP: Repeated or negative ID in XML file");
            return false;
        }
        wdata.setUserDefinedId(userId);

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
            wdata.getGUID().getEntityId().setValue(2, c[0]);
            wdata.getGUID().getEntityId().setValue(1, c[1]);
            wdata.getGUID().getEntityId().setValue(0, c[2]);
        }

        if (topicName != null) {
            wdata.setTopicName(topicName);
        }
        if (topicDataType != null) {
            wdata.setTypeName(topicDataType);
        }
        if (topicKind != null) {
            wdata.setTopicKind(topicKind);
        }

        if (reliabilityQos != null) {
            if ("RELIABLE_RELIABILITY_QOS".equals(reliabilityQos)) {
                wdata.getQos().reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
            } else if ("BEST_EFFORT_RELIABILITY_QOS".equals(reliabilityQos)) {
                wdata.getQos().reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
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
            wdata.getUnicastLocatorList().pushBack(loc);
        }

        for (Locator cfgLoc : multicastLocators) {
            org.fiware.kiara.ps.rtps.common.Locator loc = new org.fiware.kiara.ps.rtps.common.Locator();
            loc.setKind(LocatorKind.LOCATOR_KIND_UDPv4);
            loc.setIPv4Address(cfgLoc.address);
            loc.setPort(cfgLoc.port);
            wdata.getUnicastLocatorList().pushBack(loc);
        }

        if (topic != null) {
            if (topic.name != null) {
                wdata.setTopicName(topic.name);
            }
            if (topic.dataType != null) {
                wdata.setTypeName(topic.dataType);
            }
            if (topic.kind != null) {
                wdata.setTopicKind(topic.kind);
            }

            if ("EPROSIMA_UNKNOWN_STRING".equals(wdata.getTopicName()) || "EPROSIMA_UNKNOWN_STRING".equals(wdata.getTypeName())) {
                logger.error("RTPS EDP: Bad XML file, topic: {} or typeName: {} undefined", wdata.getTopicName(), wdata.getTopicName());
                return false;
            }
        }

        if (durabilityQos != null) {
            if ("TRANSIENT_LOCAL_DURABILITY_QOS".equals(durabilityQos)) {
                wdata.getQos().durability.kind = DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
            } else if ("VOLATILE_DURABILITY_QOS".equals(durabilityQos)) {
                wdata.getQos().durability.kind = DurabilityQosPolicyKind.VOLATILE_DURABILITY_QOS;
            } else {
                logger.error("RTPS EDP: Bad XML file, durability of kind: {} is not valid", durabilityQos);
                return false;
            }
        }

        if (ownershipQos != null) {
            if ("SHARED_OWNERSHIP_QOS".equals(ownershipQos.kind)) {
                wdata.getQos().ownership.kind = OwnershipQosPolicyKind.SHARED_OWNERSHIP_QOS;
            } else if ("EXCLUSIVE_OWNERSHIP_QOS".equals(ownershipQos.kind)) {
                wdata.getQos().ownership.kind = OwnershipQosPolicyKind.EXCLUSIVE_OWNERSHIP_QOS;
            } else {
                logger.error("RTPS EDP: Bad XML file, ownership of kind: {} is not valid", ownershipQos.kind);
                return false;
            }
        }
        for (String elem : partitionQos) {
            wdata.getQos().partition.pushBack(elem);
        }
        if (livelinessQos != null) {
            if ("AUTOMATIC_LIVELINESS_QOS".equals(livelinessQos.kind)) {
                wdata.getQos().liveliness.kind = LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
            } else if ("MANUAL_BY_PARTICIPANT_LIVELINESS_QOS".equals(livelinessQos.kind)) {
                wdata.getQos().liveliness.kind = LivelinessQosPolicyKind.MANUAL_BY_PARTICIPANT_LIVELINESS_QOS;
            } else if ("MANUAL_BY_TOPIC_LIVELINESS_QOS".equals(livelinessQos.kind)) {
                wdata.getQos().liveliness.kind = LivelinessQosPolicyKind.MANUAL_BY_TOPIC_LIVELINESS_QOS;
            } else {
                logger.error("RTPS EDP: Bad XML file, liveliness of kind: {} is not valid", livelinessQos.kind);
                return false;
            }
            if ("INF".equals(livelinessQos.leaseDuration_ms)) {
                wdata.getQos().liveliness.leaseDuration.timeInfinite();
            } else {
                if (livelinessQos.leaseDuration_ms != null) {
                    try {
                        wdata.getQos().liveliness.leaseDuration.setMilliSecondsDouble(Long.parseLong(livelinessQos.leaseDuration_ms));
                    } catch (NumberFormatException ex) {
                        logger.warn("RTPS EDP: BAD XML:livelinessQos leaseDuration is a bad number: {} setting to INF", livelinessQos.leaseDuration_ms);
                        wdata.getQos().liveliness.leaseDuration.timeInfinite();
                    }
                }
            }
        }
        if (wdata.getUserDefinedId() == 0) {
            logger.error("Writer XML endpoint with NO ID defined");
            return false;
        }

        pdata.writers.add(wdata);
        return true;
    }

}
