package com.corteva.service.dto.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.service.product.detail.dto.Counties;
import com.corteva.service.product.detail.dto.County;

/**
 * This is a test class for Getter Setter of Models.
 * @author Sapient
 */
public class CountiesTest extends BaseAbstractTest {
	
	/**
	 * The Inject Counties.
	 */
	@InjectMocks
	private Counties counties;
	
	/**
	 * The County List.
	 */
	private List<County> countyList;
	
	/**
	 * SetUp method
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		countyList = new ArrayList<County>();
		
		County county = new County();
		county.setContent("test content");
		county.setState("test state");
		
		countyList.add(county);
		counties.setCounty(countyList);
	}

	/**
	 * test get Country
	 */
	@Test
	public void getCounty() {
		Assert.assertEquals(counties.getCounty(), countyList);
	}
	
	/**
	 * test toString method
	 */
	@Test
	public void testToString() {
		Assert.assertEquals("Counties [county=[County [content=test content, state=test state]]]", counties.toString());
	}
}