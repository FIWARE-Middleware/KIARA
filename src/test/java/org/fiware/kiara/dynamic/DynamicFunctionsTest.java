package org.fiware.kiara.dynamic;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.fiware.kiara.dynamic.services.DynamicFunction;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.serialization.MockTransportMessage;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeDescriptorBuilderImpl;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.ExceptionTypeDescriptor;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DynamicFunctionsTest {
    
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
            
            DynamicFunction dynFunction = builder.createFunction(functionDesc);
            assertTrue(true);
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
            
            DynamicFunction dynFunction = builder.createFunction(functionDesc);
            assertTrue(true);
        } catch (DynamicTypeException dte) {
            assertTrue(false);
        }
        
    }

}
