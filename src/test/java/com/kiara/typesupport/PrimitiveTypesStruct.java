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


package com.kiara.typesupport;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import com.kiara.serialization.impl.Serializable;
import com.kiara.serialization.impl.SerializerImpl;
import com.kiara.serialization.impl.CDRSerializer;
import com.kiara.transport.impl.TransportMessage;

/**
 * Class definition for the user defined type PrimitiveTypesStruct.
 *
 * @author Kiaragen tool.
 *
 */
public class PrimitiveTypesStruct implements Serializable {

	/*
	 *	Attributes
	 */

	private char myChar;

	private byte myByte;

	private short myUShort;

	private short myShort;

	private int myUInt;

	private int myInt;

	private long myULong;

	private long myLong;

	private float myFloat;

	private double myDouble;

	private boolean myBoolean;

	private java.lang.String myStryng;

	private java.lang.String myStryng5;


	/*
	 *	Default constructor
	 */
	public PrimitiveTypesStruct() {

		this.myChar = 'c';

		this.myByte = 0;

		this.myUShort = 0;

		this.myShort = 0;

		this.myUInt = 0;

		this.myInt = 0;

		this.myULong = 0;

		this.myLong = 0;

		this.myFloat = (float) 0.0;

		this.myDouble = 0.0;

		this.myBoolean = false;

		this.myStryng = "";

		this.myStryng5 = "";
	}

	/*
	 * This method serializes a PrimitiveTypesStruct.
	 *
	 * @see com.kiara.serialization.impl.Serializable#serialize(com.kiara.serialization.impl.SerializerImpl, com.kiara.transport.impl.TransportMessage, java.lang.String)
	 */
	public void serialize(SerializerImpl impl, TransportMessage message, String name) {

		impl.serializeChar(message, name, this.myChar);

		impl.serializeByte(message, name, this.myByte);

		impl.serializeUI16(message, name, this.myUShort);

		impl.serializeI16(message, name, this.myShort);

		impl.serializeUI32(message, name, this.myUInt);

		impl.serializeI32(message, name, this.myInt);

		impl.serializeUI64(message, name, this.myULong);

		impl.serializeI64(message, name, this.myLong);

		impl.serializeFloat32(message, name, this.myFloat);

		impl.serializeFloat64(message, name, this.myDouble);

		impl.serializeBoolean(message, name, this.myBoolean);

		impl.serializeString(message, name, this.myStryng);

		impl.serializeString(message, name, this.myStryng5);
	}

	/*
	 * This method deserializes a PrimitiveTypesStruct.
	 *
	 * @see com.kiara.serialization.impl.Serializable#deserialize(com.kiara.serialization.impl.SerializerImpl, com.kiara.transport.impl.TransportMessage, java.lang.String)
	 */
	public void deserialize(SerializerImpl impl, TransportMessage message, String name) {

			this.myChar = impl.deserializeChar(message, name);

			this.myByte = impl.deserializeByte(message, name);

			this.myUShort = impl.deserializeUI16(message, name);

			this.myShort = impl.deserializeI16(message, name);

			this.myUInt = impl.deserializeUI32(message, name);

			this.myInt = impl.deserializeI32(message, name);

			this.myULong = impl.deserializeUI64(message, name);

			this.myLong = impl.deserializeI64(message, name);

			this.myFloat = impl.deserializeFloat32(message, name);

			this.myDouble = impl.deserializeFloat64(message, name);

			this.myBoolean = impl.deserializeBoolean(message, name);

			this.myStryng = impl.deserializeString(message, name);

			this.myStryng5 = impl.deserializeString(message, name);
	}

	/*
	 * @param other An object instance of Object
	 */
	 @Override
	public boolean equals(Object other) {
		boolean comparison = true;

		if (other instanceof PrimitiveTypesStruct) {

			comparison = comparison && (this.myChar == ((PrimitiveTypesStruct) other).myChar);

			comparison = comparison && (this.myByte == ((PrimitiveTypesStruct) other).myByte);

			comparison = comparison && (this.myUShort == ((PrimitiveTypesStruct) other).myUShort);

			comparison = comparison && (this.myShort == ((PrimitiveTypesStruct) other).myShort);

			comparison = comparison && (this.myUInt == ((PrimitiveTypesStruct) other).myUInt);

			comparison = comparison && (this.myInt == ((PrimitiveTypesStruct) other).myInt);

			comparison = comparison && (this.myULong == ((PrimitiveTypesStruct) other).myULong);

			comparison = comparison && (this.myLong == ((PrimitiveTypesStruct) other).myLong);

			comparison = comparison && (this.myFloat == ((PrimitiveTypesStruct) other).myFloat);

			comparison = comparison && (this.myDouble == ((PrimitiveTypesStruct) other).myDouble);

			comparison = comparison && (this.myBoolean == ((PrimitiveTypesStruct) other).myBoolean);

			comparison = comparison && (this.myStryng.compareTo(((PrimitiveTypesStruct) other).myStryng) == 0);

			comparison = comparison && (this.myStryng5.compareTo(((PrimitiveTypesStruct) other).myStryng5) == 0);

		}

		return comparison;
	}

	/*
	 *This method calculates the maximum size in CDR for this class.
	 *
	 * @param current_alignment Integer containing the current position in the buffer.
	 */
	public static int getMaxCdrSerializedSize(int current_alignment)
	{
	    int current_align = current_alignment;

	    current_align += 1 + CDRSerializer.alignment(current_align, 1);
	    current_align += 1 + CDRSerializer.alignment(current_align, 1);
	    current_align += 2 + CDRSerializer.alignment(current_align, 2);
	    current_align += 2 + CDRSerializer.alignment(current_align, 2);
	    current_align += 4 + CDRSerializer.alignment(current_align, 4);
	    current_align += 4 + CDRSerializer.alignment(current_align, 4);
	    current_align += 8 + CDRSerializer.alignment(current_align, 8);
	    current_align += 8 + CDRSerializer.alignment(current_align, 8);
	    current_align += 4 + CDRSerializer.alignment(current_align, 4);
	    current_align += 8 + CDRSerializer.alignment(current_align, 8);
	    current_align += 1 + CDRSerializer.alignment(current_align, 1);
	    current_align += 4 + CDRSerializer.alignment(current_align, 4) + 255 + 1;
	    current_align += 4 + CDRSerializer.alignment(current_align, 4) + 5 + 1;

	    return current_align;
	}


	/*
	 * Method to get the attribute myChar.
	 */
	public char getmyChar() {
		return this.myChar;
	}

	/*
	 * Method to set the attribute myChar.
	 */
	public void setmyChar(char myChar) {
		this.myChar = myChar;
	}

	/*
	 * Method to get the attribute myByte.
	 */
	public byte getmyByte() {
		return this.myByte;
	}

	/*
	 * Method to set the attribute myByte.
	 */
	public void setmyByte(byte myByte) {
		this.myByte = myByte;
	}

	/*
	 * Method to get the attribute myUShort.
	 */
	public short getmyUShort() {
		return this.myUShort;
	}

	/*
	 * Method to set the attribute myUShort.
	 */
	public void setmyUShort(short myUShort) {
		this.myUShort = myUShort;
	}

	/*
	 * Method to get the attribute myShort.
	 */
	public short getmyShort() {
		return this.myShort;
	}

	/*
	 * Method to set the attribute myShort.
	 */
	public void setmyShort(short myShort) {
		this.myShort = myShort;
	}

	/*
	 * Method to get the attribute myUInt.
	 */
	public int getmyUInt() {
		return this.myUInt;
	}

	/*
	 * Method to set the attribute myUInt.
	 */
	public void setmyUInt(int myUInt) {
		this.myUInt = myUInt;
	}

	/*
	 * Method to get the attribute myInt.
	 */
	public int getmyInt() {
		return this.myInt;
	}

	/*
	 * Method to set the attribute myInt.
	 */
	public void setmyInt(int myInt) {
		this.myInt = myInt;
	}

	/*
	 * Method to get the attribute myULong.
	 */
	public long getmyULong() {
		return this.myULong;
	}

	/*
	 * Method to set the attribute myULong.
	 */
	public void setmyULong(long myULong) {
		this.myULong = myULong;
	}

	/*
	 * Method to get the attribute myLong.
	 */
	public long getmyLong() {
		return this.myLong;
	}

	/*
	 * Method to set the attribute myLong.
	 */
	public void setmyLong(long myLong) {
		this.myLong = myLong;
	}

	/*
	 * Method to get the attribute myFloat.
	 */
	public float getmyFloat() {
		return this.myFloat;
	}

	/*
	 * Method to set the attribute myFloat.
	 */
	public void setmyFloat(float myFloat) {
		this.myFloat = myFloat;
	}

	/*
	 * Method to get the attribute myDouble.
	 */
	public double getmyDouble() {
		return this.myDouble;
	}

	/*
	 * Method to set the attribute myDouble.
	 */
	public void setmyDouble(double myDouble) {
		this.myDouble = myDouble;
	}

	/*
	 * Method to get the attribute myBoolean.
	 */
	public boolean getmyBoolean() {
		return this.myBoolean;
	}

	/*
	 * Method to set the attribute myBoolean.
	 */
	public void setmyBoolean(boolean myBoolean) {
		this.myBoolean = myBoolean;
	}

	/*
	 * Method to get the attribute myStryng.
	 */
	public java.lang.String getmyStryng() {
		return this.myStryng;
	}

	/*
	 * Method to set the attribute myStryng.
	 */
	public void setmyStryng(java.lang.String myStryng) {
		this.myStryng = myStryng;
	}

	/*
	 * Method to get the attribute myStryng5.
	 */
	public java.lang.String getmyStryng5() {
		return this.myStryng5;
	}

	/*
	 * Method to set the attribute myStryng5.
	 */
	public void setmyStryng5(java.lang.String myStryng5) {
		this.myStryng5 = myStryng5;
	}
}
