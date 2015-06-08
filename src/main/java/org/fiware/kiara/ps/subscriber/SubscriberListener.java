package org.fiware.kiara.ps.subscriber;

import org.fiware.kiara.ps.rtps.common.MatchingInfo;

public abstract class SubscriberListener {
    
    public abstract void onNewDataMessage(Subscriber sub);
    
    public abstract void onSubscriptionMatched(Subscriber sub, MatchingInfo info);

}
