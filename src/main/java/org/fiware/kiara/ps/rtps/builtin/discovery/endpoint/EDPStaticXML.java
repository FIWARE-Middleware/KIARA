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
package org.fiware.kiara.ps.rtps.builtin.discovery.endpoint;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.fiware.kiara.ps.rtps.builtin.data.ReaderProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.WriterProxyData;
import org.fiware.kiara.ps.rtps.builtin.discovery.endpoint.config.StaticDiscovery;
import org.slf4j.LoggerFactory;

/**
 * Class EDPStaticXML used to parse the XML file that contains information about
 * remote endpoints.
 *
 * 
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class EDPStaticXML {

    private final Set<Short> m_endpointIds;
    private final Set<Integer> m_entityIds;
    private final List<StaticRTPSParticipantInfo> m_RTPSParticipants;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EDPStaticXML.class);

    public EDPStaticXML() {
        this.m_endpointIds = new HashSet<>();
        this.m_entityIds = new HashSet<>();
        this.m_RTPSParticipants = new ArrayList<>();
    }

    /**
     * Look for a reader in the previously loaded endpoints.
     *
     * @param partname RTPSParticipant name
     * @param id Id of the reader
     * @return Pointer to return the information.
     */
    public ReaderProxyData lookforReader(String partname, short id) {
        for (StaticRTPSParticipantInfo pit : m_RTPSParticipants) {
            if (pit.participantName.equals(partname) || true) {// it doenst matter the name fo the RTPSParticipant, only for organizational purposes
                for (ReaderProxyData rit : pit.readers) {
                    if (rit.getUserDefinedId() == id) {
                        return rit;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Look for a writer in the previously loaded endpoints.
     *
     * @param partname RTPSParticipant name
     * @param id Id of the writer
     * @return Pointer to return the information.
     */
    public WriterProxyData lookforWriter(String partname, short id) {
        for (StaticRTPSParticipantInfo pit : m_RTPSParticipants) {
            if (pit.participantName.equals(partname) || true) { //it doenst matter the name fo the RTPSParticipant, only for organizational purposes
                for (WriterProxyData wit : pit.writers) {
                    if (wit.getUserDefinedId() == id) {
                        return wit;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Load the XML file
     *
     * @param filename Name of the file to load and parse.
     * @return True if correct.
     */
    public boolean loadXMLFile(String filename) {
        try {
            StaticDiscovery config = StaticDiscovery.fromXML(new File(filename));
            config.process(m_RTPSParticipants, m_endpointIds, m_entityIds);
        } catch (Exception ex) {
            logger.error("RTPS EDP: Error reading xml file ({})", ex);
            return false;
        }
        return true;
    }

}
