package org.fiware.kiara.ps.writer;

import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.fiware.kiara.ps.rtps.writer.WriterListener;

public class MyWriterListener extends WriterListener {

    @Override
    public void onWriterMatched(RTPSWriter writer, MatchingInfo info) {
        System.out.println("Writer listener Matching");
    }

}
