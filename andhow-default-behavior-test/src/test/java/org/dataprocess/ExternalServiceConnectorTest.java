package org.dataprocess;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author ericeverman
 */
public class ExternalServiceConnectorTest {
	
	public ExternalServiceConnectorTest() {
	}
	

	/**
	 * Test of getConnectionUrl method, of class EarthMapMaker.
	 */
	@Test
	public void testAllConfigValues() {
		ExternalServiceConnector esc = new ExternalServiceConnector();
		assertEquals("http://forwardcorp.com/service/", esc.getConnectionUrl());
		assertEquals(60, esc.getConnectionTimeout());
	}
	
}
