package org.fiware.kiara.typecode;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public interface TypeDescriptor {
    
    /*
     * Public Methods
     */
    
    public TypeKind getKind();
    
    public boolean isData();
    
    public boolean isPrimitive();

    public boolean isVoid();

    // ---------------------- Container -----------------------
    
    public boolean isContainer();
    
    public boolean isArray();
    
    public boolean isList();
    
    public boolean isMap();
    
    public boolean isSet();
    
    // ----------------------- Membered -----------------------
    
    public boolean isMembered();
    
    public boolean isEnum();
    
    public boolean isUnion();
    
    public boolean isStruct();
    
    public boolean isException();
    
    // --------------------- Service ------------------------------
    
    public boolean isService();
    
    // --------------------- Function ------------------------------
    
    public boolean isFunction();
    
}
