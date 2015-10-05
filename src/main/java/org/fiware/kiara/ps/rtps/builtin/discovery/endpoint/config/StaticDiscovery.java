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

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fiware.kiara.ps.rtps.builtin.discovery.endpoint.EDPStaticXML;
import org.fiware.kiara.ps.rtps.builtin.discovery.endpoint.StaticRTPSParticipantInfo;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
@JsonRootName(value = "staticdiscovery")
public class StaticDiscovery {
    @JacksonXmlProperty(localName="participant")
    public List<Participant> participants = new ArrayList<>();

    private final static ObjectReader xmlReader;
    private final static ObjectWriter xmlWriter;

    static {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        xmlWriter = xmlMapper.writer();
        xmlReader = xmlMapper.reader(StaticDiscovery.class);
    }

    public static StaticDiscovery fromXML(String xml) throws IOException {
        return xmlReader.<StaticDiscovery>readValue(xml);
    }

    public static StaticDiscovery fromXML(File xmlFile) throws IOException {
        return xmlReader.<StaticDiscovery>readValue(xmlFile);
    }

    public String toXML() throws IOException {
        return xmlWriter.writeValueAsString(this);
    }

    public void toXML(File xmlFile) throws IOException {
        xmlWriter.writeValue(xmlFile, this);
    }

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EDPStaticXML.class);

    public boolean process(List<StaticRTPSParticipantInfo> participants, Set<Short> endpointIds, Set<Integer> entityIds) {
        for (Participant cfgParticipant : this.participants) {
            StaticRTPSParticipantInfo pdata= new StaticRTPSParticipantInfo();
            pdata.participantName = cfgParticipant.name;
            for (Reader cfgReader : cfgParticipant.readers) {
                if (!cfgReader.process(pdata, endpointIds, entityIds)) {
                    logger.error("RTPS EDP: Reader Endpoint has error, ignoring");
                }
            }
            for (Writer cfgWriter : cfgParticipant.writers) {
                if (!cfgWriter.process(pdata, endpointIds, entityIds)) {
                    logger.error("RTPS EDP: Writer Endpoint has error, ignoring");
                }
            }
            participants.add(pdata);
        }
        return true;
    }

}
