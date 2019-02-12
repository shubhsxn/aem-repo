package com.corteva.model.component.test;

import java.lang.reflect.Field;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.ProductDocumentModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * This is a test class for Product Document Model.
 * 
 * @author Sapient
 * 
 */
public class ProductDocumentModelTest extends BaseAbstractTest {

	/** The mock base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The mock product document model. */
	@InjectMocks
	private ProductDocumentModel productModel;

	/** The product document label. */
	private String productDocumentLabel;

	/** The product document link. */
	private String productDocumentLink;

	/** The current resource. */
	@Mock
	private Resource currentResource;

	/** The resolver. */
	@Mock
	private ResourceResolver resolver;

	/** The page. */
	@Mock
	private Page page;

	/** The page mgr. */
	@Mock
	private PageManager pageMgr;

	/** The properties. */
	@Mock
	private ValueMap properties;

	/** The mock session. */
	private Session mockSession;

	/**
	 * This method instantiates a new instance of Product Document Model.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		context.registerInjectActivateService(new BaseConfigurationService());
		ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		Configuration myServiceConfig;
		myServiceConfig = configAdmin.getConfiguration("com.corteva.core.configurations.ProductConfigurationService");
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(CortevaConstant.PDF_DOCUMENT_ROOT_PATH, "/testpath");
		myServiceConfig.update(props);
		mockSession = getResourceResolver().adaptTo(Session.class);
		baseService = context.getService(BaseConfigurationService.class);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		Node mockPage = mockSession.getRootNode().addNode("/content/corteva/na/us");
		mockPage.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, "cq:Page");
		context.create().resource("/content/corteva/na/us");
		page = context.pageManager().create("/content/corteva/na/us", "en", "/apps/sample/templates/homepage", "en");
		baseService = context.getService(BaseConfigurationService.class);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		Field productDocumentLabelField;
		productDocumentLabelField = productModel.getClass().getDeclaredField("productDocumentLabel");
		productDocumentLabelField.setAccessible(true);
		productDocumentLabelField.set(productModel, productDocumentLabel);
		Field productDocumentLinkField;
		productDocumentLinkField = productModel.getClass().getDeclaredField("productDocumentLink");
		productDocumentLinkField.setAccessible(true);
		productDocumentLinkField.set(productModel, productDocumentLink);
		Field currentResourceField;
		currentResourceField = productModel.getClass().getDeclaredField("currentResource");
		currentResourceField.setAccessible(true);
		currentResourceField.set(productModel, currentResource);
		Field resolverField;
		resolverField = productModel.getClass().getSuperclass().getDeclaredField("resourceResolver");
		resolverField.setAccessible(true);
		resolverField.set(productModel, resolver);
		Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageMgr);
		Mockito.when(pageMgr.getContainingPage(currentResource)).thenReturn(page);
	}

	/**
	 * Test get product document label.
	 */
	@Test
	public void testGetProductDocumentLabel() {
		Node pageNode = page.getContentResource().adaptTo(Node.class);
		try {
			pageNode.setProperty("productName", "Test Product Name");
			Assert.assertNotNull(productModel.getProductDocumentLabel());
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetProductDocumentLabel()" + e.getMessage());
		}

	}

	/**
	 * Test get product document label for other case.
	 */
	@Test
	public void testGetProductDocumentLabelForOtherCase() {
		Assert.assertNull(productModel.getProductDocumentLabel());
	}

	/**
	 * Test get product document link.
	 */
	@Test
	public void testGetProductDocumentLink() {
		Assert.assertNull(productModel.getProductDocumentLink());
	}

	/**
	 * Test compare to.
	 */
	@Test
	public void testCompareTo() {
		productModel.setProductDocumentLabel("testLabel");
		Assert.assertEquals(0, productModel.compareTo(productModel));
	}

	/**
	 * Test equals.
	 */
	@Test
	public void testEquals() {
		Assert.assertEquals(true, productModel.equals(productModel));
	}

	/**
	 * Test hash code.
	 */
	@Test
	public void testHashCode() {
		Assert.assertNotNull(productModel.hashCode());
	}

	/**
	 * Test set product document label.
	 */
	@Test
	public void testSetProductDocumentLabel() {
		productModel.setProductDocumentLabel("testLabel");
	}
}