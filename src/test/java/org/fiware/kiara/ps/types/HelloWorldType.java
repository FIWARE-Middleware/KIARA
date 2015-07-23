package org.fiware.kiara.ps.types;

import java.io.IOException;

import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.topic.SerializableDataType;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;

public class HelloWorldType extends SerializableDataType<HelloWorld> {

    public HelloWorldType() {
        super(HelloWorld.class, "HelloWorld", HelloWorld.getMaxSize(), false);
    }

    @Override
    public HelloWorld createData() {
        return new HelloWorld();
    }

    
}
