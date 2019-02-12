package com.corteva.core.test;

import org.junit.Test;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.utils.SearchInput;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

/**
 * This is a test class for Getter Setter of Models.
 * @author Sapient
 */
public class GetterSetterTest extends BaseAbstractTest {
	
	/** The Constant ACCESSOR_VALIDATOR. */
	private static final com.openpojo.validation.Validator ACCESSOR_VALIDATOR = ValidatorBuilder.create()
			.with(new GetterTester()).with(new SetterTester()).build();
	
	/**
	 * Validate accessors.
	 * @param clazz the clazz
	 */
	public static void validateAccessors(final Class<?> clazz) {
		ACCESSOR_VALIDATOR.validate(PojoClassFactory.getPojoClass(clazz));
	}
	
	/**
	 * Test accesors should access proper field.
	 */
	@Test
	public void testAccesors_shouldAccessProperField() {
		validateAccessors(SearchInput.class);
	}
}