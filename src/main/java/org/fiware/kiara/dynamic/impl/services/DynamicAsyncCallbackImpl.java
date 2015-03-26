package org.fiware.kiara.dynamic.impl.services;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.fiware.kiara.dynamic.DynamicValueBuilderImpl;
import org.fiware.kiara.dynamic.services.DynamicAsyncCallback;
import org.fiware.kiara.dynamic.services.DynamicFunctionResponse;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;

public abstract class DynamicAsyncCallbackImpl implements DynamicAsyncCallback {

    @Override
    public void process(TransportMessage message, SerializerImpl ser, FunctionTypeDescriptor typeDescriptor) {
        
        try {
            if (message != null && message.getPayload() != null) {
                final BinaryInputStream bis = BinaryInputStream.fromByteBuffer(message.getPayload());
                    
                DynamicFunctionResponse ret = DynamicValueBuilderImpl.getInstance().createFunctionResponse(typeDescriptor);
                
                // Deserialize response message ID
                final Object responseMessageId = ser.deserializeMessageId(bis);
                
                ret.deserialize(ser, bis, "");
                
                onSuccess(ret);
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }

}
