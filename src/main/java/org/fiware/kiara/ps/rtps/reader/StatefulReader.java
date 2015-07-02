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

import org.fiware.kiara.ps.publisher.WriterProxy;
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.ReaderTimes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class StatefulReader extends RTPSReader {
    
    // TODO Implement

    public StatefulReader(RTPSParticipant participant, GUID guid,
            ReaderAttributes att, ReaderHistoryCache history,
            ReaderListener listener) {
        super(participant, guid, att, history, listener);
        // TODO Auto-generated constructor stub
    }

    public void updateTimes(ReaderTimes times) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean acceptMsgFrom(GUID rntityGUID, WriterProxy proxy) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean matchedWriterAdd(RemoteWriterAttributes wdata) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean matchedWriterRemove(RemoteWriterAttributes wdata) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean matchedWriterIsMatched(RemoteWriterAttributes wdata) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean changeReceived(CacheChange change, WriterProxy proxy) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean changeRemovedByHistory(CacheChange change, WriterProxy proxy) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public CacheChange nextUntakenCache(WriterProxy proxy) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CacheChange nextUnreadCache(WriterProxy proxy) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean nextUntakenCache(CacheChange change, WriterProxy proxy) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean nextUnreadCache(CacheChange change, WriterProxy proxy) {
        // TODO Auto-generated method stub
        return false;
    }

    
}
