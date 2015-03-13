 /* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * @file .java
 * This file contains the class representing a user defined structure.
 *
 * This file was generated by using the tool Kiaragen.
 *
 */
package org.fiware.kiara.struct;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import org.fiware.kiara.serialization.impl.BasicSerializers;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;

import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.impl.ListAsArraySerializer;
import org.fiware.kiara.serialization.impl.ListAsSequenceSerializer;
import org.fiware.kiara.serialization.impl.Serializer;
import org.fiware.kiara.serialization.impl.MapAsMapSerializer;
import org.fiware.kiara.serialization.impl.SetAsSetSerializer;
import org.fiware.kiara.serialization.impl.ObjectSerializer;
import org.fiware.kiara.serialization.impl.EnumSerializer;

/**
 * Class definition for the user defined type OuterStruct.
 *
 * @author Kiaragen tool.
 *
 */
public class OuterStruct implements Serializable {

    /*
     *	Attributes
     */
    private int outerLongAtt;
    private MidStruct midStructAtt;
    private InnerStruct innerStructAtt;

    /*
         *      Attribute Serializers
         */



	/*
     *	Default constructor
     */
    public OuterStruct() {
        this.outerLongAtt = 0;
        this.midStructAtt = new MidStruct();
        this.innerStructAtt = new InnerStruct();
    }

    /*
     * This method serializes a OuterStruct.
     *
	 * @see org.fiware.kiara.serialization.impl.Serializable#serialize(org.fiware.kiara.serialization.impl.SerializerImpl, org.fiware.kiara.serialization.impl.BinaryOutputStream, java.lang.String)
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        impl.serializeI32(message, name, this.outerLongAtt);
        impl.serialize(message, name, this.midStructAtt);
        impl.serialize(message, name, this.innerStructAtt);
    }

    /*
     * This method deserializes a OuterStruct.
     *
	 * @see org.fiware.kiara.serialization.impl.Serializable#deserialize(org.fiware.kiara.serialization.impl.SerializerImpl, org.fiware.kiara.serialization.impl.BinaryInputStream, java.lang.String)
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.outerLongAtt = impl.deserializeI32(message, name);
        try {
            this.midStructAtt = impl.deserialize(message, name, MidStruct.class);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            this.innerStructAtt = impl.deserialize(message, name, InnerStruct.class);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /*
     * @param other An object instance of Object
     */
    @Override
    public boolean equals(Object other) {
        boolean comparison = true;

        if (other instanceof OuterStruct) {

            comparison = comparison && (this.outerLongAtt == ((OuterStruct) other).outerLongAtt);

            comparison = comparison && this.midStructAtt.equals(((OuterStruct) other).midStructAtt);

            comparison = comparison && this.innerStructAtt.equals(((OuterStruct) other).innerStructAtt);

        }

        return comparison;
    }

    /*
     * Method to get the attribute outerLongAtt.
     */
    public int getOuterLongAtt() {
        return this.outerLongAtt;
    }

    /*
     * Method to set the attribute outerLongAtt.
     */
    public void setOuterLongAtt(int outerLongAtt) {
        this.outerLongAtt = outerLongAtt;
    }

    /*
     * Method to get the attribute midStructAtt.
     */
    public MidStruct getMidStructAtt() {
        return this.midStructAtt;
    }

    /*
     * Method to set the attribute midStructAtt.
     */
    public void setMidStructAtt(MidStruct midStructAtt) {
        this.midStructAtt = midStructAtt;
    }

    /*
     * Method to get the attribute innerStructAtt.
     */
    public InnerStruct getInnerStructAtt() {
        return this.innerStructAtt;
    }

    /*
     * Method to set the attribute innerStructAtt.
     */
    public void setInnerStructAtt(InnerStruct innerStructAtt) {
        this.innerStructAtt = innerStructAtt;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.outerLongAtt, this.midStructAtt, this.innerStructAtt);
    }
}
