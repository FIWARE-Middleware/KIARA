package org.fiware.kiara.server;

import org.fiware.kiara.dynamic.services.DynamicFunctionHandler;
import org.fiware.kiara.exceptions.IDLParseException;

/**
 * This interface represent service that can be registered with the server.
 */
public interface Service {

    /**
     * Register servant object with the service.
     *
     * @param serviceImpl The service implementation
     */
    public void register(Object serviceImpl);

    /**
     * Register dynamic handler with the service.
     *
     * @param idlOperationName Name of the operation in the IDL file
     * @param handler The dynamic handler to be called when a certain input is received
     */
    public void register(String idlOperationName, DynamicFunctionHandler handler);

    /**
     *
     * Load service IDL from string.
     * This function is only required when service is handled via dynamic
     * handlers. Automatically generated servant objects contain supported IDL.
     *
     * @param idlContents The contents of the IDL file describing the services
     * @throws IDLParseException If the file cannot be parsed properly
     */
    public void loadServiceIDLFromString(String idlContents) throws IDLParseException;

}
