package org.fiware.kiara.dynamic;

import org.fiware.kiara.dynamic.data.DynamicArray;
import org.fiware.kiara.dynamic.data.DynamicList;
import org.fiware.kiara.dynamic.data.DynamicMap;
import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.dynamic.impl.DynamicTypeBuilderImpl;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.ListTypeDescriptor;
import org.fiware.kiara.typecode.data.MapTypeDescriptor;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.data.SetTypeDescriptor;
import org.fiware.kiara.typecode.impl.TypeDescriptorBuilderImpl;
import org.fiware.kiara.typecode.impl.data.ArrayTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.MapTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.PrimitiveTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.ListTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.SetTypeDescriptorImpl;

public class DynamicTest {
    
    public static void main(String [] args) {
        
        /*
        // Get TypeDescriptor factory instance
        TypeDescriptorBuilder descriptorBuilder = TypeDescriptorBuilderImpl.getInstance();
        
        // Create data
        PrimitiveTypeDescriptor intDescriptor = 
                 (PrimitiveTypeDescriptor) descriptorBuilder.createTypeDescriptor(TypeKind.INT_32_TYPE, "");
        
        ArrayTypeDescriptor arrayDescriptor =
                (ArrayTypeDescriptor) descriptorBuilder.createTypeDescriptor(TypeKind.ARRAY_TYPE, "");
        
        arrayDescriptor.setContentType(intDescriptor);
        arrayDescriptor.setDimensions(3, 2);
        
        // Get DynamicType factory instance
        DynamicTypeBuilder dynamicTypeBuilder = DynamicTypeBuilderImpl.getInstance();
        
        // Set values
        DynamicArray dynArray = 
                (DynamicArray) dynamicTypeBuilder.createData(arrayDescriptor);
        
        int value = 0;
        for (int rows = 0; rows < 3; ++rows) {
            for (int columns = 0; columns < 2; ++columns) {
                DynamicPrimitive temp = 
                        (DynamicPrimitive) ((DynamicArray) dynArray.getElementAt(rows)).getElementAt(columns);
                temp.set(value);
                value++;
            }
        }*/
        
        /*DataTypeDescriptor stringDesc = new PrimitiveTypeDescriptor(TypeKind.STRING_TYPE, "");
        stringDesc.setMaxFixedLength(6);
        
        DynamicPrimitive intType1 = (DynamicPrimitive) builder.createData(new PrimitiveTypeDescriptor(TypeKind.INT_32_TYPE, ""));
        DynamicPrimitive stringType = (DynamicPrimitive) builder.createData(stringDesc);
        
        stringType.set("Hello!");
        
        intType1.set(3);*/
        
        // primitive equals
        
        /*
        DataTypeDescriptor stringDesc = new PrimitiveTypeDescriptor(TypeKind.STRING_TYPE, "");
        DynamicPrimitive dynStr1 = (DynamicPrimitive) builder.createData(stringDesc);
        DynamicPrimitive dynStr2 = (DynamicPrimitive) builder.createData(stringDesc);
        DynamicPrimitive dynStr3 = (DynamicPrimitive) builder.createData(stringDesc);
        dynStr1.set("One");
        dynStr2.set("Two");
        dynStr3.set("Two");
        
        System.out.println("STR1.equals(STR2): " + dynStr1.equals(dynStr2));
        System.out.println("STR1.equals(STR3): " + dynStr1.equals(dynStr3));
        System.out.println("STR2.equals(STR3): " + dynStr2.equals(dynStr2));
        */
        
        // Array equals
        
        /*
        ArrayTypeDescriptor arrayDesc = new ArrayTypeDescriptor(3, 5);
        arrayDesc.setContentType(new PrimitiveTypeDescriptor(TypeKind.INT_32_TYPE, "myInt"));
        DynamicPrimitive intType1 = (DynamicPrimitive) builder.createData(new PrimitiveTypeDescriptor(TypeKind.INT_32_TYPE, ""));
        
        DynamicArray dyn_array = (DynamicArray) builder.createData(arrayDesc);
        //dyn_array.setElementAt(intType1, 1, 2);
        DynamicArray dyn_array2 = (DynamicArray) builder.createData(arrayDesc);
       // dyn_array2.setElementAt(intType1, 1, 2);
        //dyn_array.setElementAt(4, 1, 2);
        ((DynamicPrimitive) dyn_array.getElementAt(0, 3)).set(5);
        ((DynamicPrimitive) dyn_array2.getElementAt(0, 3)).set(6);
        if (dyn_array.equals(dyn_array2)) {
            System.out.println("Equals");
        } else {
            System.out.println("NOT Equals");
        }
        
        */
        
        
        
        //TestIfz test = new Test();


        //ArrayTypeDescriptor stringArray = new ArrayTypeDescriptor(3, 5, 7);
        
        
        
        /*
        DataTypeDescriptor listDescriptor = new SequenceTypeDescriptor();
        DataTypeDescriptor intDesc = new PrimitiveTypeDescriptor(TypeKind.INT_32_TYPE, "");
        listDescriptor.setContentType(intDesc);
        listDescriptor.setMaxSize(10);
        
        DynamicList list = (DynamicList) builder.createData(listDescriptor);
        for (int i=0; i < 10; ++i) {
            ((DynamicPrimitive) list.getElementAt(i)).set(i);
        }
        */
        
        // Lit of multidimArray
        
        /*
        DataTypeDescriptor listDescriptor = new SequenceTypeDescriptor();
        ArrayTypeDescriptor arrayDesc = new ArrayTypeDescriptor(3, 5);
        DataTypeDescriptor intDesc = new PrimitiveTypeDescriptor(TypeKind.INT_32_TYPE, "");
        arrayDesc.setContentType(intDesc);
        arrayDesc.setDimensions(3, 5, 2);
        listDescriptor.setMaxSize(10);
        listDescriptor.setContentType(arrayDesc);
        
        DynamicList list = (DynamicList) builder.createData(listDescriptor);
        for (int i=0; i < 10; ++i) {
            for(int j=0; j < 3; ++j) {
                for (int k=0; k < 5; ++k) {
                    for (int l=0; l < 2; ++l) {
                        ((DynamicPrimitive)((DynamicArray) list.getElementAt(i)).getElementAt(j, k, l)).set(5);
                    }
                }
            }
        }
        */
        
        // Sets
        
        /*
        DataTypeDescriptor setDescriptor = new SetTypeDescriptor();
        DataTypeDescriptor intDesc = new PrimitiveTypeDescriptor(TypeKind.INT_32_TYPE, "");
        setDescriptor.setMaxSize(10);
        setDescriptor.setContentType(intDesc);
        
        DynamicSet set = (DynamicSet) builder.createData(setDescriptor);
        for (int i=0; i < 10; ++i) {
            //DynamicPrimitive temp = (DynamicPrimitive) builder.createData(intDesc);
            //temp.set(i);
            ((DynamicPrimitive) set.getElementAt(i)).set(i);
        }
        
        ((DynamicPrimitive) set.getElementAt(5)).set(0);
        ((DynamicPrimitive) set.getElementAt(5)).set(20);
        ((DynamicPrimitive) set.getElementAt(0)).set(0);
        ((DynamicPrimitive) set.getElementAt(9)).set(3);
        */
        
        // Maps
        /*
        DataTypeDescriptor intDesc = new PrimitiveTypeDescriptor(TypeKind.INT_32_TYPE, "");
        DataTypeDescriptor stringDesc = new PrimitiveTypeDescriptor(TypeKind.STRING_TYPE, "");
        DataTypeDescriptor mapDesc = new MapTypeDescriptor();
        mapDesc.setKeyTypeDescriptor(intDesc);
        mapDesc.setValueTypeDescriptor(stringDesc);
        mapDesc.setMaxSize(5);
        DynamicMap dynMap = (DynamicMap) builder.createData(mapDesc);
        
        for (int i=0; i < 5; ++i) {
            ((DynamicPrimitive) dynMap.getKeyAt(i)).set(i);
            ((DynamicPrimitive) dynMap.getValueAt(i)).set("String " + i);
        }
        
        DynamicPrimitive intVal = (DynamicPrimitive) builder.createData(intDesc);
        intVal.set(3);
        ((DynamicPrimitive) dynMap.getValue(intVal)).set("Changed String");
        ((DynamicPrimitive) dynMap.getKeyAt(3)).set(4);
        ((DynamicPrimitive) dynMap.getKeyAt(3)).set(3);
        */
        
        /*TypeDescriptorBuilder tdbuilder = TypeDescriptorBuilderImpl.getInstance();
        DynamicTypeBuilder dynamicTypeBuilder = DynamicTypeBuilderImpl.getInstance();
        
        ArrayTypeDescriptorImpl arrayDesc = (ArrayTypeDescriptorImpl) tdbuilder.createTypeDescriptor(TypeKind.ARRAY_TYPE, "");
        arrayDesc.setContentType((DataTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.INT_32_TYPE, ""));
        arrayDesc.setDimensions(2);
        
        SetTypeDescriptor setDesc =  (SetTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.SET_TYPE, "");
        setDesc.setContentType((DataTypeDescriptor) arrayDesc);
        setDesc.setMaxSize(3);
        
        SetTypeDescriptor outerSetDesc = (SetTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.SET_TYPE, "");
        outerSetDesc.setContentType((DataTypeDescriptor) setDesc);
        outerSetDesc.setMaxSize(3);
        
        //DynamicArray dynArray = (DynamicArray) builder.createData(arrayDesc);
        
        DynamicSet dynSet = (DynamicSet) dynamicTypeBuilder.createData((DataTypeDescriptor) outerSetDesc);
        int counter = 0;
        for(int m=0; m < 3; ++m) {
            for(int i=0; i < 3; ++i) {
                for (int j = 0; j < 2; ++j) {
                    ((DynamicPrimitive) ((DynamicArray) ((DynamicSet) dynSet.getElementAt(m)).getElementAt(i)).getElementAt(j)).set(++counter);
                    
                }
            }
        }*/
        
        
        /*TypeDescriptorBuilder tdbuilder = TypeDescriptorBuilderImpl.getInstance();
        DynamicTypeBuilder dynamicTypeBuilder = DynamicTypeBuilderImpl.getInstance();
        
        ArrayTypeDescriptorImpl arrayDesc = (ArrayTypeDescriptorImpl) tdbuilder.createTypeDescriptor(TypeKind.ARRAY_TYPE, "");
        arrayDesc.setContentType((DataTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.INT_32_TYPE, ""));
        arrayDesc.setDimensions(2);
        
        ListTypeDescriptor listDesc =  (ListTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.LIST_TYPE, "");
        listDesc.setContentType((DataTypeDescriptor) arrayDesc);
        listDesc.setMaxSize(3);
        
        SetTypeDescriptor outerSetDesc = (SetTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.SET_TYPE, "");
        outerSetDesc.setContentType((DataTypeDescriptor) listDesc);
        outerSetDesc.setMaxSize(3);
        
        //DynamicArray dynArray = (DynamicArray) builder.createData(arrayDesc);
        
        DynamicSet dynSet = (DynamicSet) dynamicTypeBuilder.createData((DataTypeDescriptor) outerSetDesc);
        int counter = 0;
        for(int m=0; m < 3; ++m) {
            for(int i=0; i < 3; ++i) {
                for (int j = 0; j < 2; ++j) {
                    ((DynamicPrimitive) ((DynamicArray) ((DynamicList) dynSet.getElementAt(m)).getElementAt(i)).getElementAt(j)).set(++counter);
                    
                }
            }
        }*/
        
        TypeDescriptorBuilder tdbuilder = TypeDescriptorBuilderImpl.getInstance();
        DynamicTypeBuilder dynamicTypeBuilder = DynamicTypeBuilderImpl.getInstance();
        
        ArrayTypeDescriptorImpl arrayDesc = (ArrayTypeDescriptorImpl) tdbuilder.createTypeDescriptor(TypeKind.ARRAY_TYPE, "");
        arrayDesc.setContentType((DataTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.INT_32_TYPE, ""));
        arrayDesc.setDimensions(2);
        
        ListTypeDescriptor listDesc =  (ListTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.LIST_TYPE, "");
        listDesc.setContentType((DataTypeDescriptor) arrayDesc);
        listDesc.setMaxSize(3);
        
        MapTypeDescriptor outerSetDesc = (MapTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.MAP_TYPE, "");
        outerSetDesc.setKeyTypeDescriptor((DataTypeDescriptor) listDesc);
        outerSetDesc.setValueTypeDescriptor((DataTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.INT_32_TYPE, ""));
        outerSetDesc.setMaxSize(3);
        
        //DynamicArray dynArray = (DynamicArray) builder.createData(arrayDesc);
        
        DynamicMap dynSet = (DynamicMap) dynamicTypeBuilder.createData((DataTypeDescriptor) outerSetDesc);
        int counter = 0;
        for(int m=0; m < 3; ++m) {
            for(int i=0; i < 3; ++i) {
                for (int j = 0; j < 2; ++j) {
                    ((DynamicPrimitive) ((DynamicArray) ((DynamicList) dynSet.getKeyAt(m)).getElementAt(i)).getElementAt(j)).set(++counter);
                    
                }
            }
        }
        
        counter = 0;
        for(int m=0; m < 3; ++m) {
            for(int i=0; i < 3; ++i) {
                for (int j = 0; j < 2; ++j) {
                    ((DynamicPrimitive) ((DynamicArray) ((DynamicList) dynSet.getKeyAt(m)).getElementAt(i)).getElementAt(j)).set(33);
                    
                }
            }
        }
        
        
        System.out.println("");

    }

}
