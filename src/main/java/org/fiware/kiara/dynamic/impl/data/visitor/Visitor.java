package org.fiware.kiara.dynamic.impl.data.visitor;

import org.fiware.kiara.dynamic.impl.data.DynamicDataImpl;

public interface Visitor {

    public void visit(Object... params);
    
    public boolean notify(DynamicDataImpl value, Object... params);
    
}
