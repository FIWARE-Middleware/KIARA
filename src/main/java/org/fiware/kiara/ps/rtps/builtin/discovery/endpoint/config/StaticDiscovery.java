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
import org.fiware.kiara.ps.rtps.builtin.discovery.endpoint.StaticRTPSParticipantInfo;

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

    public boolean process(List<StaticRTPSParticipantInfo> participants, Set<Short> endpointIds, Set<Integer> entityIds) {
        StaticRTPSParticipantInfo pdata= new StaticRTPSParticipantInfo();
        for (Participant cfgParticipant : this.participants) {
            pdata.participantName = cfgParticipant.name;
            for (Reader cfgReader : cfgParticipant.readers) {
                cfgReader.process(pdata, endpointIds, entityIds);
            }
            for (Writer cfgWriter : cfgParticipant.writers) {
                cfgWriter.process(pdata, endpointIds, entityIds);
            }
        }
        return true;
    }

}
