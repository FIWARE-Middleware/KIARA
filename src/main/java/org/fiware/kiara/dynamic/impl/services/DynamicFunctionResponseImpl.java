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
 */
package org.fiware.kiara.dynamic.impl.services;

import java.io.IOException;

import org.fiware.kiara.Kiara;
import org.fiware.kiara.dynamic.services.DynamicFunctionResponse;
import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicException;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.transport.Transport;
import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.ExceptionTypeDescriptor;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;
import org.fiware.kiara.typecode.impl.services.FunctionTypeDescriptorImpl;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class DynamicFunctionResponseImpl extends DynamicTypeImpl implements DynamicFunctionResponse { 
    
    private DynamicData m_returnType;
    private boolean m_isException;
    private SerializerImpl m_serializer;
    private Transport m_transport;
    
    public DynamicFunctionResponseImpl(TypeDescriptor typeDescriptor) {
        super(typeDescriptor, "DynamicFunctionImpl");
    }
    
    public DynamicFunctionResponseImpl(TypeDescriptor typeDescriptor, Serializer serializer, Transport transport) {
        super(typeDescriptor, "DynamicFunctionImpl");
        this.m_serializer = (SerializerImpl) serializer;
        this.m_transport = transport;
    }

    @Override
    public void setReturnValue(DynamicData returnType) {

        if (returnType instanceof DynamicException) {
            // Exception return type
            boolean success = false;
            for (ExceptionTypeDescriptor ex : ((FunctionTypeDescriptorImpl) this.m_typeDescriptor).getExceptions()) {
                if (ex.getName().equals(((ExceptionTypeDescriptor) returnType.getTypeDescriptor()).getName())) {
                    this.m_returnType = returnType;
                    this.m_isException = true;
                    success = true;
                }
            }
            if (!success) {
                throw new DynamicTypeException(this.m_className + 
                        " - A dynamic exception matching the one specified cannot be found.");
            }
        } else {
            if (returnType.getTypeDescriptor().getKind() == ((FunctionTypeDescriptor) this.m_typeDescriptor).getReturnType().getKind()) {
                this.m_returnType = returnType;
                this.m_isException = false;
            } else {
                throw new DynamicTypeException(this.m_className + 
                        " - A dynamic data whose type is " + returnType.getTypeDescriptor().getKind() + 
                        " cannot be added to a function whose specified return type is " + 
                        ((FunctionTypeDescriptor) this.m_typeDescriptor).getReturnType().getKind());
            }
        }
    }

    @Override
    public DynamicData getReturnValue() {
        return this.m_returnType;
    }

    @Override
    public boolean isException() {
        return this.m_isException;
    }

    @Override
    public void setException(boolean isException) {
        this.m_isException = isException;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        if (this.m_isException) {
            impl.serializeUI32(message, name, Integer.parseInt(((ExceptionTypeDescriptor) this.m_returnType.getTypeDescriptor()).getMd5()));
            impl.serializeString(message, name, ((ExceptionTypeDescriptor) this.m_returnType.getTypeDescriptor()).getName());
        } else {
            impl.serializeUI32(message, name, 0);
        }
        this.m_returnType.serialize(impl, message, name);
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        int errorCode = impl.deserializeUI32(message, name);
        if (errorCode != 0) { // Exception has been raised on server's side
            this.m_isException = true;
            String exceptionName = impl.deserializeString(message, name);

            //DataTypeDescriptor td = null;
            for (ExceptionTypeDescriptor ex : ((FunctionTypeDescriptorImpl) this.m_typeDescriptor).getExceptions()) {
                if (ex.getName().equals(exceptionName)) {
                    DynamicException dynEx = (DynamicException) Kiara.getDynamicValueBuilder().createData(ex);
                    dynEx.deserialize(impl, message, exceptionName);
                    this.m_returnType = dynEx;
                }
            }
            
            if (this.m_returnType == null) {
                throw new DynamicTypeException(this.m_className + " - The exception '" + exceptionName + "' sent by server cannot be found in the function specification.");
            }
        } else {
            this.m_isException = false;
            DataTypeDescriptor td = ((FunctionTypeDescriptor) this.m_typeDescriptor).getReturnType();
            DynamicData dynData = Kiara.getDynamicValueBuilder().createData(td);
            dynData.deserialize(impl, message, name);
            this.m_returnType = dynData;
        }
    }
    
    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject instanceof DynamicFunctionResponseImpl) {
            return this.m_isException == ((DynamicFunctionResponseImpl)anotherObject).m_isException && 
                    this.m_returnType.equals(((DynamicFunctionResponseImpl)anotherObject).m_returnType);
        } 
        return false;
    }

    
    
}
