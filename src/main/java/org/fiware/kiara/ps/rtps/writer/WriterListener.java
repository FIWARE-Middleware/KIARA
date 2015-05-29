package org.fiware.kiara.ps.rtps.writer;

import org.fiware.kiara.ps.rtps.common.MatchingInfo;

public abstract class WriterListener {
	
	public abstract void onWriterMatcher(RTPSWriter writer, MatchingInfo info);

}
