package org.fiware.kiara.ps.types;

import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.topic.SerializableDataType;

public class HelloWorldType extends SerializableDataType<HelloWorld> {

    public HelloWorldType() {
        super(HelloWorld.class, "HelloWorld", SerializedPayload.PAYLOAD_MAX_SIZE, true);
    }

    @Override
    public HelloWorld createData() {
        return new HelloWorld();
    }
    
}
