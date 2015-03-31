package org.fiware.kiara.server;

import java.util.List;
import org.fiware.kiara.dynamic.services.DynamicFunctionHandler;
import org.fiware.kiara.exceptions.IDLParseException;

public interface Service {

    public void register(Object serviceImpl);

    public void register(String idlOperationName, DynamicFunctionHandler handler);

    public void loadServiceIDLFromString(String idlContents) throws IDLParseException;

}
