package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;

public class PrimitiveTypeDescriptorImpl extends DataTypeDescriptorImpl implements PrimitiveTypeDescriptor {

    private boolean m_primitive = false;
    private int m_maxFixedLength = 0;
    
    public PrimitiveTypeDescriptorImpl(TypeKind kind, String name) {
        super(kind, name);
        this.initialize();
        if (name != null && name.length() != 0) {
            this.m_name = name;
        }
    }
    
    @Override
    public boolean isData() {
        return true;
    }
    
    @Override
    public boolean isPrimitive() {
        return this.m_primitive; 
    }
    
    @Override
    public boolean isString() {
        return this.m_kind == TypeKind.STRING_TYPE;
    }
    
    @Override
    public void setMaxFixedLength(int length) {
        if (this.m_kind == TypeKind.STRING_TYPE) {
            this.m_maxFixedLength = length;
        } else {
            throw new TypeDescriptorException("PrimitiveTypeDescriptor - Only PrimitiveTypeDescriptor objects whose kind is STRING_TYPE are allowed to have maximum fixed length.");
        }
    }
    
    @Override
    public int getMaxFixedLength() {
        if (this.m_kind == TypeKind.STRING_TYPE) {
            return this.m_maxFixedLength;
        } else {
            throw new TypeDescriptorException("PrimitiveTypeDescriptor - Only PrimitiveTypeDescriptor objects whose kind is STRING_TYPE are allowed to have maximum fixed length.");
        }
    }
    
    /*
     * Private Methods
     */
    
    private void initialize() {
        switch (this.m_kind) {
            case BOOLEAN_TYPE:
                this.m_name = "boolean";
                this.m_primitive = true;
                break;
            case BYTE_TYPE:
                this.m_name = "byte";
                this.m_primitive = true;
                break;
            case INT_16_TYPE:
                this.m_name = "short";
                this.m_primitive = true;
                break;
            case INT_32_TYPE:
                this.m_name = "int";
                this.m_primitive = true;
                break;
            case INT_64_TYPE:
                this.m_name = "long";
                this.m_primitive = true;
                break;
            case FLOAT_32_TYPE:
                this.m_name = "float";
                this.m_primitive = true;
                break;
            case FLOAT_64_TYPE:
                this.m_name = "double";
                this.m_primitive = true;
                break;
            case CHAR_8_TYPE:
                this.m_name = "char";
                this.m_primitive = true;
                break;
            case STRING_TYPE:
                this.m_name = "string";
                this.m_primitive = true;
                this.m_maxFixedLength = 255;
                break;
            
            default:
                this.m_name = "null";
                break;
        }
    }

}
