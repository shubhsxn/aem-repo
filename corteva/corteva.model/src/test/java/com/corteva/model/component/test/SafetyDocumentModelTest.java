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
import com.corteva.model.component.models.SafetyDocumentModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * This is a test class for Safety Document Model.
 * 
 * @author Sapient
 * 
 */
public class SafetyDocumentModelTest extends BaseAbstractTest {

	/** The mock base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The mock safety document model. */
	@InjectMocks
	private SafetyDocumentModel safetyDocModel;

	/** The safety document label. */
	private String safetyDocumentLabel;

	/** The safety document link. */
	private String safetyDocumentLink;

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
	 * This method instantiates a new instance of Safety Document Model.
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
		Field safetyDocumentLabelField;
		safetyDocumentLabelField = safetyDocModel.getClass().getDeclaredField("safetyDocumentLabel");
		safetyDocumentLabelField.setAccessible(true);
		safetyDocumentLabelField.set(safetyDocModel, safetyDocumentLabel);
		Field safetyDocumentLinkField;
		safetyDocumentLinkField = safetyDocModel.getClass().getDeclaredField("safetyDocumentLink");
		safetyDocumentLinkField.setAccessible(true);
		safetyDocumentLinkField.set(safetyDocModel, safetyDocumentLink);
		Field currentResourceField;
		currentResourceField = safetyDocModel.getClass().getDeclaredField("currentResource");
		currentResourceField.setAccessible(true);
		currentResourceField.set(safetyDocModel, currentResource);
		Field resolverField;
		resolverField = safetyDocModel.getClass().getSuperclass().getDeclaredField("resourceResolver");
		resolverField.setAccessible(true);
		resolverField.set(safetyDocModel, resolver);
		Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageMgr);
		Mockito.when(pageMgr.getContainingPage(currentResource)).thenReturn(page);
	}

	/**
	 * Test get safety document label.
	 */
	@Test
	public void testGetSafetyDocumentLabel() {
		Node pageNode = page.getContentResource().adaptTo(Node.class);
		try {
			pageNode.setProperty("productName", "Test Product Name");
			Assert.assertNotNull(safetyDocModel.getSafetyDocumentLabel());
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetSafetyDocumentLabel()" + e.getMessage());
		}

	}

	/**
	 * Test get safety document label for other case.
	 */
	@Test
	public void testGetSafetyDocumentLabelForOtherCase() {
		Assert.assertNull(safetyDocModel.getSafetyDocumentLabel());

	}

	/**
	 * Test get safety document link.
	 */
	@Test
	public void testGetSafetyDocumentLink() {
		Assert.assertNull(safetyDocModel.getSafetyDocumentLink());
	}

	/**
	 * Test compare to.
	 */
	@Test
	public void testCompareTo() {
		safetyDocModel.setSafetyDocumentLabel("testLabel");
		Assert.assertEquals(0, safetyDocModel.compareTo(safetyDocModel));
	}

	/**
	 * Test equals.
	 */
	@Test
	public void testEquals() {
		Assert.assertEquals(true, safetyDocModel.equals(safetyDocModel));
	}

	/**
	 * Test hash code.
	 */
	@Test
	public void testHashCode() {
		Assert.assertNotNull(safetyDocModel.hashCode());
	}

	/**
	 * Test set safety document label.
	 */
	@Test
	public void testSetProductDocumentLabel() {
		safetyDocModel.setSafetyDocumentLabel("testLabel");
	}
}