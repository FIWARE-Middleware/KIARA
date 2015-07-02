package org.fiware.kiara.ps.reader;

import org.fiware.kiara.ps.Domain;
import org.fiware.kiara.ps.rtps.RTPSDomain;
import org.fiware.kiara.ps.rtps.attributes.HistoryCacheAttributes;
import org.fiware.kiara.ps.rtps.attributes.RTPSParticipantAttributes;
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.ReaderListener;
import org.fiware.kiara.ps.rtps.utils.InfoEndianness;
import org.fiware.kiara.ps.topic.SerializableDataType;
import org.fiware.kiara.ps.types.HelloWorld;
import org.fiware.kiara.ps.types.HelloWorldType;

public class ReaderTest {
    
    public static void main(String [] args) {
        
        RTPSParticipantAttributes pparam = new RTPSParticipantAttributes();
        pparam.builtinAtt.useSimplePDP = false;
        pparam.builtinAtt.useWriterLP = false;
        
        RTPSParticipant participant = RTPSDomain.createParticipant(pparam, null);
        if (participant == null) {
            System.out.println("ERROR creating participant");
            return;
        }
        
        HistoryCacheAttributes hatt = new HistoryCacheAttributes();
        hatt.payloadMaxSize = 500;
        
        ReaderHistoryCache rhist = new ReaderHistoryCache(hatt);
        
        ReaderAttributes ratt = new ReaderAttributes();
        
        Locator loc = new Locator();
        loc.setIPv4Address("239.255.0.1");
        loc.setPort(7400);
        
        ratt.endpointAtt.multicastLocatorList.pushBack(loc);
        
        RTPSReader reader = RTPSDomain.createRTPSReader(participant, ratt, rhist, new MyListener());
        
        if (reader == null) {
            System.out.println("ERROR creating reader");
            return;
        }
        
    }
    

}
