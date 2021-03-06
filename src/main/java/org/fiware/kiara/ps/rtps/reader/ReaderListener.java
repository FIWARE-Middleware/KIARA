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
package org.fiware.kiara.ps.rtps.reader;

import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.history.CacheChange;

/**
 * Listener to be invoked when an event should occur
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public abstract class ReaderListener {

    /**
     * This method will be executed when a new Reader has matched
     * 
     * @param reader The matched {@link RTPSReader}
     * @param info The {@link MatchingInfo}
     */
    public abstract void onReaderMatched(RTPSReader reader, MatchingInfo info);

    /**
     * This method will be executed when a new CacheChange has been added
     * 
     * @param reader The matched {@link RTPSReader}
     * @param change The {@link CacheChange} that has been added
     */
    public abstract void onNewCacheChangeAdded(RTPSReader reader, CacheChange change);

}
