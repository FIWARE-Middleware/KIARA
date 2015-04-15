package org.fiware.kiara.dynamic;

import static org.junit.Assert.*;

import java.io.IOException;

import org.fiware.kiara.Kiara;
import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.dynamic.services.DynamicFunctionRequest;
import org.fiware.kiara.dynamic.services.DynamicFunctionResponse;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeDescriptorBuilderImpl;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.ExceptionTypeDescriptor;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.impl.FunctionTypeDescriptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DynamicFunctionsTest {
    
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
    
    @Test
    public void NR_NP_NE_FunctionDescription() {
        try {
            tdbuilder.createFunctionType("add");
            assertTrue(true);
        } catch (TypeDescriptorException tde) {
            assertTrue(false);
        } catch (DynamicTypeException dte) {
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void NR_NP_E_FunctionDescription() {
        try {
            FunctionTypeDescriptor functionDesc = tdbuilder.createFunctionType("add");
            ExceptionTypeDescriptor exceptionDesc = tdbuilder.createExceptionType("MyException");
            functionDesc.addException(exceptionDesc);
            assertTrue(true);
        } catch (TypeDescriptorException tde) {
            assertTrue(false);
        } catch (DynamicTypeException dte) {
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void NR_P_NE_FunctionDescription() {
        try {
            FunctionTypeDescriptor functionDesc = tdbuilder.createFunctionType("add");
            PrimitiveTypeDescriptor floatDesc = tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE);
            functionDesc.addParameter(floatDesc, "n1");
            functionDesc.addParameter(floatDesc, "n2");
            assertTrue(true);
        } catch (TypeDescriptorException tde) {
            assertTrue(false);
        } catch (DynamicTypeException dte) {
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void NR_P_E_FunctionDescription() {
        try {
            FunctionTypeDescriptor functionDesc = tdbuilder.createFunctionType("add");
            ExceptionTypeDescriptor exceptionDesc = tdbuilder.createExceptionType("MyException");
            PrimitiveTypeDescriptor floatDesc = tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE);
            functionDesc.addParameter(floatDesc, "n1");
            functionDesc.addParameter(floatDesc, "n2");
            functionDesc.addException(exceptionDesc);
            assertTrue(true);
        } catch (TypeDescriptorException tde) {
            assertTrue(false);
        } catch (DynamicTypeException dte) {
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void R_NP_NE_FunctionDescription() {
        try {
            FunctionTypeDescriptor functionDesc = tdbuilder.createFunctionType("add");
            PrimitiveTypeDescriptor floatDesc = tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE);
            functionDesc.setReturnType(floatDesc);
            assertTrue(true);
        } catch (TypeDescriptorException tde) {
            assertTrue(false);
        } catch (DynamicTypeException dte) {
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void R_NP_E_FunctionDescription() {
        try {
            FunctionTypeDescriptor functionDesc = tdbuilder.createFunctionType("add");
            ExceptionTypeDescriptor exceptionDesc = tdbuilder.createExceptionType("MyException");
            PrimitiveTypeDescriptor floatDesc = tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE);
            functionDesc.addException(exceptionDesc);
            functionDesc.setReturnType(floatDesc);
            assertTrue(true);
        } catch (TypeDescriptorException tde) {
            assertTrue(false);
        } catch (DynamicTypeException dte) {
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void R_P_NE_FunctionDescription() {
        try {
            FunctionTypeDescriptor functionDesc = tdbuilder.createFunctionType("add");
            PrimitiveTypeDescriptor floatDesc = tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE);
            functionDesc.addParameter(floatDesc, "n1");
            functionDesc.addParameter(floatDesc, "n2");
            functionDesc.setReturnType(floatDesc);
            assertTrue(true);
        } catch (TypeDescriptorException tde) {
            assertTrue(false);
        } catch (DynamicTypeException dte) {
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void R_P_E_FunctionDescription() {
        try {
            FunctionTypeDescriptor functionDesc = tdbuilder.createFunctionType("add");
            ExceptionTypeDescriptor exceptionDesc = tdbuilder.createExceptionType("MyException");
            PrimitiveTypeDescriptor floatDesc = tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE);
            functionDesc.addParameter(floatDesc, "n1");
            functionDesc.addParameter(floatDesc, "n2");
            functionDesc.addException(exceptionDesc);
            assertTrue(true);
        } catch (TypeDescriptorException tde) {
            assertTrue(false);
        } catch (DynamicTypeException dte) {
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void simpleDynamicFunction() {
        try {
            FunctionTypeDescriptor functionDesc = tdbuilder.createFunctionType("add");
            ExceptionTypeDescriptor exceptionDesc = tdbuilder.createExceptionType("MyException");
            PrimitiveTypeDescriptor floatDesc = tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE);
            functionDesc.addParameter(floatDesc, "n1");
            functionDesc.addParameter(floatDesc, "n2");
            functionDesc.addException(exceptionDesc);
            
            DynamicFunctionRequest dynFunctionReq = builder.createFunctionRequest(functionDesc);
            DynamicFunctionResponse dynFunctionRes = builder.createFunctionResponse(functionDesc);
            assertTrue(dynFunctionReq != null && dynFunctionRes != null);
        } catch (DynamicTypeException dte) {
            assertTrue(false);
        }
        
    }
    
    @Test
    public void exceptionDynamicFunction() {
        try {
            FunctionTypeDescriptor functionDesc = tdbuilder.createFunctionType("add");
            PrimitiveTypeDescriptor floatDesc = tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE);
            functionDesc.addParameter(floatDesc, "n1");
            functionDesc.addParameter(floatDesc, "n2");
            
            DynamicFunctionRequest dynFunctionReq = builder.createFunctionRequest(functionDesc);
            DynamicFunctionResponse dynFunctionRes = builder.createFunctionResponse(functionDesc);
            assertTrue(dynFunctionReq != null && dynFunctionRes != null);
        } catch (DynamicTypeException dte) {
            assertTrue(false);
        }
        
    }
    
    @Test
    public void functionRequestPrimitiveParamsSerialization() {
        try {
            FunctionTypeDescriptor functionDesc = tdbuilder.createFunctionType("add");
            PrimitiveTypeDescriptor charDesc = tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE);
            PrimitiveTypeDescriptor byteDesc = tdbuilder.createPrimitiveType(TypeKind.BYTE_TYPE);
            PrimitiveTypeDescriptor shortDesc = tdbuilder.createPrimitiveType(TypeKind.INT_16_TYPE);
            PrimitiveTypeDescriptor ushortDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_16_TYPE);
            PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
            PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);
            PrimitiveTypeDescriptor longDesc = tdbuilder.createPrimitiveType(TypeKind.INT_64_TYPE);
            PrimitiveTypeDescriptor ulongDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_64_TYPE);
            PrimitiveTypeDescriptor floatDesc = tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE);
            PrimitiveTypeDescriptor doubleDesc = tdbuilder.createPrimitiveType(TypeKind.FLOAT_64_TYPE);
            PrimitiveTypeDescriptor booleanDesc = tdbuilder.createPrimitiveType(TypeKind.BOOLEAN_TYPE);
            PrimitiveTypeDescriptor stringDesc = tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE);
            functionDesc.addParameter(charDesc, "param1");
            functionDesc.addParameter(byteDesc, "param2");
            functionDesc.addParameter(shortDesc, "param3");
            functionDesc.addParameter(ushortDesc, "param4");
            functionDesc.addParameter(intDesc, "param5");
            functionDesc.addParameter(uintDesc, "param6");
            functionDesc.addParameter(longDesc, "param7");
            functionDesc.addParameter(ulongDesc, "param8");
            functionDesc.addParameter(floatDesc, "param9");
            functionDesc.addParameter(doubleDesc, "param10");
            functionDesc.addParameter(booleanDesc, "param11");
            functionDesc.addParameter(stringDesc, "param12");
            
            DynamicFunctionRequest dynFunctionReq = builder.createFunctionRequest(functionDesc);
            DynamicFunctionRequest outputDynFunctionReq = builder.createFunctionRequest(functionDesc);
            
            ((DynamicPrimitive) dynFunctionReq.getParameter("param1")).set('S');
            dynFunctionReq.serialize(ser, bos, "");
            reset();
            outputDynFunctionReq.deserialize(ser, bis, "");
            
            assertTrue(dynFunctionReq.equals(outputDynFunctionReq));
            
            ((DynamicPrimitive) dynFunctionReq.getParameter("param1")).set('A');
            assertTrue(!dynFunctionReq.equals(outputDynFunctionReq));
            
            
        } catch (DynamicTypeException dte) {
            dte.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    @Test
    public void functionResponsePrimitiveRetTypeSerialization() {
        try {
            FunctionTypeDescriptor functionDesc = tdbuilder.createFunctionType("add");
            PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
            functionDesc.setReturnType(intDesc);
            
            DynamicFunctionResponse dynFunctionReq = builder.createFunctionResponse(functionDesc);
            DynamicFunctionResponse outputDynFunctionReq = builder.createFunctionResponse(functionDesc);
            dynFunctionReq.setReturnValue(builder.createData(intDesc));

            dynFunctionReq.setException(false);
            ((DynamicPrimitive) dynFunctionReq.getReturnValue()).set(5);
            dynFunctionReq.serialize(ser, bos, "");
            reset();
            outputDynFunctionReq.deserialize(ser, bis, "");

            assertTrue(dynFunctionReq.equals(outputDynFunctionReq));

            ((DynamicPrimitive) dynFunctionReq.getReturnValue()).set(3);
            assertTrue(!dynFunctionReq.equals(outputDynFunctionReq));


        } catch (DynamicTypeException dte) {
            dte.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
