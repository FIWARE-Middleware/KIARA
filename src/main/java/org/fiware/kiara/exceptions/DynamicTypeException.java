package org.fiware.kiara.exceptions;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class DynamicTypeException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public DynamicTypeException(String message) {
        super(message);
    }

}
