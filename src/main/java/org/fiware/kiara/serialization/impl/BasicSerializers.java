/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
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
 */
package org.fiware.kiara.serialization.impl;

import java.io.IOException;

/**
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class BasicSerializers {

    public static class Float32Serializer implements org.fiware.kiara.serialization.impl.Serializer<Float> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, Float value) throws IOException {
            impl.serializeFloat32(message, name, value);
        }

        @Override
        public Float read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            return impl.deserializeFloat32(message, name);
        }
    }

    public static class Float64Serializer implements org.fiware.kiara.serialization.impl.Serializer<Double> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, Double value) throws IOException {
            impl.serializeFloat64(message, name, value);
        }

        @Override
        public Double read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            return impl.deserializeFloat64(message, name);
        }
    }

    public static class CharSerializer implements org.fiware.kiara.serialization.impl.Serializer<Character> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, Character value) throws IOException {
            impl.serializeChar(message, name, value);
        }

        @Override
        public Character read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            return impl.deserializeChar(message, name);
        }
    }

    public static class ByteSerializer implements org.fiware.kiara.serialization.impl.Serializer<Byte> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, Byte value) throws IOException {
            impl.serializeByte(message, name, value);
        }

        @Override
        public Byte read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            return impl.deserializeByte(message, name);
        }
    }

    public static class I16Serializer implements org.fiware.kiara.serialization.impl.Serializer<Short> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, Short value) throws IOException {
            impl.serializeI16(message, name, value);
        }

        @Override
        public Short read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            return impl.deserializeI16(message, name);
        }
    }

    public static class UI16Serializer implements org.fiware.kiara.serialization.impl.Serializer<Short> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, Short value) throws IOException {
            impl.serializeUI16(message, name, value);
        }

        @Override
        public Short read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            return impl.deserializeUI16(message, name);
        }
    }

    public static class I32Serializer implements org.fiware.kiara.serialization.impl.Serializer<Integer> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, Integer value) throws IOException {
            impl.serializeI32(message, name, value);
        }

        @Override
        public Integer read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            return impl.deserializeI32(message, name);
        }
    }

    public static class UI32Serializer implements org.fiware.kiara.serialization.impl.Serializer<Integer> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, Integer value) throws IOException {
            impl.serializeUI32(message, name, value);
        }

        @Override
        public Integer read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            return impl.deserializeUI32(message, name);
        }
    }

    public static class I64Serializer implements org.fiware.kiara.serialization.impl.Serializer<Long> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, Long value) throws IOException {
            impl.serializeI64(message, name, value);
        }

        @Override
        public Long read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            return impl.deserializeI64(message, name);
        }
    }

    public static class UI64Serializer implements org.fiware.kiara.serialization.impl.Serializer<Long> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, Long value) throws IOException {
            impl.serializeUI64(message, name, value);
        }

        @Override
        public Long read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            return impl.deserializeUI64(message, name);
        }
    }

    public static class StringSerializer implements org.fiware.kiara.serialization.impl.Serializer<java.lang.String> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, java.lang.String value) throws IOException {
            impl.serializeString(message, name, value);
        }

        @Override
        public java.lang.String read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            return impl.deserializeString(message, name);
        }
    }

    public static class BooleanSerializer implements org.fiware.kiara.serialization.impl.Serializer<Boolean> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, Boolean value) throws IOException {
            impl.serializeBoolean(message, name, value);
        }

        @Override
        public Boolean read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            return impl.deserializeBoolean(message, name);
        }
    }

    public static class ArrayAsFloat32SequenceSerializer implements Serializer<float[]> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, float[] array) throws IOException {
            impl.serializeSequenceBegin(message, name);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeFloat32(message, name, array[i]);
            }
            impl.serializeSequenceEnd(message, name);
        }

        @Override
        public float[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeSequenceBegin(message, name);
            int length = impl.deserializeI32(message, "");
            float[] array = new float[length];
            for (int i = 0; i < length; ++i) {
                array[i] = impl.deserializeFloat32(message, name);
            }
            impl.deserializeSequenceEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsFloat64SequenceSerializer implements Serializer<double[]> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, double[] array) throws IOException {
            impl.serializeSequenceBegin(message, name);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeFloat64(message, name, array[i]);
            }
            impl.serializeSequenceEnd(message, name);
        }

        @Override
        public double[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeSequenceBegin(message, name);
            int length = impl.deserializeI32(message, "");
            double[] array = new double[length];
            for (int i = 0; i < length; ++i) {
                array[i] = impl.deserializeFloat64(message, name);
            }
            impl.deserializeSequenceEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsCharSequenceSerializer implements Serializer<char[]> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, char[] array) throws IOException {
            impl.serializeSequenceBegin(message, name);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeChar(message, name, array[i]);
            }
            impl.serializeSequenceEnd(message, name);
        }

        @Override
        public char[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeSequenceBegin(message, name);
            int length = impl.deserializeI32(message, "");
            char[] array = new char[length];
            for (int i = 0; i < length; ++i) {
                array[i] = impl.deserializeChar(message, name);
            }
            impl.deserializeSequenceEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsByteSequenceSerializer implements Serializer<byte[]> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, byte[] array) throws IOException {
            impl.serializeSequenceBegin(message, name);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeByte(message, name, array[i]);
            }
            impl.serializeSequenceEnd(message, name);
        }

        @Override
        public byte[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeSequenceBegin(message, name);
            int length = impl.deserializeI32(message, "");
            byte[] array = new byte[length];
            for (int i = 0; i < length; ++i) {
                array[i] = impl.deserializeByte(message, name);
            }
            impl.deserializeSequenceEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsI16SequenceSerializer implements Serializer<short[]> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, short[] array) throws IOException {
            impl.serializeSequenceBegin(message, name);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeI16(message, name, array[i]);
            }
            impl.serializeSequenceEnd(message, name);
        }

        @Override
        public short[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeSequenceBegin(message, name);
            int length = impl.deserializeI32(message, "");
            short[] array = new short[length];
            for (int i = 0; i < length; ++i) {
                array[i] = impl.deserializeI16(message, name);
            }
            impl.deserializeSequenceEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsUI16SequenceSerializer implements Serializer<short[]> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, short[] array) throws IOException {
            impl.serializeSequenceBegin(message, name);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeUI16(message, name, array[i]);
            }
            impl.serializeSequenceEnd(message, name);
        }

        @Override
        public short[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeSequenceBegin(message, name);
            int length = impl.deserializeI32(message, "");
            short[] array = new short[length];
            for (int i = 0; i < length; ++i) {
                array[i] = impl.deserializeUI16(message, name);
            }
            impl.deserializeSequenceEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsI32SequenceSerializer implements Serializer<int[]> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, int[] array) throws IOException {
            impl.serializeSequenceBegin(message, name);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeI32(message, name, array[i]);
            }
            impl.serializeSequenceEnd(message, name);
        }

        @Override
        public int[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeSequenceBegin(message, name);
            int length = impl.deserializeI32(message, "");
            int[] array = new int[length];
            for (int i = 0; i < length; ++i) {
                array[i] = impl.deserializeI32(message, name);
            }
            impl.deserializeSequenceEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsUI32SequenceSerializer implements Serializer<int[]> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, int[] array) throws IOException {
            impl.serializeSequenceBegin(message, name);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeUI32(message, name, array[i]);
            }
            impl.serializeSequenceEnd(message, name);
        }

        @Override
        public int[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeSequenceBegin(message, name);
            int length = impl.deserializeI32(message, "");
            int[] array = new int[length];
            for (int i = 0; i < length; ++i) {
                array[i] = impl.deserializeUI32(message, name);
            }
            impl.deserializeSequenceEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsI64SequenceSerializer implements Serializer<long[]> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, long[] array) throws IOException {
            impl.serializeSequenceBegin(message, name);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeI64(message, name, array[i]);
            }
            impl.serializeSequenceEnd(message, name);
        }

        @Override
        public long[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeSequenceBegin(message, name);
            int length = impl.deserializeI32(message, "");
            long[] array = new long[length];
            for (int i = 0; i < length; ++i) {
                array[i] = impl.deserializeI64(message, name);
            }
            impl.deserializeSequenceEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsUI64SequenceSerializer implements Serializer<long[]> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, long[] array) throws IOException {
            impl.serializeSequenceBegin(message, name);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeUI64(message, name, array[i]);
            }
            impl.serializeSequenceEnd(message, name);
        }

        @Override
        public long[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeSequenceBegin(message, name);
            int length = impl.deserializeI32(message, "");
            long[] array = new long[length];
            for (int i = 0; i < length; ++i) {
                array[i] = impl.deserializeUI64(message, name);
            }
            impl.deserializeSequenceEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsStringSequenceSerializer implements Serializer<java.lang.String[]> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, java.lang.String[] array) throws IOException {
            impl.serializeSequenceBegin(message, name);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeString(message, name, array[i]);
            }
            impl.serializeSequenceEnd(message, name);
        }

        @Override
        public java.lang.String[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeSequenceBegin(message, name);
            int length = impl.deserializeI32(message, "");
            java.lang.String[] array = new java.lang.String[length];
            for (int i = 0; i < length; ++i) {
                array[i] = impl.deserializeString(message, name);
            }
            impl.deserializeSequenceEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsBooleanSequenceSerializer implements Serializer<boolean[]> {

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, boolean[] array) throws IOException {
            impl.serializeSequenceBegin(message, name);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeBoolean(message, name, array[i]);
            }
            impl.serializeSequenceEnd(message, name);
        }

        @Override
        public boolean[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeSequenceBegin(message, name);
            int length = impl.deserializeI32(message, "");
            boolean[] array = new boolean[length];
            for (int i = 0; i < length; ++i) {
                array[i] = impl.deserializeBoolean(message, name);
            }
            impl.deserializeSequenceEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsFloat32ArraySerializer implements Serializer<float[]> {

        private final int arrayDim;

        public ArrayAsFloat32ArraySerializer(int arrayDim) {
            this.arrayDim = arrayDim;
        }

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, float[] array) throws IOException {
            impl.serializeArrayBegin(message, name, arrayDim);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeFloat32(message, name, array[i]);
            }
            impl.serializeArrayEnd(message, name);
        }

        @Override
        public float[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeArrayBegin(message, name);
            final float[] array = new float[arrayDim];
            for (int i = 0; i < arrayDim; ++i) {
                array[i] = impl.deserializeFloat32(message, name);
            }
            impl.deserializeArrayEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsFloat64ArraySerializer implements Serializer<double[]> {

        private final int arrayDim;

        public ArrayAsFloat64ArraySerializer(int arrayDim) {
            this.arrayDim = arrayDim;
        }

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, double[] array) throws IOException {
            impl.serializeArrayBegin(message, name, arrayDim);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeFloat64(message, name, array[i]);
            }
            impl.serializeArrayEnd(message, name);
        }

        @Override
        public double[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeArrayBegin(message, name);
            final double[] array = new double[arrayDim];
            for (int i = 0; i < arrayDim; ++i) {
                array[i] = impl.deserializeFloat64(message, name);
            }
            impl.deserializeArrayEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsCharArraySerializer implements Serializer<char[]> {

        private final int arrayDim;

        public ArrayAsCharArraySerializer(int arrayDim) {
            this.arrayDim = arrayDim;
        }

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, char[] array) throws IOException {
            impl.serializeArrayBegin(message, name, arrayDim);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeChar(message, name, array[i]);
            }
            impl.serializeArrayEnd(message, name);
        }

        @Override
        public char[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeArrayBegin(message, name);
            final char[] array = new char[arrayDim];
            for (int i = 0; i < arrayDim; ++i) {
                array[i] = impl.deserializeChar(message, name);
            }
            impl.deserializeArrayEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsByteArraySerializer implements Serializer<byte[]> {

        private final int arrayDim;

        public ArrayAsByteArraySerializer(int arrayDim) {
            this.arrayDim = arrayDim;
        }

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, byte[] array) throws IOException {
            impl.serializeArrayBegin(message, name, arrayDim);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeByte(message, name, array[i]);
            }
            impl.serializeArrayEnd(message, name);
        }

        @Override
        public byte[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeArrayBegin(message, name);
            final byte[] array = new byte[arrayDim];
            for (int i = 0; i < arrayDim; ++i) {
                array[i] = impl.deserializeByte(message, name);
            }
            impl.deserializeArrayEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsI16ArraySerializer implements Serializer<short[]> {

        private final int arrayDim;

        public ArrayAsI16ArraySerializer(int arrayDim) {
            this.arrayDim = arrayDim;
        }

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, short[] array) throws IOException {
            impl.serializeArrayBegin(message, name, arrayDim);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeI16(message, name, array[i]);
            }
            impl.serializeArrayEnd(message, name);
        }

        @Override
        public short[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeArrayBegin(message, name);
            final short[] array = new short[arrayDim];
            for (int i = 0; i < arrayDim; ++i) {
                array[i] = impl.deserializeI16(message, name);
            }
            impl.deserializeArrayEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsUI16ArraySerializer implements Serializer<short[]> {

        private final int arrayDim;

        public ArrayAsUI16ArraySerializer(int arrayDim) {
            this.arrayDim = arrayDim;
        }

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, short[] array) throws IOException {
            impl.serializeArrayBegin(message, name, arrayDim);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeUI16(message, name, array[i]);
            }
            impl.serializeArrayEnd(message, name);
        }

        @Override
        public short[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeArrayBegin(message, name);
            final short[] array = new short[arrayDim];
            for (int i = 0; i < arrayDim; ++i) {
                array[i] = impl.deserializeUI16(message, name);
            }
            impl.deserializeArrayEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsI32ArraySerializer implements Serializer<int[]> {

        private final int arrayDim;

        public ArrayAsI32ArraySerializer(int arrayDim) {
            this.arrayDim = arrayDim;
        }

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, int[] array) throws IOException {
            impl.serializeArrayBegin(message, name, arrayDim);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeI32(message, name, array[i]);
            }
            impl.serializeArrayEnd(message, name);
        }

        @Override
        public int[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeArrayBegin(message, name);
            final int[] array = new int[arrayDim];
            for (int i = 0; i < arrayDim; ++i) {
                array[i] = impl.deserializeI32(message, name);
            }
            impl.deserializeArrayEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsUI32ArraySerializer implements Serializer<int[]> {

        private final int arrayDim;

        public ArrayAsUI32ArraySerializer(int arrayDim) {
            this.arrayDim = arrayDim;
        }

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, int[] array) throws IOException {
            impl.serializeArrayBegin(message, name, arrayDim);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeUI32(message, name, array[i]);
            }
            impl.serializeArrayEnd(message, name);
        }

        @Override
        public int[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeArrayBegin(message, name);
            final int[] array = new int[arrayDim];
            for (int i = 0; i < arrayDim; ++i) {
                array[i] = impl.deserializeUI32(message, name);
            }
            impl.deserializeArrayEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsI64ArraySerializer implements Serializer<long[]> {

        private final int arrayDim;

        public ArrayAsI64ArraySerializer(int arrayDim) {
            this.arrayDim = arrayDim;
        }

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, long[] array) throws IOException {
            impl.serializeArrayBegin(message, name, arrayDim);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeI64(message, name, array[i]);
            }
            impl.serializeArrayEnd(message, name);
        }

        @Override
        public long[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeArrayBegin(message, name);
            final long[] array = new long[arrayDim];
            for (int i = 0; i < arrayDim; ++i) {
                array[i] = impl.deserializeI64(message, name);
            }
            impl.deserializeArrayEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsUI64ArraySerializer implements Serializer<long[]> {

        private final int arrayDim;

        public ArrayAsUI64ArraySerializer(int arrayDim) {
            this.arrayDim = arrayDim;
        }

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, long[] array) throws IOException {
            impl.serializeArrayBegin(message, name, arrayDim);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeUI64(message, name, array[i]);
            }
            impl.serializeArrayEnd(message, name);
        }

        @Override
        public long[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeArrayBegin(message, name);
            final long[] array = new long[arrayDim];
            for (int i = 0; i < arrayDim; ++i) {
                array[i] = impl.deserializeUI64(message, name);
            }
            impl.deserializeArrayEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsStringArraySerializer implements Serializer<java.lang.String[]> {

        private final int arrayDim;

        public ArrayAsStringArraySerializer(int arrayDim) {
            this.arrayDim = arrayDim;
        }

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, java.lang.String[] array) throws IOException {
            impl.serializeArrayBegin(message, name, arrayDim);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeString(message, name, array[i]);
            }
            impl.serializeArrayEnd(message, name);
        }

        @Override
        public java.lang.String[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeArrayBegin(message, name);
            final java.lang.String[] array = new java.lang.String[arrayDim];
            for (int i = 0; i < arrayDim; ++i) {
                array[i] = impl.deserializeString(message, name);
            }
            impl.deserializeArrayEnd(message, name);
            return array;
        }
    }

    public static class ArrayAsBooleanArraySerializer implements Serializer<boolean[]> {

        private final int arrayDim;

        public ArrayAsBooleanArraySerializer(int arrayDim) {
            this.arrayDim = arrayDim;
        }

        @Override
        public void write(SerializerImpl impl, BinaryOutputStream message, String name, boolean[] array) throws IOException {
            impl.serializeArrayBegin(message, name, arrayDim);
            impl.serializeI32(message, "", array.length);
            for (int i = 0; i < array.length; ++i) {
                impl.serializeBoolean(message, name, array[i]);
            }
            impl.serializeArrayEnd(message, name);
        }

        @Override
        public boolean[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
            impl.deserializeArrayBegin(message, name);
            final boolean[] array = new boolean[arrayDim];
            for (int i = 0; i < arrayDim; ++i) {
                array[i] = impl.deserializeBoolean(message, name);
            }
            impl.deserializeArrayEnd(message, name);
            return array;
        }
    }
}
