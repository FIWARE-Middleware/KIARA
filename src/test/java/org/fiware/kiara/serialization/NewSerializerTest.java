/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fiware.kiara.serialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.fiware.kiara.serialization.impl.ArrayAsArraySerializer;
import org.fiware.kiara.serialization.impl.ArrayAsSequenceSerializer;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.impl.IntArrayAsArraySerializer;
import org.fiware.kiara.serialization.impl.IntArrayAsSequenceSerializer;
import org.fiware.kiara.serialization.impl.IntegerSerializer;
import org.fiware.kiara.serialization.impl.ListAsArraySerializer;
import org.fiware.kiara.serialization.impl.ListAsSequenceSerializer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rubinste
 */
public class NewSerializerTest {

    private final CDRSerializer ser;

    public NewSerializerTest() {
        this.ser = new CDRSerializer();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void arraySerializationTest() throws IOException {

        // serialize int[2][3] = { {1,2,3}, {3,2,1} }
        List<List<Integer>> array = new ArrayList<>();
        array.add(Arrays.asList(1, 2, 3));
        array.add(Arrays.asList(3, 2, 1));

        org.fiware.kiara.serialization.impl.Serializer<List<List<Integer>>> s1
                = new ListAsArraySerializer<>(2, new ListAsArraySerializer<>(3, new IntegerSerializer()));

        BinaryOutputStream bos = new BinaryOutputStream();

        s1.write(ser, bos, "", array);

        // deserialize int[2][3]
        org.fiware.kiara.serialization.impl.Serializer<int[][]> s2 = new ArrayAsArraySerializer<>(2, int[].class, new IntArrayAsArraySerializer(3));

        BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
        int[][] result = s2.read(ser, bis, "");

        Assert.assertArrayEquals(new int[][]{{1, 2, 3}, {3, 2, 1}}, result);
    }

    @Test
    public void sequenceSerializationTest() throws IOException {

        // serialize list<list<int>> = { {1,2,3}, {3,2,1} }
        List<List<Integer>> array = new ArrayList<>();
        array.add(Arrays.asList(1, 2, 3));
        array.add(Arrays.asList(3, 2, 1));

        org.fiware.kiara.serialization.impl.Serializer<List<List<Integer>>> s1
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new IntegerSerializer()));

        BinaryOutputStream bos = new BinaryOutputStream();

        s1.write(ser, bos, "", array);

        // deserialize list<list<int>>
        org.fiware.kiara.serialization.impl.Serializer<int[][]> s2
                = new ArrayAsSequenceSerializer<>(int[].class, new IntArrayAsSequenceSerializer());

        BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
        int[][] result = s2.read(ser, bis, "");

        Assert.assertArrayEquals(new int[][]{{1, 2, 3}, {3, 2, 1}}, result);
    }

    @Test
    public void sequenceArraySerializationTest() throws IOException {

        // serialize list<int[3]> = { {1,2,3}, {3,2,1} }
        List<List<Integer>> array = new ArrayList<>();
        array.add(Arrays.asList(1, 2, 3));
        array.add(Arrays.asList(3, 2, 1));

        org.fiware.kiara.serialization.impl.Serializer<List<List<Integer>>> s1
                = new ListAsSequenceSerializer<>(new ListAsArraySerializer<>(3, new IntegerSerializer()));

        BinaryOutputStream bos = new BinaryOutputStream();

        s1.write(ser, bos, "", array);

        // deserialize list<int[3]>
        org.fiware.kiara.serialization.impl.Serializer<int[][]> s2
                = new ArrayAsSequenceSerializer<>(int[].class, new IntArrayAsArraySerializer(3));

        BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
        int[][] result = s2.read(ser, bis, "");

        Assert.assertArrayEquals(new int[][]{{1, 2, 3}, {3, 2, 1}}, result);
    }

}
