package org.fiware.kiara.ps.rtps.builtin.discovery.participant;

import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.ReaderListener;

public class PDPSimpleListener extends ReaderListener {

    public PDPSimpleListener(PDPSimple pdpSimple) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onReaderMatched(RTPSReader reader, MatchingInfo info) {
        // TODO Auto-generated method stub
        System.out.println("PDPSimpleListener: READER MATCHED");
    }

    @Override
    public void onNewCacheChangeAdded(RTPSReader reader, CacheChange change) {
        // TODO Auto-generated method stub
        System.out.println("PDPSimpleListener: NEW CHANGE ADDED");
    }

}
