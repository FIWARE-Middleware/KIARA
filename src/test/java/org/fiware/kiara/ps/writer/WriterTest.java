package org.fiware.kiara.ps.writer;

import org.fiware.kiara.ps.rtps.RTPSDomain;
import org.fiware.kiara.ps.rtps.attributes.HistoryCacheAttributes;
import org.fiware.kiara.ps.rtps.attributes.RTPSParticipantAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.ReliabilityKind;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.fiware.kiara.ps.types.HelloWorld;
import org.fiware.kiara.ps.types.HelloWorldType;
import org.fiware.kiara.serialization.impl.Serializable;

public class WriterTest {
    
    public static void main(String [] args) {
        
        RTPSParticipantAttributes pparam = new RTPSParticipantAttributes();
        pparam.builtinAtt.useSimplePDP = true;
        pparam.builtinAtt.useStaticEDP = true;
        pparam.builtinAtt.useWriterLP = false;
        pparam.builtinAtt.setStaticEndpointXMLFilename("WRITER_ENDPOINTS.xml");
        
        RTPSParticipant participant = RTPSDomain.createParticipant(pparam, null);
        if (participant == null) {
            System.out.println("ERROR creating participant");
            return;
        }
        
        HistoryCacheAttributes hatt = new HistoryCacheAttributes();
        hatt.payloadMaxSize = 500;
        
        WriterHistoryCache w_history = new WriterHistoryCache(hatt);
        
        WriterAttributes watt = new WriterAttributes();
        watt.endpointAtt.reliabilityKind = ReliabilityKind.BEST_EFFORT;
        
        RTPSWriter writer = RTPSDomain.createRTPSWriter(participant, watt, w_history, null);
        if (writer == null) {
            System.out.println("ERROR creating writer");
            return;
        }
        
        /*RemoteReaderAttributes ratt = new RemoteReaderAttributes();
        Locator loc = new Locator();
        loc.setIPv4Address("239.255.0.1");
        loc.setPort(7400);
        
        ratt.endpoint.multicastLocatorList.pushBack(loc);
        writer.matchedReaderAdd(ratt);*/
        
        CacheChange change = writer.newChange(ChangeKind.ALIVE, new InstanceHandle());
        
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");

        change.getSerializedPayload().setData((Serializable) hw); 
        change.getSerializedPayload().setLength((short) type.getTypeSize());

        w_history.addChange(change);
        
        
    }

}
