package org.fiware.kiara.ps.common;

import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.common.MatchingStatus;
import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.subscriber.SampleInfo;
import org.fiware.kiara.ps.subscriber.Subscriber;
import org.fiware.kiara.ps.subscriber.SubscriberListener;
import org.fiware.kiara.ps.types.HelloWorld;
import org.fiware.kiara.ps.types.HelloWorldType;
import org.fiware.kiara.serialization.impl.Serializable;

public class SubListener extends SubscriberListener {
    
    private int n_matched;

    @Override
    public void onNewDataMessage(Subscriber<?> sub) {
        System.out.println("Message received");
        SampleInfo info = new SampleInfo();
        HelloWorld type = (HelloWorld) sub.takeNextData(null);
        while(type != null) {
            HelloWorld instance = (HelloWorld) type;
            System.out.println(instance.getInnerStringAtt());
            type = (HelloWorld) sub.takeNextData(null);
        }
    }

    @Override
    public void onSubscriptionMatched(Subscriber sub, MatchingInfo info) {
        if (info.status == MatchingStatus.MATCHED_MATHING) {
            n_matched++;
            System.out.println("Publisher Matched. Total : " + this.n_matched);
        } else {
            n_matched--;
            System.out.println("Publisher Unmatched. Total : " + this.n_matched);
        }
    }


}
