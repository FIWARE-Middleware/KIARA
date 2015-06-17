package org.fiware.kiara.ps.rtps.writer;

import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.history.CacheChange;

public class ReaderLocator extends Locator {
    // TODO Implement
    
    private Locator m_locator;
    
    private boolean m_expectsInlineQos;
    
    private List<CacheChange> m_requestedChanges;
    
    private List<CacheChange> m_unsentChanges;
    
    private int m_used;
    
    public ReaderLocator() {
        this.m_requestedChanges = new ArrayList<CacheChange>();
        this.m_unsentChanges = new ArrayList<CacheChange>();
    }

    public Locator getLocator() {
        return m_locator;
    }

    public void setLocator(Locator m_locator) {
        this.m_locator = m_locator;
    }

    public boolean getExpectsInlineQos() {
        return m_expectsInlineQos;
    }

    public void setExpectsInlineQos(boolean m_expectsInlineQos) {
        this.m_expectsInlineQos = m_expectsInlineQos;
    }

    public List<CacheChange> getRequestedChanges() {
        return m_requestedChanges;
    }

    public void setRequestedChanges(List<CacheChange> m_requestedChanges) {
        this.m_requestedChanges = m_requestedChanges;
    }

    public List<CacheChange> getUnsentChanges() {
        return m_unsentChanges;
    }

    public void setUnsentChanges(List<CacheChange> m_unsentChanges) {
        this.m_unsentChanges = m_unsentChanges;
    }

    public int getUsed() {
        return m_used;
    }

    public void setUsed(int m_used) {
        this.m_used = m_used;
    }
    
    public void increaseUsed() {
        ++this.m_used;
    }

    public void decreaseUsed() {
        --this.m_used;
    }
    
}
