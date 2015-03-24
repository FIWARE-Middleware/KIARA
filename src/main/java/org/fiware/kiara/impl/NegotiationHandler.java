/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
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
package org.fiware.kiara.impl;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import org.fiware.kiara.transport.http.HttpMessage;
import org.fiware.kiara.transport.impl.TransportConnectionListener;
import org.fiware.kiara.transport.impl.TransportImpl;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.transport.impl.TransportMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class NegotiationHandler implements TransportConnectionListener, TransportMessageListener, Closeable {

    private static final Logger logger = LoggerFactory.getLogger(NegotiationHandler.class);

    public NegotiationHandler() {
    }

    @Override
    public void onConnectionOpened(TransportImpl connection) {
        connection.addMessageListener(this);
    }

    @Override
    public void onConnectionClosed(TransportImpl connection) {
        connection.removeMessageListener(this);
    }

    @Override
    public boolean onMessage(TransportMessage message) {
        final TransportImpl transport = message.getTransport();
        final TransportMessage response = transport.createTransportMessage(message);

        String responseText = null;
        String contentType = null;
        boolean requestProcessed = false;

        if (message instanceof HttpMessage) {
            try {
                final HttpMessage request = (HttpMessage) message;
                URI requestUri = new URI(request.getRequestUri()).normalize();

                responseText = "TEST 123 : " + requestUri;
                contentType = "text/plain; charset=UTF-8";

            } catch (URISyntaxException ex) {
                logger.error("Error", ex);
                responseText = ex.toString();
                contentType = "text/plain; charset=UTF-8";
                requestProcessed = true;
            }
        }

        try {
            if (responseText != null && contentType != null) {
                response.setPayload(ByteBuffer.wrap(responseText.getBytes("UTF-8")));
                response.setContentType(contentType);
                transport.send(response);
            }
        } catch (UnsupportedEncodingException ex) {
            logger.error("No UTF-8 encoding", ex);
        }
        return true;
    }

    @Override
    public void close() throws IOException {
    }

}
