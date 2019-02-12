package com.corteva.service.api.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.service.api.APIClient;
import com.corteva.service.api.ApiException;
import com.corteva.service.api.ApiResponse;
import com.corteva.service.product.detail.dto.AgrianRegulatoryData;

/**
 * The Class ApiclientTest.
 */
public class ApiclientTest extends BaseAbstractTest {
	
	/** The api response. */
	ApiResponse<AgrianRegulatoryData> apiResponse;
	
	/**
	 * Sets the up.
	 * @throws ApiException the api exception
	 */
	@Before
	public void setUp() throws ApiException {
		apiResponse = APIClient.callAPI(
				"https://www.agrian.com/webservices/agrian_products.cfc?method=get_regulatory_data_v2&username=dp_cp_labelfinder&password=xfszUgJF&product_id=9235&pdfs=false",
				AgrianRegulatoryData.class);
	}
	
	/**
	 * Test build call response not null.
	 */
	@Test
	public void testBuildCall_ResponseNotNull() {
		Assert.assertNotNull(apiResponse);
	}
	
	/**
	 * Test build call response pared.
	 * @throws ApiException the api exception
	 */
	@Test
	public void testBuildCall_ResponsePared() throws ApiException {
		Assert.assertNotNull(apiResponse.getData());
	}
	
	/**
	 * Test build call product ID.
	 * @throws ApiException the api exception
	 */
	@Test
	public void testBuildCall_ProductID() throws ApiException {
		Assert.assertTrue("9235".equals(apiResponse.getData().getAgrianProduct().getProductId()));
	}
}