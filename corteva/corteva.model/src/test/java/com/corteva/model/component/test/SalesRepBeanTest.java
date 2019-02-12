package com.corteva.model.component.test;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.model.component.bean.SalesRepBean;

import com.corteva.model.component.exception.InvalidCsvException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * This is a test class for Sales Rep BEan class
 *
 * @author Sapient
 */
public class SalesRepBeanTest extends BaseAbstractTest {

    /**
     * The model class mocked
     */
    @Mock
    private SalesRepBean salesRepBean = new SalesRepBean();
    /**
     * This method instantiates a new instance of Find a sales rep retailer model
     */
    @Before
    public void setUp() {
        //MockitoAnnotations.initMocks(this);
        salesRepBean.setAddressLine1("10060 Hereford St");
        salesRepBean.setEmail("abcd@test.com");
        salesRepBean.setCity("Lacombe");
        salesRepBean.setCountry("Canada");
        salesRepBean.setPhone("98765433218");
        salesRepBean.setPhone2("89765464645");
        salesRepBean.setZipCode("T41 X3");
        salesRepBean.setState("");
        salesRepBean.setName("test");
        salesRepBean.setTitle("title");
      

        salesRepBean.setWebsite("test.com");

    }

    @Test
    public void testGetterSetter(){
        Assert.assertNotNull(salesRepBean.getAddressLine1());
        Assert.assertNotNull(salesRepBean.getCity());
        Assert.assertNotNull(salesRepBean.getCountry());
        Assert.assertNotNull(salesRepBean.getEmail());
        Assert.assertNotNull(salesRepBean.getPhone());
        Assert.assertNotNull(salesRepBean.getPhone2());
        Assert.assertNotNull(salesRepBean.getWebsite());
        Assert.assertNotNull(salesRepBean.getZipCode());
        Assert.assertNotNull(salesRepBean.getName());
        Assert.assertNotNull(salesRepBean.getTitle());
        Assert.assertNotNull(salesRepBean.getState());
      

    }
}
