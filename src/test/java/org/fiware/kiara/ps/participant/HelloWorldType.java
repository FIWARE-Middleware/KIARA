package org.fiware.kiara.ps.participant;

import org.fiware.kiara.ps.topic.SerializableDataType;

public class HelloWorldType extends SerializableDataType<HelloWorld> {

    public HelloWorldType() {
        super(HelloWorld.class, "HelloWorld", HelloWorld.getMaxSize(), false);
    }

}
