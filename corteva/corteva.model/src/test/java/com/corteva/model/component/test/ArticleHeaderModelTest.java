package com.corteva.model.component.test;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.model.component.models.ArticleHeaderModel;
import com.corteva.model.component.models.ProductRegistrationModel;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

/**
 * This is a test class for Accordion Model.
 * 
 * @author Sapient
 * 
 */
public class ArticleHeaderModelTest extends BaseAbstractTest {

	/** The experience fragment model. */
	@Mock
	private ArticleHeaderModel articleHeaderModel;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	@Mock
	private MockSlingHttpServletRequest mockRequest;

	@Mock
	private Page page;

	/** The resource resolver. */
	@Mock
	private ResourceResolver resourceResolver;
	/** The resource resolver factory. */
	@Mock
	private ResourceResolverFactory resourceResolverFactory;

	Locale locale = new Locale("en", "US");

	/** The resource. */
	@Mock
	private Resource resource;

	/** The regional page resource. */
	@Mock
	private Resource regionalPageResource;

	private PageManager pageMgr;

	/** The ValueMap. */
	@Mock
	private ValueMap valueMap;

	@InjectMocks
	private ProductRegistrationModel productRegistrationModel = new ProductRegistrationModel();

	/**
	 * This method instantiates a new instance of Cards Container Sling Model.
	 */
	@Before
	public void setUp() {
		try {
			MockitoAnnotations.initMocks(this);
			baseService = new BaseConfigurationService();
			getContext().registerInjectActivateService(baseService);
			getContext().addModelsForPackage("com.corteva.model.component.models");
			mockRequest = getRequest();
			mockRequest.setServletPath("/content/corteva/na/US/en");
			String currentResPath = "/content/corteva/na/us/en/home/our-merger/jcr:content";

			String regionalPagePath = "/content/corteva/na";
			String pagePath = "content/corteva/na/us/en/homepage";

			context.create().resource(currentResPath);
			context.currentResource(currentResPath);
			context.create().resource("/content/corteva/na");
			context.currentResource("/content/corteva/na");

			articleHeaderModel = getRequest().adaptTo(ArticleHeaderModel.class);

			resourceResolver = Mockito.mock(ResourceResolver.class);
			Mockito.when(resource.getResourceResolver()).thenReturn(resourceResolver);

			pageMgr = Mockito.mock(PageManager.class);
			pageMgr = resourceResolver.adaptTo(PageManager.class);
			Field resolverField;
			resolverField = articleHeaderModel.getClass().getDeclaredField("resourceResolver");
			resolverField.setAccessible(true);
			resolverField.set(articleHeaderModel, resourceResolver);

			page = Mockito.mock(Page.class);
			Mockito.when(page.getPath()).thenReturn("/content/corteva/na/us/en/homepage");
			Mockito.when(resourceResolver.getResource("/content/corteva/na")).thenReturn(regionalPageResource);

			Field viewTypeField;
			viewTypeField = articleHeaderModel.getClass().getDeclaredField("currentPage");
			viewTypeField.setAccessible(true);
			viewTypeField.set(articleHeaderModel, page);
			Mockito.when(page.getProperties()).thenReturn(createPagePropertyValueMap());

			resource = Mockito.mock(Resource.class);
			Mockito.when(page.getProperties()).thenReturn(createPagePropertyValueMap());
			context.pageManager().create("/content/corteva/na/us/en", "homepage", "/apps/sample/templates/homepage",
					"homepage");

			pageMgr = Mockito.mock(PageManager.class);
			Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageMgr);
			Mockito.when(resourceResolver.getResource(regionalPagePath)).thenReturn(regionalPageResource);
			Mockito.when(pageMgr.getContainingPage(regionalPageResource)).thenReturn(page);
			Mockito.when(page.getContentResource()).thenReturn(resource);
			Mockito.when(page.getPath()).thenReturn("/content/corteva/na/us/en/homepage");
			Mockito.when(resource.getValueMap()).thenReturn(valueMap);
			Mockito.when(valueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY))
					.thenReturn(CortevaConstant.CORPORATE_TAG);


		} catch (NoSuchFieldException e) {
			Assert.fail("Repository Exception occurred in setUp(): " + e.getMessage());
		} catch (SecurityException e) {
			Assert.fail("Security Exception occurred in setUp(): " + e.getMessage());
		} catch (IllegalArgumentException e) {
			Assert.fail("Illegal Argument Exception occurred in setUp(): " + e.getMessage());
		} catch (IllegalAccessException e) {
			Assert.fail("Illegal Access Exception occurred in setUp(): " + e.getMessage());
		} catch (WCMException e) {
			Assert.fail("Illegal Access Exception occurred in setUp(): " + e.getMessage());
		}

	}

	/* This method creates Temp Page PRoperties Value Map. */
	private ValueMap createTemplateValueMap() {
		Map<String, Object> pagePropMap = new HashMap<>();
		pagePropMap.put("imageSource", "james");
		return new ValueMapDecorator(pagePropMap);
	}

	/* This method creates Temp Page PRoperties Value Map. */
	private ValueMap createPagePropertyValueMap() {
		Map<String, Object> pagePropMap = new HashMap<>();
		pagePropMap.put("author", "authorName");
		Calendar cal = Calendar.getInstance();
		cal.setTime(cal.getTime());
		pagePropMap.put(CortevaConstant.CQ_LAST_MODIFIED, cal);
		pagePropMap.put("displayDate", cal);
		pagePropMap.put("cq:articleType", (Object) "corteva:content/Blog");
		return new ValueMapDecorator(pagePropMap);
	}

	@Test
	public void testGetAuthorText() {
		Assert.assertEquals("authorName", articleHeaderModel.getAuthorText());
	}

	@Test
	public void testGetCustomDisplayDate() {
		Assert.assertEquals(
				CommonUtils.getFormattedLocalizedDate(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()),
						CommonUtils.getLocaleObject(page.getPath(), resourceResolver), false, mockRequest, baseService),
				articleHeaderModel.getCustomDisplayDate());
	}

	@Test
	public void testGetLastModifiedDate() {
		Assert.assertEquals(
				CommonUtils.getFormattedLocalizedDate(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()),
						CommonUtils.getLocaleObject(page.getPath(), resourceResolver), false, mockRequest, baseService),
				articleHeaderModel.getLastModifiedDate());
	}

	@Test
	public void testGetArticleType() {
		final TagManager mockTagManager = Mockito.mock(TagManager.class);
		Tag fakeTag = Mockito.mock(Tag.class);

		Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getTitle()).thenReturn("Blog");
		context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
		// Assert.assertEquals("Blog", articleHeaderModel.getArticleType());
	}

	@Test
	public void testGetHeroImageSource() {
		try {
			Field viewTypeField;
			viewTypeField = articleHeaderModel.getClass().getDeclaredField("fileReference");
			viewTypeField.setAccessible(true);
			viewTypeField.set(articleHeaderModel, "/content/corteva/na/us/en/jcr:content/root/responsivegrid");
			ValueMap properties = createTemplateValueMap();
			Mockito.when(resource.getValueMap()).thenReturn(properties);
			Assert.assertNotNull(articleHeaderModel.getHeroImageSource());
		} catch (NoSuchFieldException e) {
			Assert.fail("NoSuchFieldException  occurred in testGetHeroImageSource(): " + e.getMessage());
		} catch (SecurityException e) {
			Assert.fail("SecurityException  occurred in testGetHeroImageSource(): " + e.getMessage());
		} catch (IllegalArgumentException e) {
			Assert.fail("IllegalArgumentException  occurred in testGetHeroImageSource(): " + e.getMessage());
		} catch (IllegalAccessException e) {
			Assert.fail("IllegalAccessException  occurred in testGetHeroImageSource(): " + e.getMessage());
		}
	}

}
