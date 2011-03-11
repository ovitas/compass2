package no.ovitas.compass2.kb.store;

import junit.framework.TestCase;

public class CoreTest extends TestCase {
    public void testGetHello() throws Exception {
        assertEquals("Hello", Core.getHello());
    }
}
