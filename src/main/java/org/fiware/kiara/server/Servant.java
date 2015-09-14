package org.fiware.kiara.server;

import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.transport.Transport;
import org.fiware.kiara.transport.impl.TransportMessage;

/**
 * This interface provides an abstraction used by the server to execute the
 * provided functions when a client request is received.
 */
public interface Servant
{
    /**
     * This interface provides an abstraction used by the server to execute the
     * provided functions when a client request is received.
     *
     * @return service name
     */
    public String getServiceName();

    /**
     * This function processes incoming request message and returns produced
     * response message. It is automatically generated.
     *
     * @param ser serializer for incoming message
     * @param message incoming request message
     * @param transport transport layer abstraction
     * @param messageId message ID
     * @param bis stream for reading incoming message data
     * @return response message for transport
     * @see TransportMessage
     */
    public TransportMessage process(Serializer ser, TransportMessage message, Transport transport, Object messageId, BinaryInputStream bis);
}
