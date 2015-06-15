package org.fiware.kiara.ps.participant;

import org.fiware.kiara.ps.publisher.Publisher;
import org.fiware.kiara.ps.publisher.PublisherListener;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.common.MatchingStatus;

public class PubListener extends PublisherListener {
    
    private int n_matched;
    
    public PubListener() {
        this.n_matched = 0;
    }

    @Override
    public void onPublicationMatched(Publisher pub, MatchingInfo info) {
        if (info.status == MatchingStatus.MATCHED_MATHING) {
            n_matched++;
            System.out.println("Publisher Matched. Total : " + this.n_matched);
        } else {
            n_matched--;
            System.out.println("Publisher Unmatched. Total : " + this.n_matched);
        }
    }
    
    public int getNMatched() {
        return this.n_matched;
    }
    
}
