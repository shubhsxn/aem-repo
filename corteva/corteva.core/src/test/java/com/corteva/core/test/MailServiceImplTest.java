package com.corteva.core.test;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Property;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.Email;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.mail.MailService;
import com.corteva.core.mail.imp.MailServiceImpl;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

/**
 * The Class MailServiceImplTest.
 */
public class MailServiceImplTest extends BaseAbstractTest {

	/** The sling context. */
	@Rule
	public final SlingContext slingContext = new SlingContext();

	/** The mail service. */
	@InjectMocks
	MailServiceImpl mailService;

	/** The resource resolver factory. */
	@Mock
	private ResourceResolverFactory resourceResolverFactory;

	/** The message gateway service. */
	@Mock
	private MessageGatewayService messageGatewayService;
	
	/** The base configuration service. */
	@Mock
	private BaseConfigurationService baseConfigurationService ;

	/** The message gateway html email. */
	@Mock
	private MessageGateway<Email> messageGatewayHtmlEmail;

	/** The resource resolver. */
	@Mock
	private ResourceResolver resourceResolver;

	/** The resource. */
	@Mock
	private Resource resource;

	/** The content. */
	@Mock
	private Node content;

	/** The property. */
	@Mock
	private Property property;

	/** The input stream. */
	@Mock
	private InputStream inputStream;

	/** The binary. */
	@Mock
	private Binary binary;

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Map<String, Object> serviceParamMap = new HashMap<>();
		serviceParamMap.put(ResourceResolverFactory.SUBSERVICE, "mailService");
		slingContext.registerService(MailService.class, mailService);
		inputStream = IOUtils.toInputStream("some test data for my input stream", "UTF-8");
		Mockito.when(resourceResolverFactory.getServiceResourceResolver(serviceParamMap)).thenReturn(resourceResolver);
		Mockito.when(resourceResolver.getResource("/etc/notification/email/corteva/publishreviewnotification"))
				.thenReturn(resource);
		Mockito.when(resource.adaptTo(Node.class)).thenReturn(content);
		Mockito.when(content.hasNode("en")).thenReturn(true);
		Mockito.when(content.getNode("en")).thenReturn(content);
		Mockito.when(content.getNode("text")).thenReturn(content);
		Mockito.when(content.getNode("jcr:content")).thenReturn(content);
		Mockito.when(content.getProperty(JcrConstants.JCR_DATA)).thenReturn(property);
		Mockito.when(property.getBinary()).thenReturn(binary);
		Mockito.when(binary.getStream()).thenReturn(inputStream);
		Mockito.when(messageGatewayService.getGateway(Email.class)).thenReturn(messageGatewayHtmlEmail);
		Mockito.when(baseConfigurationService.getToggleInfo(CortevaConstant.FEATURE_JAVA_FRAMEWORK, CortevaConstant.FEATURE_FLAG_CONFIG_NAME)).thenReturn(false, true);
	}

	/**
	 * Test sendmail.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSendmail() throws Exception {
		List<String> emailList = new ArrayList<>();
		emailList.add("test@test.com");
		String emailTemplate = "/etc/notification/email/corteva/publishreviewnotification";
		/**
		 * Invokes sendMail() with featureFlag value as false 
		 */
		mailService.sendMail(emailList, null, emailTemplate);
		/**
		 * Invokes sendMail() with featureFlag value as true
		 */
		mailService.sendMail(emailList, null, emailTemplate);
		assertNotNull(mailService.getResourceResolver());
		assertNotNull(mailService.getNodeFromResource(emailTemplate));
	}

}
