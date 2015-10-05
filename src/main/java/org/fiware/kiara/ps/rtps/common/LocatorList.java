/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fiware.kiara.ps.rtps.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
* Class LocatorList, a {@link Locator} vector that doesn't avoid duplicates.
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class LocatorList implements Iterable<Locator> {

    /**
     * List containing all the {@link Locator} elements
     */
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

    /**
     * Copies the content of a LocatorList reference
     * 
     * @param other LocatorList reference to be copied
     */
    public void copy(LocatorList other) {
        this.m_locators.clear();
        for (Locator loc : other.m_locators) {
            this.m_locators.add(new Locator(loc));
        }
    }

    /**
     * Empties the Locator list
     */
    public void clear() {
        this.m_locators.clear();
    }

    /**
     * Returns the Locator object at the begining of the list
     * 
     * @return A reference to the first Locator in the list
     */
    public Locator begin() {
        if (this.m_locators.size() > 0) {
            return this.m_locators.get(0);
        } 
        return null;
    }

    /**
     * Get the list of Locator objects
     * 
     * @return The list of Locators
     */
    public List<Locator> getLocators() {
        return this.m_locators;
    }

    /**
     * Creates a new list to store Locators with an initial size
     * 
     * @param size The initial size of the list
     */
    public void reserve(int size) {
        List<Locator> newList = new ArrayList<Locator>(size);
        newList.addAll(this.m_locators);
        this.m_locators = newList;
    }

    /**
     * Resized the list of Locator objects
     * 
     * @param size The size to which the list will be changed
     */
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

    /**
     * Adds a Locator object at the end of the list
     * 
     * @param locator The new Locator object to be added
     */
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

    /**
     * Pushes an entire Locator list at the end of the current list
     * 
     * @param locatorList The list of Locators to be added
     */
    public void pushBack(LocatorList locatorList) {
        for (Locator it: locatorList.m_locators) {
            this.pushBack(it);
        }
    }

    /**
     * Get the emptinnes status of the list
     * 
     * @return true if the list is empty; false otherwise
     */
    public boolean isEmpty() {
        return this.m_locators.isEmpty();
    }

    /**
     * Checks whether the LocatorList contains a specific Locator object
     * 
     * @param loc The Locator object to be searched
     * @return true if the object exists inside the list; false otherwise
     */
    public boolean contains(Locator loc) {
        return this.m_locators.contains(loc);
    }

    /**
     * Get if the LocatorList is valid
     * 
     * @return true if the LocatorList is valid; false otherwise
     */
    public boolean isValid() {
        for (Locator it : this.m_locators) {
            if (!it.isValid()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a new iterafor for the LocatorList
     */
    @Override
    public Iterator<Locator> iterator() {
        return m_locators.iterator();
    }
    
    /**
     * Converts the LocatorList to its String representation
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        for (Locator loc : this.m_locators) {
            sb.append(loc.toString());
            sb.append(", ");
        }
        return sb.toString();
    }

}
