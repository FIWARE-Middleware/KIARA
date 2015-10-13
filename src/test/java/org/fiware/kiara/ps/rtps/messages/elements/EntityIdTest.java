package org.fiware.kiara.ps.rtps.messages.elements;

import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntityIdTest {
    
    public EntityIdTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isUnknown method, of class EntityId.
     */
    /*
    @Test
    public void testIsUnknown() {
        EntityId instance = new EntityId(EntityId.EntityIdEnum.ENTITYID_UNKNOWN);
        System.out.println(System.identityHashCode(instance.m_value));
        
        assertEquals(true, instance.equals(new EntityId(EntityId.EntityIdEnum.ENTITYID_UNKNOWN)));
        
        instance.setValue(0, (byte)0x00);
        instance.setValue(1, (byte)0x00);
        instance.setValue(2, (byte)0x01);
        instance.setValue(3, (byte)0xC1);
        
        instance = new EntityId(EntityId.EntityIdEnum.ENTITYID_UNKNOWN);
        System.out.println(System.identityHashCode(instance.m_value));
        assertEquals(false, instance.equals(new EntityId(EntityId.EntityIdEnum.ENTITYID_RTPSPARTICIPANT)));
    }
    */
    
}
