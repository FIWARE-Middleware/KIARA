package org.fiware.kiara.ps.rtps.builtin.discovery.participant;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.common.EncapsulationKind;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.rtps.participant.DiscoveryStatus;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipantDiscoveryInfo;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.ReaderListener;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PDPSimpleListener extends ReaderListener {
    
    private PDPSimple m_SPDP;
    
    private ParticipantProxyData m_participantProxyData;
    
    private static final Logger logger = LoggerFactory.getLogger(PDPSimpleListener.class);

    public PDPSimpleListener(PDPSimple pdpSimple) {
        this.m_SPDP = pdpSimple;
        this.m_participantProxyData = new ParticipantProxyData();
    }

    @Override
    public void onReaderMatched(RTPSReader reader, MatchingInfo info) {
        // TODO Auto-generated method stub
        System.out.println("PDPSimpleListener: READER MATCHED");
    }

    @Override
    public void onNewCacheChangeAdded(RTPSReader reader, CacheChange change_in) {
        // TODO Auto-generated method stub
        System.out.println("PDPSimpleListener: NEW CHANGE ADDED");
        CacheChange change = change_in;
        logger.info("SPDP Message Received");
        
        if (change.getInstanceHandle().equals(new InstanceHandle())) {
            if (!this.getKey(change)) {
                logger.warn("Problem getting the key of the change, removing.");
                this.m_SPDP.getSPDPReaderHistory().removeChange(change);
                return;
            }
        }
        
        if (change.getKind() == ChangeKind.ALIVE) {
            // Load information in temporal RTPSParticipant PROXY DATA
            this.m_participantProxyData.clear();
            if (this.m_participantProxyData.readFromCDRMessage(change)) {
                
            }
            
        } else {
            GUID guid = new GUID();
            guid = change.getInstanceHandle().toGUID();
            this.m_SPDP.removeRemoteParticipant(guid);
            RTPSParticipantDiscoveryInfo info = new RTPSParticipantDiscoveryInfo();
            info.status = DiscoveryStatus.REMOVED_PARTICIPANT;
            info.guid = guid;
            if (this.m_SPDP.getRTPSParticipant().getListener() != null) {
                this.m_SPDP.getRTPSParticipant().getListener().onRTPSParticipantDiscovery(this.m_SPDP.getRTPSParticipant(), info);
            }
        }
    }
    
    private boolean getKey(CacheChange change) {
        SerializedPayload pl = change.getSerializedPayload();
        pl.updateSerializer();
        BinaryInputStream bis = new BinaryInputStream(pl.getBuffer());
        short pid;
        short plength;
        try {
            while (bis.getPosition() < pl.getBuffer().length) {
                pid = pl.getSerializer().deserializeUI16(bis, "");
                plength = pl.getSerializer().deserializeUI16(bis, "");
                if (pid == ParameterId.PID_SENTINEL.getValue()) {
                    break;
                }
                if (pid == ParameterId.PID_PARTICIPANT_GUID.getValue()) {
                    GUID guid = new GUID();
                    guid.deserialize(pl.getSerializer(), bis, "");
                    return true;
                }
                if (pid == ParameterId.PID_KEY_HASH.getValue()) {
                    
                    return true;
                }
                bis.skipBytes(plength);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}
