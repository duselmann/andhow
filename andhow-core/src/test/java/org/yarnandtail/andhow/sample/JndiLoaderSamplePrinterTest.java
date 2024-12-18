/*
 */
package org.yarnandtail.andhow.sample;

import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.yarnandtail.andhow.api.GroupProxyMutable;
import org.yarnandtail.andhow.internal.NameAndProperty;
import org.yarnandtail.andhow.internal.StaticPropertyConfigurationMutable;
import org.yarnandtail.andhow.name.CaseInsensitiveNaming;
import org.yarnandtail.andhow.property.IntProp;
import org.yarnandtail.andhow.property.StrProp;
import org.yarnandtail.andhow.util.TextUtil;

/**
 *
 * @author ericeverman
 */
public class JndiLoaderSamplePrinterTest {
	
	StaticPropertyConfigurationMutable config;
	GroupProxyMutable groupProxy1;
	
	public static interface Config {
		IntProp MY_PROP1 = IntProp.builder().build();
		StrProp MY_PROP2 = StrProp.builder().defaultValue("La la la").desc("mp description")
				.helpText("Long text on how to use the property").mustStartWith("La").mustEndWith("la")
				.mustBeNonNull().aliasIn("mp2").aliasInAndOut("mp2_alias2").aliasOut("mp2_out").build();
	}
	
	@BeforeEach
	public void setup() {
		config = new StaticPropertyConfigurationMutable(new CaseInsensitiveNaming());
		
		groupProxy1 = new GroupProxyMutable(
				JndiLoaderSamplePrinterTest.Config.class.getCanonicalName(),
				JndiLoaderSamplePrinterTest.class.getCanonicalName() + "$Config"
		);
		groupProxy1.addProperty(new NameAndProperty("MY_PROP1", Config.MY_PROP1));
		groupProxy1.addProperty(new NameAndProperty("MY_PROP2", Config.MY_PROP2));
		
		assertNull(config.addProperty(groupProxy1, Config.MY_PROP1));
		assertNull(config.addProperty(groupProxy1, Config.MY_PROP2));

	}
	

	@Test
	public void generalTest() throws UnsupportedEncodingException {
		
		TestPrintStream out = new TestPrintStream();
		JndiLoaderSamplePrinter printer = new JndiLoaderSamplePrinter();
		String[] lines;	//The output line array
		
		
		//Print Sample start
		out.reset();
		printer.printSampleStart(config, out);
		
		//System.out.println(out.getTextAsString());
		lines = out.getTextAsLines();
		assertEquals(6, lines.length);
		assertEquals("<Context>", lines[0]);
		assertEquals("<!-- " + TextUtil.repeat("- ", 45), lines[1]);
		assertEquals("Sample JNDI config file generated by AndHow!", lines[2]);
		assertEquals("strong.simple.valid.AppConfiguration  -  https://github.com/eeverman/andhow", lines[3]);
		assertEquals("This sample uses Tomcat syntax. JNDI configuration for other servers will be similar.", lines[4]);
		assertEquals(TextUtil.repeat("- ", 45) + " -->", lines[5]);
		
		//Print group header
		out.reset();
		printer.printPropertyGroupStart(config, out, groupProxy1);
		
		//System.out.println(out.getTextAsString());
		lines = out.getTextAsLines();
		assertEquals(2, lines.length);
		assertEquals("<!-- " + TextUtil.repeat("- ", 45), lines[0]);
		assertEquals("Property Group " + JndiLoaderSamplePrinterTest.Config.class.getCanonicalName() + " -->",
				lines[1]);
		
		//Print MY_PROP1
		out.reset();
		printer.printProperty(config, out, groupProxy1, Config.MY_PROP1);
		
		//System.out.println(out.getTextAsString());
		lines = out.getTextAsLines();
		assertEquals(3, lines.length);
		assertEquals("<!-- ", lines[0]);
		assertEquals("MY_PROP1 (Integer) -->", lines[1]);
		assertEquals("<Environment name=\"" +
				JndiLoaderSamplePrinterTest.Config.class.getCanonicalName().replace(".", "/")
				+ "/MY_PROP1\" value=\"[Integer]\" "
				+ "type=\"java.lang.Integer\" override=\"false\"/>",
				lines[2]);
		
		//Print MY_PROP2
		out.reset();
		printer.printProperty(config, out, groupProxy1, Config.MY_PROP2);
		
		//System.out.println(out.getTextAsString());
		lines = out.getTextAsLines();
		assertEquals(9, lines.length);
		assertEquals("<!-- ", lines[0]);
		assertEquals("MY_PROP2 (String) NON-NULL - mp description", lines[1]);
		assertEquals("Recognized aliases: mp2, mp2_alias2", lines[2]);
		assertEquals("Default Value: La la la", lines[3]);
		assertEquals("Long text on how to use the property", lines[4]);
		assertEquals("The property value must:", lines[5]);
		assertEquals("- start with 'La'", lines[6]);
		assertEquals("- end with 'la' -->", lines[7]);
		assertEquals("<Environment name=\"" +
				JndiLoaderSamplePrinterTest.Config.class.getCanonicalName().replace(".", "/")
				+ "/MY_PROP2\" value=\"La la la\" "
				+ "type=\"java.lang.String\" override=\"false\"/>",
				lines[8]);
		
		//Print group closing (should be empty line)
		out.reset();
		printer.printPropertyGroupEnd(config, out, groupProxy1);
		
		//System.out.println(out.getTextAsString());
		lines = out.getTextAsLines();
		assertEquals(1, lines.length);
		assertEquals("", lines[0]);
		
		//Print file closing (should be empty line)
		out.reset();
		printer.printSampleEnd(config, out);
		
		//System.out.println(out.getTextAsString());
		lines = out.getTextAsLines();
		assertEquals(1, lines.length);
		assertEquals("</Context>", lines[0]);
	}

	/**
	 * Test of getFormat method, of class PropFileLoaderSamplePrinter.
	 */
	@Test
	public void testGetFormat() {
	}

	/**
	 * Test of getSampleFileStart method, of class PropFileLoaderSamplePrinter.
	 */
	@Test
	public void testGetSampleFileStart() {
	}

	/**
	 * Test of getSampleStartComment method, of class PropFileLoaderSamplePrinter.
	 */
	@Test
	public void testGetSampleStartComment() {
	}

	/**
	 * Test of getInAliaseString method, of class PropFileLoaderSamplePrinter.
	 */
	@Test
	public void testGetInAliaseString() {
	}

	/**
	 * Test of getActualProperty method, of class PropFileLoaderSamplePrinter.
	 */
	@Test
	public void testGetActualProperty() throws Exception {
	}

	/**
	 * Test of getSampleFileEnd method, of class PropFileLoaderSamplePrinter.
	 */
	@Test
	public void testGetSampleFileEnd() {
	}

	/**
	 * Test of getSampleFileExtension method, of class PropFileLoaderSamplePrinter.
	 */
	@Test
	public void testGetSampleFileExtension() {
	}
	
}
