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
     * @param serviceImpl
     */
    public void register(Object serviceImpl);

    /**
     * Register dynamic handler with the service.
     *
     * @param idlOperationName
     * @param handler
     */
    public void register(String idlOperationName, DynamicFunctionHandler handler);

    /**
     *
     * Load service IDL from string.
     * This function is only required when service is handled via dynamic
     * handlers. Automatically generated servant objects contain supported IDL.
     *
     * @param idlContents
     * @throws IDLParseException
     */
    public void loadServiceIDLFromString(String idlContents) throws IDLParseException;

}
