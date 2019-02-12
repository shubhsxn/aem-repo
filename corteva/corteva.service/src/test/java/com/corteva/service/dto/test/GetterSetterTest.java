package com.corteva.service.dto.test;

import org.junit.Test;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.service.api.ApiException;
import com.corteva.service.api.ApiResponse;
import com.corteva.service.product.detail.dto.AgrianProduct;
import com.corteva.service.product.detail.dto.AgrianRegulatoryData;
import com.corteva.service.product.detail.dto.Ai;
import com.corteva.service.product.detail.dto.Ais;
import com.corteva.service.product.detail.dto.Chemical;
import com.corteva.service.product.detail.dto.Chemicals;
import com.corteva.service.product.detail.dto.County;
import com.corteva.service.product.detail.dto.Documents;
import com.corteva.service.product.detail.dto.DotDescription;
import com.corteva.service.product.detail.dto.DotScenario;
import com.corteva.service.product.detail.dto.HazardClasses;
import com.corteva.service.product.detail.dto.HazardCodes;
import com.corteva.service.product.detail.dto.OshaHazardClass;
import com.corteva.service.product.detail.dto.OshaHazardClasses;
import com.corteva.service.product.detail.dto.Placards;
import com.corteva.service.product.detail.dto.PlantBackRestrictions;
import com.corteva.service.product.detail.dto.Registration;
import com.corteva.service.product.detail.dto.Registrations;
import com.corteva.service.product.detail.dto.Restriction;
import com.corteva.service.product.detail.dto.States;
import com.corteva.service.product.detail.dto.UnlicensedAreas;
import com.corteva.service.product.dto.AgrianProducts;
import com.corteva.service.product.dto.Product;
import com.corteva.service.product.dto.ProductTypes;
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
		validateAccessors(AgrianProduct.class);
		validateAccessors(AgrianRegulatoryData.class);
		validateAccessors(Ai.class);
		validateAccessors(Ais.class);
		validateAccessors(Chemical.class);
		validateAccessors(Chemicals.class);
		validateAccessors(Documents.class);
		validateAccessors(DotDescription.class);
		validateAccessors(DotScenario.class);
		validateAccessors(HazardClasses.class);
		validateAccessors(HazardCodes.class);
		validateAccessors(OshaHazardClasses.class);
		validateAccessors(OshaHazardClass.class);
		validateAccessors(Placards.class);
		validateAccessors(PlantBackRestrictions.class);
		validateAccessors(Registration.class);
		validateAccessors(Registrations.class);
		validateAccessors(Restriction.class);
		validateAccessors(States.class);
		validateAccessors(UnlicensedAreas.class);
		validateAccessors(AgrianProducts.class);
		validateAccessors(Product.class);
		validateAccessors(ProductTypes.class);
		validateAccessors(com.corteva.service.product.dto.Registration.class);
		validateAccessors(com.corteva.service.product.dto.Registrations.class);
		validateAccessors(ApiException.class);
		validateAccessors(ApiResponse.class);
		validateAccessors(County.class);
	}
}