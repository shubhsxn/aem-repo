package com.corteva.model.component.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.model.component.models.BioDetailCardContainerModel;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;

/**
 * This is a test class for Bio Detail Card Container Model.
 * 
 * @author Sapient
 * 
 */

public class BioDetailCardContainerModelTest extends BaseAbstractTest {

	/** The experience fragment model. */
	@Mock
	private BioDetailCardContainerModel bioDetailCardContainerModel;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/**
	 * The mocked query builder.
	 */
	@Mock
	private QueryBuilder queryBuilder;
	/**
	 * The mocked tag object.
	 */
	@Mock
	private Tag tag;
	/**
	 * The mocked query object.
	 */
	@Mock
	private Query query;
	/**
	 * The mocked page object.
	 */
	@Mock
	private Page page;

	/**
	 * The mocked page object.
	 */
	@Mock
	private Page page2;

	/**
	 * The mocked page object.
	 */
	@Mock
	private Page page3;

	/**
	 * The mocked page object.
	 */
	@Mock
	private Page page4;

	/**
	 * The mocked resource object.
	 */
	@Mock
	private Resource mockResource;

	/**
	 * The mocked resource object.
	 */
	@Mock
	private Resource mockResource2;

	/**
	 * The mocked resource object.
	 */
	@Mock
	private Resource mockResource3;

	/**
	 * The mocked resource object.
	 */
	@Mock
	private Node mockNode;

	/**
	 * The mocked resource object.
	 */
	@Mock
	private Resource mockResource4;
	/**
	 * The mocked request object.
	 */
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	/**
	 * The mocked resource resolver object.
	 */
	@Mock
	private ResourceResolver resourceResolver;
	/**
	 * The mocked tagmanager object.
	 */
	@Mock
	private TagManager tagManager;

	@Mock
	private Property mockProperty;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		bioDetailCardContainerModel = getRequest().adaptTo(BioDetailCardContainerModel.class);
		context.registerAdapter(ResourceResolver.class, QueryBuilder.class, queryBuilder);
		context.registerAdapter(Resource.class, Page.class, page);
		mockRequest.setServletPath("/content/corteva/na/US/en");
		bioDetailCardContainerModel.setFolderPath("content/corteva/na/us/en/biography");
		bioDetailCardContainerModel.setResourceResolver(getResourceResolver());
		Mockito.when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class)))
				.thenReturn(query);
		final SearchResult searchResult = Mockito.mock(SearchResult.class);
		Mockito.when(query.getResult()).thenReturn(searchResult);
	}

	@Test
	public void testFindAllBioCardsNoResult() {
		final List<Resource> results = new ArrayList<>();
		try {
			Mockito.when(query.getResult().getResources()).thenReturn(results.iterator());
			bioDetailCardContainerModel.init();
			assertEquals(0, bioDetailCardContainerModel.getBioDetailPages().size());
		} catch (IllegalArgumentException e) {
			Assert.fail("Repository Exception occurred in testFindAllBioCardsNoResult(): " + e.getMessage());
		}
	}

	@Test
	public void testGetBioDetail() {
		try {
			BioDetailCardContainerModel bioModel = new BioDetailCardContainerModel(mockRequest);
			final List<Resource> results = new ArrayList<>();
			results.add(mockResource);
			results.add(mockResource2);
			results.add(mockResource3);
			results.add(mockResource4);
			String[] str = new String[1];
			str[0] = "corteva:country/US";
			Mockito.when(query.getResult().getResources()).thenReturn(results.iterator());
			Mockito.when(page.getProperties()).thenReturn(createTemplateValueMap(str, "1"));
			Mockito.when(page2.getProperties()).thenReturn(createTemplateValueMap(str, null));
			Mockito.when(page3.getProperties()).thenReturn(createTemplateValueMap(str, null));
			Mockito.when(page4.getProperties()).thenReturn(createTemplateValueMap(str, "1"));
			final TagManager mockTagManager = Mockito.mock(TagManager.class);
			Tag fakeTag = Mockito.mock(Tag.class);
			Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
			Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(page);
			Mockito.when(mockResource2.adaptTo(Page.class)).thenReturn(page2);
			Mockito.when(mockResource3.adaptTo(Page.class)).thenReturn(page3);
			Mockito.when(mockResource4.adaptTo(Page.class)).thenReturn(page4);
			Mockito.when(mockResource.getParent()).thenReturn(mockResource);
			Mockito.when(mockResource2.getParent()).thenReturn(mockResource2);
			Mockito.when(mockResource3.getParent()).thenReturn(mockResource3);
			Mockito.when(mockResource4.getParent()).thenReturn(mockResource4);
			context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
			final List<Resource> childCompNodes = new ArrayList<>();
			childCompNodes.add(mockResource);
			Mockito.when(page.getContentResource(Mockito.anyString())).thenReturn(mockResource);
			Mockito.when(page2.getContentResource(Mockito.anyString())).thenReturn(mockResource2);
			Mockito.when(page3.getContentResource(Mockito.anyString())).thenReturn(mockResource3);
			Mockito.when(page4.getContentResource(Mockito.anyString())).thenReturn(mockResource4);
			Mockito.when(mockResource.adaptTo(Node.class)).thenReturn(mockNode);

			Mockito.when(mockRequest.getResource()).thenReturn(mockResource);
			Mockito.when(mockRequest.getResource().getPath()).thenReturn("/content/corteva/na/us/en/homepage");

			Mockito.when(mockNode.hasProperty(Mockito.anyString())).thenReturn(true);
			Mockito.when(mockNode.hasProperty("bioHeadShotImagePath")).thenReturn(false);
			Mockito.when(mockNode.getProperty(Mockito.anyString())).thenReturn(mockProperty);
			Mockito.when(mockProperty.getString()).thenReturn("corteva/components/content/bioDetail/v1/bioDetail");
			mockRequest.setServletPath("/content/corteva/na/us/en/homepage");
			Mockito.when(mockResource.getChildren()).thenReturn(childCompNodes);
			Mockito.when(mockResource2.getChildren()).thenReturn(childCompNodes);
			Mockito.when(mockResource3.getChildren()).thenReturn(childCompNodes);
			Mockito.when(mockResource4.getChildren()).thenReturn(childCompNodes);
			bioModel.setFolderPath("/content/corteva/na/us/en/biography");
			Mockito.when(mockRequest.getResourceResolver()).thenReturn(getResourceResolver());
			bioModel.setResourceResolver(getResourceResolver());
			bioModel.init();
			assertEquals(4, bioModel.getBioDetailPages().size());
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testGetBioDetail(): " + e.getMessage());
		}
	}

	/* This method creates Page 1 Properties Value Map. */
	private ValueMap createTemplateValueMap(String[] str, String ranking) {
		Map<String, Object> pagePropMap = new HashMap<>();
		pagePropMap.put("cq:biographyTags", (Object) str);
		if (null != ranking) {
			pagePropMap.put("ranking", (Object) ranking);
		}

		Map<String, Object> props = pagePropMap;
		return new ValueMapDecorator(props);
	}

	@Test
	public void testGetBioDetailPages() {
		Assert.assertNotNull(bioDetailCardContainerModel.getBioDetailPages());
	}

	@Test
	public void testGetFolderPath() {
		Assert.assertNotNull(bioDetailCardContainerModel.getFolderPath());
	}

	@Test
	public void testGetTagsList() {
		Assert.assertNotNull(bioDetailCardContainerModel.getTagsList());
	}
}
