package org.fiware.kiara.client;

import org.fiware.kiara.dynamic.services.DynamicProxy;

public interface Connection {

    public <T> T getServiceProxy(Class<T> interfaceClass) throws Exception;

    public DynamicProxy getDynamicProxy(String name);

}
