package com.corteva.core.test;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.metadata.SimpleMetaDataMap;
import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.mail.MailService;
import com.corteva.core.workflow.ParticipantStepImpl;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * The Class ParticipantStepImplTest.
 */
public class ParticipantStepImplTest extends BaseAbstractTest {

	/** The workflow session. */
	@Mock
	private WorkflowSession workflowSession;

	/** The process. */
	@InjectMocks
	private ParticipantStepImpl process;

	/** The resource resolver factory. */
	@Mock
	private ResourceResolverFactory resourceResolverFactory;

	/** The resolver. */
	@Mock
	private ResourceResolver resolver;

	/** The resource. */
	@Mock
	private Resource resource;
	
	/** The jcr:content resource. */
	@Mock
	private Resource jcrContentResource;
	
	/** The valueMap. */
	@Mock
	private ValueMap valueMap;

	/** The work flow data. */
	@Mock
	private WorkflowData workFlowData;

	/** The work item node. */
	@Mock
	private Node workItemNode;

	/** The base config service. */
	@Mock
	private BaseConfigurationService baseConfigService;

	/** The work flow. */
	@Mock
	private Workflow workFlow;

	/** The page manager. */
	@Mock
	private PageManager pageManager;

	/** The page. */
	@Mock
	private Page page;

	/** The country lang map. */
	private Map<String, String> countryLangMap;

	/** The work item. */
	private WorkItem workItem;

	/** The meta data. */
	private MetaDataMap metaData;

	/** The mail service. */
	@Mock
	private MailService mailService;
	
	@Mock
	private Session mockSession;

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		workItem = Mockito.mock(WorkItem.class);
		metaData = new SimpleMetaDataMap();
		metaData.putIfAbsent(CortevaConstant.WORKFLOW_METADATA_PROCESS_ARGS, "role=reviewer");
		countryLangMap = new HashMap<String, String>();
		countryLangMap.put("country", "US");
		countryLangMap.put("language", "en");
		countryLangMap.put("region", "NA");
		mockSession = getResourceResolver().adaptTo(Session.class);
		context.registerService(BaseConfigurationService.class, baseConfigService);
		Mockito.when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resolver);
		Mockito.when(mailService.getResourceResolver()).thenReturn(resolver);
		pageManager = Mockito.mock(PageManager.class);
		context.create().resource("/content/corteva");
		page = Mockito.mock(Page.class);
		Mockito.when(resolver.getResource("/content/corteva/na"))
				.thenReturn(resource);
		Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
		Mockito.when(page.getContentResource()).thenReturn(jcrContentResource);
		Mockito.when(jcrContentResource.getValueMap()).thenReturn(valueMap);
		Mockito.when(valueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY)).thenReturn(StringUtils.EMPTY);
		Mockito.when(workItem.getContentPath()).thenReturn("/content/corteva/na/us/en/homepage/our-merger");
	}

	/**
	 * Test get participant from config.
	 */
	@Test
	public void testGetParticipantFromConfig() {
		Mockito.when(baseConfigService.getPropertyValueFromConfiguration(countryLangMap, "reviewer",
				CortevaConstant.USER_CONFIG_NAME)).thenReturn("us-reviewer");
		try {
			Assert.assertNotNull(callExecuteMethod());
		} catch (WorkflowException e) {
			Assert.fail("Workflow Exception occurred in testGetParticipantFromConfig()" + e.getMessage());
		}
	}

	/**
	 * Test get participant blank from config.
	 */
	@Test
	public void testGetParticipantBlankFromConfig() {
		Mockito.when(baseConfigService.getPropertyValueFromConfiguration(countryLangMap, "reviewer",
				CortevaConstant.USER_CONFIG_NAME)).thenReturn(StringUtils.EMPTY);
		try {
			Assert.assertNotNull(callExecuteMethod());
		} catch (WorkflowException e) {
			Assert.fail("Workflow Exception occurred in testGetParticipantFromConfig()" + e.getMessage());
		}
	}

	/**
	 * Call execute method.
	 *
	 * @throws WorkflowException
	 *             the workflow exception
	 */
	private String callExecuteMethod() throws WorkflowException {
		return process.getParticipant(workItem, workflowSession, metaData);
	}
}