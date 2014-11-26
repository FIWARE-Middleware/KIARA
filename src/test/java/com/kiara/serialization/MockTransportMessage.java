package com.kiara.serialization;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.kiara.transport.impl.TransportMessage;

public class MockTransportMessage extends TransportMessage {

    private final Map<String, Object> properties = new HashMap<String, Object>();

    /**
     * @param connection
     * @param payload
     */
    public MockTransportMessage(ByteBuffer payload) {
        super(new MockTransportImpl(), payload);
    }

    @Override
    public TransportMessage set(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    @Override
    public Object get(String name) {
        return properties.get(name);
    }

}