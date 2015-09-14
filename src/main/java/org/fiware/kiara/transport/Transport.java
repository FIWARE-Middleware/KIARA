package org.fiware.kiara.transport;

/**
 * This interface provides a simple abstraction for network transport implementations.
 * The user can creates directly a network transport using the interface
 * {@link org.fiware.kiara.Context}.
 * The {@link org.fiware.kiara.Context} interface will return the network
 * transport implementation encapsulated in this interface.
 */
public interface Transport {

    /**
     * This function returns an instance of a factory class which was used to
     * create this transport instance.
     *
     * @return transport factory.
     * @see TransportFactory
     */
    public TransportFactory getTransportFactory();
}
