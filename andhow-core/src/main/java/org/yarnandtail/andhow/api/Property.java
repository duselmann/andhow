package org.yarnandtail.andhow.api;

import java.util.List;

/**
 * Represents a configuration point of an application.
 * 
 * Implementations are typed so that they return String, Integer, Boolean, etc.,
 rather than just configuration strings.
 
 Implementor's Notes:
 See IntProp as a best example of how to implement a new Property.
 The reason for creating a new implementation would be to handle a new type, 
 such as a DateTime type.
 
 <ul>
 * <li>All implementations can use the PropertyBase as a base class.  Most
 * methods are already present w/ just a few methods left to implement.
 * <li>All implementations should have a static builder() method that returns
 * a builder capable of building an instance.  by convention, builders are
 * inner classes of their associated Property.  The PropertyBuilderBase is an
 * easy base class to extend that provide nearly all needed functionality.
 * <li>Builders should provide easy access to Validators for their appropriate type.
 * For instance the StrProp has a value type of String and has an
 * associated StringRegex Validator.  By convention, the builder methods to add
 * validators use the 'must' terminology, as in:  mustMatchRegex(String regex),
 * or mustStartWith(String prefix).
 * </ul>
 * @author eeverman
 */
public interface Property<T> {
	
	/**
	 * Returns the effective value of this property.
	 * 
	 * The effective value is the explicitly configured value, or if that is null,
	 * the default value.  Explicitly setting a property to null is not possible
	 * because it will just be ignored and the default used instead.
	 * 
	 * @return May be null, unless the property is marked as required.
	 */
	T getValue();
	
	/**
	 * The value found and loaded for this value by a Loader.
	 * 
	 * If no non-null value was found by a loader for this property, null is returned.
	 * 
	 * @return May be null
	 */
	T getExplicitValue();
	
	/**
	 * The default value, as defined when this Property was constructed.
	 * 
	 * @return May be null
	 */
	T getDefaultValue();
	
	/**
	 * If true, the effective value must be non-null to be considered valid.
	 * 
	 * The effective value is the explicitly configured value, or if that is null,
	 * the default value.
	 * 
	 * @return True if a non-null value is required.
	 */
	boolean isNonNullRequired();
	
	/**
	 * The basic type of the property:  Flag, name/value, multi=value.
	 * @return Never null
	 */
	PropertyType getPropertyType();
	
	/**
	 * The type of the value (String, Number, Integer, etc).
	 * For Properties that allow multiple values (not yet implemented), an array
	 * of values of the specified type can be fetched.
	 * @return Never null
	 */
	ValueType<T> getValueType();
	
	/**
	 * The Trimmer responsible for trimming String values before they are converted
	 * to the appropriate property type.
	 * 
	 * @return Never null
	 */
	Trimmer getTrimmer();
	
	/**
	 * A description of the property, what it is for and what it does in the system.
	 * 
	 * When the description is presented, the canonical name, validation requirements
	 * and helpText is presented as well, so it does not need to all be included in
	 * the description.
	 * 
	 * @return May be null
	 */
	String getDescription();
	
	/**
	 * List of validators to validate the converted value.
	 * @return An unmodifiable list of Validators.  Never null.
	 */
	List<Validator<T>> getValidators();
	
	/**
	 * The list of Aliases requested by this property in its declaration.
	 * 
	 * CAUTION:  This is NOT necessarily the list of Alias actually available
	 * for this property - use AndHow.getAlias() for that.
	 * 
	 * Since Properties from unrelated libraries can be
	 * used in the same application, the main AndHow application level configuration
	 * must be able to disable aliases that conflict (i.e., two properties are
	 * aliased as 'my-prop'.
	 * 
	 * Note that the returned aliases may be coalesced.  For instance, if a property
	 * is build as:
	 * <code>IntProp MyProp = IntProp.builder().inAlias("Bob").outAlias("Bob").build();</code>
	 * getConfiguredAliases() may return a single Alias object w/ the name "Bob",
	 * marked as being both both in and out.
	 * 
	 * @return A list of Alias assigned to this Property in the property
	 *	declaration or an empty list if there are none.
	 */
	List<Name> getRequestedAliases();
	
	
	/**
	 * Added details that will be shown if the user requests help.
	 * 
	 * Assume that the the canonical name, validation requirements
	 * and description are presented as well.
	 * 
	 * Help text can provide added details, such as details on how to specify
	 * special values or interrelated properties.
	 * 
	 * @return May be null
	 */
	String getHelpText();

}
