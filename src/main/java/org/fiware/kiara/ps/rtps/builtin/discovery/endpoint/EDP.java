package org.fiware.kiara.ps.rtps.builtin.discovery.endpoint;

import org.fiware.kiara.ps.attributes.TopicAttributes;
import org.fiware.kiara.ps.qos.ReaderQos;
import org.fiware.kiara.ps.qos.WriterQos;
import org.fiware.kiara.ps.rtps.attributes.BuiltinAttributes;
import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.ReaderProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.WriterProxyData;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;

public class EDP {

    public boolean newLocalWriterProxyData(RTPSWriter writer,
            TopicAttributes topicAtt, WriterQos wqos) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean newLocalReaderProxyData(RTPSReader reader,
            TopicAttributes topicAtt, ReaderQos rqos) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean updatedLocalWriter(RTPSWriter writer, WriterQos wqos) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean updatedLocalReader(RTPSReader reader, ReaderQos rqos) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean removeLocalWriter(RTPSWriter writer) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean removeLocalReader(RTPSReader reader) {
        // TODO Auto-generated method stub
        return false;
    }

    public void initEDP(BuiltinAttributes m_discovery) {
        // TODO Auto-generated method stub
        
    }

    public void assignRemoteEndpoints(ParticipantProxyData pdata) {
        // TODO Auto-generated method stub
        
    }

    public void unpairReaderProxy(ReaderProxyData rit) {
        // TODO Auto-generated method stub
        
    }

    public void unpairWriterProxy(WriterProxyData wit) {
        // TODO Auto-generated method stub
        
    }

    public void removeRemoteEndpoints(ParticipantProxyData pdata) {
        // TODO Auto-generated method stub
        
    }

}
