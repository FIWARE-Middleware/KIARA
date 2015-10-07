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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.rtps.common.DurabilityKind;
import org.fiware.kiara.ps.rtps.common.TopicKind;

/**
 * Represents an Endpoint defined using XML
 * 
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class Endpoint {
    
    /**
     * User identifier
     */
    public short userId = 0;
    
    /**
     * Entity identifier
     */
    public String entityId;
    
    /**
     * String indicating the topic name
     */
    public String topicName;
    
    /**
     * String indicating the topic data type name
     */
    public String topicDataType;
    
    /**
     * {@link TopicKind} enumeration indicating the type of the topic
     */
    public TopicKind topicKind;
    
    /**
     * Indicated if the Endpoint is BEST_EFFORT or RELIABLE
     */
    public String reliabilityQos;
    
    /**
     * List of unicast {@link Locator}
     */
    @JacksonXmlProperty(localName="unicastLocator")
    public List<Locator> unicastLocators = new ArrayList<>();
    
    /**
     * List of multicast {@link Locator}
     */
    @JacksonXmlProperty(localName="multicastLocator")
    public List<Locator> multicastLocators = new ArrayList<>();
    
    /**
     * Attribute indicating the {@link Topic}
     */
    public Topic topic;
    
    /**
     * String indicating the {@link DurabilityKind} of te {@link Endpoint}
     */
    public String durabilityQos;
    
    /**
     * {@link OwnershipQos} attribute for this {@link Endpoint}
     */
    public OwnershipQos ownershipQos;
    
    /**
     * {@link LivelinessQos} attribute for this {@link Endpoint}
     */
    public LivelinessQos livelinessQos;
    
    /**
     * List of Partition QoS
     */
    public List<String> partitionQos = new ArrayList<>();

}
