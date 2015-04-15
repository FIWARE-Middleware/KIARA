package org.fiware.kiara.dynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.fiware.kiara.Kiara;
import org.fiware.kiara.dynamic.data.DynamicArray;
import org.fiware.kiara.dynamic.data.DynamicList;
import org.fiware.kiara.dynamic.data.DynamicMap;
import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.dynamic.data.DynamicSet;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeDescriptorBuilderImpl;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.data.ListTypeDescriptor;
import org.fiware.kiara.typecode.data.MapTypeDescriptor;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.data.SetTypeDescriptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DynamicContainerTest {
    
    private CDRSerializer ser;
    BinaryOutputStream bos;
    BinaryInputStream bis;
    DynamicValueBuilder builder;
    TypeDescriptorBuilder tdbuilder;

    @Before
    public void init() {
        this.ser = new CDRSerializer();
        this.bos = new BinaryOutputStream();
        builder = Kiara.getDynamicValueBuilder();
        tdbuilder = Kiara.getTypeDescriptorBuilder();
    }

    @After
    public void detach() {
        //this.message.getPayload().clear();
    }

    public void reset() {
        this.bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
    }
    
    // ------------------ Data description & modification ---------------------------

    /*
     * standardArrayTest
     */
    @Test
    public void standardArrayTest() {
        
        PrimitiveTypeDescriptor primDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ArrayTypeDescriptor arrayDesc = (ArrayTypeDescriptor) tdbuilder.createArrayType(primDesc, 3, 5);
        
        int counter = 0;
        DynamicArray dynArray = (DynamicArray) builder.createData(arrayDesc);
        for (int i=0; i < 3; ++i) {
            for (int j=0; j < 5; ++j) {
                ((DynamicPrimitive) dynArray.getElementAt(i, j)).set(++counter);
            }
        }
        
        counter = 0;
        for (int i=0; i < 3; ++i) {
            for (int j=0; j < 5; ++j) {
                assertEquals(((DynamicPrimitive) dynArray.getElementAt(i, j)).get(), ++counter);
            }
        }
        
        reset();
    }
    
    /*
     * wrongDimAccessArrayTest
     */
    @Test
    public void wrongDimAccessArrayTest() {
        
        PrimitiveTypeDescriptor primDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ArrayTypeDescriptor arrayDesc = (ArrayTypeDescriptor) tdbuilder.createArrayType(primDesc, 3, 5);
        
        int counter = 0;
        DynamicArray dynArray = (DynamicArray) builder.createData(arrayDesc);
        try {
            for (int i=0; i < 4; ++i) {
                for (int j=0; j < 5; ++j) {
                    ((DynamicPrimitive) dynArray.getElementAt(i, j)).set(++counter);
                }
            }
            assertTrue(false);
        } catch (DynamicTypeException e) {
            assertTrue(true);
        }
        
        reset();
    }
    
    /*
     * standardListTest
     */
    @Test
    public void standardListTest() {
        
        PrimitiveTypeDescriptor primDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ListTypeDescriptor innerListDesc = tdbuilder.createListType(primDesc, 5);
        
        ListTypeDescriptor outerListDesc = tdbuilder.createListType(innerListDesc, 3);
        
        int counter = 0;
        DynamicList outerDynList = (DynamicList) builder.createData(outerListDesc);
        for (int i=0; i < 3; ++i) {
            DynamicList innerDynList = (DynamicList) builder.createData(innerListDesc);
            for (int j=0; j < 5; ++j) {
                DynamicPrimitive dynPrimitive = (DynamicPrimitive) builder.createData(primDesc);
                dynPrimitive.set(++counter);
                innerDynList.add(dynPrimitive);
            }
            outerDynList.add(innerDynList);
        }
        
        counter = 0;
        for (int i=0; i < 3; ++i) {
            for (int j=0; j < 5; ++j) {
                assertEquals(((DynamicPrimitive) ((DynamicList) ((DynamicList) outerDynList.get(i))).get(j)).get(), ++counter);
            }
        }
        
        reset();
    }
    
    // ------------------ Serialization tests ---------------------------
    
    /*
     * unidimArraySerializationTest
     */
    @Test
    public void unidimArraySerializationTest() {
        
        PrimitiveTypeDescriptor primDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ArrayTypeDescriptor arrayDesc = (ArrayTypeDescriptor) tdbuilder.createArrayType(primDesc, 5);
        
        int counter = 0;
        DynamicArray dynArray = (DynamicArray) builder.createData(arrayDesc);
        DynamicArray outputDynArray = (DynamicArray) builder.createData(arrayDesc);
        
        for (int i=0; i < 5; ++i) {
            ((DynamicPrimitive) dynArray.getElementAt(i)).set(++counter);
        }
        
        counter = 0;
        try {
            dynArray.serialize(ser, bos, "");
            reset();
            outputDynArray.deserialize(ser, bis, "");
            assertTrue(dynArray.equals(outputDynArray));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * multidimArraySerializationTest
     */
    @Test
    public void multidimArraySerializationTest() {
        
        PrimitiveTypeDescriptor primDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ArrayTypeDescriptor arrayDesc = (ArrayTypeDescriptor) tdbuilder.createArrayType(primDesc, 3, 5);
        
        int counter = 0;
        DynamicArray dynArray = (DynamicArray) builder.createData(arrayDesc);
        DynamicArray outputDynArray = (DynamicArray) builder.createData(arrayDesc);
        
        for (int i=0; i < 3; ++i) {
            for (int j=0; j < 5; ++j) {
                ((DynamicPrimitive) dynArray.getElementAt(i, j)).set(++counter);
            }
        }
        
        counter = 0;
        try {
            dynArray.serialize(ser, bos, "");
            reset();
            outputDynArray.deserialize(ser, bis, "");
            assertTrue(dynArray.equals(outputDynArray));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * unidimListSerializationTest
     */
    @Test
    public void unidimListSerializationTest() {
        
        PrimitiveTypeDescriptor primDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ListTypeDescriptor listDesc = (ListTypeDescriptor) tdbuilder.createListType(primDesc, 5);
        
        int counter = 0;
        DynamicList dynList = (DynamicList) builder.createData(listDesc);
        DynamicList outputDynList= (DynamicList) builder.createData(listDesc);
        
        for (int i=0; i < 5; ++i) {
            DynamicPrimitive dynPrimitive = (DynamicPrimitive) builder.createData(primDesc);
            dynPrimitive.set(++counter);
            dynList.add(dynPrimitive);
        }
        
        counter = 0;
        try {
            dynList.serialize(ser, bos, "");
            reset();
            outputDynList.deserialize(ser, bis, "");
            assertTrue(dynList.equals(outputDynList));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * multidimListSerializationTest
     */
    @Test
    public void multidimListSerializationTest() {
        
        PrimitiveTypeDescriptor primDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ListTypeDescriptor listDesc = (ListTypeDescriptor) tdbuilder.createListType(primDesc, 5);
        
        ListTypeDescriptor outerListDesc = (ListTypeDescriptor) tdbuilder.createListType(listDesc, 3);
        
        int counter = 0;
        DynamicList dynList = (DynamicList) builder.createData(outerListDesc);
        DynamicList outputDynList= (DynamicList) builder.createData(outerListDesc);
        
        for (int i=0; i < 3; ++i) {
            DynamicList innerDynList = (DynamicList) builder.createData(listDesc);
            for (int j=0; j < 5; ++j) {
                DynamicPrimitive dynPrimitive = (DynamicPrimitive) builder.createData(primDesc);
                dynPrimitive.set(++counter);
                innerDynList.add(dynPrimitive);
            }
            dynList.add(innerDynList);
        }
        
        counter = 0;
        try {
            dynList.serialize(ser, bos, "");
            reset();
            outputDynList.deserialize(ser, bis, "");
            assertTrue(dynList.equals(outputDynList));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * unidimSetSerializationTest
     */
    @Test
    public void unidimSetSerializationTest() {
        
        PrimitiveTypeDescriptor primDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        SetTypeDescriptor setDesc = (SetTypeDescriptor) tdbuilder.createSetType(primDesc, 5);
        
        int counter = 0;
        DynamicSet dynSet= (DynamicSet) builder.createData(setDesc);
        DynamicSet outputDynSet = (DynamicSet) builder.createData(setDesc);
        
        for (int i=0; i < 5; ++i) {
            DynamicPrimitive dynPrimitive = (DynamicPrimitive) builder.createData(primDesc);
            dynPrimitive.set(++counter);
            dynSet.add(dynPrimitive);
        }
        
        counter = 0;
        try {
            dynSet.serialize(ser, bos, "");
            reset();
            outputDynSet.deserialize(ser, bis, "");
            assertTrue(dynSet.equals(outputDynSet));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * multidimSetSerializationTest
     */
    @Test
    public void multidimSetSerializationTest() {
        
        PrimitiveTypeDescriptor primDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        SetTypeDescriptor setDesc = (SetTypeDescriptor) tdbuilder.createSetType(primDesc, 5);
        
        SetTypeDescriptor outerSetDesc = (SetTypeDescriptor) tdbuilder.createSetType(setDesc, 3);
        
        int counter = 0;
        DynamicSet dynSet = (DynamicSet) builder.createData(outerSetDesc);
        DynamicSet outputDynSet= (DynamicSet) builder.createData(outerSetDesc);
        
        for (int i=0; i < 3; ++i) {
            DynamicSet innerDynSet = (DynamicSet) builder.createData(setDesc);
            for (int j=0; j < 5; ++j) {
                DynamicPrimitive dynPrimitive = (DynamicPrimitive) builder.createData(primDesc);
                dynPrimitive.set(++counter);
                innerDynSet.add(dynPrimitive);
            }
            dynSet.add(innerDynSet);
        }
        
        counter = 0;
        try {
            dynSet.serialize(ser, bos, "");
            reset();
            outputDynSet.deserialize(ser, bis, "");
            assertTrue(dynSet.equals(outputDynSet));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * unidimMapSerializationTest
     */
    @Test
    public void unidimMapSerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor strDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE);
        
        MapTypeDescriptor mapDesc = (MapTypeDescriptor) tdbuilder.createMapType(intDesc, strDesc, 5);
        
        int counter = 0;
        DynamicMap dynMap= (DynamicMap) builder.createData(mapDesc);
        DynamicMap outputDynMap = (DynamicMap) builder.createData(mapDesc);
        
        for (int i=0; i < 5; ++i) {
            DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
            DynamicPrimitive dynStr= (DynamicPrimitive) builder.createData(strDesc);
            dynInt.set(++counter);
            dynStr.set("Hello World - "+counter);
            dynMap.put(dynInt, dynStr);
        }
        
        counter = 0;
        try {
            dynMap.serialize(ser, bos, "");
            reset();
            outputDynMap.deserialize(ser, bis, "");
            assertTrue(dynMap.equals(outputDynMap));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * multidimMapSerializationTest
     */
    @Test
    public void multidimMapSerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor strDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE);
        
        MapTypeDescriptor mapDesc = (MapTypeDescriptor) tdbuilder.createMapType(intDesc, strDesc, 5);
        
        MapTypeDescriptor outerMapDesc = (MapTypeDescriptor) tdbuilder.createMapType(mapDesc, intDesc, 3);
        
        int counter = 0;
        DynamicMap dynMap= (DynamicMap) builder.createData(outerMapDesc);
        DynamicMap outputDynMap = (DynamicMap) builder.createData(outerMapDesc);
        
        for (int i=0; i < 3; ++i) {
            DynamicMap innerDynMap = (DynamicMap) builder.createData(mapDesc);
            DynamicPrimitive outerDynInt = (DynamicPrimitive) builder.createData(intDesc);
            outerDynInt.set(i);
            for (int j=0; j < 5; ++j) {
                DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
                DynamicPrimitive dynStr= (DynamicPrimitive) builder.createData(strDesc);
                dynInt.set(++counter);
                dynStr.set("Hello World - "+counter);
                innerDynMap.put(dynInt, dynStr);
            }
            dynMap.put(innerDynMap, outerDynInt);
        }
        
        counter = 0;
        try {
            dynMap.serialize(ser, bos, "");
            reset();
            outputDynMap.deserialize(ser, bis, "");
            assertTrue(dynMap.equals(outputDynMap));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    // ----------------- Heterogeneous containers -----------------------
    
    /*
     * arrayListSerializationTest
     */
    @Test
    public void arrayListSerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ListTypeDescriptor innerDesc = (ListTypeDescriptor) tdbuilder.createListType(intDesc, 5);

        ArrayTypeDescriptor outerDesc = (ArrayTypeDescriptor) tdbuilder.createArrayType(innerDesc, 3, 4);
        
        int counter = 0;
        
        DynamicArray inputDyn = (DynamicArray) builder.createData(outerDesc);
        DynamicArray ouputDyn = (DynamicArray) builder.createData(outerDesc);
        
        for (int i=0; i < 3; ++i) {
            for (int j=0; j < 4; ++j) {
                DynamicList innerDyn = (DynamicList) builder.createData(innerDesc);
                for (int k=0; k < 5; ++k) {
                    DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
                    dynInt.set(++counter);
                    innerDyn.add(dynInt);
                }
                inputDyn.setElementAt(innerDyn, i, j);
            }
        }
        
        counter = 0;
        try {
            inputDyn.serialize(ser, bos, "");
            reset();
            ouputDyn.deserialize(ser, bis, "");
            assertTrue(inputDyn.equals(ouputDyn));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * arraySetSerializationTest
     */
    @Test
    public void arraySetSerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        SetTypeDescriptor innerDesc = (SetTypeDescriptor) tdbuilder.createSetType(intDesc, 5);

        ArrayTypeDescriptor outerDesc = (ArrayTypeDescriptor) tdbuilder.createArrayType(innerDesc, 3, 4);
        
        int counter = 0;
        
        DynamicArray inputDyn = (DynamicArray) builder.createData(outerDesc);
        DynamicArray ouputDyn = (DynamicArray) builder.createData(outerDesc);
        
        for (int i=0; i < 3; ++i) {
            for (int j=0; j < 4; ++j) {
                DynamicSet innerDyn = (DynamicSet) builder.createData(innerDesc);
                for (int k=0; k < 5; ++k) {
                    DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
                    dynInt.set(++counter);
                    innerDyn.add(dynInt);
                }
                inputDyn.setElementAt(innerDyn, i, j);
            }
        }
        
        counter = 0;
        try {
            inputDyn.serialize(ser, bos, "");
            reset();
            ouputDyn.deserialize(ser, bis, "");
            assertTrue(inputDyn.equals(ouputDyn));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * arrayMapSerializationTest
     */
    @Test
    public void arrayMapSerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor strDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE);
        
        MapTypeDescriptor innerDesc = (MapTypeDescriptor) tdbuilder.createMapType(intDesc, strDesc, 5);

        ArrayTypeDescriptor outerDesc = (ArrayTypeDescriptor) tdbuilder.createArrayType(innerDesc, 3, 4);
        
        int counter = 0;
        
        DynamicArray inputDyn = (DynamicArray) builder.createData(outerDesc);
        DynamicArray ouputDyn = (DynamicArray) builder.createData(outerDesc);
        
        for (int i=0; i < 3; ++i) {
            for (int j=0; j < 4; ++j) {
                DynamicMap innerDyn = (DynamicMap) builder.createData(innerDesc);
                for (int k=0; k < 5; ++k) {
                    DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
                    DynamicPrimitive dynStr = (DynamicPrimitive) builder.createData(strDesc);
                    dynInt.set(++counter);
                    dynStr.set("Hello " + counter);
                    innerDyn.put(dynInt, dynStr);
                }
                inputDyn.setElementAt(innerDyn, i, j);
            }
        }
        
        counter = 0;
        try {
            inputDyn.serialize(ser, bos, "");
            reset();
            ouputDyn.deserialize(ser, bis, "");
            assertTrue(inputDyn.equals(ouputDyn));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * listArraySerializationTest
     */
    @Test
    public void listArraySerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ArrayTypeDescriptor innerDesc = (ArrayTypeDescriptor) tdbuilder.createArrayType(intDesc, 4, 5);

        ListTypeDescriptor outerDesc = (ListTypeDescriptor) tdbuilder.createListType(innerDesc, 3);
        
        int counter = 0;
        
        DynamicList inputDyn = (DynamicList) builder.createData(outerDesc);
        DynamicList ouputDyn = (DynamicList) builder.createData(outerDesc);
        
        for (int i=0; i < 3; ++i) {
            DynamicArray innerDyn = (DynamicArray) builder.createData(innerDesc);
            for (int j=0; j < 4; ++j) {
                for (int k=0; k < 5; ++k) {
                    DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
                    dynInt.set(++counter);
                    innerDyn.setElementAt(dynInt, j, k);
                }
            }
            inputDyn.add(innerDyn);
        }
        
        counter = 0;
        try {
            inputDyn.serialize(ser, bos, "");
            reset();
            ouputDyn.deserialize(ser, bis, "");
            assertTrue(inputDyn.equals(ouputDyn));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * listSetSerializationTest
     */
    @Test
    public void listSetSerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        SetTypeDescriptor innerDesc = (SetTypeDescriptor) tdbuilder.createSetType(intDesc, 4);

        ListTypeDescriptor outerDesc = (ListTypeDescriptor) tdbuilder.createListType(innerDesc, 3);
        
        int counter = 0;
        
        DynamicList inputDyn = (DynamicList) builder.createData(outerDesc);
        DynamicList ouputDyn = (DynamicList) builder.createData(outerDesc);
        
        for (int i=0; i < 3; ++i) {
            DynamicSet innerDyn = (DynamicSet) builder.createData(innerDesc);
            for (int j=0; j < 4; ++j) {
                DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
                dynInt.set(++counter);
                innerDyn.add(dynInt);
            }
            inputDyn.add(innerDyn);
        }
        
        counter = 0;
        try {
            inputDyn.serialize(ser, bos, "");
            reset();
            ouputDyn.deserialize(ser, bis, "");
            assertTrue(inputDyn.equals(ouputDyn));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * listMapSerializationTest
     */
    @Test
    public void listMapSerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor strDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE);

        MapTypeDescriptor innerDesc = (MapTypeDescriptor) tdbuilder.createMapType(intDesc, strDesc, 4);

        ListTypeDescriptor outerDesc = (ListTypeDescriptor) tdbuilder.createListType(innerDesc, 3);
        
        int counter = 0;
        
        DynamicList inputDyn = (DynamicList) builder.createData(outerDesc);
        DynamicList ouputDyn = (DynamicList) builder.createData(outerDesc);
        
        for (int i=0; i < 3; ++i) {
            DynamicMap innerDyn = (DynamicMap) builder.createData(innerDesc);
            for (int j=0; j < 4; ++j) {
                DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
                DynamicPrimitive dynStr = (DynamicPrimitive) builder.createData(strDesc);
                dynInt.set(++counter);
                dynStr.set("Hello " + counter);
                innerDyn.put(dynInt, dynStr);
            }
            inputDyn.add(innerDyn);
        }
        
        counter = 0;
        try {
            inputDyn.serialize(ser, bos, "");
            reset();
            ouputDyn.deserialize(ser, bis, "");
            assertTrue(inputDyn.equals(ouputDyn));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * setArraySerializationTest
     */
    @Test
    public void setArraySerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ArrayTypeDescriptor innerDesc = (ArrayTypeDescriptor) tdbuilder.createArrayType(intDesc, 4, 5);

        SetTypeDescriptor outerDesc = (SetTypeDescriptor) tdbuilder.createSetType(innerDesc, 3);
        
        int counter = 0;
        
        DynamicSet inputDyn = (DynamicSet) builder.createData(outerDesc);
        DynamicSet ouputDyn = (DynamicSet) builder.createData(outerDesc);
        
        for (int i=0; i < 3; ++i) {
            DynamicArray innerDyn = (DynamicArray) builder.createData(innerDesc);
            for (int j=0; j < 4; ++j) {
                for (int k=0; k < 5; ++k) {
                    DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
                    dynInt.set(++counter);
                    innerDyn.setElementAt(dynInt, j, k);
                }
            }
            inputDyn.add(innerDyn);
        }
        
        counter = 0;
        try {
            inputDyn.serialize(ser, bos, "");
            reset();
            ouputDyn.deserialize(ser, bis, "");
            assertTrue(inputDyn.equals(ouputDyn));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * setListSerializationTest
     */
    @Test
    public void setListSerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ListTypeDescriptor innerDesc = (ListTypeDescriptor) tdbuilder.createListType(intDesc, 4);

        SetTypeDescriptor outerDesc = (SetTypeDescriptor) tdbuilder.createSetType(innerDesc, 3);
        
        int counter = 0;
        
        DynamicSet inputDyn = (DynamicSet) builder.createData(outerDesc);
        DynamicSet ouputDyn = (DynamicSet) builder.createData(outerDesc);
        
        for (int i=0; i < 3; ++i) {
            DynamicList innerDyn = (DynamicList) builder.createData(innerDesc);
            for (int j=0; j < 4; ++j) {
                DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
                dynInt.set(++counter);
                innerDyn.add(dynInt);
            }
            inputDyn.add(innerDyn);
        }
        
        counter = 0;
        try {
            inputDyn.serialize(ser, bos, "");
            reset();
            ouputDyn.deserialize(ser, bis, "");
            assertTrue(inputDyn.equals(ouputDyn));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * setMapSerializationTest
     */
    @Test
    public void setMapSerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor strDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE);

        MapTypeDescriptor innerDesc = (MapTypeDescriptor) tdbuilder.createMapType(intDesc, strDesc, 4);

        SetTypeDescriptor outerDesc = (SetTypeDescriptor) tdbuilder.createSetType(innerDesc, 3);
        
        int counter = 0;
        
        DynamicSet inputDyn = (DynamicSet) builder.createData(outerDesc);
        DynamicSet ouputDyn = (DynamicSet) builder.createData(outerDesc);
        
        for (int i=0; i < 3; ++i) {
            DynamicMap innerDyn = (DynamicMap) builder.createData(innerDesc);
            for (int j=0; j < 4; ++j) {
                DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
                DynamicPrimitive dynStr = (DynamicPrimitive) builder.createData(strDesc);
                dynInt.set(++counter);
                dynStr.set("Hello " + counter);
                innerDyn.put(dynInt, dynStr);
            }
            inputDyn.add(innerDyn);
        }
        
        counter = 0;
        try {
            inputDyn.serialize(ser, bos, "");
            reset();
            ouputDyn.deserialize(ser, bis, "");
            assertTrue(inputDyn.equals(ouputDyn));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * mapArraySerializationTest
     */
    @Test
    public void mapArraySerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ArrayTypeDescriptor innerDesc = (ArrayTypeDescriptor) tdbuilder.createArrayType(intDesc, 4, 5);

        MapTypeDescriptor outerDesc = (MapTypeDescriptor) tdbuilder.createMapType(intDesc, innerDesc, 3);
        
        int counter = 0;
        
        DynamicMap inputDyn = (DynamicMap) builder.createData(outerDesc);
        DynamicMap ouputDyn = (DynamicMap) builder.createData(outerDesc);
        
        for (int i=0; i < 3; ++i) {
            DynamicArray innerDyn = (DynamicArray) builder.createData(innerDesc);
            DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
            dynInt.set(++counter);
            for (int j=0; j < 4; ++j) {
                for (int k=0; k < 5; ++k) {
                    innerDyn.setElementAt(dynInt, j, k);
                }
            }
            inputDyn.put(dynInt, innerDyn);
        }
        
        counter = 0;
        try {
            inputDyn.serialize(ser, bos, "");
            reset();
            ouputDyn.deserialize(ser, bis, "");
            assertTrue(inputDyn.equals(ouputDyn));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * mapListSerializationTest
     */
    @Test
    public void mapListSerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ListTypeDescriptor innerDesc = (ListTypeDescriptor) tdbuilder.createListType(intDesc, 4);

        MapTypeDescriptor outerDesc = (MapTypeDescriptor) tdbuilder.createMapType(intDesc, innerDesc, 3);
        
        int counter = 0;
        
        DynamicMap inputDyn = (DynamicMap) builder.createData(outerDesc);
        DynamicMap ouputDyn = (DynamicMap) builder.createData(outerDesc);
        
        for (int i=0; i < 3; ++i) {
            DynamicList innerDyn = (DynamicList) builder.createData(innerDesc);
            DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
            dynInt.set(++counter);
            for (int j=0; j < 4; ++j) {
                innerDyn.add(dynInt);
            }
            inputDyn.put(dynInt, innerDyn);
        }
        
        counter = 0;
        try {
            inputDyn.serialize(ser, bos, "");
            reset();
            ouputDyn.deserialize(ser, bis, "");
            assertTrue(inputDyn.equals(ouputDyn));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    /*
     * mapSetSerializationTest
     */
    @Test
    public void mapSetSerializationTest() {
        
        PrimitiveTypeDescriptor intDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        SetTypeDescriptor innerDesc = (SetTypeDescriptor) tdbuilder.createSetType(intDesc, 4);

        MapTypeDescriptor outerDesc = (MapTypeDescriptor) tdbuilder.createMapType(intDesc, innerDesc, 3);
        
        int counter = 0;
        int keyVal = 0;
        
        DynamicMap inputDyn = (DynamicMap) builder.createData(outerDesc);
        DynamicMap ouputDyn = (DynamicMap) builder.createData(outerDesc);
        
        for (int i=0; i < 3; ++i) {
            DynamicSet innerDyn = (DynamicSet) builder.createData(innerDesc);
            DynamicPrimitive key = (DynamicPrimitive) builder.createData(intDesc);
            key.set(++keyVal);
            for (int j=0; j < 4; ++j) {
                DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
                dynInt.set(++counter);
                innerDyn.add(dynInt);
            }
            inputDyn.put(key, innerDyn);
        }
        
        counter = 0;
        try {
            inputDyn.serialize(ser, bos, "");
            reset();
            ouputDyn.deserialize(ser, bis, "");
            assertTrue(inputDyn.equals(ouputDyn));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
}
