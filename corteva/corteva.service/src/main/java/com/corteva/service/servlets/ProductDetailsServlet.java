package com.corteva.service.servlets;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.TagUtil;
import com.corteva.model.component.models.ProductDocumentModel;
import com.corteva.model.component.models.ProductLabelModel;
import com.corteva.model.component.models.ProductRegistrationModel;
import com.corteva.model.component.models.SafetyDocumentModel;
import com.corteva.service.api.APIClient;
import com.corteva.service.api.ApiException;
import com.corteva.service.api.ApiResponse;
import com.corteva.service.product.detail.dto.AgrianProduct;
import com.corteva.service.product.detail.dto.AgrianRegulatoryData;
import com.corteva.service.product.detail.dto.Document;
import com.corteva.service.product.detail.dto.Documents;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is the servlet class to get the product details by product ID. 
 */
@Component(immediate = true, service = Servlet.class, property = {
		CortevaConstant.SLING_SERVLET_PATHS + CortevaConstant.PDP_SERVLET_URL,
		CortevaConstant.SLING_SERVLET_METHODS + CortevaConstant.GET,
		CortevaConstant.SLING_SERVLET_EXTENSIONS + CortevaConstant.JSON })
public class ProductDetailsServlet extends AbstractServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7815015307250280937L;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductDetailsServlet.class);

	/** The base service. */
	private transient BaseConfigurationService configurationService;

	/** The Constant CQ_STATE_LIST Map Key */
	private static final String CQ_STATE_LIST = "cq:stateList";

	/**
	 * The Constant TYPE_SUPPLEMENTAL_LABEL to hold document type Supplemental Label
	 */
	private static final String TYPE_FEDERAL_SUPPLEMENTAL_LABEL = "Supplemental Label";

	/** The Constant TYPE_RECOMMENDATION_BULLETIN to hold document type 2EE */
	private static final String TYPE_RECOMMENDATION_BULLETIN = "2EE";

	/** The Constant DOWNLOAD_DISPLAY_DATE. */
	private static final String DOWNLOAD_DISPLAY_DATE = "downloadDisplayDate";

	/**
	 * The ascii code for &nbsp.
	 */
	private static final String ASCII_CONSTANT_SPACE = "\u00A0";

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.api.servlets.SlingSafeMethodsServlet#doPost(org.apache.sling
	 * .api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)
	 */
	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		LOGGER.debug("Entering doGet of ProductDetailsServlet");
		boolean featureFlag = true;
		if (null != configurationService) {
			featureFlag = configurationService.getToggleInfo(CortevaConstant.PRODUCT_DETAILS_SERVLET,
					CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
		}
		if (featureFlag) {
			AgrianRegulatoryData agrianRegulatoryData = null;
			try {
				String productId = null;
				String pdpSource = StringUtils.EMPTY;
				String currentPagePath = getPagePathFromRequest(request);
				String[] selector = request.getRequestPathInfo().getSelectors();
				if (selector.length > 1) {
					productId = selector[0];
					pdpSource = selector[1];
				} else {
					productId = getProductIdFromSelector(selector);
				}
				if (StringUtils.equalsIgnoreCase(pdpSource, CortevaConstant.AGRIAN) || StringUtils.isBlank(pdpSource)) {
					ApiResponse<AgrianRegulatoryData> apiResponse = APIClient.callAPI(
							configurationService.getPropValueFromConfiguration(CortevaConstant.PRODUCT_CONFIG_NAME,
									"productDetailServicePath") + productId,
							AgrianRegulatoryData.class);
					agrianRegulatoryData = createLicensedState(apiResponse, request);
				} else {
					agrianRegulatoryData = getNonAgrianData(request, productId, pdpSource, currentPagePath);
				}
			} catch (ApiException e) {
				LOGGER.error("Exception occured during execution of get product details", e);
				response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			ObjectMapper mapper = new ObjectMapper();
			LOGGER.debug("Exiting doGet of ProductDetailsServlet");
			response.setContentType(CortevaConstant.CONTENT_TYPE_JSON);
			response.setCharacterEncoding(CortevaConstant.CHARACTER_ENCODING_UTF_8);
			response.getWriter().write(mapper.writeValueAsString(agrianRegulatoryData));
		} else {
			LOGGER.debug("Feature Flag is turned off for {}", this.getClass().getName());
		}
	}

	/**
	 * Gets the product id from selector.
	 *
	 * @param selector
	 *            the selector
	 * @return the product id from selector
	 */
	private String getProductIdFromSelector(String[] selector) {
		LOGGER.debug("Inside getProductIdFromSelector() method");
		String productId = null;
		if (selector.length > 0) {
			productId = selector[0];
		}
		LOGGER.debug("Product ID :: {}", productId);
		return productId;
	}

	/**
	 * Gets the non agrian data.
	 *
	 * @param request
	 *            the request
	 * @param productId
	 *            the product id
	 * @param pdpSource
	 *            the pdp source
	 * @param currentPagePath
	 *            the current page path
	 * @return the non agrian data
	 */
	private AgrianRegulatoryData getNonAgrianData(SlingHttpServletRequest request, String productId, String pdpSource,
			String currentPagePath) {
		LOGGER.debug("Inside getNonAgrianData() method");
		AgrianRegulatoryData agrianRegulatoryData = new AgrianRegulatoryData();
		if (StringUtils.isNotBlank(currentPagePath)
				&& StringUtils.equalsIgnoreCase(pdpSource, CortevaConstant.NON_AGRIAN)) {
			Map<String, String> regionLangMap = CommonUtils.getRegionCountryLanguage(currentPagePath,
					request.getResourceResolver());
			agrianRegulatoryData = getNonAgrianData(request, regionLangMap, productId);
		}
		LOGGER.debug("Non Agrian Regulatory Data :: {}", agrianRegulatoryData);
		return agrianRegulatoryData;
	}

	/**
	 * Creates the licensed state.
	 *
	 * @param apiResponse
	 *            the api response
	 * @param request
	 *            the request
	 * @return the agrian regulatory data
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private AgrianRegulatoryData createLicensedState(ApiResponse<AgrianRegulatoryData> apiResponse,
			final SlingHttpServletRequest request) throws IOException {
		LOGGER.debug("Entering createLicensedState of ProductDetailsServlet");
		AgrianRegulatoryData agrianRegulatoryData = apiResponse.getData();
		TagManager tagManager = request.getResourceResolver().adaptTo(TagManager.class);
		List<Tag> listChildren = TagUtil.listChildren(tagManager,
				configurationService.getPropValueFromConfiguration(CortevaConstant.PRODUCT_CONFIG_NAME,
						CortevaConstant.STATE_BASE_TAG_PATH) + "NA" + CortevaConstant.FORWARD_SLASH
						+ CortevaConstant.US);
		Map<String, String> licensedState = new HashMap<>();
		List<String> unlicensedStatesFromService = agrianRegulatoryData.getAgrianProduct().getUnlicensedAreas()
				.getStates().getState();
		for (Tag tag : listChildren) {
			if (!unlicensedStatesFromService.contains(tag.getName())) {

				licensedState.put(tag.getName(),
						tag.getTitle().replaceAll(ASCII_CONSTANT_SPACE, StringUtils.EMPTY).trim());
			}
		}
		List<Document> documentList = agrianRegulatoryData.getAgrianProduct().getDocuments().getDocument();
		if (null != documentList) {
			filterStatesForDocuments(documentList, unlicensedStatesFromService, licensedState);
			Collections.sort(documentList, alphabeticalDocumentComp);
		}
		agrianRegulatoryData.getAgrianProduct().setLicensedState(licensedState);
		ProductRegistrationModel model = new ProductRegistrationModel();
		String licensedStateMap;
		licensedStateMap = model.processLicensedStatesMap(licensedState, "us", request.getResourceResolver());
		if (null != licensedStateMap) {
			agrianRegulatoryData.getAgrianProduct().setLicensedStateMap(licensedStateMap);
		}
		LOGGER.debug("Exiting createLicensedState of ProductDetailsServlet");
		return agrianRegulatoryData;
	}

	/**
	 * @param docList
	 *            the list of documents
	 * @param unlicensedStates
	 *            the list of unlicensed states
	 * @param licensedStates
	 *            the map of licensed states
	 */
	private void filterStatesForDocuments(List<Document> docList, List<String> unlicensedStates,
			Map<String, String> licensedStates) {
		for (Document document : docList) {
			String filteredStates = null;
			String statesFromDocument = document.getStates();
			if (StringUtils.isNotBlank(document.getType())
					&& (StringUtils.equals(TYPE_FEDERAL_SUPPLEMENTAL_LABEL, document.getType())
							|| StringUtils.equals(TYPE_RECOMMENDATION_BULLETIN, document.getType()))) {
				LOGGER.debug("{} :: statesFromDocument :: {}", document.getDescription(), statesFromDocument);

				if (StringUtils.isNotBlank(statesFromDocument)) {
					filteredStates = filterStatesForDocument(statesFromDocument, unlicensedStates);
				} else {
					List<String> licensedStatesList = new ArrayList<>(licensedStates.keySet());
					Collections.sort(licensedStatesList);
					filteredStates = licensedStatesList.toString();
				}
			} else {
				filteredStates = filterStatesForDocument(statesFromDocument, null);
			}
			if (StringUtils.isNotBlank(filteredStates) && (StringUtils.length(filteredStates) > 1)) {
				filteredStates = filteredStates.substring(1, filteredStates.length() - 1);
			} else {
				filteredStates = StringUtils.EMPTY;
			}
			LOGGER.debug("filteredStates :: {}", filteredStates);
			document.setStates(filteredStates);
		}
	}

	/**
	 * @param statesStr
	 *            states from document
	 * @param unlicensedStates
	 *            the list of unlicensed states
	 * @return states filtered states
	 */
	private String filterStatesForDocument(String statesStr, List<String> unlicensedStates) {
		String states = null;
		String[] splitStates = StringUtils.split(statesStr, CortevaConstant.COMMA);
		if (null != splitStates && splitStates.length > 0) {
			Arrays.sort(splitStates);
			List<String> docStates = new ArrayList<>(Arrays.asList(splitStates));
			if (null != unlicensedStates) {
				docStates.removeAll(unlicensedStates);
			}
			states = docStates.toString();
		}
		return states;
	}

	/**
	 * Gets the non agrian data.
	 *
	 * @param request
	 *            the request
	 * @param regionLangMap
	 *            the region lang map
	 * @param productId
	 *            the product id
	 * @return the non agrian data
	 */
	private AgrianRegulatoryData getNonAgrianData(SlingHttpServletRequest request, Map<String, String> regionLangMap,
			String productId) {
		LOGGER.debug("Entering getNonAgrianData of ProductDetailsServlet");
		AgrianRegulatoryData agrianRegulatoryData = new AgrianRegulatoryData();
		String pdpRootPath = configurationService.getPropertyValueFromConfiguration(regionLangMap,
				CortevaConstant.PDP_ROOT_PATH, CortevaConstant.PRODUCT_CONFIG_NAME);
		pdpRootPath = CommonUtils.getPdpRootPath(regionLangMap, pdpRootPath);
		Resource pageRes = CommonUtils.findNonAgrianPdpResource(request.getResourceResolver(), pdpRootPath, productId);
		if (null != pageRes) {
			Resource pageResource = pageRes.getChild(CortevaConstant.JCR_CONTENT);
			if (null != pageResource) {
				ValueMap vMap = pageResource.getValueMap();
				String productName = null;
				if (!vMap.isEmpty()) {
					productName = vMap.containsKey(CortevaConstant.PRODUCT_NAME)
							? vMap.get(CortevaConstant.PRODUCT_NAME, String.class)
							: null;
				}
				Resource productLabelRes = CommonUtils.getResourceOfType(pageResource.getChild(CortevaConstant.ROOT),
						CortevaConstant.PRODUCT_LABEL_RES_TYPE);
				Resource productRegRes = CommonUtils.getResourceOfType(pageResource.getChild(CortevaConstant.ROOT),
						CortevaConstant.PRODUCT_REGISTRATION_RES_TYPE);
				List<Document> docsList = createDocumentList(request, productLabelRes, regionLangMap);
				if (!docsList.isEmpty()) {
					AgrianProduct agrianProduct = new AgrianProduct();
					Documents docs = new Documents();
					docs.setDocument(docsList);
					agrianProduct.setDocuments(docs);
					agrianProduct.setLicensedState(
							createStateListForNonAgrianProducts(request, regionLangMap, productRegRes));
					agrianProduct.setProductId(productId);
					agrianProduct.setProductName(productName);
					agrianProduct.setCountryCode(getCountryCode(regionLangMap));
					agrianRegulatoryData.setAgrianProduct(agrianProduct);
				}
			}
		}
		LOGGER.debug("Exiting getNonAgrianData of ProductDetailsServlet");
		return agrianRegulatoryData;
	}

	/**
	 * Creates the state list for non agrian products.
	 *
	 * @param request
	 *            the request
	 * @param regionLangMap
	 *            the region lang map
	 * @param productRegRes
	 *            the product reg res
	 * @return the map
	 */
	private Map<String, String> createStateListForNonAgrianProducts(SlingHttpServletRequest request,
			Map<String, String> regionLangMap, Resource productRegRes) {
		LOGGER.debug("Entering createStateListForNonAgrianProducts() method");
		Map<String, String> licensedStateMap = new HashMap<>();
		if (null != productRegRes) {
			licensedStateMap = fetchStateTagsBasedOnRegion(productRegRes, regionLangMap, request.getResourceResolver());
		}
		LOGGER.debug("Licensed State Map for Non Agrian Products :: {}", licensedStateMap);
		return licensedStateMap;
	}

	/**
	 * Creates the document list.
	 *
	 * @param request
	 *            the request
	 * @param productLabelRes
	 *            the product label res
	 * @param regionLangMap
	 * 			  the region language map
	 * @return the list
	 */
	private List<Document> createDocumentList(SlingHttpServletRequest request, Resource productLabelRes, Map<String, String> regionLangMap) {
		List<Document> docsList = new ArrayList<>();
		LOGGER.debug("Entering createDocumentList() method");
		if (null != productLabelRes) {
			ProductLabelModel productLabelModel = productLabelRes.adaptTo(ProductLabelModel.class);
			if (null != productLabelModel) {
				docsList = createNonAgrianDocumentsList(productLabelModel, request.getResourceResolver(), request, regionLangMap);
			}
		}
		LOGGER.debug("Document List :: {}", docsList);
		return docsList;
	}

	/**
	 * Creates the non agrian documents list.
	 *
	 * @param productLabelModel
	 *            the product label model
	 * @param resolver
	 *            the resolver
	 * @param request
	 *            the request
	 * @param regionLangMap
	 * 			  the region langugage map
	 * @return the list
	 */
	private List<Document> createNonAgrianDocumentsList(ProductLabelModel productLabelModel,
			ResourceResolver resolver, SlingHttpServletRequest request, Map<String, String> regionLangMap) {
		LOGGER.debug("Entering createNonAgrianDocumentsList() method of Product Details Servlet");
		List<Document> docsList = new ArrayList<>();
		boolean productDisplayDate = Boolean.parseBoolean(productLabelModel.getProductDisplayDate());
		boolean displayTime = false;
		boolean safetyDisplayDate = Boolean.parseBoolean(productLabelModel.getSafetyDisplayDate());
		Document doc;
		List<ProductDocumentModel> productDocList = productLabelModel.getProductDocItemList();
		if (null != productDocList && !productDocList.isEmpty()) {
			for (ProductDocumentModel prdDocModel : productDocList) {
				doc = new Document();
				doc.setFilename(prdDocModel.getProductDocumentLink());
				doc.setDescription(prdDocModel.getProductDocumentLabel());
				doc.setDisplayDate(
						displayDocumentDate(productDisplayDate, displayTime, prdDocModel.getProductDocumentLink(), resolver, request, regionLangMap));
				doc.setType("Label");
				docsList.add(doc);
			}
		}
		List<SafetyDocumentModel> safetyDocList = productLabelModel.getSafetyDocItemList();
		if (null != safetyDocList && !safetyDocList.isEmpty()) {
			for (SafetyDocumentModel safetyDocModel : safetyDocList) {
				doc = new Document();
				doc.setFilename(safetyDocModel.getSafetyDocumentLink());
				doc.setDescription(safetyDocModel.getSafetyDocumentLabel());
				doc.setDisplayDate(
						displayDocumentDate(safetyDisplayDate, displayTime, safetyDocModel.getSafetyDocumentLink(), resolver, request, regionLangMap));
				doc.setType("MSDS");
				docsList.add(doc);
			}
		}
		LOGGER.debug("Document List for Non Agrian Products :: {}", docsList);
		return docsList;
	}

	/**
	 * Fetch state tags based on region.
	 *
	 * @param productRegRes
	 *            the product reg res
	 * @param regionStateLanguageMap
	 *            the region state language map
	 * @param resolver
	 *            the resolver
	 * @return the map
	 */
	private Map<String, String> fetchStateTagsBasedOnRegion(Resource productRegRes,
			Map<String, String> regionStateLanguageMap, ResourceResolver resolver) {
		LOGGER.debug("Entering fetchStateTagsBasedOnRegion() method");
		Map<String, String> stateTagsMap = new HashMap<>();
		if (null != regionStateLanguageMap && !regionStateLanguageMap.isEmpty()) {
			boolean containsKey = regionStateLanguageMap.containsKey(CortevaConstant.COUNTRY)
					&& regionStateLanguageMap.containsKey(CortevaConstant.LANGUAGE);
			if (containsKey) {
				String country = regionStateLanguageMap.get(CortevaConstant.COUNTRY);
				String language = regionStateLanguageMap.get(CortevaConstant.LANGUAGE);
				ValueMap vMap = productRegRes.getValueMap();
				if (!vMap.isEmpty() && vMap.containsKey(CQ_STATE_LIST + country)) {
					String[] stateTags = vMap.get(CQ_STATE_LIST + country, String[].class);
					stateTagsMap = TagUtil.createProductStateMapFromTags(stateTags, resolver, new Locale(language));
				}
			}
		}
		LOGGER.debug("States Tags Map from Product Registration component for Non Agrian products :: {}", stateTagsMap);
		return stateTagsMap;
	}

	/**
	 * Gets the country code.
	 *
	 * @param regionCountryLang
	 *            the region country lang
	 * @return the country code
	 */
	private String getCountryCode(Map<String, String> regionCountryLang) {
		LOGGER.debug("Inside getCountryCode() method");
		boolean containsKey = regionCountryLang.containsKey(CortevaConstant.COUNTRY);
		String countryCode = StringUtils.EMPTY;
		if (containsKey) {
			countryCode = regionCountryLang.get(CortevaConstant.COUNTRY);
		}
		LOGGER.debug("Country Code :: {}", countryCode);
		return countryCode;
	}

	/**
	 * Display document date.
	 *
	 * @param displayDate
	 *            the display date
	 * @param displayTime
	 *            the flag displayTime
	 * @param documentPath
	 *            the document path
	 * @param resolver
	 *            the resolver
	 * @param request
	 *            the request
	 * @param regionLangMap
	 * 			  the region Language Map
	 * @return the string
	 */
	private String displayDocumentDate(boolean displayDate, boolean displayTime, String documentPath, ResourceResolver resolver, SlingHttpServletRequest request, Map<String, String> regionLangMap) {
		LOGGER.debug("Inside displayDocumentDate() method");
		String date = null;
		if (displayDate) {
			Resource docResource = resolver.resolve(documentPath);
			Resource jcrResource = docResource
					.getChild(CortevaConstant.JCR_CONTENT + CortevaConstant.FORWARD_SLASH + CortevaConstant.METADATA);
			date = getFormattedDate(jcrResource, request, displayTime, regionLangMap);
		}
		LOGGER.debug("Display Date :: {}", date);
		return date;
	}

	/**
	 * Gets the formatted date.
	 *
	 * @param jcrResource
	 *            the jcr resource
	 * @param request
	 *            the request
	 * @param displayTime
	 *            the flag displayTime
	 * @param regionLangMap
	 * 			  the region Language Map
	 * @return the formatted date
	 */
	private String getFormattedDate(Resource jcrResource, SlingHttpServletRequest request, boolean displayTime, Map<String, String> regionLangMap) {
		LOGGER.debug("Entering getFormattedDate() method");
		String datePattern = configurationService.getPropertyValueFromConfiguration(regionLangMap, CortevaConstant.DATE_PATTERN, CortevaConstant.GLOBAL_CONFIG_NAME);
		datePattern = StringUtils.trim(datePattern);
		LOGGER.debug("The date pattern from config is: {}", datePattern);
		String date = null;
		if (null != jcrResource) {
			ValueMap vMap = jcrResource.getValueMap();
			if (!vMap.isEmpty() && vMap.containsKey(DOWNLOAD_DISPLAY_DATE)) {
					LocalDateTime localDateTime = CommonUtils.getDateFromValueMap(vMap.get(DOWNLOAD_DISPLAY_DATE)).toInstant()
							.atZone(ZoneId.systemDefault()).toLocalDateTime();
					Locale locale = CommonUtils.getLocaleObject(getPagePathFromRequest(request), request.getResourceResolver());
					date =  StringUtils.isNotBlank(datePattern) ? CommonUtils.getFormattedLocalizedDateForServlet(localDateTime, locale, displayTime, datePattern)
							: CommonUtils.getFormattedLocalizedDate(localDateTime, locale, displayTime, request, configurationService);
			}
		}
		LOGGER.debug("Exiting getFormattedDate() method");
		return date;
	}

	/**
	 * Comparator to sort Document list by document title and then by states.
	 */
	private static Comparator<Document> alphabeticalDocumentComp = new Comparator<Document>() {
		@Override
		public int compare(Document o1, Document o2) {
			int flag = 0;
			if (null != o1.getDescription() && null != o2.getDescription()) {
				flag = o1.getDescription().compareTo(o2.getDescription());
			}
			if (0 == flag && null != o1.getStates() && null != o2.getStates()) {
				flag = o1.getStates().compareTo(o2.getStates());
			}
			return flag;
		}
	};

	/**
	 * Gets the page path from request.
	 *
	 * @param request
	 *            the request
	 * @return the path of the page
	 */
	private String getPagePathFromRequest(SlingHttpServletRequest request) {
		LOGGER.debug("Inside getPagePathFromRequest() method");
		String pagePath = StringUtils.EMPTY;
		String currentPage = request.getParameter(CortevaConstant.CURRENT_PAGE);
		if (StringUtils.isNotBlank(currentPage)) {
			ResourceResolver resourceResolver = request.getResourceResolver();
			try {
				String resourcePath = new URI(currentPage).getPath();
				Resource res = resourceResolver.resolve(request, resourcePath);
				pagePath = res.getPath();
				LOGGER.debug("Path of the referred page is {} ", pagePath);
			} catch (URISyntaxException e) {
				LOGGER.error("Exception occured while fetching page path from referrer", e);
			}
		}
		return pagePath;
	}
}