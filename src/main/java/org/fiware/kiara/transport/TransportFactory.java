/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
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
package org.fiware.kiara.transport;

import com.google.common.util.concurrent.ListenableFuture;

import org.fiware.kiara.exceptions.impl.InvalidAddressException;

import java.io.IOException;
import java.util.Map;

/**
 * This interface provides the abstraction of the factory that creates
 * transports and server transports.
 * It has a name and can be registered with the context.
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public interface TransportFactory {

    /**
     * This function returns name of the transport.
     * @return transport name
     */
    public String getName();

    /**
     * This function returns priority of the transport which depends on the efficiency.
     * @return transport priority
     */
    public int getPriority();

    /**
     * This function returns true when transport uses secure connections.
     * @return secure connection state of the transport
     */
    public boolean isSecureTransport();

    /**
     * This function instantiates a network transport when the user wants later to configure it.
     *
     * @param uri
     * @param settings
     * @return future which is done when transport is opened.
     * @throws InvalidAddressException
     * @throws IOException
     * @see ListenableFuture
     * @see Transport
     */
    public ListenableFuture<Transport> createTransport(String uri, Map<String, Object> settings) throws InvalidAddressException, IOException;

    /**
     * This function instantiates a network server transport when the user wants later to configure.
     *
     * @param url
     * @return new server transport instance
     * @throws IOException
     * @see ServerTransport
     */
    public ServerTransport createServerTransport(String url) throws IOException;

}
