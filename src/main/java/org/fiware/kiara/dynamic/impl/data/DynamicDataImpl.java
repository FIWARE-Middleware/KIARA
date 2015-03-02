package org.fiware.kiara.dynamic.impl.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.fiware.kiara.dynamic.DynamicData;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.dynamic.impl.data.visitor.Element;
import org.fiware.kiara.dynamic.impl.data.visitor.Visitor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;

public class DynamicDataImpl extends DynamicTypeImpl implements DynamicData, Element, Visitor {
    
    //protected PropertyChangeSupport m_propertyChangeSupport;
    
    protected Visitor m_visitor;
    
    public DynamicDataImpl(DataTypeDescriptor dataDescriptor, String className) {
        super(dataDescriptor, className);
    }
    
    /*public void setObserver(DynamicDataImpl observer) {
        this.m_observer = observer;
    }
    
    public void visit(Object... params) {
        
    }*/

    @Override
    public void visit(Object... params) {
        
    }
    
    @Override
    public boolean notify(DynamicDataImpl value, Object... params) {
        return false;
    }

    @Override
    public void registerVisitor(Visitor visitor) {
        this.m_visitor = visitor;
    }
    
    public Object[] trimParams(Object[] params) {
        Object[] ret = new Object[params.length-1];
        for (int i=0; i < params.length-1; ++i) {
            ret[i] = params[i+1];
        }
        return ret;
    }
    
    public Object[] appendParams(DynamicData value, Object[] params) {
        Object[] ret = new Object[params.length+1];
        ret[0] = value;
        for (int i=0; i < params.length; ++i) {
            ret[i+1] = params[i];
        }
        return ret;
    }

}
