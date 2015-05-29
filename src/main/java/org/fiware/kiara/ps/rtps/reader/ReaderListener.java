package org.fiware.kiara.ps.rtps.reader;

import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.history.CacheChange;

public abstract class ReaderListener {
	
	public abstract void onReaderMatched(RTPSReader reader, MatchingInfo info);
	
	public abstract void onNewCacheChangeAdded(RTPSReader reader, CacheChange change);

}
