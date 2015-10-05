package org.fiware.kiara.ps.reader;

import java.io.IOException;

import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.ReaderListener;
import org.fiware.kiara.ps.types.HelloWorld;
import org.fiware.kiara.ps.types.HelloWorldType;

public class MyListener extends ReaderListener {
    
    private int m_received;
    
    public MyListener() {
        this.m_received = 0;
    }

    @Override
    public void onReaderMatched(RTPSReader reader, MatchingInfo info) {
        System.out.println("NEW READER MATCHED");
    }

    @Override
    public void onNewCacheChangeAdded(RTPSReader reader, CacheChange change) {
        System.out.println("RECEIVED (" + this.m_received + "): "/* + change.getSerializedPayload().getBuffer()*/);
        HelloWorldType type = new HelloWorldType();
        HelloWorld obj = type.createData();
        change.getSerializedPayload().setData(obj);
        try {
            change.getSerializedPayload().deserializeData();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        reader.getHistory().removeChange(change);
        this.m_received++;
        
        System.out.println("Data:");
        System.out.println("\tLongAtt: " + obj.getInnerLongAtt());
        System.out.println("\tStringAtt: " + obj.getInnerStringAtt());
    }
    
}
