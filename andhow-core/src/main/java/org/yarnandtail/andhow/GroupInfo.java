package org.yarnandtail.andhow;

import java.lang.annotation.*;

/**
 * Annotation to allow users to include documentation on PropertyGroups.
 * 
 * When sample configuration or documentation is generated, these values are used
 * to provide better details on groups.
 * 
 * @author eeverman
 */
@Retention(RetentionPolicy.RUNTIME) //ensures this annotation is available to the VM, not just compiler
@Target(ElementType.TYPE)	//Only use on type declarations
@Documented	//Include values for this annotation in JavaDocs
public @interface GroupInfo {
	String name();
	String desc();
}
