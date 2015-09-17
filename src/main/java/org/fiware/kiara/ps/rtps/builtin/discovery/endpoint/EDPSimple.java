package org.fiware.kiara.ps.rtps.builtin.discovery.endpoint;

import org.fiware.kiara.ps.rtps.attributes.BuiltinAttributes;
import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.ReaderProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.WriterProxyData;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;

/**
 * Class EDPSimple, implements the Simple Endpoint Discovery Protocol defined in
 * the RTPS specification. Inherits from EDP class.
 */
public class EDPSimple extends EDP {

    /**
     * Constructor.
     *
     * @param pdpSimple Reference to the PDPSimple
     * @param m_RTPSParticipant Reference to the RTPSParticipantImpl
     */
    public EDPSimple(PDPSimple pdpSimple, RTPSParticipant m_RTPSParticipant) {
        // FIXME Weird, but original code does not call inherited constructor
        //super(pdpSimple, m_RTPSParticipant);
        super(null, null);
    }

    /**
     * Initialization method.
     *
     * @param m_discovery Reference to the BuiltinAttributes.
     * @return True if correct.
     */
    @Override
    public boolean initEDP(BuiltinAttributes m_discovery) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method assigns the remote builtin endpoints that the remote
     * RTPSParticipant indicates is using to our local builtin endpoints.
     *
     * @param pdata GUIDPrefix the RTPSParticipantProxyData object.
     */
    @Override
    public void assignRemoteEndpoints(ParticipantProxyData pdata) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This methods generates the change disposing of the local Reader and calls
     * the unpairing and removal methods of the base class.
     *
     * @param R Reference to the RTPSReader object.
     * @return True if correct.
     */
    @Override
    public boolean removeLocalReader(RTPSReader R) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This methods generates the change disposing of the local Writer and calls
     * the unpairing and removal methods of the base class.
     *
     * @param W Reference to the RTPSWriter object.
     * @return True if correct.
     */
    @Override
    public boolean removeLocalWriter(RTPSWriter W) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method generates the corresponding change in the subscription writer
     * and send it to all known remote endpoints.
     *
     * @param rdata Reference to the ReaderProxyData object.
     * @return true if correct.
     */
    @Override
    public boolean processLocalReaderProxyData(ReaderProxyData rdata) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method generates the corresponding change in the publciations writer
     * and send it to all known remote endpoints.
     *
     * @param wdata Reference to the WriterProxyData object.
     * @return true if correct.
     */
    @Override
    public boolean processLocalWriterProxyData(WriterProxyData wdata) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
