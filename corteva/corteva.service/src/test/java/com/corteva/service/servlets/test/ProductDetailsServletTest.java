/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
*/
package com.corteva.service.servlets.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.service.servlets.ProductDetailsServlet;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

/**
 * The Class ProductDetailsServletTest.
 */
public class ProductDetailsServletTest extends BaseAbstractTest {

	/** The Article feed servlet. */
	private ProductDetailsServlet productDetailsServlet;

	/** The mock request. */
	@Mock
	private MockSlingHttpServletRequest mockRequest;

	/** The mock response. */
	@Mock
	private MockSlingHttpServletResponse mockResponse;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The resource resolver. */
	@Mock
	private ResourceResolver resourceResolver;

	/** The mock resource. */
	@Mock
	private Resource mockResource;

	/** The page mgr. */
	@Mock
	private PageManager pageMgr;

	/** The page. */
	@Mock
	private Page page;

	/** The mock session. */
	private Session mockSession;

	/** The tag manager. */
	@Mock
	private TagManager tagManager;

	/**
	 * Sets the method parameters and adds nodes to the mock session.
	 */
	@Before
	public void setUp() {
		try {
			MockitoAnnotations.initMocks(this);
			productDetailsServlet = new ProductDetailsServlet();
			context.registerInjectActivateService(new BaseConfigurationService());
			Mockito.when(mockRequest.getResourceResolver()).thenReturn(resourceResolver);
			Mockito.when(resourceResolver.resolve(Mockito.any(SlingHttpServletRequest.class), Mockito.anyString()))
					.thenReturn(mockResource);
			productDetailsServlet.bindBaseConfigurationService(baseService);
			Mockito.when(baseService.getToggleInfo(CortevaConstant.PRODUCT_DETAILS_SERVLET,
					CortevaConstant.FEATURE_FLAG_CONFIG_NAME)).thenReturn(true);
			StringWriter responseWriter = new StringWriter();
			Mockito.when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
			mockSession = getResourceResolver().adaptTo(Session.class);
			final TagManager mockTagManager = Mockito.mock(TagManager.class);
			Mockito.when(resourceResolver.adaptTo(TagManager.class)).thenReturn(mockTagManager);
			Tag fakeTag = Mockito.mock(Tag.class);
			Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
			Mockito.when(fakeTag.getTagID()).thenReturn("AL");
			Mockito.when(fakeTag.getName()).thenReturn("AL");
			Mockito.when(fakeTag.getTitle()).thenReturn("Alabama");
			context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
			List<Tag> childTags = new ArrayList<>();
			childTags.add(fakeTag);
			Mockito.when(fakeTag.listChildren()).thenReturn(childTags.iterator());
			mockResource = context.create()
					.resource(CortevaConstant.PATH_TO_JSON_FILE + CortevaConstant.FORWARD_SLASH + "us"
							+ CortevaConstant.DOT + CortevaConstant.JSON_LOWERCASE + CortevaConstant.FORWARD_SLASH
							+ CortevaConstant.JCR_CONTENT);
			Node node = mockResource.adaptTo(Node.class);
			try {
				node.setProperty("jcr:data", "{\"type\": \"FeatureCollection\",\r\n" + "\"features\": [\r\n"
						+ "{ \"type\": \"Feature\", \"id\": 0, \"properties\": { \"UF\": \"AC\", \"presence\": 1, \"name\": \"Acre\", \"REGIAO\": \"NO\" }, \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [ -68.61895, -11.129709 ], [ -68.685345, -11.156667 ], [ -68.771162, -11.178199 ], [ -68.833969, -11.138409 ], [ -68.862165, -11.080244 ], [ -68.88022, -11.049307 ], [ -68.931174, -11.049095 ],[ -68.61895, -11.129709 ] ] ] } }]}");
			} catch (RepositoryException e) {
				Assert.fail("RepositoryException occurred in testGetRegisteredStatesForUS" + e.getMessage());
			}
			Mockito.when(resourceResolver.getResource(CortevaConstant.PATH_TO_JSON_FILE + CortevaConstant.FORWARD_SLASH
					+ "us" + CortevaConstant.DOT + CortevaConstant.JSON_LOWERCASE + CortevaConstant.FORWARD_SLASH
					+ CortevaConstant.JCR_CONTENT)).thenReturn(mockResource);
		} catch (Exception e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}
	}

	/**
	 * Test create get article feed when article type is All.
	 */
	@Test
	public void testdoGetProductDetail() {
		try {
			RequestPathInfo requestPathInfo = Mockito.mock(RequestPathInfo.class);
			String[] selector = new String[1];
			selector[0] = "9235";
			Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(requestPathInfo);
			Mockito.when(resourceResolver.getResource(Mockito.anyString())).thenReturn(mockResource);
			Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
			Mockito.when(baseService.getPropValueFromConfiguration(Mockito.anyString(), Mockito.anyString()))
					.thenReturn(
							"http://www.agrian.com/webservices/agrian_products.cfc?method=get_regulatory_data_v2&username=dp_cp_labelfinder&password=xfszUgJF&pdfs=false&product_id=");
			productDetailsServlet.doGet(mockRequest, mockResponse);
			Assert.assertNotEquals(500, mockResponse.getStatus());
		} catch (ServletException | IOException e) {
			Assert.fail("Exception occurred in testdoGetAllArticleType()" + e.getMessage());
		}
	}

	/**
	 * Testdo get product detail for non agrian products.
	 */
	@Test
	public void testdoGetProductDetailForNonAgrianProducts() {
		try {
			RequestPathInfo requestPathInfo = Mockito.mock(RequestPathInfo.class);
			String[] selector = new String[2];
			selector[0] = "us-non-agrian-product-1";
			selector[1] = "nonagrian";
			Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(requestPathInfo);
			Mockito.when(mockRequest.getParameter(CortevaConstant.CURRENT_PAGE))
					.thenReturn("/content/corteva/na/us/en/homepage/products/label-finder");
			Mockito.when(resourceResolver.getResource(Mockito.anyString())).thenReturn(mockResource);
			Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
			Mockito.when(baseService.getPropertyValueFromConfiguration(Mockito.anyMap(), Mockito.anyString(),
					Mockito.anyString())).thenReturn("d.M.yy");
			Node mockPage = mockSession.getRootNode().addNode("/content/corteva/na/us");
			mockPage.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, "cq:Page");
			context.create().resource("/content/corteva/na/us");
			page = context.pageManager().create("/content/corteva/na/us", "en", "/apps/sample/templates/homepage",
					"en");
			Resource res = context.create().resource("/content/corteva/na/us/en/homepage/products/label-finder");
			Mockito.when(pageMgr.getContainingPage(res)).thenReturn(page);
			Mockito.when(mockRequest.getResource()).thenReturn(res);
			Resource res1 = context.create().resource("/content/corteva/na");
			Mockito.when(resourceResolver.getResource("/content/corteva/na")).thenReturn(res1);
			Mockito.when(pageMgr.getContainingPage(res1)).thenReturn(page);
			Mockito.when(resourceResolver.resolve("/content/corteva/na/us/en/homepage/products/label-finder"))
					.thenReturn(res);
			Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageMgr);
			Mockito.when(
					resourceResolver.resolve(mockRequest, "/content/corteva/na/us/en/homepage/products/label-finder"))
					.thenReturn(res);
			createNonAgrianData();
			productDetailsServlet.doGet(mockRequest, mockResponse);
			Assert.assertNotEquals(500, mockResponse.getStatus());
		} catch (ServletException | IOException | RepositoryException | WCMException e) {
			Assert.fail("Exception occurred in testdoGetProductDetailForNonAgrianProducts()" + e.getMessage());
		}
	}

	/**
	 * Creates the non agrian data.
	 */
	private void createNonAgrianData() {
		String nonAgrianPdpRootPath = "/content/corteva/na/us/en/homepage/products";
		String nonAgrianPdpPagePath = "/content/corteva/na/us/en/homepage/products/us-non-agrian-product-1";
		try {
			Resource productRootRes = context.create().resource(nonAgrianPdpRootPath);
			Mockito.when(resourceResolver.resolve(nonAgrianPdpRootPath)).thenReturn(productRootRes);
			Resource res = context.create().resource(nonAgrianPdpPagePath);
			Mockito.when(resourceResolver.resolve(nonAgrianPdpPagePath)).thenReturn(res);
			Node prodRootNode = productRootRes.adaptTo(Node.class);
			Node resNode = res.adaptTo(Node.class);
			prodRootNode.addNode(nonAgrianPdpPagePath);
			Session session = Mockito.mock(Session.class);
			QueryBuilder queryBuilder = Mockito.mock(QueryBuilder.class);
			Query query = Mockito.mock(Query.class);
			Mockito.when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
			Mockito.when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
			Mockito.when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class)))
					.thenReturn(query);
			final List<Resource> results = new ArrayList<>();
			results.add(res);
			SearchResult searchResult = Mockito.mock(SearchResult.class);
			Node childNode = resNode.addNode(CortevaConstant.JCR_CONTENT);
			childNode.setProperty(CortevaConstant.PRODUCT_NAME, "US Non Agrian Product 1");
			Node rootChildNode = childNode.addNode(CortevaConstant.ROOT);
			Node productLabelNode = rootChildNode.addNode("productlabel");
			productLabelNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE, CortevaConstant.PRODUCT_LABEL_RES_TYPE);
			Node productRegNode = rootChildNode.addNode("productregistration");
			productRegNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE,
					CortevaConstant.PRODUCT_REGISTRATION_RES_TYPE);
			createProductLabelResource(productLabelNode);
			createProductRegResource(productRegNode);
			Mockito.when(query.getResult()).thenReturn(searchResult);
			Mockito.when(searchResult.getResources()).thenReturn(results.iterator());
			// Mockito.when(searchResult.getResources().next()).thenReturn(res);
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in createNonAgrianData()" + e.getMessage());
		}
	}

	/**
	 * Creates the product label resource.
	 *
	 * @param productLabelNode
	 *            the product label node
	 * @throws ItemExistsException
	 *             the item exists exception
	 * @throws PathNotFoundException
	 *             the path not found exception
	 * @throws VersionException
	 *             the version exception
	 * @throws ConstraintViolationException
	 *             the constraint violation exception
	 * @throws LockException
	 *             the lock exception
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private void createProductLabelResource(Node productLabelNode) throws ItemExistsException, PathNotFoundException,
			VersionException, ConstraintViolationException, LockException, RepositoryException {
		String docPath = "/content/dam/dpagco/corteva/NA/US/english/general-resources/test.pdf";
		productLabelNode.setProperty("productDisplayDate", CortevaConstant.TRUE);
		productLabelNode.setProperty("safetyDisplayDate", CortevaConstant.FALSE);
		Node productDocumentConfig = productLabelNode.addNode("productDocumentConfig");
		Node itemNode = productDocumentConfig.addNode("item0");
		itemNode.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, CortevaConstant.NT_UNSTRUCTURED);
		itemNode.setProperty("productDocumentLabel", "Gamma");
		itemNode.setProperty("productDocumentLink", docPath);
		Node safetyDocumentConfig = productLabelNode.addNode("safetyDocumentConfig");
		Node itemNodeMsds = safetyDocumentConfig.addNode("item0");
		itemNodeMsds.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, CortevaConstant.NT_UNSTRUCTURED);
		itemNodeMsds.setProperty("safetyDocumentLabel", "Beta");
		itemNodeMsds.setProperty("safetyDocumentLink", docPath);
		documentResource(docPath);
	}

	/**
	 * Creates the product reg resource.
	 *
	 * @param productRegNode
	 *            the product reg node
	 * @throws ValueFormatException
	 *             the value format exception
	 * @throws VersionException
	 *             the version exception
	 * @throws LockException
	 *             the lock exception
	 * @throws ConstraintViolationException
	 *             the constraint violation exception
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private void createProductRegResource(Node productRegNode) throws ValueFormatException, VersionException,
			LockException, ConstraintViolationException, RepositoryException {
		String[] states = { "corteva:regionCountryState/US/CA" };
		productRegNode.setProperty("cq:stateListUS", states);
		tagManager = Mockito.mock(TagManager.class);
		Mockito.when(resourceResolver.adaptTo(TagManager.class)).thenReturn(tagManager);
		Tag fakeTag = Mockito.mock(Tag.class);
		Mockito.when(tagManager.resolve(Mockito.anyString())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getName()).thenReturn("California");
		Mockito.when(fakeTag.getTagID()).thenReturn("corteva:regionCountryState/US/CA");
	}

	/**
	 * Document resource.
	 *
	 * @param documentPath
	 *            the document path
	 * @throws ItemExistsException
	 *             the item exists exception
	 * @throws PathNotFoundException
	 *             the path not found exception
	 * @throws VersionException
	 *             the version exception
	 * @throws ConstraintViolationException
	 *             the constraint violation exception
	 * @throws LockException
	 *             the lock exception
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private void documentResource(String documentPath) throws ItemExistsException, PathNotFoundException,
			VersionException, ConstraintViolationException, LockException, RepositoryException {
		Resource res = context.create().resource(documentPath);
		Mockito.when(resourceResolver.resolve(documentPath)).thenReturn(res);
		Node resNode = res.adaptTo(Node.class);
		Node childNode = resNode
				.addNode(CortevaConstant.JCR_CONTENT + CortevaConstant.FORWARD_SLASH + CortevaConstant.METADATA);
		childNode.setProperty("downloadDisplayDate", new SimpleDateFormat().getCalendar());
	}
}