/*
 * ===========================================================================
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.

 * ===========================================================================
 */
package com.corteva.core.constants;

/**
 * Constant class to hold all Corteva project constants.
 *
 * @author Sapient
 */
public final class CortevaConstant {

	/**
	 * private constructor for denying direct instantiation.
	 */
	private CortevaConstant() {
		// No operation
	}

	public static final String NODE_PREFIX = "node";

	public static final String COUNTRY_BLANK_ERROR_MESSAGE = "Following row number does not have any Country name - ";

	public static final String SUCCESS_MESSAGE = "<h3 id=\"message\" style=\"color: rgb(0, 0, 255);\">All rows are successfully ingested.</h3>";

	public static final String CONFIG_ISSUE_MESSAGE = "<h3 id=\"message\" style=\"color: red;\">Some Issue with the servlet cofig. Please validate.</h3>";

	public static final String ERROR_MESSAGE = "<h3 id=\"message\" style=\"color: red;\">Error during ingestion. Please see logs for more details.</h3>";

	public static final String MESSAGE = "message";

	public static final String DATA = "data";

	public static final String AEM_BASE_PATH = "/etc/home";

	public static final String CSV_BASE_PATH_FALLBACK = "D:\\DuPont\\ProductData\\";

	public static final String RESPONSIVE_GRID = "responsivegrid_";

	public static final String CORTEVA_ROOT_PATH = "rootPath";

	public static final String REGION = "consumerRegionCountry";

	public static final String COUNTRY = "country";

	public static final String LANGUAGE = "language";

	public static final String CATEGORY = "category";

	public static final String SUBCATEGORY = "subcategory";

	public static final String DEV = "dev";

	public static final String QA = "qa";

	public static final String STAGE = "preprod";

	public static final String PROD = "prod";

	public static final String CORTEVA_QA = "test";

	public static final String JCR_CONTENT = "jcr:content";

	public static final String NT_UNSTRUCTURED = "nt:unstructured";

	public static final String LAST_MODIFIED_BY = "jcr:lastModifiedBy";

	public static final String JCR_LAST_MODIFIED = "jcr:lastModified";

	public static final String SLING_RESOURCE_TYPE = "sling:resourceType";

	public static final String JCR_UNDERSCORE_CONTENT = "_jcr_content";

	public static final String GLOBAL_CONFIG_NAME = "com.corteva.core.configurations.GlobalConfigurationService";

	public static final String IMAGE_CONFIG_NAME = "com.corteva.core.configurations.ImageConfigurationService";

	public static final String VIDEO_CONFIG_NAME = "com.corteva.core.configurations.VideoConfigurationService";

	public static final String ELOQUA_CONFIG_NAME = "com.corteva.core.configurations.EloquaConfigurationService";

	public static final String COMPONENT_CONFIG_NAME = "com.corteva.core.configurations.ComponentConfigurationService";

	/**
	 * The Constant FEATURE_FLAG_CONFIG_NAME for config pid of Feature Flag
	 * Configuration.
	 */
	public static final String FEATURE_FLAG_CONFIG_NAME = "com.corteva.core.configurations.FeatureFlagConfigurationService";

	/**
	 * The Constant SEARCH_CONFIG_NAME for config pid of Search Configuration.
	 */
	public static final String SEARCH_CONFIG_NAME = "com.corteva.core.configurations.SearchConfigurationService";

	/**
	 * The Constant FEATURE_JS_FRAMEWORK for jsFramework feature in Feature Flag
	 * Configuration.
	 */
	public static final String FEATURE_JS_FRAMEWORK = "jsFramework";

	/**
	 * The Constant FEATURE_JAVA_FRAMEWORK for javaFramework feature in Feature Flag
	 * Configuration.
	 */
	public static final String FEATURE_JAVA_FRAMEWORK = "javaFramework";

	public static final String COMMA = ",";

	public static final String IS_IMAGE = "/is/image";

	public static final String DOLLAR_SIGN = "$";

	public static final String QUESTION_MARK = "?";

	public static final String SPACE = " ";

	public static final String EXP_FRAGMENT_CONTAINER_RESOURCE = "/apps/corteva/components/content/experiencefragmentContainer/v1/experiencefragmentContainer";

	public static final String PATHFIELD_ROOT_PATH = "rootPath";

	public static final String EXP_FRAGMENT_ROOT_PATH = "/content/experience-fragments/corteva";

	public static final String FORWARD_SLASH = "/";

	public static final String EXP_FRAG_PATH = "expfragPath";

	public static final String SCENE_7_ROOT_PATH = "sceneSevenImageRoot";

	public static final String SCENE_7_VIDEO_ROOT_PATH = "sceneSevenVideoRoot";

	public static final String DOT = ".";

	public static final String CONTENT_ROOT = "/content";

	public static final String CONTENT_DAM_PATH = CortevaConstant.CONTENT_ROOT + "/dam";

	public static final String HTML_EXTENSION = "html";

	public static final String IMAGE_PNG = ".png";

	public static final String IMAGE_JPG = "jpg";

	public static final String IMAGE_GIF = "gif";

	public static final String IMAGE_BMP = "bmp";

	public static final String IMAGE_TIF = "tif";

	public static final String IMAGE_VCF = "vcf";

	public static final String IMAGE_JPEG = "jpeg";

	public static final String HTTP = "http://";

	public static final String HTTPS = "https://";
	
	public static final String SEO_SCHEMA = "schema.org";
	
	public static final String SEO_IMAGE_OBJECT = "ImageObject";
	
	public static final String SEO_ORGANISATION = "Organization";
	
	public static final String SEO_ARTICLE_OBJECT = "Article";
	
	public static final String ARTICLE_HEADER_COMPONENT_NODE = "/jcr:content/root/articleheader";
	
	public static final String GENERIC_IMAGE_PROPERTY = "fileReference";

	public static final String EXPERIENCE_FRAGMENTS = "experience-fragments";

	public static final int ONE = 1;

	public static final int TWO = 2;

	public static final int THREE = 3;

	public static final int FOUR = 4;

	public static final int FIVE = 5;

	public static final int SIX = 6;

	public static final int SEVEN = 7;

	public static final String UNDERSCORE = "_";

	public static final String RESPONSIVE_GRID_RESOURCE = "wcm/foundation/components/responsivegrid";

	public static final String EXP_FRAGMENT_COMP_PATH = "corteva/components/content/experienceFragments/v1/experienceFragments";

	public static final String FRAGMENT_PATH_NAME = "fragmentPath";

	public static final String CARD_VIEW = "cardView";

	public static final String JCR_PRIMARY_TYPE = "jcr:primaryType";

	public static final String CONTEXTUAL_PATH_BROWSER = "corteva/components/content/contextualpathbrowser";

	public static final String FIELD_LABEL = "fieldLabel";

	public static final String CLASS = "class";

	public static final String NAME = "name";

	public static final String REQUIRED = "required";

	public static final String VALUE = "value";

	public static final String CONTENT_TYPE_JSON = "application/json";

	public static final String CHARACTER_ENCODING_UTF_8 = "UTF-8";

	public static final String PAGE_PATH = "pagePath";

	public static final String JCR_TITLE = "jcr:title";

	public static final String JCR_DESCRIPTION = "jcr:description";

	public static final String JCR_CREATED = "jcr:created";

	public static final String MM_DD_YY = "MM/dd/yyyy";

	public static final String ASSET_PATH = "assetPath";

	public static final String PAGE_PROPERTIES_KEYS = "pagePropertiesKeys";

	public static final String IMAGE_KEY = "imageKey";

	public static final String DATE = "Date";

	public static final String ASSET_PROPERTIES_KEYS = "assetPropertiesKeys";

	public static final String METADATA = "metadata";

	public static final String CQ_LAST_MODIFIED = "cq:lastModified";

	public static final String TEXTFIELD_RESOURCE = "granite/ui/components/coral/foundation/form/textfield";

	public static final String BASEPAGE_RESOURCE = "corteva/components/structure/page-base";

	public static final String CQ_PAGE_CONTENT = "cq:PageContent";

	public static final String CQ_TEMPLATE = "cq:template";

	public static final String PRIMARY_IMAGE = "primaryImage";

	public static final String PAGE_JSON = "pageJson";

	public static final String IMAGE_JSON = "imageJson";

	public static final String PHANTOM_EXECUTABLE_LOCATION = "phantomExecutableLocation";

	public static final String PHANTOM_JS_LOCATION = "phantomJsLocation";

	public static final String DESTINATION_PDF_LOCATION = "destinationPdfLocation";

	public static final String PHANTOM_JS_CONFIG = "com.corteva.core.configurations.PhantomJSLocation";

	public static final String REFERER = "referer";

	public static final String CONTENT_TYPE_PDF = "application/pdf";

	public static final String COLON = ":";

	public static final String PDF_EXTN = "pdf";

	public static final String SUCCESS = "SUCCESS";

	public static final String ERROR = "ERROR";

	public static final String POST = "POST";

	public static final String JSON = "JSON";

	public static final String GET = "GET";

	public static final String BASE_PAGE_RESOURCE_TYPE = "corteva/components/structure/page-base";

	public static final int ARRAY_INIT = 5;

	public static final String HASH = "#";

	public static final String FLAG_ON = "on";

	public static final String FLAG_OFF = "off";

	/**
	 * The Constant to store the SLING SERVLET PATHS.
	 */
	public static final String SLING_SERVLET_PATHS = "sling.servlet.paths=";

	/**
	 * The Constant to store the SLING SERVLET METHODS.
	 */
	public static final String SLING_SERVLET_METHODS = "sling.servlet.methods=";

	/**
	 * The Constant to store the SLING SERVLET EXTENSIONS.
	 */
	public static final String SLING_SERVLET_EXTENSIONS = "sling.servlet.extensions=";

	/**
	 * The Constant to store the SLING SERVLET RESOURCE TYPES.
	 */
	public static final String SLING_SERVLET_RESOURCE_TYPES = "sling.servlet.resourceTypes=";

	public static final String SLING_SERVLET_SELECTORS = "sling.servlet.selectors=";

	/**
	 * The Constant to store ANCHOR NAV TEMPORARY NODE NAME.
	 */
	public static final String ANCHOR_NAV_TEMP_NODE = "anchorTempNodes";

	/**
	 * The Constant is used to store dynamic link for email.
	 */
	public static final String PAYLOAD_LINK = "payload.link";

	/**
	 * The Constant is used to store title for dynamic link for email.
	 */
	public static final String PAYLOAD_TITLE = "payload.title";

	/**
	 * The Constant is used to store host name for dynamic link for email
	 */
	public static final String HOST_PREFIX = "host.prefix";

	/**
	 * The Constant is used to store editor prefix in dynamic link for email
	 */
	public static final String EDITOR = "editor.html";

	/**
	 * The Constant is used to store separator for dynamic link
	 */
	public static final String SEPERATOR = "/";

	/**
	 * The Constant is used to store page absolute time for page publish and
	 * unpublish
	 */
	public static final String ABSOLUTE_TIME = "absoluteTime";

	/**
	 * The Constant is used to store page absolute date for page publish and
	 * unpublish
	 */
	public static final String ABSOLUTE_DATE = "absoluteDate";

	/**
	 * The Constant is used to store date format for absolute date
	 */
	public static final String ABSOLUTE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss Z";

	/**
	 * The Constant is used to store mail service for getting service resource
	 * resolver
	 */
	public static final String MAIL_SERVICE = "mailService";

	/**
	 * The Constant SYSTEM_USER_SERVICE.
	 */
	public static final String SYSTEM_USER_SERVICE = "cortevaAdminService";

	/**
	 * The Constant is used to get process arguments at 0 index for workflow process
	 */
	public static final int ZERO = 0;

	/**
	 * The Constant is used to get process arguments at 1 index for workflow process
	 */
	public static final String SEMI_COLON = ";";

	/**
	 * The Constant is used to split process arguments for workflow process
	 */
	public static final String EQUAL = "=";

	/**
	 * The Constant to hold OFF value of TRUE.
	 */
	public static final String TRUE = "true";

	/**
	 * The Constant to hold OFF value of FALSE.
	 */
	public static final String FALSE = "false";

	/**
	 * The Constant to hold two rows layout selection for fixed grid tiles
	 * container.
	 */
	public static final String TWO_ROWS_LAYOUT = "twoRows";

	/**
	 * The Constant to hold three rows layout selection for fixed grid tiles
	 * container.
	 */
	public static final String THREE_ROWS_LAYOUT = "threeRows";

	public static final String KEY = "key";

	public static final String HASH_SEPERATOR = "###";

	public static final String DATE_FORMAT = "dd MMM yyyy HH:mm:ss z";

	public static final String SITE_MAP_INDEX = "sitemapIndex";

	public static final String CRAWL_STRING = "index,follow";

	public static final String NO_CRAWL_STRING = "noindex,nofollow";

	public static final String TEXT = "text";

	/**
	 * The Constant for structured Node
	 */
	public static final String NODE_TYPE = "nt:unstructured";

	/**
	 * The Constant to hold OFF value of ALT TEXT.
	 */
	public static final String ALT_TEXT = "altText";

	/**
	 * The Constant to hold metadata IMAGE ALT TEXT.
	 */
	public static final String IMAGE_ALT_TEXT = "imageAltText";

	/**
	 * The Constant to hold metadata LOGO ALT TEXT.
	 */
	public static final String LOGO_ALT_TEXT = "logoAltText";

	/**
	 * The Constant to hold metadata ICON ALT TEXT.
	 */
	public static final String ICON_ALT_TEXT = "iconAltText";

	/**
	 * The Constant to hold OFF value of Title.
	 */
	public static final String TITLE = "title";

	/**
	 * The Constant to hold OFF value of Root Node inside content folder.
	 */
	public static final String ROOT = "root";

	/**
	 * The Constant to hold the cq:tags property
	 */
	public static final String CQ_TAGS = "cq:tags";

	public static final String CORPORATE_TAG = "corteva:corporate";

	/**
	 * The Constant to hold the tag root for product efficacy tags
	 */
	public static final String EFFICACY_TAG_ROOT = "corteva:efficacy";

	/**
	 * The Constant to hold YYYY-MM-dd date format
	 */
	public static final String DATE_FORMAT_YYYY_MM_DD = "YYYY-MM-dd";

	/**
	 * The Constant to hold value of corporare folder name in content path
	 */
	public static final String CORPORATE_CONTENT_NODE_NAME = "corporate";

	/**
	 * The Constant to hold value of base content root path
	 */
	public static final String CONTENT_ROOT_PATH = "/content/corteva";

	/**
	 * The Constant to hold value of corporate content root path
	 */
	public static final String CONTENT_CORPORATE_ROOT_PATH = "/content/corteva/corporate";

	/**
	 * The Constant to hold value of Hyphen
	 */
	public static final String HYPHEN = "-";

	/** Query Builder Constants */
	/**
	 * The Constant to hold type of property constant to Query
	 */
	public static final String PROPERTY_CONSTANT = "_property";
	/**
	 * The Constant to hold value of property constant to Query
	 */
	public static final String PROPERTY_VALUE_CONSTANT = "_property.value";
	/**
	 * The Constant to hold value of limit of results to retrieve
	 */
	public static final String PROPERTY_LIMIT = "p.limit";
	/**
	 * The Constant to hold value of constant offset
	 */
	public static final String PROPERTY_OFFSET = "p.offset";
	/**
	 * The Constant to hold value of order by clause
	 */
	public static final String PROPERTY_ORDER_BY = "_orderby";
	/**
	 * The Constant to hold value of order by Sorting
	 */
	public static final String PROPERTY_ORDERBY_SORT = "_orderby.sort";
	/**
	 * The Constant to hold value of hits property
	 */
	public static final String PROPERTY_HITS = "p.hits";
	/**
	 * The Constant to hold value of property that returns selected attribute in
	 * result
	 */
	public static final String PROPERTY_SELECTIVE_PROPERTIES = "p.properties";
	/**
	 * The Constant to hold value of property operation
	 */
	public static final String PROPERTY_OPERATION = "_property.operation";
	/**
	 * The Constant to hold value of primary type of Page
	 */
	public static final String PROPERTY_PAGE_TYPE = "cq:Page";
	/**
	 * The Constant to hold value of like operator
	 */
	public static final String PROPERTY_LIKE_OPERATOR = "%";
	/**
	 * The Constant to hold value of property like
	 */
	public static final String PROPERTY_LIKE = "like";
	/**
	 * The Constant to hold value of property exists
	 */
	public static final String PROPERTY_EXISTS = "exists";
	/**
	 * The Constant to hold value of property group by
	 */
	public static final String PROPERTY_GROUP_BY = "group";
	/**
	 * The Constant to hold value of @ operator
	 */
	public static final String PROPERTY_OPERATOR_AMPERSAND = "@";
	/**
	 * The Constant to hold value of default dropdown value
	 */
	public static final String ALL = "All";
	/**
	 * The Constant to hold value of default dropdown name for Year
	 */
	public static final String DEFAULT_SELECT_VALUE_YEAR = "Year";
	/**
	 * The Constant to hold value of default dropdown name for months
	 */
	public static final String DEFAULT_SELECT_VALUE_ALL_MONTHS = "All Months";
	/**
	 * The Constant to hold value of unlimited result in p.limit
	 */
	public static final String UNLIMITED_RESULT = "-1";
	/**
	 * The Constant to hold authorName for workflow
	 */
	public static final String AUTHOR_NAME = "authorName";

	/**
	 * The Constant to hold servlet name
	 */
	public static final String ARTICLE_FILTER_FEED_SERVLET = "articalFilterFeedServlet";
	public static final String CONTENT_META_DATA_SERVLET = "contentMetaDataServlet";
	public static final String CREATE_DELETE_NODE_SERVLET = "createDeleteNodeServlet";
	public static final String CREATE_PDF_SERVLET = "createPDFServlet";
	public static final String REORDER_NODES_SERVLET = "reorderNodesServlet";
	public static final String GET_META_DATA_SCHEMA_SERVLET = "getMetadataSchemaServlet";

	/**
	 * The Constant PRODUCT_FILTER_SERVLET.
	 */
	public static final String PRODUCT_FILTER_SERVLET = "productFilterServlet";
	public static final String PRODUCT_DETAILS_SERVLET = "productDetailsServlet";

	public static final String PRODUCT_CACHE_LISTENER = "productCacheListener";

	/**
	 * The Constant to hold content type property of Page Template
	 */
	public static final String CONTENT_TYPE = "contentType";

	public static final String CONTENT_TYPE_ARTICLE = "Article";

	/**
	 * The Constant ABSOLUTE_DATE_FORMAT.
	 */
	public static final String ABSOLUTE_DATE_FORMAT = "yyyy-MM-dd HH:mm";

	/**
	 * The Constant to hold jcr date timeformat
	 */
	public static final String JCR_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	public static final String ICON = "icon";

	/**
	 * The Constant HOT_SPOT_TITLE.
	 */
	public static final String HOT_SPOT_TITLE = "hotSpotTitle";

	/**
	 * The Constant HOT_SPOT_TEXT.
	 */
	public static final String HOT_SPOT_TEXT = "hotSpotText";

	/**
	 * The Constant HOT_SPOT_BUTTON_TITLE.
	 */
	public static final String HOT_SPOT_BUTTON_TITLE = "hotSpotButtonTitle";

	/**
	 * The Constant HOT_SPOT_BRACKET.
	 */
	public static final String HOT_SPOT_BRACKET = "]";

	/**
	 * The Constant MODAL_ID.
	 */
	public static final String MODAL_ID = "modalId";

	/**
	 * The Constant CAPTCHA_SITE_KEY.
	 */
	public static final String CAPTCHA_SITE_KEY = "captchaSiteKey";

	/**
	 * The Constant CAPTCHA_SECRET_KEY.
	 */
	public static final String CAPTCHA_SECRET_KEY = "captchaSecretKey";

	/**
	 * This Constant hold query parameter used in country selector
	 */
	public static final String PATH_FLAT = "path.flat";

	/**
	 * This Constant hold type parameter which is used in forming query in country
	 * selector
	 */
	public static final String TYPE = "type";

	/**
	 * This Constant hold root path property which is used in country selector to
	 * fetch region specific pages
	 */
	public static final String COUNTRY_SELECTOR_ROOT_PATH = "countrySelectorRootPath";

	/**
	 * Taxonomy Structure for consumer NA region US country tags
	 */
	public static final String CONSUMER_REGION_COUNTRY_TAXONOMY_STRUCTURE_FOR_NA_US = "/etc/tags/corteva/consumerRegionCountry/NA/US";

	/**
	 * Taxonomy Structure for consumer NA region CA country tags
	 */
	public static final String ARITCLE_L2_TAXONOMY_STRUCTURE_FOR_NA_CA = "/etc/tags/corteva/consumerRegionCountry/NA/CA";

	/**
	 * The Constant TAGGED_ASSET_PATH_SUFFIX to hold suffix of configuration
	 * property from product configuration service.
	 */
	public static final String TAGGED_ASSET_PATH_SUFFIX = "TaggedAssetsPath";

	/**
	 * The Constant PRODUCT_CONFIG_NAME.
	 */
	public static final String PRODUCT_CONFIG_NAME = "com.corteva.core.configurations.ProductConfigurationService";

	/**
	 * The Constant PDF_DOCUMENT_ROOT_PATH.
	 */
	public static final String PDF_DOCUMENT_ROOT_PATH = "pdfDocumentRootPath";

	/**
	 * The Constant LABEL_FINDER_PATH.
	 */
	public static final String LABEL_FINDER_PATH = "labelFinderPath";

	/**
	 * The Constant AGRIAN.
	 */
	public static final String AGRIAN = "agrian";

	/**
	 * This Constant holds the metadata schema property for dam folders
	 */
	public static final String METADATA_SCHEMA_PROPERTY = "metadataSchema";

	/**
	 * This Constant holds the parameter for dam folder path
	 */
	public static final String DAM_FOLDER_PATH = "folderPath";

	/**
	 * The Constant PDP_SERVLET_URL.
	 */
	public static final String PDP_SERVLET_URL = "/bin/corteva/product/getProductDetails";

	/**
	 * Taxonomy Structure for contentType tags
	 */
	public static final String CONTENT_TYPES_TAXONOMY_STRUCTURE = "/etc/tags/corteva/contentTypes";

	/**
	 * Taxonomy Structure for L2 level article tags
	 */
	public static final String ARITCLE_L2_TAXONOMY_STRUCTURE = CONTENT_TYPES_TAXONOMY_STRUCTURE + "/article/";

	/**
	 * The Constant to hold non agrian source name.
	 */
	public static final String NON_AGRIAN = "nonagrian";

	public static final String PDP_SOURCE = "pdpSrc";

	public static final String PRODUCT_ID = "productId";

	public static final String CQ_PRODUCT_TYPE = "cq:productType";

	public static final String IS_ASSET_FROM_S7 = "renderAssetFromScene7";

	public static final String PRODUCT_NAME = "productName";

	public static final String AGRIAN_NODES_ROOT_PATH = "/etc/designs/corteva/product";

	public static final String PRODUCT_ID_UPPER = "ProductId";

	public static final String PRODUCT_NAME_UPPER = "ProductName";

	public static final String COUNTRY_TAXONOMY_STRUCTURE = "/etc/tags/corteva/consumerRegionCountry/";

	public static final String PDP_RESOURCE_TYPE = "corteva/components/structure/pdp";

	public static final String ARTICLE_RESOURCE_TYPE = "corteva/components/structure/article-page";

	public static final String PDP_ROOT_PATH = "pdpRootPath";

	public static final String PRODUCTS = "products";

	public static final String PRODUCT_LABEL_RES_TYPE = "corteva/components/content/productLabel/v1/productLabel";

	public static final String PRODUCT_REGISTRATION_RES_TYPE = "corteva/components/content/productRegistration/v1/productRegistration";

	/**
	 * The Constant PRODUCT_FILTER_COMPONENT.
	 */
	public static final String PRODUCT_FILTER_COMPONENT = "corteva/components/content/productFilter/v1/productFilter";

	/**
	 * The Constant PRODUCT_ID_PROPERTY.
	 */
	public static final String PRODUCT_ID_PROPERTY = "productId";

	/**
	 * The Constant PRODUCT_NAME_PROPERTY.
	 */
	public static final String PRODUCT_NAME_PROPERTY = "productName";

	/**
	 * The Constant PRODUCT_LASTMODIFIEDDATE_PROPERTY.
	 */
	public static final String PRODUCT_LASTMODIFIEDDATE_PROPERTY = "lastModifiedDate";

	/**
	 * The Constant PRODUCT_NODE_PATH.
	 */
	public static final String PRODUCT_NODE_PATH = "/etc/corteva/product";

	public static final String PRODUCT_NODE_PATH_PROPERTY = "productRootPath";

	/**
	 * The Constant TEMP_PRODUCT_NODE_PATH.
	 */
	public static final String TEMP_PRODUCT_NODE_PATH = "/etc/corteva/temp";

	/**
	 * The Constant PRODUCT_FILTER_URL.
	 */
	public static final String PRODUCT_FILTER_URL = "/bin/corteva/product/getProductFiltersList";

	/**
	 * The Constant PRODUCT_FILTER_SELECTOR.
	 */
	public static final String PRODUCT_FILTER_SELECTOR = "product";

	public static final String PDP_TEMPLATE = "/conf/corteva/settings/wcm/templates/product-detail-page";

	public static final String HIDE_PRODUCT = "hideProduct";

	public static final String HIDE_IN_NAVIGATION = "hideInNav";

	public static final String US = "US";

	public static final String STATE_BASE_TAG_PATH = "productCountryTagBasePath";

	public static final String STATE_KEY = "pdp.productLabelFinder.stateKey";

	public static final String SPECIMEN_LABEL_KEY = "pdp.productLabelFinder.productLabelKey";

	public static final String SAFETY_LABEL_KEY = "pdp.productLabelFinder.safetyLabelKey";

	public static final String SEARCH_TEXT_KEY = "pdp.productLabelFinder.noResultsText";

	public static final String SUGGESTION_TEXT_KEY = "pdp.productLabelFinder.suggestionText";

	public static final String PID = "pid";

	public static final String AMPERSAND = "&";

	public static final String MAX_SUGGESTION_LENGTH = "maximumSuggestionLength";

	public static final String MIN_CHAR_LENGTH = "minimumCharacterLength";

	public static final String EN = "en";

	public static final String HOMEPAGE = "homepage";

	public static final String DOWNLOAD_DISPLAY_DATE = "downloadDisplayDate";

	public static final String MMM_DD_YYYY = "MMM dd, yyyy";

	public static final String LABEL = "Label";

	public static final String MSDS = "MSDS";

	public static final String CURRENT_PAGE = "currentPage";

	public static final String REFACTOR_TEMPLATE_SERVLET = "refactorTemplateServlet";

	public static final String LANGUAGE_MASTER_TAG = "corteva:languageMaster";

	/**
	 * The Constant MEDIA_TYPE_SCENE7_VIDEO to hold media type value for scene7
	 * video
	 */
	public static final String MEDIA_TYPE_SCENE7_VIDEO = "scene7video";

	/**
	 * The Constant REQUEST_PROPERTY_USER_AGENT to hold request property for user
	 * agent
	 */
	public static final String REQUEST_PROPERTY_USER_AGENT = "User-Agent";

	/**
	 * The Constant WORKFLOW_METADATA_PROCESS_ARGS to hold workflow metadata
	 * PROCESS_ARGS parameter
	 */
	public static final String WORKFLOW_METADATA_PROCESS_ARGS = "PROCESS_ARGS";

	/**
	 * The Constant YOUTUBE_API to hold YouTube api url
	 */
	public static final String YOUTUBE_API = "youtubeApi";

	/**
	 * The Constant YOUTUBE_KEY to hold YouTube api key
	 */
	public static final String YOUTUBE_KEY = "youtubeKey";

	/**
	 * The Constant to hold display date property.
	 */
	public static final String DISPLAY_DATE = "displayDate";

	/**
	 * The Constant to hold expfragHeader page property.
	 */
	public static final String PAGE_PROPERTY_EXPERIENCE_FRAGMENT_HEADER = "expfragHeader";

	/**
	 * The Constant to hold expfragFooter page property.
	 */
	public static final String PAGE_PROPERTY_EXPERIENCE_FRAGMENT_FOOTER = "expfragFooter";

	/**
	 * The Constant to hold company page property.
	 */
	public static final String PAGE_PROPERTY_COMPANY = "company";

	/**
	 * The Constant to hold disableAnalytics page property.
	 */
	public static final String PAGE_PROPERTY_DISABLE_ANALYTICS = "disableAnalytics";

	/**
	 * The Constant to hold stageHeaderScript page property.
	 */
	public static final String PAGE_PROPERTY_STAGE_HEADER_SCRIPT = "stageHeaderScript";

	/**
	 * The Constant to hold stageFooterScript page property.
	 */
	public static final String PAGE_PROPERTY_STAGE_FOOTER_SCRIPT = "stageFooterScript";

	/**
	 * The Constant to hold prodHeaderScript page property.
	 */
	public static final String PAGE_PROPERTY_PROD_HEADER_SCRIPT = "prodHeaderScript";

	/**
	 * The Constant to hold prodFooterScript page property.
	 */
	public static final String PAGE_PROPERTY_PROD_FOOTER_SCRIPT = "prodFooterScript";

	public static final String NA = "NA";

	/**
	 * The Constant DISPATCHER_HOSTS.
	 */
	public static final String DISPATCHER_HOSTS = "dispatcherHosts";

	public static final String JSON_LOWERCASE = "json";

	public static final String GROUP_1 = "group.1";

	public static final String GROUP_2 = "group.2";

	/**
	 * The Constant ALTERNATE_HREF_PROPERTY.
	 */
	public static final String ALTERNATE_HREF_PROPERTY = "cq:alternateHref";

	/**
	 * The Constant to define a variable to get the page path from page property.
	 */
	public static final String ITEM = "item";

	public static final String DASH = "-";

	public static final String REVIEWER = "reviewer";

	public static final String PUBLISHER = "publisher";

	public static final String USER_CONFIG_NAME = "com.corteva.core.configurations.UserConfigurationService";
	/**
	 * The Constant to hold flag value for whether page is within Corporate hierarchy
	 */
	public static final String BASE_SERVICE_API = "com.core.configuration.BaseServiceApiConfiguration";

	public static final String GLOBAL_FLAG = "global";

	/**
	 * The path of the JSON Files of the MAP view for Product Registration.
	 */
	public static final String PATH_TO_JSON_FILE = "/etc/corteva/jsonFiles";

	/**
	 * The node property used to retrieve the JSON file of a country for Product
	 * Registration in MAP View.
	 */
	public static final String JCR_DATA = "jcr:data";

	/**
	 * The ascii code for &nbsp.
	 */
	public static final String ASCII_CONSTANT_SPACE = "\u00A0";

	/** Brand Name Corteva */
	public static final String CORTEVA = "corteva";

	public static final String BRAND = "brand";

	public static final String EXP_FRAGMENT_ROOT = "/content/experience-fragments";

	public static final String CONTENT_ROOT_PATH_DAM_DPAGCO = "/content/dam/dpagco";

	public static final String CONTENT_ROOT_PATH_DAM = "/content/dam";
	  
    /**
	 * The Constant to hold value of base content root path
	 */
	public static final String CONTENT_ROOT_PATH_BREVANT = "/content/brevant";
	
	/** The Constant to hold type of value constant to Query */
	public static final String VALUE_CONSTANT = "_value";
	
	/** The Constant to hold value of property group operation */
	public static final String PROPERTY_GROUP_OPERATION = "group.p";
	
	/** Taxonomy root path */
	public static final String TAXONOMY_ROOT = "/etc/tags/corteva";
	
	/**
	 * The Constant PRODUCT_LICENSED_STATES_PROPERTY.
	 */
	public static final String PRODUCT_LICENSED_STATES_PROPERTY = "licensedStates";
	
	/**
	 * The Constant PRODUCT_UNLICENSED_STATES_PROPERTY.
	 */
	public static final String PRODUCT_UNLICENSED_STATES_PROPERTY = "unlicensedStates";

	/**
	 * The Constant to store SERVER NAME.
	 */
	public static final String SERVER_NAME = "server.name";
	
	/**
	 * The Constant to store SERVER DOMAIN.
	 */
	public static final String SERVER_DOMAIN = "server.domain";
	
	/**
	 * The Constant to hold the tag root for state tags
	 */
	public static final String STATE_TAG_ROOT = "corteva:regionCountryState";
	
	/** The Constant to hold value of brand name for Brevant */
	public static final String BRAND_NAME_BREVANT = "brevant";
	
	/** The Constant to hold property name for date field in Global Config. */
	public static final String DATE_PATTERN = "date.pattern";
	
	/** The Constant to hold Language master node name. */
	public static final String LANGUAGE_MASTERS = "language-masters";
}


