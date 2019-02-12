package com.corteva.model.component.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.bean.ProductBean;
import com.corteva.model.component.models.CropListModel;
import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * This is a test class for Primary card Model.
 * 
 * @author Sapient
 * 
 */
public class CropListModelTest extends BaseAbstractTest {
	
	/**
     * The Constant Crop Type Tag.
     */
    private static final String CROP_TYPES_TAG = "Nutrient Management";
    
	/** The experience fragment model. */
	@Mock
	private CropListModel cropListModel;
	
	/**
	 * The mocked range iterator.
	 */
	@Mock
	private RangeIterator<Resource> rangeIterator;
	
	/**
	 * The mocked resource.
	 */
	@Mock
	private Resource resource;
	
	/**
	 * The mocked jcr:content resource.
	 */
	@Mock
	private Resource jcrContentResource;
	
	/** The mock valueMap */
	@Mock
	private ValueMap mockValueMap;
	
	/**
	 * The mocked request object.
	 */
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	
	/** The current res path. */
	private String currentResPath;
	
	/** The mocked page object. */
	@Mock
    private Page page;
	
	/* The image path. */
	private String[] cropTypeList;
	
	/** the mock base service */
	@Mock
	private BaseConfigurationService baseService;
	
	/**
	 * This method instantiates a new instance of Product Header Sling Model.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockRequest = getRequest();
		getContext().addModelsForPackage("com.corteva.model.component.models");
		mockRequest.setServletPath("/content/corteva/corporate");
		currentResPath = "/content/corteva/corporate/product-detail-page/jcr:content";
		context.create().resource(currentResPath);
		context.currentResource(currentResPath);
		context.registerAdapter(Resource.class, Page.class, page);
		PageManager pageManager = Mockito.mock(PageManager.class);
		Mockito.when(pageManager.getContainingPage(context.currentResource(currentResPath))).thenReturn(page);
		Mockito.when(page.getContentResource()).thenReturn(jcrContentResource);
		Mockito.when(jcrContentResource.getValueMap()).thenReturn(mockValueMap);
		Mockito.when(mockValueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY)).thenReturn(CortevaConstant.CORPORATE_TAG);
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		cropListModel = getRequest().adaptTo(CropListModel.class);
		Locale localeMock = new Locale(CortevaConstant.EN);
		TagManager mockTagManager = Mockito.mock(TagManager.class);
		Tag fakeTag = Mockito.mock(Tag.class);
		Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getParent()).thenReturn(fakeTag);
		Mockito.when(fakeTag.getTitle()).thenReturn(CROP_TYPES_TAG);
		Mockito.when(fakeTag.getTitle(localeMock)).thenReturn(CROP_TYPES_TAG);
		Mockito.when(fakeTag.getName()).thenReturn(CROP_TYPES_TAG);
		Mockito.when(fakeTag.getTagID()).thenReturn(CROP_TYPES_TAG);
		Mockito.when(mockTagManager.find(Mockito.anyString(),Mockito.any(String[].class))).thenReturn(rangeIterator);
		Mockito.when(rangeIterator.hasNext()).thenReturn(true,false);
		Mockito.when(rangeIterator.next()).thenReturn(resource);
	    context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
		
		List<ProductBean> productList = new ArrayList<>();
		List<String> listMock = new ArrayList<>();
		listMock.add("List1");
		listMock.add("List2");
		ProductBean productBean = new ProductBean();		
		productBean.setProductTag(fakeTag);
		productBean.setProductTagTitle(CROP_TYPES_TAG);
		productBean.setSubProductTagTitleList(listMock);
		productBean.setImageRenditionBean(null);
		productBean.setSuppressed(false);
		productList.add(productBean);
		
		cropTypeList = new String[]{"product", "crop"};
		Field viewTypeField;
		viewTypeField = cropListModel.getClass().getDeclaredField("cropType");
		viewTypeField.setAccessible(true);
		viewTypeField.set(cropListModel, cropTypeList);
		}
	
	/**
	 * Test method for product list.
	 */
	@Test
	public void testGetProductList() {
		Assert.assertNotNull(cropListModel.getProductList());
	}

}
