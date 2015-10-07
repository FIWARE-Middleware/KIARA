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
package org.fiware.kiara.ps.qos;

import org.fiware.kiara.ps.qos.policies.DeadLineQosPolicy;
import org.fiware.kiara.ps.qos.policies.DestinationOrderQosPolicy;
import org.fiware.kiara.ps.qos.policies.DestinationOrderQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.DurabilityQosPolicy;
import org.fiware.kiara.ps.qos.policies.DurabilityQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.DurabilityServiceQosPolicy;
import org.fiware.kiara.ps.qos.policies.GroupDataQosPolicy;
import org.fiware.kiara.ps.qos.policies.LatencyBudgetQosPolicy;
import org.fiware.kiara.ps.qos.policies.LifespanQosPolicy;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicy;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.OwnershipQosPolicy;
import org.fiware.kiara.ps.qos.policies.OwnershipQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.OwnershipStrengthQosPolicy;
import org.fiware.kiara.ps.qos.policies.PartitionQosPolicy;
import org.fiware.kiara.ps.qos.policies.PresentationQosPolicy;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicy;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.TimeBasedFilterQosPolicy;
import org.fiware.kiara.ps.qos.policies.TopicDataQosPolicy;
import org.fiware.kiara.ps.qos.policies.UserDataQosPolicy;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class WriterQos, containing all the possible Qos that can be set for a
 * determined Publisher. Although these values can be set and are transmitted
 * during the Endpoint Discovery Protocol, not all of the behaviour associated
 * with them has been implemented in the library. Please consult each of them to
 * check for implementation details and default values.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class WriterQos {

    /**
     * Durability Qos, implemented in the library.
     */
    public DurabilityQosPolicy durability;

    /**
     * Durability Service Qos, NOT implemented in the library.
     */
    public DurabilityServiceQosPolicy durabilityService;

    /**
     * Deadline Qos, NOT implemented in the library.
     */
    public DeadLineQosPolicy deadline;

    /**
     * Latency Budget Qos, NOT implemented in the library.
     */
    public LatencyBudgetQosPolicy latencyBudget;

    /**
     * Liveliness Qos, implemented in the library.
     */
    public LivelinessQosPolicy liveliness;

    /**
     * Reliability Qos, implemented in the library.
     */
    public ReliabilityQosPolicy reliability;

    /**
     * Lifespan Qos, NOT implemented in the library.
     */
    public LifespanQosPolicy lifespan;

    /**
     * UserData Qos, NOT implemented in the library.
     */
    public UserDataQosPolicy userData;

    /**
     * Time Based Filter Qos, NOT implemented in the library.
     */
    public TimeBasedFilterQosPolicy timeBasedFilter;

    /**
     * Ownership Qos, NOT implemented in the library.
     */
    public OwnershipQosPolicy ownership;

    /**
     * Owenership Strength Qos, NOT implemented in the library.
     */
    public OwnershipStrengthQosPolicy ownershipStrength;

    /**
     * Destination Order Qos, NOT implemented in the library.
     */
    public DestinationOrderQosPolicy destinationOrder;

    /**
     * Presentation Qos, NOT implemented in the library.
     */
    public PresentationQosPolicy presentation;

    /**
     * Partition Qos, implemented in the library.
     */
    public PartitionQosPolicy partition;

    /**
     * Topic Data Qos, NOT implemented in the library.
     */
    public TopicDataQosPolicy topicData;

    /**
     * Group Data Qos, NOT implemented in the library.
     */
    public GroupDataQosPolicy groupData;

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(WriterQos.class);

    /**
     * Default WriterQos constructor
     */
    public WriterQos() {
        this.reliability = new ReliabilityQosPolicy();
        this.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
        this.durability = new DurabilityQosPolicy();
        this.durability.kind = DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;

        this.durability = new DurabilityQosPolicy();
        this.durabilityService = new DurabilityServiceQosPolicy();
        this.deadline = new DeadLineQosPolicy();
        this.latencyBudget = new LatencyBudgetQosPolicy();
        this.liveliness = new LivelinessQosPolicy();
        this.reliability = new ReliabilityQosPolicy();
        this.lifespan = new LifespanQosPolicy();
        this.userData = new UserDataQosPolicy();
        this.timeBasedFilter = new TimeBasedFilterQosPolicy();
        this.ownership = new OwnershipQosPolicy();
        this.ownershipStrength = new OwnershipStrengthQosPolicy();
        this.destinationOrder = new DestinationOrderQosPolicy();
        this.presentation = new PresentationQosPolicy();
        this.partition = new PartitionQosPolicy();
        this.topicData = new TopicDataQosPolicy();
        this.groupData = new GroupDataQosPolicy();
    }

    /**
     * Set Qos from another class
     *
     * @param qos Reference from a WriterQos object.
     * @param firstTime Boolean indicating whether is the first time (If not
     * some parameters cannot be set).
     */
    public void setQos(WriterQos qos, boolean firstTime) {
        if (firstTime) {
            durability.copy(qos.durability);
            durability.parent.hasChanged = true;
        }
        if (firstTime || deadline.period != qos.deadline.period) {
            deadline.copy(qos.deadline);
            deadline.parent.hasChanged = true;
        }
        if (latencyBudget.duration != qos.latencyBudget.duration) {
            latencyBudget.copy(qos.latencyBudget);
            latencyBudget.parent.hasChanged = true;
        }
        if (!liveliness.leaseDuration.equals(qos.liveliness.leaseDuration)) {
            liveliness.leaseDuration.copy(qos.liveliness.leaseDuration);
            liveliness.parent.hasChanged = true;
        }
        if (firstTime) {
            liveliness.copy(qos.liveliness);
            liveliness.parent.hasChanged = true;
        }
        if (firstTime) {
            reliability.copy(qos.reliability);
            reliability.parent.hasChanged = true;
        }
        if (firstTime) {
            ownership.copy(qos.ownership);
            ownership.parent.hasChanged = true;
        }
        if (destinationOrder.kind != qos.destinationOrder.kind) {
            destinationOrder.copy(qos.destinationOrder);
            destinationOrder.parent.hasChanged = true;
        }
        if (userData.getDataBuf() != null && !userData.getDataBuf().equals(qos.userData.getDataBuf())) {
            userData.copy(qos.userData);
            userData.parent.hasChanged = true;
        }
        if (firstTime || !timeBasedFilter.minimumSeparation.equals(qos.timeBasedFilter.minimumSeparation)) {
            timeBasedFilter.copy(qos.timeBasedFilter);
            timeBasedFilter.parent.hasChanged = true;
        }
        if (firstTime || presentation.accessScope != qos.presentation.accessScope
                || presentation.coherentAccess != qos.presentation.coherentAccess
                || presentation.orderedAccess != qos.presentation.orderedAccess) {
            presentation.copy(qos.presentation);
            presentation.parent.hasChanged = true;
        }
        if (qos.partition.getNames().size() > 0) {
            partition.copy(qos.partition);
            partition.parent.hasChanged = true;
        }

        if (!topicData.getValue().equals(qos.topicData.getValue())) {
            topicData.copy(qos.topicData);
            topicData.parent.hasChanged = true;
        }
        if (!groupData.getValue().equals(qos.groupData.getValue())) {
            groupData.copy(qos.groupData);
            groupData.parent.hasChanged = true;
        }
        if (firstTime || durabilityService.kind != qos.durabilityService.kind
                || durabilityService.historyDepth != qos.durabilityService.historyDepth
                || durabilityService.maxInstances != qos.durabilityService.maxInstances
                || durabilityService.maxSamples != qos.durabilityService.maxSamples
                || durabilityService.maxSamplesPerInstance != qos.durabilityService.maxSamplesPerInstance
                || durabilityService.serviceCleanupDelay != qos.durabilityService.serviceCleanupDelay) {
            durabilityService.copy(qos.durabilityService);
            durabilityService.parent.hasChanged = true;
        }
        if (lifespan.duration != qos.lifespan.duration) {
            lifespan.copy(qos.lifespan);
            lifespan.parent.hasChanged = true;
        }
        if (qos.ownershipStrength.value != ownershipStrength.value) {
            ownershipStrength.copy(qos.ownershipStrength);
            ownershipStrength.parent.hasChanged = true;
        }
    }

    /**
     * Check if the Qos values are compatible between each other.
     * @return True if correct.
     */
    public boolean checkQos() {
        if (durability.kind == DurabilityQosPolicyKind.TRANSIENT_DURABILITY_QOS) {
            logger.error("TRANSIENT Durability not supported");
            return false;
        }
        if (durability.kind == DurabilityQosPolicyKind.PERSISTENT_DURABILITY_QOS) {
            logger.error("PERSISTENT Durability not supported");
            return false;
        }
        if (destinationOrder.kind == DestinationOrderQosPolicyKind.BY_SOURCE_TIMESTAMP_DESTINATIONORDER_QOS) {
            logger.error("BY SOURCE TIMESTAMP DestinationOrder not supported");
            return false;
        }
        if (reliability.kind == ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS && ownership.kind == OwnershipQosPolicyKind.EXCLUSIVE_OWNERSHIP_QOS) {
            logger.error("BEST_EFFORT incompatible with EXCLUSIVE ownership");
            return false;
        }
        if (liveliness.kind == LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS || liveliness.kind == LivelinessQosPolicyKind.MANUAL_BY_PARTICIPANT_LIVELINESS_QOS) {
            if (liveliness.leaseDuration.isLowerThan(new Timestamp().timeInfinite()) && liveliness.leaseDuration.isLowerOrEqualThan(liveliness.announcementPeriod)) {
                logger.error("WRITERQOS: LeaseDuration <= announcement period.");
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if Qos can be updated
     * @param qos writer QoS
     * @return true if Qos can be updated
     */
    public boolean canQosBeUpdated(WriterQos qos) {
        boolean updatable = true;
        if (durability.kind != qos.durability.kind) {
            updatable = false;
            logger.warn("Durability kind cannot be changed after the creation of a publisher.");
        }

        if (liveliness.kind != qos.liveliness.kind) {
            updatable = false;
            logger.warn("Liveliness Kind cannot be changed after the creation of a publisher.");
        }

        if (reliability.kind != qos.reliability.kind) {
            updatable = false;
            logger.warn("Reliability Kind cannot be changed after the creation of a publisher.");
        }
        if (ownership.kind != qos.ownership.kind) {
            updatable = false;
            logger.warn("Ownership Kind cannot be changed after the creation of a publisher.");
        }
        if (destinationOrder.kind != qos.destinationOrder.kind) {
            updatable = false;
            logger.warn("Destination order Kind cannot be changed after the creation of a publisher.");
        }
        return updatable;
    }

    /**
     * This method copies all the contents of a WriterQos object
     * 
     * @param qos The WriterQos to copy
     */
    public void copy(WriterQos qos) {
        this.durability.copy(qos.durability);
        this.durabilityService.copy(qos.durabilityService);
        this.deadline.copy(qos.deadline);
        this.latencyBudget.copy(qos.latencyBudget);
        this.liveliness.copy(qos.liveliness);
        this.reliability.copy(qos.reliability);
        this.lifespan.copy(qos.lifespan);
        this.userData.copy(qos.userData);
        this.timeBasedFilter.copy(qos.timeBasedFilter);
        this.ownership.copy(qos.ownership);
        this.ownershipStrength.copy(qos.ownershipStrength);
        this.destinationOrder.copy(qos.destinationOrder);
        this.presentation.copy(qos.presentation);
        this.partition.copy(qos.partition);
        this.topicData.copy(qos.topicData);
        this.groupData.copy(qos.groupData);
    }

}
