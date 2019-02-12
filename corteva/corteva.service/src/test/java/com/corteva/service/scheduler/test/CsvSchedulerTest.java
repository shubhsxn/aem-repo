package com.corteva.service.scheduler.test;

import com.adobe.granite.ui.components.Value;
import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.service.scheduler.CsvScheduler;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

public class CsvSchedulerTest extends BaseAbstractTest{

	/** The Csv Scheduler. */
	@InjectMocks
	private CsvScheduler csvScheduler;

	@Mock
	private Iterable<Resource> mockIterableRes;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The resource resolver. */
	@Mock
	private ResourceResolver resourceResolver;

	/** The mock resource. */
	@Mock
	private Resource mockFileResource;


	/** The mock resource. */
	@Mock
	private Resource mockCsvResource;

	/** The mock resource. */
	@Mock
	private Resource mockCsvResourceJcrContent;
	/** The iterator for resource. */
	@Mock
	private Iterator<Resource> iterator;
	@Mock
	Node mockNode;
	@Mock
	Asset mockAsset;
	@Mock
	Session mockSession;
	@Mock
	Rendition mockRendition;
	@Mock
	Replicator mockReplicator;
	@Mock
	ResourceResolverFactory resFactory;
	/**
	 * Sets the method parameters and adds nodes to the mock session.
	 */
	@Before
	public void setUp() {
		try {
			MockitoAnnotations.initMocks(this);
			context.registerInjectActivateService(new BaseConfigurationService());
			csvScheduler.bindBaseConfigurationService(baseService);

			csvScheduler.bindResourceResolverFactory(resFactory);
			csvScheduler.bindReplicator(mockReplicator);
			Field viewTypeField = csvScheduler.getClass().getDeclaredField("csvStorePath");
			viewTypeField.setAccessible(true);
			viewTypeField.set(csvScheduler,"/content/dam/csv");
			Field viewTypeField1 = csvScheduler.getClass().getDeclaredField("jsonStorePath");
			viewTypeField1.setAccessible(true);
			viewTypeField1.set(csvScheduler,"/content/brevant");
			Mockito.when(resFactory.getServiceResourceResolver(Mockito.anyMap())).thenReturn(resourceResolver);

			Mockito.when(resourceResolver.adaptTo(Session.class)).thenReturn(mockSession);
			Mockito.when(mockSession.isLive()).thenReturn(true);
			Mockito.when(mockNode.hasNode(Mockito.anyString())).thenReturn(true);
			Mockito.when(mockNode.getPath()).thenReturn("/content/corteva/na/us");
			Mockito.when(mockNode.getNode(Mockito.anyString())).thenReturn(mockNode);
			Mockito.when(mockSession.getRootNode()).thenReturn(mockNode);

		} catch (Exception e) {


			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}
	}

	private void getFileResource() {
		Mockito.when(resourceResolver.getResource(Mockito.anyString())).thenReturn(mockFileResource);
		ValueMap mockValMap = Mockito.mock(ValueMap.class);
		Mockito.when(mockFileResource.getValueMap()).thenReturn(mockValMap);
		Mockito.when(mockValMap.get(Mockito.anyString())).thenReturn("UTF-8");
	}

	@After
	public void tearDown() {
		csvScheduler.unbindBaseConfigurationService(baseService);
		csvScheduler.unbindResourceResolverFactory(resFactory);
		csvScheduler.unbindReplicator(mockReplicator);
	}
	/**
	 * Test create get article feed when article type is All.
	 */
	@Test
	public void testWhenCSVforSingleCountry() throws RepositoryException {
		setCSvStatus("");
		getFileResource();
		setUpCsv();
		setAsset("us_retailer.csv");
		List<String> successList = csvScheduler.runScheduler();
		Assert.assertEquals(true,successList.size()>0);
	}


	/**
	 * Test create get article feed when article type is All.
	 */
	@Test
	public void testWhenCSVDiffforMultipleLangInCountry() throws RepositoryException {
		setCSvStatus("");
		getFileResource();
		setUpCsv();
		setAsset("na_us_en_salesrep.csv");
		List<String> successList = csvScheduler.runScheduler();
		Assert.assertEquals(true,successList.size()>0);
	}

	/**
	 * Test create get article feed when article type is All.
	 */
	@Test
	public void testWhenCSVDiffforMultipleLangInCountryForContractor() throws RepositoryException {
		setCSvStatus("");
		getFileResource();
		setUpCsv();
		setAsset("na_us_en_contractor.csv");
		List<String> successList = csvScheduler.runScheduler();
		Assert.assertEquals(true,successList.size()>0);
	}


	/**
	 * Test create get article feed when article type is All.
	 */
	@Test
	public void testWhenCsvextensionXlsx() throws RepositoryException {
		setCSvStatus("");
		getFileResource();
		setUpCsv();
		setAsset("na_us_en_contractor.xlsx");
		List<String> successList = csvScheduler.runScheduler();
		Assert.assertEquals(true,successList.contains("false"));
	}
	/**
	 * Test create get article feed when article type is All.
	 */
	@Test
	public void testWhenCsvRecordNull() throws RepositoryException {
		setCSvStatus("");
		getFileResource();
		setUpBlankCsv();
		setAsset("na_us_en_salesrep.csv");
		List<String> successList = csvScheduler.runScheduler();
		Assert.assertEquals(true,successList.size()>0);
	}


	@Test
	public void testWhenCsvProcessingAlreadyStarted() throws RepositoryException {
		setCSvStatus("started");
		getFileResource();
		setUpCsv();
		setAsset("us_en_salesrep.csv");
		List<String> successList = csvScheduler.runScheduler();
		Assert.assertEquals(true,successList.size()>0);
	}
	@Test
	public void testWhenCsvAlreadyProcessed() throws RepositoryException {
		setCSvStatus("processed");
		getFileResource();
		setUpCsv();
		setAsset("us_en_salesrep.csv");
		List<String> successList = csvScheduler.runScheduler();
		Assert.assertEquals(true,successList.size()>0);
	}
	/**
	 * Test create get article feed when article type is All.
	 */
	@Test
	public void testWhenInvalidCsvFileExists() throws RepositoryException {
		setCSvStatus("");
		getFileResource();
		setUpCsv();
		setAsset("salesrep.csv");
		List<String> successList = csvScheduler.runScheduler();
		Assert.assertEquals(true,successList.contains("false"));
	}
	/**
	 * Test create get article feed when article type is All.
	 */
	@Test
	public void testWhenInvalidCsvFileCountryMissingInName() throws RepositoryException {
		setCSvStatus("");
		getFileResource();
		setUpCsv();
		setAsset("na_salesrep.csv");
		List<String> successList = csvScheduler.runScheduler();
		Assert.assertEquals(true,successList.size()>0);
	}

	/**
	 * Test create get article feed when article type is All.
	 */
	@Test
	public void testWhenFileResourceNull() throws RepositoryException {
		setCSvStatus("");
		List<String> successList = csvScheduler.runScheduler();
		Assert.assertEquals(false,successList.size()>0);
	}

	@Test
	public void testWhenCsvIsNotAset() throws RepositoryException {
		setCSvStatus("");
		setUpCsv();
		getFileResource();
		final List<Resource> results = new ArrayList<>();
		results.add(mockCsvResource);
		Mockito.when(mockFileResource.getChildren()).thenReturn(results);
		List<String> successList = csvScheduler.runScheduler();
		Assert.assertEquals(false,successList.size()>0);
	}



	@Test
	public void testWhenCsvRecordsIncorrect() throws RepositoryException {
		setCSvStatus("");
		getFileResource();
		setUpInvalidCsv();
		setAsset("us_en_salesrep.csv");
		List<String> successList = csvScheduler.runScheduler();
		Assert.assertEquals(true,successList.size()>0);
//		Mockito.verify(csvScheduler.run()).equals(null);
		//Assert.assertEquals(true,csvScheduler.runScheduler());
	}

	public void setUpCsv(){

		String str = "Lucas Belelli,2,3,Mendoza,5,,,8,9,lmbelellimichelan@dow.com,\n" +
				"Lucas Belelli,2,3,Mendoza,5,,,8,9,lmbelellimichelan@dow.com,\n"+
				"Carlos Tartaglia,2,3,Alto Valle Río Negro,5,,,+5492984571445,,LTartaglia@dow.com,";
		InputStream initialStream = new ByteArrayInputStream(str.getBytes());
		Mockito.when(mockRendition.getStream()).thenReturn(initialStream);
		Mockito.when(mockAsset.getRendition("original")).thenReturn(mockRendition);

	}


	public void setUpBlankCsv(){

		String str = "";
		InputStream initialStream = new ByteArrayInputStream(str.getBytes());
		Mockito.when(mockRendition.getStream()).thenReturn(initialStream);
		Mockito.when(mockAsset.getRendition("original")).thenReturn(mockRendition);

	}

	public void setUpInvalidCsv(){
		String str = "Lucas Belelli,,,Mendoza,,,lmbelellimichelan@dow.com,\n" +
				"Lucas Belelli,,,Mendoza,,,lmbelellimichelan@dow.com,\n" +
				"Carlos Tartaglia,,+5492984571445,,LTartaglia@dow.com,";
		InputStream initialStream = new ByteArrayInputStream(str.getBytes());
		Mockito.when(mockRendition.getStream()).thenReturn(initialStream);
		Mockito.when(mockAsset.getRendition("original")).thenReturn(mockRendition);

	}


	public void setAsset(String assetName){
		final List<Resource> results = new ArrayList<>();
		results.add(mockCsvResource);
		Mockito.when(mockFileResource.getChildren()).thenReturn(results);
		Mockito.when(mockCsvResource.adaptTo(Asset.class)).thenReturn(mockAsset);
		Mockito.when(mockAsset.getName()).thenReturn(assetName);
	}

	public void setCSvStatus(String status){
		Mockito.when(mockCsvResource.getChild(Mockito.anyString())).thenReturn(mockCsvResourceJcrContent);
		Mockito.when(mockCsvResourceJcrContent.adaptTo(Node.class)).thenReturn(mockNode);
		ValueMap mockValMap = Mockito.mock(ValueMap.class);
		Value mockVal = Mockito.mock(Value.class);
		Mockito.when(mockCsvResourceJcrContent.getValueMap()).thenReturn(mockValMap);
		Mockito.when(mockValMap.get(Mockito.any())).thenReturn(mockVal);
		Mockito.when(mockValMap.get(Mockito.any()).toString()).thenReturn(status);
		Mockito.when(mockValMap.get(Mockito.anyString(),Mockito.any())).thenReturn(new Date());
	}



}
