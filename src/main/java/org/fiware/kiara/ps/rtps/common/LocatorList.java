package org.fiware.kiara.ps.rtps.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocatorList {

    private List<Locator> m_locators;

    public LocatorList() {
        this.m_locators = new ArrayList<Locator>();
    }
    
    public LocatorList(LocatorList other) {
        this.m_locators = new ArrayList<Locator>();
        //System.arraycopy(other.m_locators, 0, this.m_locators, 0, other.m_locators.size());
        for (Locator loc : other.m_locators) {
            this.m_locators.add(new Locator(loc));
        }
    }

    public void clear() {
        this.m_locators.clear();
    }

    public Locator begin() {
        if (this.m_locators.size() > 0) {
            return this.m_locators.get(0);
        } 
        return null;
    }

    public List<Locator> getLocators() {
        return this.m_locators;
    }

    public void reserve(int size) {
        List<Locator> newList = new ArrayList<Locator>(size);
        newList.addAll(this.m_locators);
        this.m_locators = newList;
    }

    public void resize(int size) {
        if (size > this.m_locators.size()) {
            ArrayList<Locator> newList = new ArrayList<Locator>(size);
            newList.addAll(this.m_locators);
            this.m_locators = newList;
            int initialSize = this.m_locators.size();
            for (int i=0; i < (size - initialSize); ++i) {
                this.m_locators.add(new Locator());
            }
        } else if (size < this.m_locators.size()) {
            int initialSize = this.m_locators.size();
            for (int i=size; i < initialSize; ++i) {
                this.m_locators.remove(this.m_locators.size()-1);
            }
        }
    }

    public void pushBack(Locator locator) {
        boolean already = false;
        for (Locator it: this.m_locators) {
            if (it.equals(locator)) {
                already = true;
                break;
            }
        }
        if (!already) {
            this.m_locators.add(locator);
        }
    }

    public void pushBack(LocatorList locatorList) {
        for (Locator it: locatorList.m_locators) {
            this.pushBack(it);
        }
    }

    public boolean isEmpty() {
        return this.m_locators.isEmpty();
    }

    public boolean contains(Locator loc) {
        return this.m_locators.contains(loc);
    }

    public boolean isValid() {
        for (Locator it : this.m_locators) {
            if (!it.isValid()) {
                return false;
            }
        }
        return true;
    }

}
