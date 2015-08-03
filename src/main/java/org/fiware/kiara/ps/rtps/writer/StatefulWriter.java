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
package org.fiware.kiara.ps.rtps.writer;

import java.util.List;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class StatefulWriter extends RTPSWriter {
    
    // TODO Implement

    public StatefulWriter(RTPSParticipant participant, GUID guid,
            WriterAttributes att, WriterHistoryCache history,
            WriterListener listener) {
        super(participant, guid, att, history, listener);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean matchedReaderAdd(RemoteReaderAttributes ratt) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean matchedReaderRemove(RemoteReaderAttributes ratt) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean matchedReaderIsMatched(RemoteReaderAttributes ratt) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void updateAttributes(WriterAttributes att) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unsentChangesNotEmpty() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unsentChangeAddedToHistory(CacheChange change) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean changeRemovedByHistory(CacheChange change) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
    * Get the number of matched readers
    * @return Number of the matched readers
    */
    public int getMatchedReadersSize() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
