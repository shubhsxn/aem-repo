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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.utils.TagUtil;
import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

/**
 * This is test class to test the Tag Util class.
 */
public class TagUtilsTest extends BaseAbstractTest {

	/** The experience fragment model. */
	@Mock
	private TagUtil tagUtil;
	
	/**
	 * The mocked request object.
	 */
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	
	/** The current res path. */
	private String currentResPath;
	
	/**
	 * The mocked resource resolver object.
	 */
	@Mock
	private ResourceResolver resourceResolver;
	
	/**
	 * The mocked tag manager object.
	 */
	@Mock
	private TagManager tagmanager;
	
	/**
	 * The mocked tag object.
	 */
	@Mock
	private Tag tag;
	
	/** The Constant CAREER. */
	private static final String CAREER = "career";
	
	/** The Constant LOCALE. */
	private static final String LOCALE = "en";
	
	/** The Constant CONTENT_PATH. */
	private static final String CONTENT_PATH = "content/corteva";
	
	
	/**
	 * Sets the method parameters and registers the service.
	 */
	@Before
	public void setUp() {
		mockRequest = getRequest();
		tagUtil = getRequest().adaptTo(TagUtil.class);
		mockRequest.setServletPath("/content/corteva/na/us/en/home");
		currentResPath = "/content/corteva/na/us/en/home/eloqua-form/jcr:content";
		context.create().resource(currentResPath);
		context.currentResource(currentResPath);
		
		final TagManager mockTagManager = Mockito.mock(TagManager.class);
		Tag fakeTag = Mockito.mock(Tag.class);
		Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getTagID()).thenReturn(CAREER);
		Mockito.when(fakeTag.getName()).thenReturn(CAREER);
		context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
		List<Tag> childTags = new ArrayList<>();
		childTags.add(fakeTag);
		Mockito.when(fakeTag.listChildren()).thenReturn(childTags.iterator());
		Mockito.when(fakeTag.listAllSubTags()).thenReturn(childTags.iterator());

	}

	/**
	 * Test get resources.
	 */
	@Test
	public void testGetResources() {
	final TagManager mockTagManager = Mockito.mock(TagManager.class);
	Tag fakeTag = Mockito.mock(Tag.class);
	Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
	context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
	String basePath = "content/corteva/eloqua/country";
	String[] tagIds = {"tag1","tag2","tag3"};
	List<Tag> resources = new ArrayList<>();
	
	resources.add(fakeTag);
	Iterator iterator = resources.iterator();
	RangeIterator<Resource> rangeIteratorTags = new RangeIterator() {

        int index = 0;
       /** 
        * The method is an intentionally-blank override
        */
        @Override
        public void skip(long skipNum) {
        	throw new UnsupportedOperationException();        	
        }

        @Override
        public long getSize() {
            return resources.size();
        }

        @Override
        public long getPosition() {
            return index;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Object next() {
            index++;
            return iterator.next();
        }
    };	
    Mockito.when(mockTagManager.find(Mockito.anyString(),Mockito.any(String[].class))).thenReturn(rangeIteratorTags);
	Mockito.when(fakeTag.getTagID()).thenReturn(CAREER);
	Assert.assertNotNull(tagUtil.getResources(mockTagManager, fakeTag, basePath));
	Assert.assertNotNull(tagUtil.getResources(mockTagManager, tagIds, basePath));
	}

	/**
	 * Test list all sub tags.
	 */
	@Test
	public void testListAllSubTags() {
		final TagManager mockTagManager = Mockito.mock(TagManager.class);
		Tag fakeTag = Mockito.mock(Tag.class);
		String rootTagPath = CONTENT_PATH;
		Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
		List<Tag> childTags = new ArrayList<>();
		childTags.add(fakeTag);
		Mockito.when(fakeTag.listAllSubTags()).thenReturn(childTags.iterator());
		Assert.assertNotNull(tagUtil.listAllSubTags(fakeTag));
		Assert.assertNotNull(tagUtil.listAllSubTags(mockTagManager,rootTagPath));
	}

	/**
	 * Test list children.
	 */
	@Test
	public void testListChildren() {
		final TagManager mockTagManager = Mockito.mock(TagManager.class);
		Tag fakeTag = Mockito.mock(Tag.class);
		String rootTagPath = CONTENT_PATH;
		Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
		List<Tag> childTags = new ArrayList<>();
		childTags.add(fakeTag);
		Mockito.when(fakeTag.listChildren()).thenReturn(childTags.iterator());
		Assert.assertNotNull(tagUtil.listChildren(mockTagManager,rootTagPath));
		Assert.assertNotNull(tagUtil.listChildren(fakeTag));
	}

	/**
	 * Test create tag list.
	 */
	@Test
	public void testCreateTagList() {
		Tag fakeTag = Mockito.mock(Tag.class);
		List<Tag> tags = new ArrayList<>();
		tags.add(fakeTag);
		Assert.assertNotNull(tagUtil.createTagList(tags.iterator()));
	}
	
	/**
	 * Test get tag.
	 */
	@Test
	public void testGetTag() {
		final TagManager mockTagManager = Mockito.mock(TagManager.class);
		Tag fakeTag = Mockito.mock(Tag.class);
		Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		Assert.assertNotNull(tagUtil.getTag(mockTagManager, CAREER));
	}
	
	/**
	 * Test get tag name.
	 */
	@Test
	public void testGetTagName() {
		final TagManager mockTagManager = Mockito.mock(TagManager.class);
		Tag fakeTag = Mockito.mock(Tag.class);
		Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getName()).thenReturn(CAREER);
		Assert.assertNotNull(tagUtil.getTagName(mockTagManager, CAREER));
	}
	
	/**
	 * Test get tag title.
	 */
	@Test
	public void testGetTagTitle() {
		final TagManager mockTagManager = Mockito.mock(TagManager.class);
		Tag fakeTag = Mockito.mock(Tag.class);
		Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getTitle()).thenReturn(CAREER);
		Assert.assertNotNull(tagUtil.getTagTitle(mockTagManager, CAREER));
	}
	
	/**
	 * Test get localized tag title.
	 */
	@Test
	public void testGetLocalizedTagTitle() {
		final TagManager mockTagManager = Mockito.mock(TagManager.class);
		Tag fakeTag = Mockito.mock(Tag.class);
		Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getTitle(Mockito.any())).thenReturn(CAREER);
		Assert.assertNotNull(tagUtil.getLocalizedTagTitle(mockTagManager, new Locale(LOCALE), CAREER));
	}
	
	/**
	 * Test get tag value.
	 */
	@Test
	public void testGetTagValue() {
		final TagManager mockTagManager = Mockito.mock(TagManager.class);
		Tag fakeTag = Mockito.mock(Tag.class);
		context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
		Mockito.when(fakeTag.getName()).thenReturn(CAREER);
		Assert.assertNotNull(tagUtil.getTagValue(fakeTag));
	}
	
	/**
	 * Test get tag value from tag id.
	 */
	@Test
	public void testGetTagValueFromTagId() {
		final TagManager mockTagManager = Mockito.mock(TagManager.class);
		Tag fakeTag = Mockito.mock(Tag.class);
		Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getName()).thenReturn(CAREER);
		context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);		
		Assert.assertNotNull(tagUtil.getTagValueFromTagId(context.currentResource(currentResPath),CAREER));
	}
	
	/**
	 * Test get map from tags.
	 */
	@Test
	public void testGetMapFromTags() {
		Assert.assertNotNull(tagUtil.getMapFromTags(getResourceResolver(),CONTENT_PATH));
	}
	
	/**
	 * Test create product state map from tags.
	 */
	@Test
	public void testCreateProductStateMapFromTags() {
		String[] states = {"corteva:consumerRegionCountry/NA/US/CA"};
		Locale locale = new Locale("en");
		tagmanager = Mockito.mock(TagManager.class);
		resourceResolver = Mockito.mock(ResourceResolver.class);
		Tag fakeTag = Mockito.mock(Tag.class);
		Mockito.when(resourceResolver.adaptTo(TagManager.class)).thenReturn(tagmanager);
		Mockito.when(tagmanager.resolve(Mockito.anyString())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getName()).thenReturn("California");
		Mockito.when(fakeTag.getTagID()).thenReturn("corteva:regionCountryState/US/CA");
		Assert.assertNotNull(tagUtil.createProductStateMapFromTags(states, resourceResolver, locale));
	}
	
}
