package org.fiware.kiara.ps.common;

import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.common.MatchingStatus;
import org.fiware.kiara.ps.subscriber.Subscriber;
import org.fiware.kiara.ps.subscriber.SubscriberListener;

public class SubListener extends SubscriberListener {
    
    private int n_matched;

    @Override
    public void onNewDataMessage(Subscriber sub) {
        System.out.println("Message received");
    }

    @Override
    public void onSubscriptionMatched(Subscriber sub, MatchingInfo info) {
        if (info.status == MatchingStatus.MATCHED_MATHING) {
            n_matched++;
            System.out.println("Subscriber Matched. Total : " + this.n_matched);
        } else {
            n_matched--;
            System.out.println("Subscriber Unmatched. Total : " + this.n_matched);
        }
    }

}
