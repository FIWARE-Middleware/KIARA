package org.fiware.kiara.dynamic.impl.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.fiware.kiara.dynamic.DynamicData;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.dynamic.impl.data.visitor.Element;
import org.fiware.kiara.dynamic.impl.data.visitor.Visitor;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptor;

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
    public boolean exists(DynamicDataImpl value, Object... params) {
        return false;
    }

    @Override
    public void registerVisitor(Visitor visitor) {
        this.m_visitor = visitor;
    }

}
