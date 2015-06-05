package org.fiware.kiara.ps.publisher;

import org.fiware.kiara.ps.rtps.common.MatchingInfo;

public abstract class PublisherListener {
    
    public abstract void onPublicationMatched(Publisher pub, MatchingInfo info);

}
