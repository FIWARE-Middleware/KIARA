package org.fiware.kiara.typecode.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeKind;

public class ArrayTypeDescriptor extends ContainerTypeDescriptor {
    
    private ArrayList<Integer> m_dimensions;
    private int m_linearSize;
    
    @Override
    public void setMaxSize(int size) {
        throw new TypeDescriptorException("ArrayTypeDescriptor - Array type descriptions do not support maximum size. Use setDimensions function instead.");
    }

    public ArrayTypeDescriptor(int... dimensions) {
        super(TypeKind.ARRAY_TYPE, "array");
        if (dimensions.length == 0) {
            throw new TypeDescriptorException("ArrayTypeDescriptor - No dimensions have been specified for this array.");
        } else if (!checkDimensions(dimensions)) {
            throw new TypeDescriptorException("ArrayTypeDescriptor - Dimensions cannot be neither negative nor zero.");
        }
        this.m_dimensions = new ArrayList<Integer>();
        this.insertDimensions(dimensions);
    }
    
    private boolean checkDimensions(int... dimensions) {
        for (int dim : dimensions) {
            if (!(dim > 0)) {
                return false;
            }
        }
        return true;
    }
    
    public ArrayTypeDescriptor(DataTypeDescriptor contentType, int... dimensions) {
        super(TypeKind.ARRAY_TYPE, "array", contentType);
        if (dimensions.length == 0) {
            throw new TypeDescriptorException("ArrayTypeDescriptor - No dimensions have been specified for this array.");
        }
        this.m_dimensions = new ArrayList<Integer>();
        this.insertDimensions(dimensions);
    }
    
    public void setDimensions(int... dimensions) {
        if(this.m_dimensions.size() != 0) {
            this.m_dimensions.clear();
        }
        this.insertDimensions(dimensions);
    }
    
    public int getLinearSize() {
        return this.m_linearSize;
    }
    
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
