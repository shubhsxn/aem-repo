package com.corteva.service.scheduler;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.bean.SalesRepBean;
import com.corteva.model.component.exception.InvalidCsvException;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.dam.api.Asset;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;


import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ValueMap;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.nio.charset.Charset;


/**
 * The is the scheduler to process CSV
 *
 * @author Sapient
 */
@Designate(ocd = CsvScheduler.Config.class)
@Component(service = Runnable.class, configurationPolicy = ConfigurationPolicy.REQUIRE)

public class CsvScheduler implements Runnable {
    /**
     * The constant to hold property name of CSv processed Starts
     */
    public static final String CSV_PROCESSED_STATUS = "csvProcessedStatus";

    /**
     * The constant to hold property name of CSv processed Starts
     */
    public static final String CSV_PROCESSED_STATUS_TIME = "csvProcessedStatusTime";
    /**
     * The constant to hold file type Sales Rep
     */
    public static final String FILETYPE_SALESREP = "salesrep";
    /**
     * The constant to hold file type retailer
     */
    public static final String FILETYPE_RETAILER = "retailer";
    /**
     * The constant to hold file type Contractor
     */
    public static final String FILETYPE_CONTRACTOR = "contractor";
    /**
     * The constant to hold file ext CSV
     */
    public static final String CSV = "csv";
    
    /**
     * The constant to hold CSv status failed value
     */
    public static final String CSV_STATUS_FAILED = "failed";
    /**
     * The constant to hold CSv status processed value
     */
    public static final String CSV_STATUS_PROCESSED = "processed";
    /**
     * The constant to hold CSv status started value
     */
    public static final String CSV_PROCESSED_STARTED = "started";


    /**
     * The Interface Config.
     */
    @ObjectClassDefinition(name = "CSV Scheduler", description = "CSV Scheduler")
    public static @interface Config {

        /**
         * Scheduler expression.
         *
         * @return the string
         */
        @AttributeDefinition(name = "Scheduler expression") String scheduler_expression() default "0 0 0 * * ?";

        /**
         * Product node path.
         *
         * @return the string
         */
        @AttributeDefinition(name = "Scheduler CSV path") String csvPath() default "/content/dam/dpagco/brevant/sales_rep_retailer_csv";

        /**
         * Product node path tos tore JSON
         *
         * @return the string
         */
        @AttributeDefinition(name = "Scheduler JSON store path") String jsonStorePath() default "/content/brevant";
    }

    /**
     * Logger Instantiation.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvScheduler.class);

    /**
     * The resource resolver factory.
     */
    private ResourceResolverFactory resourceResolverFactory;

    /**
     * The base service.
     */
    private transient BaseConfigurationService configurationService;
    /**
     * The Path where JSON to be stored
     */
    private String jsonStorePath;
    /**
     * The Path where CSV to be stored
     */
    private String csvStorePath;

    /**
     * Activate.
     *
     * @param config the config
     */
    @Activate
    protected void activate(final Config config) {

        LOGGER.info("activation of csv scheduler");
        jsonStorePath = config.jsonStorePath();
        csvStorePath = config.csvPath();
        LOGGER.info("activation of csv scheduler complete ");
    }


    /**
     * This variable holds the constant for Json_Store
     */
    public static final String JSON_STORE_NODE_NAME = "/json_store";
    /**
     * This variable holds the constant for Json_Store property name
     */
    public static final String JSON_STORE_PROPERTY_NAME = "json_store";

    /**
     * Bind resource resolver factory.
     *
     * @param resourceResolverFactory the resource resolver factory
     */
    @Reference
    public void bindResourceResolverFactory(ResourceResolverFactory resourceResolverFactory) {
        this.resourceResolverFactory = resourceResolverFactory;
    }

    /**
     * Unbind resource resolver factory.
     *
     * @param resourceResolverFactory the resource resolver factory
     */
    public void unbindResourceResolverFactory(ResourceResolverFactory resourceResolverFactory) {
        this.resourceResolverFactory = resourceResolverFactory;
    }

    /**
     * Bind base configuration service.
     *
     * @param configurationService the base service
     */
    @Reference
    public void bindBaseConfigurationService(BaseConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * Unbind base configuration service.
     *
     * @param configurationService the configurationService
     */
    public void unbindBaseConfigurationService(BaseConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * The replicator.
     */
    private Replicator replicator;

    /**
     * Bind replicator
     *
     * @param replicator the replicator
     */
    @Reference
    public void bindReplicator(Replicator replicator) {
        this.replicator = replicator;
    }

    /**
     * Unbind replicator
     *
     * @param replicator the replicator
     */
    public void unbindReplicator(Replicator replicator) {
        this.replicator = replicator;
    }

    @Override
    public void run() {
        List<String> successFlagList = runScheduler();
        LOGGER.debug("Scheduler ended : {}", successFlagList.size());

    }

    /**
     * This method processes Scheduler code
     *
     * @return successFlag the flag
     */
    public List<String> runScheduler() {
        List<String> successFlag = new ArrayList<>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, CortevaConstant.SYSTEM_USER_SERVICE);
        try (ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(paramMap)) {
            Resource fileResource = resourceResolver.getResource(csvStorePath);
            if (null == fileResource) {
                LOGGER.error("Path where to look for CSV does not exist : {}", csvStorePath);
                return successFlag;
            }
            Asset asset = null;
            for (Resource csvResource : fileResource.getChildren()) {

                LOGGER.debug("Working on resource : {}", csvResource.getPath());
                asset = csvResource.adaptTo(Asset.class);
                if (null == asset) {
                    LOGGER.debug("Resource is not an asset ,skipping it : {} ", csvResource.getPath());
                    continue;
                }
                String assetName = asset.getName();
                if (!FilenameUtils.isExtension(assetName, CSV)) {
                    LOGGER.debug("Invalid File Extension- should be CSV : {}", csvResource.getPath());
                    successFlag.add("false");
                    continue;
                }
                validateCsvNamePatternAndProcess(successFlag, resourceResolver, asset, csvResource);
            }
        } catch (LoginException e) {
            LOGGER.error("Unable to fetch resource Resolver");
        }
        return successFlag;
    }

    /**
     * This method validates the CSV name pattern and process the file
     *
     * @param successFlag      the success flag list
     * @param resourceResolver the resource resolver
     * @param asset            the asset
     * @param csvResource      the csv resource
     */
    private void validateCsvNamePatternAndProcess(List<String> successFlag, ResourceResolver resourceResolver, Asset asset, Resource csvResource) {
        try {
            String regionName;
            String countryName;
            String languageNode = StringUtils.EMPTY;
            String fileType;

            String[] assetNameArr = asset.getName().split(CortevaConstant.UNDERSCORE); // valid CSV name is na_US_en_salesrep.csv
            regionName = assetNameArr.length > CortevaConstant.ONE ? assetNameArr[0] : "";
            countryName = assetNameArr.length > CortevaConstant.ONE ? assetNameArr[1] : "";
            if (StringUtils.isBlank(regionName)) {
                throw new InvalidCsvException("Valid File does not exist - region name not present");
            }
            if (StringUtils.isBlank(countryName)) {
                throw new InvalidCsvException("Valid File does not exist - country name not present");
            }
            String fileName;

            if (assetNameArr.length > 3) {
                languageNode = assetNameArr[2];
                fileName = assetNameArr[3];
            } else if (assetNameArr.length > 2) {
                fileName = assetNameArr[2];
            } else {
                throw new InvalidCsvException("Incorrect File Name");
            }
            fileType = validateAndGetFileType(fileName);
            processEachCsvInRootPath(resourceResolver, csvResource, asset, countryName, fileType, languageNode, regionName);
            successFlag.add("true");
        } catch (InvalidCsvException e) {
            successFlag.add("false");
            LOGGER.error("CSV file {} could not be processed : {}", asset.getName(), e.getMessage());
        }
    }


    /**
     * This method validates and returns the file Type
     *
     * @param fileName the assetName
     * @return the file type
     * @throws InvalidCsvException the invalidCsvException
     */
    private String validateAndGetFileType(String fileName) throws InvalidCsvException {
        String fileType = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(fileName)) {
            if (StringUtils.equalsIgnoreCase(fileName, FILETYPE_SALESREP + CortevaConstant.DOT + CSV)) {
                fileType = FILETYPE_SALESREP;
            } else if (StringUtils.equalsIgnoreCase(fileName, FILETYPE_RETAILER + CortevaConstant.DOT + CSV)) {
                fileType = FILETYPE_RETAILER;
            } else if (StringUtils.equalsIgnoreCase(fileName, FILETYPE_CONTRACTOR + CortevaConstant.DOT + CSV)) {
                fileType = FILETYPE_CONTRACTOR;
            } else {
                throw new InvalidCsvException("Valid File does not exist : Incorrect name . Correct name patters is is Eg. na_US_en_salesrep.csv ");
            }
        }
        return fileType;
    }

    /**
     * This method validates if CSV is processed
     *
     * @param csvResourceJcrContent CSV resource JCR content node
     * @return valid
     */
    private boolean validateIfCsvProcessed(Resource csvResourceJcrContent) {
        ValueMap valMap = csvResourceJcrContent.getValueMap();
        if (StringUtils.equalsIgnoreCase(valMap.get(CSV_PROCESSED_STATUS).toString(), CSV_STATUS_PROCESSED) || StringUtils.equalsIgnoreCase(valMap.get(CSV_PROCESSED_STATUS).toString(), CSV_PROCESSED_STARTED)) {
            //check last modified time and csv processed time same or different. Ignore if same
            Date lastProcessedTime = valMap.get(CSV_PROCESSED_STATUS_TIME, Date.class);
            Date lastModifiedDate = valMap.get("jcr:lastModified", Date.class);
            if (null != lastModifiedDate && null != lastProcessedTime && lastModifiedDate.before(lastProcessedTime)) {
                LOGGER.info("Processed file present. Skipping processing");
                return true;
            }
        }
        return false;
    }

    /**
     * This method processes each CSV in root path
     *
     * @param resourceResolver the resolver
     * @param csvResource      the csv resource
     * @param asset            the asset
     * @param countryName      the country name in file
     * @param fileName         the file name in file
     * @param languageNodeName the lang node name
     * @param regionName       the region name
     * @throws InvalidCsvException the invalid CSV Excpetion
     */
    private void processEachCsvInRootPath(ResourceResolver resourceResolver, Resource csvResource, Asset asset, String countryName, String fileName, String languageNodeName, String regionName) throws InvalidCsvException {
        LOGGER.debug("START : processEachCsvInRootPath");
        Resource csvResourceJcrContent = csvResource.getChild(CortevaConstant.JCR_CONTENT);
        if (null == csvResourceJcrContent) {
            throw new InvalidCsvException("Corrupted asset : JCR content node missing for Resource." + csvResource.getPath());
        }
        Node node = csvResourceJcrContent.adaptTo(Node.class);
        if (null != csvResourceJcrContent.getValueMap().get(CSV_PROCESSED_STATUS) && validateIfCsvProcessed(csvResourceJcrContent)) {
            return;
        }
        final Session session = resourceResolver.adaptTo(Session.class);
        saveStatusPropertyOnCsvNode(node, session, CSV_PROCESSED_STARTED);
        String processedStatus = StringUtils.EMPTY;
        try {
            processedStatus = processCsv(countryName, fileName, resourceResolver, asset, languageNodeName, regionName);
        } catch (InvalidCsvException e) {
            processedStatus = CSV_STATUS_FAILED;
            throw new InvalidCsvException(e.getMessage());
        } finally {
            saveStatusPropertyOnCsvNode(node, session, processedStatus);
        }
        LOGGER.debug("END : processEachCsvInRootPath");
    }

    /**
     * Thsi method processes CSV
     *
     * @param countryName      the country name
     * @param fileName         the file name
     * @param resourceResolver the resolver
     * @param asset            the asset
     * @param languageNodeName the language node name
     * @param regionName       the region Name
     * @return procescsv status
     * @throws InvalidCsvException the invalidcsvexception
     */
    public String processCsv(String countryName, String fileName, ResourceResolver resourceResolver, Asset asset, String languageNodeName, String regionName) throws InvalidCsvException {
        LOGGER.debug("START : Process Csv");
        LOGGER.debug("Country name is {} and CSV type is {}", countryName, fileName);
        String processedStatus = StringUtils.EMPTY;
        try {
            String resPath;
            if (StringUtils.isBlank(languageNodeName)) {
                resPath = jsonStorePath + CortevaConstant.FORWARD_SLASH + StringUtils.lowerCase(regionName) + CortevaConstant.FORWARD_SLASH + StringUtils.lowerCase(countryName);
            } else {
                resPath = jsonStorePath + CortevaConstant.FORWARD_SLASH + StringUtils.lowerCase(regionName) + CortevaConstant.FORWARD_SLASH + StringUtils.lowerCase(countryName) + CortevaConstant.FORWARD_SLASH + StringUtils.lowerCase(languageNodeName);
            }
            Resource languageRes = resourceResolver.getResource(resPath + CortevaConstant.FORWARD_SLASH + CortevaConstant.JCR_CONTENT);
            if (null == languageRes) {
                throw new InvalidCsvException("Path created based on CSV name to store JSON is invalid :" + resPath);

            }
            String encodingFormat = "ISO-8859-1";
            if (null != languageRes.getValueMap().get("encodingType")) {
                encodingFormat = languageRes.getValueMap().get("encodingType").toString();
            }
            LOGGER.debug("Encoding Format is {}", encodingFormat);
            List<CSVRecord> csvRecordList = getListOfCsvRecords(asset, encodingFormat);
            if (csvRecordList == null || csvRecordList.isEmpty()) {
                throw new InvalidCsvException("Csv records list from getListOfCsvRecords is null or empty");
            }
            List<SalesRepBean> salesRepBeanList = getSalesRepBeanListFromCsvRecords(csvRecordList, encodingFormat);
            LOGGER.debug("Sales Rep Bean list fetched ,size is : {}", salesRepBeanList.size());
            // store Csv in content node
            if (!salesRepBeanList.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                storeCsvJsonInNode(resourceResolver, mapper.writeValueAsString(salesRepBeanList), resPath, fileName);
                //set CSVstatus as processed.
                processedStatus = CSV_STATUS_PROCESSED;
            } else {
                LOGGER.error("List of processed Sales Rep records empty , not updating JSON node.");
                processedStatus = CSV_STATUS_FAILED;
            }
            LOGGER.debug("END : processCsv method");
        } catch (InvalidCsvException e) {
            throw new InvalidCsvException("CSV Processing Failed : " + e.getMessage());
        } catch (IOException e) {
            throw new InvalidCsvException("CSV Processing Failed :IO Exception occured : " + e.getMessage());
        }
        return processedStatus;
    }

    /**
     * The popoulate CSv Data
     *
     * @param asset          the fileresource asset
     * @param encodingFormat the encodingFormat
     * @return list of csv records
     * @throws IOException         the IO excpetion
     */
    public static List<CSVRecord> getListOfCsvRecords(Asset asset, String encodingFormat) throws IOException {
        LOGGER.debug("START : getListOfCsvRecords");
        List<CSVRecord> csvRecordList = null;
        Charset charset = Charset.forName(encodingFormat);
       
        try (BufferedReader in = new BufferedReader(new InputStreamReader(asset.getRendition("original").getStream(), charset));
             CSVParser csvParser = new CSVParser(in, CSVFormat.DEFAULT);) {
            csvRecordList = csvParser.getRecords();
        }
        LOGGER.debug("END : getListOfCsvRecords");
        return csvRecordList;
    }


    /**
     * This method populates Sales Rep Bean list
     *
     * @param csvRecordList  the Csv record list
     * @param encodingFormat the encoding format
     * @return list of salesrep bean
     * @throws IOException         the Io excpetion
     * @throws InvalidCsvException the invalidCsvException
     */
    private List<SalesRepBean> getSalesRepBeanListFromCsvRecords(List<CSVRecord> csvRecordList, String encodingFormat) throws InvalidCsvException {
        LOGGER.debug("Start : getSalesRepBeanListFromCsvRecords");
        String bingMapApiUrl = getBingMapUrl();
        List<SalesRepBean> salesRepBeanList = new ArrayList<>();
        //retailer
        //Company Name	Contact Name	Address Line  #1	Location/City	Province/State	Postal Code	Phone 	Phone 2	Email	Website
        //sales rep title
        //Sales Rep Name	Title	Address Line  #1	Location/City	Province/State	Postal Code	Phone 	Phone 2	Email	Website
        int loopCounter = 0;
        for (CSVRecord record : csvRecordList) {
            loopCounter++;
            if (loopCounter > 2) {
                try {
                    SalesRepBean salesRepBean = new SalesRepBean();
                    salesRepBean.setName(record.get(0)); // company name in retailer
                    salesRepBean.setTitle(record.get(1));//contact name in retailer
                    salesRepBean.setAddressLine1(record.get(2));
                    salesRepBean.setCity(record.get(3));
                    salesRepBean.setState(record.get(4));
                    salesRepBean.setZipCode(record.get(5));
                    salesRepBean.setCountry(record.get(6));
                    salesRepBean.setPhone(record.get(7));
                    salesRepBean.setPhone2(record.get(8));
                    salesRepBean.setEmail(record.get(9));
                    salesRepBean.setWebsite(record.get(10));

                    if (StringUtils.isNotBlank(bingMapApiUrl)) {
                        salesRepBean.setLatitudeLongitude(bingMapApiUrl, salesRepBean.getCountry(), salesRepBean.getZipCode(), salesRepBean.getCity(), salesRepBean.getAddressLine1(), salesRepBean.getState(), encodingFormat);
                    }
                    salesRepBeanList.add(salesRepBean);
                } catch (Exception e) {
                    LOGGER.error("Exception occured while setting Sales Rep Bean for sales Rep :" + record.get(0) + ". Exception message :" + e.getMessage());
                }
            }
        }
        LOGGER.debug("END : getSalesRepBeanListFromCsvRecords");
        return salesRepBeanList;
    }

    /**
     * This method stores the CSV content as JSon in node
     *
     * @param resolver    the resource resolver
     * @param jsonToWrite the JSon to write
     * @param resPath     the resourcepath where to store JSon
     * @param fileName    the filename
     * @throws InvalidCsvException the invalidCSVException
     */
    private void storeCsvJsonInNode(ResourceResolver resolver, String jsonToWrite, String resPath, String fileName) throws InvalidCsvException {    // store Csv in content node
        LOGGER.debug("START : storeCsvJsonInNode");
        final Session session = resolver.adaptTo(Session.class);
        try {
            final Calendar lastModified = Calendar.getInstance();
            lastModified.setTimeInMillis(lastModified.getTimeInMillis());
            if (null != session && session.isLive()) {
                Node node = JcrUtil.createPath(resPath + JSON_STORE_NODE_NAME + CortevaConstant.UNDERSCORE + StringUtils.lowerCase(fileName), JcrConstants.NT_UNSTRUCTURED, session);
                node.setProperty(JSON_STORE_PROPERTY_NAME, jsonToWrite);
                node.setProperty(CortevaConstant.CQ_LAST_MODIFIED, lastModified);
                session.save();
                LOGGER.debug("Node path relicating is {}", node.getPath());
                replicator.replicate(session, ReplicationActionType.ACTIVATE, node.getPath());
                LOGGER.debug("Json_Store Node created at path {}{} :", resPath, JSON_STORE_NODE_NAME);

            }
        } catch (RepositoryException e) {
            throw new InvalidCsvException("JSON Node cannot be created into the configurable path : " + e.getMessage());
        } catch (ReplicationException e) {
            throw new InvalidCsvException("JSON cannot be replicated: " + e.getMessage());
        }
        LOGGER.debug("END : storeCsvJsonInNode");
    }

    /**
     * This method stores the status on CSV jcr content node
     *
     * @param csvJcrContentNode the node
     * @param session           the session
     * @param processedStatus   the processed Status
     * @throws InvalidCsvException the invalid csv exception
     */
    private void saveStatusPropertyOnCsvNode(Node csvJcrContentNode, Session session, String processedStatus) throws InvalidCsvException {
        LOGGER.debug("START : saveStatusPropertyOnCsvNode , value saved is : {}", processedStatus);
        if (null != session && session.isLive() && null != csvJcrContentNode) {
            try {
                csvJcrContentNode.setProperty(CSV_PROCESSED_STATUS, processedStatus);
                csvJcrContentNode.setProperty(CSV_PROCESSED_STATUS_TIME, Calendar.getInstance());
                session.save();
            } catch (RepositoryException e) {
                throw new InvalidCsvException("CSV File status could not ve updated " + e.getMessage());
            }
            LOGGER.debug("END : saveStatusPropertyOnCsvNode , value saved");
        } else {
        	 throw new InvalidCsvException("CSV File status update failed ");
        }
    }

    /**
     * This method retrieves Bing Map URL
     *
     * @return bingMapUrl
     * @throws InvalidCsvException the inavlid CSV Exception
     */
    private String getBingMapUrl() throws InvalidCsvException {
        /** The Constant BING_MAP_Config_Name. */
        final String bingMapConfig = "com.corteva.core.configurations.BingMapService";
        final String bingMapAPI = "https://www.pioneer.com/bingMapProxy/corteva/locations?domain=https://brevant.com";
        StringBuilder sb = new StringBuilder();
        String bingMapApiUrl = StringUtils.EMPTY;
        String siteDomain = StringUtils.EMPTY;
        try {
            bingMapApiUrl = configurationService.getPropValueFromConfiguration(bingMapConfig, "apiurl");
            siteDomain = configurationService.getPropValueFromConfiguration(bingMapConfig, "siteDomain");
            bingMapApiUrl = sb.append(bingMapApiUrl).append(siteDomain).toString();
            if (StringUtils.isBlank(bingMapApiUrl)) {
                bingMapApiUrl = bingMapAPI;
                LOGGER.debug("apUrl in if condition = \"{}\"", bingMapApiUrl);
            }
        } catch (IOException e) {
            throw new InvalidCsvException("Error occured while fetching Configuration - apiurl and siteDomain from Bing Map OSGI Config");
        }
        return bingMapApiUrl;
    }


}
