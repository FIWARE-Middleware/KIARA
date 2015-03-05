package org.fiware.kiara.typecode.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;

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
    public void setDimensionsLength(int... dimensions) {
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
    
    /*public int getLinearSize() {
        return this.m_linearSize;
    }*/
    
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
    
    /*public ArrayTypeDescriptorImpl(int... dimensions) {
    super(TypeKind.ARRAY_TYPE, "array");
    if (dimensions.length == 0) {
        throw new TypeDescriptorException("ArrayTypeDescriptor - No dimensions have been specified for this array.");
    } else if (!checkDimensions(dimensions)) {
        throw new TypeDescriptorException("ArrayTypeDescriptor - Dimensions cannot be neither negative nor zero.");
    }
    this.m_dimensions = new ArrayList<Integer>();
    this.insertDimensions(dimensions);
    }*/

    /*public ArrayTypeDescriptorImpl(DataTypeDescriptorImpl contentType, int... dimensions) {
    super(TypeKind.ARRAY_TYPE, "array", contentType);
    if (dimensions.length == 0) {
        throw new TypeDescriptorException("ArrayTypeDescriptor - No dimensions have been specified for this array.");
    }
    this.m_dimensions = new ArrayList<Integer>();
    this.insertDimensions(dimensions);
    }*/



}
