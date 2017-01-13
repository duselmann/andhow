package yarnandtail.andhow;

import java.util.ArrayList;
import static yarnandtail.andhow.AndHowTestBase.reloader;
import yarnandtail.andhow.load.CmdLineLoader;
import yarnandtail.andhow.name.BasicNamingStrategy;
import yarnandtail.andhow.property.IntProp;
import yarnandtail.andhow.property.StrProp;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import yarnandtail.andhow.load.JndiLoader;

/**
 *
 * @author ericeverman
 */
public class AndHow_AliasInTest extends AndHowTestBase {
	
	//
	//Alias names
	static final String STR_PROP1_IN = "str.Prop.1.In";
	static final String STR_PROP1_OUT_ALIAS = "strProp1Out";
	static final String STR_PROP1_IN_AND_OUT_ALIAS = "StrProp1InAndOut";
	static final String STR_PROP2_ALIAS = "strProp2";
	static final String STR_PROP2_IN_ALT1_ALIAS = "StrProp2.InAlt1";
	static final String STR_PROP2_IN_ALT2_ALIAS = "StrProp2.InAlt2";
	static final String INT_PROP1_ALIAS = "IntProp1";
	static final String INT_PROP1_ALT_IN1_ALIAS = "Int.Prop.1-Alt-In!1";
	
	
	//
	//Property values
	private static final String STR1 = "SOME_FIXED_VALUE_STRING_1";
	private static final String STR2 = "SOME_FIXED_VALUE_STRING_2";
	private static final Integer INT1 = 23572374;
	private static final Integer INT2 = 9237347;
	
	interface AliasGroup1 extends PropertyGroup {
		StrProp strProp1 = StrProp.builder().required()
				.aliasIn(STR_PROP1_IN).aliasOut(STR_PROP1_OUT_ALIAS).aliasInAndOut(STR_PROP1_IN_AND_OUT_ALIAS).build();
		
		StrProp strProp2 = StrProp.builder()
				.aliasIn(STR_PROP2_ALIAS).aliasOut(STR_PROP2_ALIAS).aliasIn(STR_PROP2_IN_ALT1_ALIAS).aliasIn(STR_PROP2_IN_ALT2_ALIAS).build();
		IntProp intProp1 = IntProp.builder()
				.aliasIn(INT_PROP1_ALIAS).aliasInAndOut(INT_PROP1_ALIAS).aliasIn(INT_PROP1_ALT_IN1_ALIAS).build();
		IntProp intProp2 = IntProp.builder().required().defaultValue(INT2).build();
	}

	
	@Test
	public void testFirstSetOfInAliasesViaCmdLine() {
		AndHow.builder().namingStrategy(new BasicNamingStrategy())
				.loader(new CmdLineLoader())
				.cmdLineArg(STR_PROP1_IN, STR1)
				.cmdLineArg(STR_PROP2_ALIAS, STR2)
				.cmdLineArg(INT_PROP1_ALIAS, INT1.toString())
				.group(AliasGroup1.class)
				.reloadForNonPropduction(reloader);
		
		assertEquals(STR1, AliasGroup1.strProp1.getValue());
		assertEquals(STR2, AliasGroup1.strProp2.getValue());
		assertEquals(INT1, AliasGroup1.intProp1.getValue());
		assertEquals(INT2, AliasGroup1.intProp2.getValue());	//default should still come thru
	}
	
	@Test
	public void testSecondSetOfInAliasesViaCmdLine() {
		AndHow.builder().namingStrategy(new BasicNamingStrategy())
				.loader(new CmdLineLoader())
				.cmdLineArg(STR_PROP1_IN_AND_OUT_ALIAS, STR1)
				.cmdLineArg(STR_PROP2_IN_ALT1_ALIAS, STR2)
				.cmdLineArg(INT_PROP1_ALT_IN1_ALIAS, INT1.toString())
				.group(AliasGroup1.class)
				.reloadForNonPropduction(reloader);
		
		assertEquals(STR1, AliasGroup1.strProp1.getValue());
		assertEquals(STR2, AliasGroup1.strProp2.getValue());
		assertEquals(INT1, AliasGroup1.intProp1.getValue());
	}
	
	@Test
	public void testThirdSetOfInAliasesViaCmdLine() {
		AndHow.builder().namingStrategy(new BasicNamingStrategy())
				.loader(new CmdLineLoader())
				.cmdLineArg(STR_PROP1_IN_AND_OUT_ALIAS, STR1)
				.cmdLineArg(STR_PROP2_IN_ALT2_ALIAS, STR2)
				.cmdLineArg(INT_PROP1_ALT_IN1_ALIAS, INT1.toString())
				.group(AliasGroup1.class)
				.reloadForNonPropduction(reloader);
		
		assertEquals(STR1, AliasGroup1.strProp1.getValue());
		assertEquals(STR2, AliasGroup1.strProp2.getValue());
		assertEquals(INT1, AliasGroup1.intProp1.getValue());
	}
	


	@Test
	public void testInAliasesViaJndiCompEnvClassPath() throws Exception {
		
		SimpleNamingContextBuilder jndi = AndHowTestBase.getJndi();

		jndi.bind("java:comp/env/" + STR_PROP1_IN, STR1);
		jndi.bind("java:comp/env/" + STR_PROP2_ALIAS, STR2);
		jndi.bind("java:comp/env/" + INT_PROP1_ALIAS, INT1.toString());

		jndi.activate();
		
		AndHow.builder()
				.loader(new JndiLoader())
				.group(AliasGroup1.class)
				.reloadForNonPropduction(reloader);
		
		assertEquals(STR1, AliasGroup1.strProp1.getValue());
		assertEquals(STR2, AliasGroup1.strProp2.getValue());
		assertEquals(INT1, AliasGroup1.intProp1.getValue());
		assertEquals(INT2, AliasGroup1.intProp2.getValue());	//default should still come thru
	}
	
	@Test
	public void testInAliasesViaJndiRootClassPath() throws Exception {
		
		SimpleNamingContextBuilder jndi = AndHowTestBase.getJndi();

		jndi.bind("java:" + STR_PROP1_IN_AND_OUT_ALIAS, STR1);
		jndi.bind("java:" + STR_PROP2_IN_ALT1_ALIAS, STR2);
		jndi.bind("java:" + INT_PROP1_ALT_IN1_ALIAS, INT1.toString());

		jndi.activate();
		
		AndHow.builder()
				.loader(new JndiLoader())
				.group(AliasGroup1.class)
				.reloadForNonPropduction(reloader);
		
		assertEquals(STR1, AliasGroup1.strProp1.getValue());
		assertEquals(STR2, AliasGroup1.strProp2.getValue());
		assertEquals(INT1, AliasGroup1.intProp1.getValue());
		assertEquals(INT2, AliasGroup1.intProp2.getValue());	//default should still come thru
	}
	
	@Test
	public void testInAliasesViaJndiCompEnvUrlNames() throws Exception {
		
		SimpleNamingContextBuilder jndi = AndHowTestBase.getJndi();

		jndi.bind("java:comp/env/" + NamingStrategy.getUriName(STR_PROP1_IN), STR1);
		jndi.bind("java:comp/env/" + NamingStrategy.getUriName(STR_PROP2_ALIAS), STR2);
		jndi.bind("java:comp/env/" + NamingStrategy.getUriName(INT_PROP1_ALIAS), INT1.toString());

		jndi.activate();
		
		AndHow.builder()
				.loader(new JndiLoader())
				.group(AliasGroup1.class)
				.reloadForNonPropduction(reloader);
		
		assertEquals(STR1, AliasGroup1.strProp1.getValue());
		assertEquals(STR2, AliasGroup1.strProp2.getValue());
		assertEquals(INT1, AliasGroup1.intProp1.getValue());
		assertEquals(INT2, AliasGroup1.intProp2.getValue());	//default should still come thru
	}
	
	@Test
	public void testInAliasesViaJndiRootUrlNames() throws Exception {
		
		SimpleNamingContextBuilder jndi = AndHowTestBase.getJndi();

		jndi.bind("java:" + NamingStrategy.getUriName(STR_PROP1_IN), STR1);
		jndi.bind("java:" + NamingStrategy.getUriName(STR_PROP2_ALIAS), STR2);
		jndi.bind("java:" + NamingStrategy.getUriName(INT_PROP1_ALIAS), INT1.toString());

		jndi.activate();
		
		AndHow.builder()
				.loader(new JndiLoader())
				.group(AliasGroup1.class)
				.reloadForNonPropduction(reloader);
		
		assertEquals(STR1, AliasGroup1.strProp1.getValue());
		assertEquals(STR2, AliasGroup1.strProp2.getValue());
		assertEquals(INT1, AliasGroup1.intProp1.getValue());
		assertEquals(INT2, AliasGroup1.intProp2.getValue());	//default should still come thru
	}
	

}