package com.corteva.service.scheduler;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.mail.MailService;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.TagUtil;
import com.corteva.service.api.APIClient;
import com.corteva.service.api.ApiException;
import com.corteva.service.api.ApiResponse;
import com.corteva.service.product.detail.dto.AgrianProduct;
import com.corteva.service.product.detail.dto.AgrianRegulatoryData;
import com.corteva.service.product.dto.AgrianProducts;
import com.corteva.service.product.dto.Product;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

/**
 * The Class ProductScheduler.
 * 
 * @author sapient
 */
@Designate(ocd = ProductScheduler.Config.class)
@Component(service = Runnable.class)
public class ProductScheduler implements Runnable {

	/**
	 * The Interface Config.
	 */
	@ObjectClassDefinition(name = "Product Importer", description = "Product Importer")
	public static @interface Config {

		/**
		 * Scheduler expression.
		 * 
		 * @return the string
		 */
		@AttributeDefinition(name = "Scheduler expression")
		String scheduler_expression() default "0 0 0 1/1 * ? *";

		/**
		 * Product node path.
		 * 
		 * @return the string
		 */
		@AttributeDefinition(name = "Product Node Path")
		String productNodePath() default CortevaConstant.PRODUCT_NODE_PATH;

		/**
		 * Email recipient.
		 * 
		 * @return the string[]
		 */
		@AttributeDefinition(name = "Email Recipient list")
		String[] emailRecipient() default "DL-DigitalMarketingDevOpsSupport@pioneer.com";

		/**
		 * Email template path.
		 * 
		 * @return the string
		 */
		@AttributeDefinition(name = "Email Template path")
		String emailTemplatePath() default "/etc/notification/email/corteva/productimportfailurenotification";

	}

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductScheduler.class);

	/** The product node path. */
	private String productNodePath;

	/** The email recipient. */
	private String[] emailRecipient;

	/** The email template path. */
	private String emailTemplatePath;

	/** The resource resolver factory. */
	private ResourceResolverFactory resourceResolverFactory;

	/** The mail service. */
	private MailService mailService;

	/** The base service. */
	private transient BaseConfigurationService configurationService;

	/** The replicator. */
	private Replicator replicator;
	
	/** The sling settings service.	*/
	private SlingSettingsService slingSettingsService;
	
	/**
	 * Bind base configuration service.
	 * 
	 * @param replicator
	 *            the replicator
	 */
	@Reference
	public void bindReplicator(Replicator replicator) {
		this.replicator = replicator;
	}

	/**
	 * Unbind base configuration service.
	 * 
	 * @param replicator
	 *            the replicator
	 */
	public void unbindReplicator(Replicator replicator) {
		this.replicator = replicator;
	}

	/**
	 * Bind base configuration service.
	 * 
	 * @param configurationService
	 *            the base service
	 */
	@Reference
	public void bindBaseConfigurationService(BaseConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/**
	 * Unbind base configuration service.
	 * 
	 * @param configurationService
	 *            the configurationService
	 */
	public void unbindBaseConfigurationService(BaseConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/**
	 * Bind resource resolver factory.
	 * 
	 * @param resourceResolverFactory
	 *            the resource resolver factory
	 */
	@Reference
	public void bindResourceResolverFactory(ResourceResolverFactory resourceResolverFactory) {
		this.resourceResolverFactory = resourceResolverFactory;
	}

	/**
	 * Unbind resource resolver factory.
	 * 
	 * @param resourceResolverFactory
	 *            the resource resolver factory
	 */
	public void unbindResourceResolverFactory(ResourceResolverFactory resourceResolverFactory) {
		this.resourceResolverFactory = resourceResolverFactory;
	}

	/**
	 * Bind mail service.
	 * 
	 * @param mailService
	 *            the mail service
	 */
	@Reference
	public void bindMailService(MailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * Unbind mail service.
	 * 
	 * @param mailService
	 *            the mail service
	 */
	public void unbindMailService(MailService mailService) {
		this.mailService = mailService;
	}
	
	/**
	 * Bind sling settings service.
	 * 
	 * @param slingSettingsService
	 *            the slingSettingsService
	 */
	@Reference
	public void bindSlingSettingsService(SlingSettingsService slingSettingsService) {
		this.slingSettingsService = slingSettingsService;
	}

	/**
	 * Unbind sling settings service.
	 * 
	 * @param slingSettingsService
	 *            the slingSettingsService
	 */
	public void unbindSlingSettingsService(SlingSettingsService slingSettingsService) {
		this.slingSettingsService = slingSettingsService;
	}

	/**
	 * Bind base configuration service.
	 * 
	 * @param replicator
	 *            the replicator
	 */
	/**
	 * Activate.
	 * 
	 * @param config
	 *            the config
	 */
	@Activate
	protected void activate(final Config config) {

		LOGGER.info("activation of product importer");

		productNodePath = config.productNodePath();
		emailRecipient = config.emailRecipient();
		emailTemplatePath = config.emailTemplatePath();

		LOGGER.info("activation of Product Importer complete ");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		boolean enabled = configurationService.getToggleInfo(CortevaConstant.PRODUCT_CONFIG_NAME,
				"productSchedulerEnabled");
		if (enabled) {
			LOGGER.info("New Version of Product Data");
			long loadStart = Calendar.getInstance().getTimeInMillis();
			try {

				ApiResponse<AgrianProducts> apiResponse = APIClient.callAPI(configurationService
						.getPropValueFromConfiguration(CortevaConstant.PRODUCT_CONFIG_NAME, "productListServicePath"),
						AgrianProducts.class);

				LOGGER.info("API response received");
				updateProductNode(apiResponse.getData(), getResourceResolver());
				LOGGER.info("Node creation/updation done");
			} catch (ApiException | RepositoryException | ReplicationException | IOException e) {
				LOGGER.error("Exception occured during execution of Product Importer", e);
				sendEmail();
			}
			long loadEnd = Calendar.getInstance().getTimeInMillis();
			LOGGER.info("Time to load and parse Product data in sec = {}", ((loadEnd - loadStart) / 1000));
		}
	}

	/**
	 * Send email.
	 */
	private void sendEmail() {
		LOGGER.info("Inside method: sendEmail");
		
		try {
			String domainURL = configurationService.getPropValueFromConfiguration(CortevaConstant.GLOBAL_CONFIG_NAME, CortevaConstant.HOST_PREFIX);
			LOGGER.info("Domain is {}", domainURL);
			
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put(CortevaConstant.SERVER_NAME, CommonUtils.getEnvironment(slingSettingsService.getRunModes()));
			paramMap.put(CortevaConstant.SERVER_DOMAIN, domainURL);
			
			mailService.sendMail(Arrays.asList(emailRecipient), paramMap, emailTemplatePath);
			LOGGER.info("Exiting method: sendEmail");
		} catch (IOException e) {
			LOGGER.error("Exception occured during execution of sendEmail function in Product Importer", e);
		}
		
	}

	/**
	 * Updates the product nodes.
	 * 
	 * @param agrianProducts
	 *            the agrian products
	 * @param resourceResolver
	 *            the resource resolver
	 * @throws RepositoryException
	 *             the repository exception
	 * @throws ReplicationException
	 *             the replication exception
	 * @throws IOException
	 *             the io exception
	 * @throws ApiException
	 *             the api exception
	 */
	private void updateProductNode(AgrianProducts agrianProducts, ResourceResolver resourceResolver)
			throws RepositoryException, ReplicationException, ApiException, IOException {
		Session session = null;
		if (resourceResolver != null) {
			session = resourceResolver.adaptTo(Session.class);
		}
		if (session != null) {
			Node productNode = null;
			if (session.nodeExists(productNodePath)) {
				productNode = session.getNode(productNodePath);
			} else {
				productNode = JcrUtil.createPath(productNodePath, JcrConstants.NT_UNSTRUCTURED, session);
			}
			productNode.setProperty("createdDate", System.currentTimeMillis());
			for (Product product : agrianProducts.getProduct()) {
				if (product.getCompanyId().equals("1") || product.getCompanyId().equals("2")) {
					try {
						updateModifiedProducts(productNode, product, resourceResolver);
					} catch (RepositoryException | ApiException | IOException e) {
						LOGGER.error("Exception occured during updation of product {} in Product Importer: {}", product.getAgrianProductId(), e);
					}
				}
			}
			session.save();
			replicator.replicate(session, ReplicationActionType.ACTIVATE, productNodePath);
			LOGGER.info("Product Node Replicated.");
			resourceResolver.close();
		}
	}
	
	/**
	 * Updates the modified product nodes.
	 * 
	 * @param productNode
	 *            the product node
	 * @param product
	 *            the product from Agrian
	 * @param resourceResolver 
	 * 			  the resource resolver
	 * @throws RepositoryException
	 *             the repository exception
	 * @throws IOException
	 *             the io exception
	 * @throws ApiException
	 *             the api exception
	 */
	private void updateModifiedProducts(Node productNode, Product product, ResourceResolver resourceResolver) throws RepositoryException, ApiException, IOException {
		String pid = product.getAgrianProductId();
		Node pdpNode = null;
		boolean fetchFromAgrian = false;
		if (productNode.hasNode(pid)) {
			pdpNode = productNode.getNode(pid);
			Date nodeDate = null;
			Date agrianProductDate = null;
			if (pdpNode.hasProperty(CortevaConstant.PRODUCT_LASTMODIFIEDDATE_PROPERTY)) {
				String lastModifiedDate = pdpNode.getProperty(CortevaConstant.PRODUCT_LASTMODIFIEDDATE_PROPERTY).getString();
				try {
					nodeDate = new SimpleDateFormat(CortevaConstant.ABSOLUTE_DATE_FORMAT).parse(lastModifiedDate);
					agrianProductDate = new SimpleDateFormat(CortevaConstant.ABSOLUTE_DATE_FORMAT).parse(product.getLastChanged());
				} catch (ParseException e) {
					LOGGER.error("ParseException occurred during execution of createTempProductNode method of Product Importer :: {}", e);
				}
			}
			if (null == nodeDate || null == agrianProductDate || nodeDate.before(agrianProductDate) || !pdpNode.hasProperty(CortevaConstant.PRODUCT_LICENSED_STATES_PROPERTY)) {
				fetchFromAgrian = true;
				LOGGER.info("Updating node for PID {}", pid);
			} else {
				LOGGER.info("No update to node for PID {}", pid);
			}
		} else {
			pdpNode = productNode.addNode(pid);
			fetchFromAgrian = true;
			LOGGER.info("Creating node for PID {}", pid);
		}
		if (fetchFromAgrian) {
			ApiResponse<AgrianRegulatoryData> apiResponse = APIClient
					.callAPI(configurationService.getPropValueFromConfiguration(CortevaConstant.PRODUCT_CONFIG_NAME,
							"productDetailServicePath") + pid, AgrianRegulatoryData.class);
			if (null != apiResponse.getData().getAgrianProduct()) {
				populateProductNodeFromAgrian(pdpNode, apiResponse.getData().getAgrianProduct(), resourceResolver);
			}
		}
	}
	
	/**
	 * Populates the product node with Agrian data
	 * 
	 * @param childNode
	 *            the product child node
	 * @param agrianProduct
	 *            the agrian product
	 * @param resourceResolver 
	 * 			  the resource resolver
	 * @throws RepositoryException
	 *             the repository exception
	 * @throws IOException 
	 */
	private void populateProductNodeFromAgrian(Node childNode, AgrianProduct agrianProduct, ResourceResolver resourceResolver) throws RepositoryException, IOException {
		List<Tag> listChildren = TagUtil.listChildren(resourceResolver.adaptTo(TagManager.class),
				configurationService.getPropValueFromConfiguration(CortevaConstant.PRODUCT_CONFIG_NAME,
						CortevaConstant.STATE_BASE_TAG_PATH) + CortevaConstant.NA + CortevaConstant.FORWARD_SLASH
						+ CortevaConstant.US);
		List<String> licensedStates = new ArrayList<String>();
		List<String> unlicensedStates = new ArrayList<String>();
		List<String> unlicensedStatesFromService = agrianProduct.getUnlicensedAreas()
				.getStates().getState();
		for (Tag tag : listChildren) {
			if (unlicensedStatesFromService.contains(tag.getName())) {
				unlicensedStates.add(tag.getTagID());
			} else {
				licensedStates.add(tag.getTagID());
			}
		}
		childNode.setProperty(CortevaConstant.PRODUCT_ID_PROPERTY, agrianProduct.getProductId());
		childNode.setProperty(CortevaConstant.PRODUCT_NAME_PROPERTY, agrianProduct.getProductName());
		childNode.setProperty(CortevaConstant.PRODUCT_LASTMODIFIEDDATE_PROPERTY, agrianProduct.getLastChanged());
		childNode.setProperty(CortevaConstant.PRODUCT_UNLICENSED_STATES_PROPERTY, unlicensedStates.toArray(new String[unlicensedStates.size()]));
		childNode.setProperty(CortevaConstant.PRODUCT_LICENSED_STATES_PROPERTY, licensedStates.toArray(new String[licensedStates.size()]));
	}

	/**
	 * Gets the resourceResolver from resourceresolverfactory.
	 * 
	 * @return the resourceResolver
	 */
	public ResourceResolver getResourceResolver() {
		LOGGER.info("entering  getResourceResolver method");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, CortevaConstant.SYSTEM_USER_SERVICE);
		ResourceResolver resourceResolver = null;
		if (resourceResolverFactory != null) {
			try {
				resourceResolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
			} catch (LoginException loginException) {
				LOGGER.error("LoginException occured in getTemplateMap method of MailServiceImpl", loginException);
			}
		}
		LOGGER.info("exiting  getResourceResolver method");
		return resourceResolver;

	}
}
