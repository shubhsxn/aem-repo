package com.corteva.core.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
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
import com.corteva.core.workflow.EmailProcessStep;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;




/**
 * The Class EmailProcessStepTest.
 */
public class EmailProcessStepTest extends BaseAbstractTest {

	/** The workflow session. */
	@Mock
	private WorkflowSession workflowSession;

	/** The process. */
	@InjectMocks
	private EmailProcessStep process;

	/** The sling context. */
	@Rule
	public final SlingContext slingContext = new SlingContext();

	/** The resource resolver factory. */
	@Mock
	private ResourceResolverFactory resourceResolverFactory;
	
	/** The resource. */
	@Mock
	private Resource resource;

	/** The mail service. */
	@Mock
	MailService mailService;

	/** The resource resolver. */
	@Mock
	private ResourceResolver resourceResolver;

	/** The user manager. */
	@Mock
	private UserManager userManager;

	/** The auth. */
	@Mock
	private Group auth;

	/** The auth receiver. */
	@Mock
	private Authorizable authReceiver;

	/** The users. */
	@Mock
	Iterator<Authorizable> users;

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

	/** The param map. */
	private Map<String, Object> paramMap;

	/** The email. */
	@Mock
	private Value email;
	
	/** The name. */
	@Mock
	private Value name;

	/** The group. */
	@Mock
	private Group group;

	/** The Constant PAGE_PATH. */
	private static final String PAGE_PATH = "/content/corteva/na/us/en/home/our-merger";

	/** The Constant OUR_MERGER. */
	private static final String OUR_MERGER = "our-merger";

	/** The Constant LOCAL_HOST. */
	private static final String LOCAL_HOST = "localhost:4502";

	/** The Constant USER_PROPERTY_EMAIL_ADDRESS. */
	private static final String USER_PROPERTY_EMAIL_ADDRESS = "profile/email";
	
	/** The Constant USER_PROPERTY_FAMILY_NAME. */
	private static final String USER_PROPERTY_FAMILY_NAME = "profile/familyName";
	
	/** The Constant USER_PROPERTY_GIVEN_NAME. */
	private static final String USER_PROPERTY_GIVEN_NAME = "profile/givenName";

	/** The Constant absoluteDate. */
	private static final String ABSOLUTE_DATE = "2018-06-09T07:45+10:00";

	/** The Constant PROCESS_ARGS. */
	private static final String PROCESS_ARGS = "PROCESS_ARGS";

	/** The Constant PUBLISHER. */
	private static final String PUBLISHER = "publisher";
	
	/** The Constant TEST. */
	private static final String TEST = "test" ;

	/** The work item. */
	private WorkItem workItem;

	/** The meta data. */
	private MetaDataMap metaData;

	/** The obj. */
	private Object obj = PAGE_PATH;
	

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Map<String, Object> serviceParamMap = new HashMap<String, Object>();
		workItem = Mockito.mock(WorkItem.class);
		metaData = new SimpleMetaDataMap();
		metaData.put(CortevaConstant.ABSOLUTE_DATE, ABSOLUTE_DATE);
		serviceParamMap.put(ResourceResolverFactory.SUBSERVICE, "mailService");
		paramMap = new HashMap<String, Object>();
		paramMap.put(CortevaConstant.PAYLOAD_LINK,
				"http://localhost:4502/editor.html/content/corteva/na/us/en/home/our-merger.html");
		paramMap.put(CortevaConstant.PAYLOAD_TITLE, OUR_MERGER);
		paramMap.put(CortevaConstant.AUTHOR_NAME, "TEST TEST");
		countryLangMap = new HashMap<String, String>();
		countryLangMap.put("country", "US");
		countryLangMap.put("language", "en");
		countryLangMap.put("region", "NA");
		slingContext.registerService(MailService.class, mailService);
		slingContext.registerService(BaseConfigurationService.class, baseConfigService);
		Mockito.when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resourceResolver);
		Mockito.when(resourceResolverFactory.getServiceResourceResolver(serviceParamMap)).thenReturn(resourceResolver);
		Mockito.when(mailService.getResourceResolver()).thenReturn(resourceResolver);
		Mockito.when(resourceResolver.adaptTo(UserManager.class)).thenReturn(userManager);
		setCommonProperties();
	}

	/**
	 * Test email to publisher user.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testEmailTouser() throws Exception {
		Mockito.when(auth.isGroup()).thenReturn(false);
		Mockito.when(((Group) auth).getMembers()).thenReturn(users);
		Mockito.when(users.hasNext()).thenReturn(true);
		setMetaDataMap();
		callExecuteMethod();
	}

	/**
	 * Test email to publisher initator.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testEmailToInitator() throws Exception {
		metaData.put(PROCESS_ARGS, "emailTemplate=/etc/notification/email/corteva/publishreviewnotification");
		Mockito.when(authReceiver.hasProperty(USER_PROPERTY_GIVEN_NAME)).thenReturn(true);
		callExecuteMethod();
	}

	/**
	 * Test email to group.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testEmailToGroup() throws Exception {
		Mockito.when(authReceiver.isGroup()).thenReturn(true);
		Mockito.when(resourceResolver.adaptTo(Group.class)).thenReturn(group);
		Mockito.when(users.hasNext()).thenReturn(true).thenReturn(false);
		setMetaDataMap();
		callExecuteMethod();
		setMetaDataMapForReviewer();
		callExecuteMethod();
	}
	
	/**
	 * Sets the meta data map.
	 */
	private void setMetaDataMap() {
		metaData.put(PROCESS_ARGS,
				"emailTemplate=/etc/notification/email/corteva/publishreviewnotification;receiverList=publisher");
	}
	
	/**
	 * Sets the meta data map.
	 */
	private void setMetaDataMapForReviewer() {
		metaData.put(PROCESS_ARGS,
				"emailTemplate=/etc/notification/email/corteva/publishreviewnotification;receiverList=reviewer");
	}

	/**
	 * Sets the common properties.
	 *
	 * @throws Exception
	 *             the exception
	 */
	private void setCommonProperties() throws RepositoryException {
		Value[] emails = { email };
		Value[] firstName = { name };
		authReceiver.setProperty(USER_PROPERTY_EMAIL_ADDRESS, email);
        auth.setProperty(USER_PROPERTY_GIVEN_NAME, firstName);
        auth.setProperty(USER_PROPERTY_FAMILY_NAME, firstName);
		Mockito.when(userManager.getAuthorizable(PUBLISHER)).thenReturn(authReceiver);
		Mockito.when(workItem.getWorkflowData()).thenReturn(workFlowData);
		Mockito.when(workFlowData.getPayload()).thenReturn(obj);
		Mockito.when(workItem.getWorkflow()).thenReturn(workFlow);
		Mockito.when(workFlowData.getMetaDataMap()).thenReturn(metaData);
		Mockito.when(workFlow.getMetaDataMap()).thenReturn(metaData);
		Mockito.when(mailService.getNodeFromResource(PAGE_PATH)).thenReturn(workItemNode);
		Mockito.when(workFlow.getInitiator()).thenReturn(TEST);
		Mockito.when(userManager.getAuthorizable(TEST)).thenReturn(authReceiver);
		Mockito.when(authReceiver.getProperty(USER_PROPERTY_GIVEN_NAME)).thenReturn(firstName);
		Mockito.when(firstName[0].getString()).thenReturn("TEST");
		Mockito.when(authReceiver.getProperty(USER_PROPERTY_FAMILY_NAME)).thenReturn(firstName);
		Mockito.when(firstName[0].getString()).thenReturn("TEST");
		Mockito.when(workItemNode.getPath()).thenReturn(PAGE_PATH);
		Mockito.when(baseConfigService.getPropertyValueFromConfiguration(countryLangMap, CortevaConstant.HOST_PREFIX,
				CortevaConstant.GLOBAL_CONFIG_NAME)).thenReturn(LOCAL_HOST);
		Mockito.when(workItemNode.getName()).thenReturn(OUR_MERGER);
		Mockito.when(authReceiver.getProperty(USER_PROPERTY_EMAIL_ADDRESS)).thenReturn(emails);
		Mockito.when(users.next()).thenReturn(authReceiver);
		Mockito.when(authReceiver.isGroup()).thenReturn(false);
		Mockito.when(emails[0].getString()).thenReturn("test@test.com");
		Mockito.when(resourceResolver.resolve(PAGE_PATH)).thenReturn(resource);
		Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
		Mockito.when(baseConfigService.getPropertyValueFromConfiguration(countryLangMap, CortevaConstant.HOST_PREFIX,
				CortevaConstant.GLOBAL_CONFIG_NAME)).thenReturn(LOCAL_HOST);
	}
	
	/**
	 * Call execute method.
	 * @throws RepositoryException 
	 *
	 * @throws Exception the exception
	 */
	private void callExecuteMethod() throws WorkflowException, RepositoryException {
		Mockito.when(workItem.getContentPath()).thenReturn("/content/corteva/na/en/us/homepage");
		Mockito.when(resourceResolver.getResource(Mockito.anyString())).thenReturn(resource);
		Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
		Mockito.when(page.getContentResource()).thenReturn(resource);
		ValueMap valueMap = Mockito.mock(ValueMap.class);
		Mockito.when(resource.getValueMap()).thenReturn(valueMap);
		Mockito.when(valueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY)).thenReturn("mock:tag");
		Mockito.when(userManager.getAuthorizable(Mockito.anyString())).thenReturn(authReceiver);
		process.execute(workItem, workflowSession, metaData);
	}
	
	/**
	 * Test Bind mail service.
	 */
	@Test
	public void bindMailService() {
		process.bindMailService(mailService);
	}

	/**
	 * Test Unbind mail service.
	 */
	@Test
	public void unbindMailService() {
		process.unbindMailService(mailService);
	}

	/**
	 * Test Bind base configuration service.
	 */
	@Test
	public void bindBaseConfigurationService() {
		process.bindBaseConfigurationService(baseConfigService);
	}

	/**
	 * Test Unbind base configuration service.
	 */
	@Test
	public void unbindBaseConfigurationService() {
		process.unbindBaseConfigurationService(baseConfigService);
	}
}
