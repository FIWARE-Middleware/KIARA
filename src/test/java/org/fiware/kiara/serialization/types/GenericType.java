package org.fiware.kiara.serialization.types;

import java.io.IOException;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class GenericType implements Serializable {

    private int myInt;
    private String myString;

    public GenericType() {
        this.myInt =0;
        this.myString = "";
    }

    public GenericType(int myInt, String myString) {
        this.myInt = myInt;
        this.myString = myString;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream bos, String name) throws IOException {
        impl.serializeI32(bos, name, this.myInt);
        impl.serializeString(bos, name, this.myString);
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream bis, String name) throws IOException {
        this.myInt = impl.deserializeI32(bis, name);
        this.myString = impl.deserializeString(bis, name);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof GenericType) {
            if (((GenericType) other).myInt == this.myInt && ((GenericType) other).myString.compareTo(this.myString) == 0) {
                return true;
            }
        }

        return false;
    }
    
    @Override
    public int hashCode() {
        return myInt;
    }

    public String getClassName() {
        return "GenericType";
    }

    public int getMyInt() {
        return this.myInt;
    }

    public void setMyInt(int myInt) {
        this.myInt = myInt;
    }

    public String getMyString() {
        return this.myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

}
