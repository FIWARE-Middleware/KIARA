package org.fiware.kiara.ps.rtps.common;

import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;

public class ChangeFromWriter {

    public ChangeFromWriterStatus status;

    public boolean isRelevant;

    public final SequenceNumber seqNum;

    private boolean m_isValid;

    private CacheChange m_change;

    public ChangeFromWriter() {
        this.status = ChangeFromWriterStatus.UNKNOWN;
        this.isRelevant = true;
        this.seqNum = new SequenceNumber();
        this.m_isValid = false;
    }

    public CacheChange getChange() {
        return this.m_change;
    }

    public boolean setChange(CacheChange change) {
        this.m_isValid = true;
        this.seqNum.copy(change.getSequenceNumber());
        this.m_change = change;
        return true;
    }

    /**
     * Set change as not valid
     */
    public void notValid() {
        isRelevant = false;
        m_isValid = false;
        m_change = null;
    }

    public boolean isValid() {
        return this.m_isValid;
    }

}
