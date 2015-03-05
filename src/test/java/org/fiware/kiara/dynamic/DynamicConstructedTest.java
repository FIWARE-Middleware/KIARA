package org.fiware.kiara.dynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.fiware.kiara.dynamic.data.DynamicArray;
import org.fiware.kiara.dynamic.data.DynamicList;
import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.serialization.MockTransportMessage;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeDescriptorBuilderImpl;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.data.ListTypeDescriptor;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.PrimitiveTypeDescriptorImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DynamicConstructedTest {
    
    private CDRSerializer ser;
    private ByteBuffer buffer;
    private TransportMessage message;
    DynamicTypeBuilder builder;
    TypeDescriptorBuilder tdbuilder;

    @Before
    public void init() {
        this.ser = new CDRSerializer();
        this.buffer = ByteBuffer.allocate(500);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.message = new MockTransportMessage(buffer);
        builder = DynamicTypeBuilderImpl.getInstance();
        tdbuilder = TypeDescriptorBuilderImpl.getInstance();
    }

    @After
    public void detach() {
        this.message.getPayload().clear();
    }

    public void reset() {
        this.message.getPayload().clear();
    }

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
                //((DynamicPrimitive) ((DynamicList) ((DynamicList) dynList.getElementAt(i))).getElementAt(j)).set(++counter);
                DynamicPrimitive dynPrimitive = (DynamicPrimitive) builder.createData(primDesc);
                dynPrimitive.set(++counter);
                innerDynList.add(dynPrimitive);
            }
            outerDynList.add(innerDynList);
        }
        
        
        /*for (int i=0; i < 3; ++i) {
            for (int j=0; j < 5; ++j) {
                //((DynamicPrimitive) ((DynamicList) ((DynamicList) dynList.getElementAt(i))).getElementAt(j)).set(++counter);
                //dynList.add();
            }
        }*/
        
        counter = 0;
        for (int i=0; i < 3; ++i) {
            for (int j=0; j < 5; ++j) {
                assertEquals(((DynamicPrimitive) ((DynamicList) ((DynamicList) outerDynList.get(i))).get(j)).get(), ++counter);
            }
        }
        
        reset();
    }
    
    /*
     * wrongDimAccessListTest
     */
    /*@Test
    public void wrongDimAccessListTest() {
        
        PrimitiveTypeDescriptor primDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        
        ListTypeDescriptor listDesc = (ListTypeDescriptor) tdbuilder.createListType(primDesc, 5);
        listDesc.setContentType(primDesc);
        listDesc.setMaxSize(5);
        
        ListTypeDescriptor outerListDesc = (ListTypeDescriptor) tdbuilder.createListType(listDesc, 3);
        outerListDesc.setContentType(listDesc);
        outerListDesc.setMaxSize(3);
        
        int counter = 0;
        DynamicList dynList = (DynamicList) builder.createData(outerListDesc);
        try {
            for (int i=0; i < 4; ++i) {
                for (int j=0; j < 5; ++j) {
                    ((DynamicPrimitive) ((DynamicList) ((DynamicList) dynList.getElementAt(i))).getElementAt(j)).set(++counter);
                }
            }
            assertTrue(false);
        } catch (DynamicTypeException e) {
            assertTrue(true);
        }
        
        reset();
    }*/
    
    
}
