/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.core.test;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.LinkUtil;

/**
 * This is test class to test the Link Util.
 */
public class LinkUtilTest extends BaseAbstractTest {

	/** The link util. */
	private LinkUtil linkUtil;

    /**
     * Method to set up the test objects.
     */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		linkUtil = new LinkUtil();
	}

	/**
	 * Test get href.
	 */
	@Test
	public void testGetHref() {
		Assert.assertEquals(StringUtils.EMPTY, LinkUtil.getHref(StringUtils.EMPTY)) ;
		Assert.assertEquals("/content/dam/corteva/na/us/en/home/test-blank", LinkUtil.getHref("/content/dam/corteva/na/us/en/home/test-blank.html"));
		Assert.assertEquals(CortevaConstant.HASH, LinkUtil.getHref(CortevaConstant.HASH));
		Assert.assertEquals("/content/corteva/na/us/en/home/test-blank.html", LinkUtil.getHref("/content/corteva/na/us/en/home/test-blank"));
		Assert.assertEquals(LinkUtil.getHref("content/corteva/na/us/en/home/test-blank.png"), CortevaConstant.HTTP.concat("content/corteva/na/us/en/home/test-blank.png"));
		Assert.assertEquals(LinkUtil.getHref("content/corteva/na/us/en/home/test-blank.gif"), CortevaConstant.HTTP.concat("content/corteva/na/us/en/home/test-blank.gif"));
		Assert.assertEquals(LinkUtil.getHref("content/corteva/na/us/en/home/test-blank.jpg"), CortevaConstant.HTTP.concat("content/corteva/na/us/en/home/test-blank.jpg"));
		Assert.assertEquals(LinkUtil.getHref("content/corteva/na/us/en/home/test-blank.jpeg"), CortevaConstant.HTTP.concat("content/corteva/na/us/en/home/test-blank.jpeg"));
		Assert.assertEquals(LinkUtil.getHref("content/corteva/na/us/en/home/test-blank.bmp"), CortevaConstant.HTTP.concat("content/corteva/na/us/en/home/test-blank.bmp"));
		Assert.assertEquals(LinkUtil.getHref("content/corteva/na/us/en/home/test-blank.tif"), CortevaConstant.HTTP.concat("content/corteva/na/us/en/home/test-blank.tif"));
		Assert.assertEquals(LinkUtil.getHref("content/corteva/na/us/en/home/test-blank"), CortevaConstant.HTTP.concat("content/corteva/na/us/en/home/test-blank"));
		
	}
}