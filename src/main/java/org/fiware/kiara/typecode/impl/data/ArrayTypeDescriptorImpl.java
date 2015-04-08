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
package org.fiware.kiara.typecode.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class ArrayTypeDescriptorImpl extends ContainerTypeDescriptorImpl implements ArrayTypeDescriptor {
    
    private ArrayList<Integer> m_dimensions;
    private int m_linearSize;
    
    @Override
    public void setMaxSize(int size) {
        throw new TypeDescriptorException("ArrayTypeDescriptor - Array type descriptions do not support maximum size. Use setDimensions function instead.");
    }
    
    @Override
    public int getMaxSize() {
        return this.m_linearSize;
    }

    public ArrayTypeDescriptorImpl() {
        super(TypeKind.ARRAY_TYPE);
        this.m_dimensions = new ArrayList<Integer>();
    }
    
    @Override
    public boolean setContentType(DataTypeDescriptor contentType) {
        if (contentType.getKind() == TypeKind.ARRAY_TYPE) {
            throw new TypeDescriptorException("ArrayTypeDescriptor - Array type descriptions do not support another array as its content type. Use dimensions instead.");
        }
        this.m_contentType = contentType;
        return true;
    }
    
    @Override
    public DataTypeDescriptor getContentType() {
        return this.m_contentType;
    }
    
    @Override
    public void setDimensions(int... dimensions) {
        this.checkDimensions(dimensions);
        if(this.m_dimensions.size() != 0) {
            this.m_dimensions.clear();
        }
        this.insertDimensions(dimensions);
    }
    
    private boolean checkDimensions(int... dimensions) {
        if (dimensions.length == 0) {
            throw new TypeDescriptorException("ArrayTypeDescriptor - No dimensions have been specified for this array.");
        } 
        for (int dim : dimensions) {
            if (!(dim > 0)) {
                throw new TypeDescriptorException("ArrayTypeDescriptor - Dimensions cannot be neither negative nor zero.");
            } 
        }
        return true;
    }
    
    @Override
    public ArrayList<Integer> getDimensions() {
        return this.m_dimensions;
    }
    
    private void insertDimensions(int... dimensions) {
        int linearSize = 1;
        for(int dim : dimensions) {
            this.m_dimensions.add(dim);
            linearSize = linearSize * dim;
        }
        this.m_linearSize = linearSize;
    }
    
    @Override
    public boolean isArray() {
        return true;
    }
    
    
}
