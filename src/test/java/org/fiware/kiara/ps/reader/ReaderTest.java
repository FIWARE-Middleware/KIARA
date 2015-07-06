package org.fiware.kiara.ps.reader;

import java.util.ArrayList;
import java.util.List;

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
    
    public static List<HelloWorld> list = new ArrayList<HelloWorld>();
    
    public static void main(String [] args) {
        list.add(new HelloWorld());
        list.add(new HelloWorld());
        list.add(new HelloWorld());
        list.get(0).setInnerLongAtt(10);
        list.get(0).setInnerStringAtt("Hello World");
        
        HelloWorld obj = new HelloWorld();
        //test(obj);
        //test2(obj);
        //test3(obj);
        /*
        int i1 = 0;
        int i2 = 5;
        i1 = i2;
        i2 = 10;
        */
        
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
    
    public static void test(HelloWorld obj) {
        HelloWorld obj2 = new HelloWorld();
        obj2.setInnerLongAtt(10);
        obj2.setInnerStringAtt("Hello World");
        obj.setInnerLongAtt(obj2.getInnerLongAtt());
        obj.setInnerStringAtt(obj2.getInnerStringAtt());
        //obj = obj2;
    }
    
    public static void test2(HelloWorld obj) {
        for (HelloWorld it : list) {
            if (it.getInnerLongAtt() == 10) {
                obj = it;
                return;
            }
        }
        
    }
    
    public static void test3(HelloWorld obj) {
        for (HelloWorld it : list) {
            if (it.getInnerLongAtt() == 10) {
                obj.copy(it);
                return;
            }
        }
        
    }
    

}
