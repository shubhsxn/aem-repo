package com.corteva.model.component.models.vendor;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.LoggerFactory;
import com.adobe.acs.commons.genericlists.GenericList;
import com.adobe.cq.sightly.WCMUsePojo;
import com.corteva.model.component.bean.FormBean;
import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author Sapient The is the sling model for the Corteva Privacy Component.
 *
 */
public class CortevaPrivacyForm extends WCMUsePojo {
	/**
	 * logger instantiation
	 */
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CortevaPrivacyForm.class);
	/**
	 * parameter to display the fullThankyouPagePath
	 */
	private String fullThankyouPagePath;
	/**
	 * parameter to display the refererDomainValue
	 */
	private List<FormBean> hiddenFieldList = new ArrayList<>();

	/**
	 * parameter to display the captcha in specific language. Default is English
	 */
	private String languageCode = "en";
	/**
	 * parameter to display the refererDomainValue
	 */
	private String refererDomainValue = "";

	@Override
	public void activate() {
		populateThankyouPage();
		processHiddenFields();
		populateSiteLangue();
		populateReferrerDomain();
	}

	/**
	 * this method populates the referrer domain
	 */
	private void populateReferrerDomain() {
		String refererDomain = getRequest().getHeader("referer");
		ValueMap nodeProperties = getResource().getValueMap();
		String refererListPath = nodeProperties.get("refererlistpath", String.class);
		LOGGER.info("*********refererListPath: {}", refererListPath);
		Page refererListPage = getPageManager().getPage(refererListPath);
		GenericList list = refererListPage.adaptTo(GenericList.class);
		if (list == null) {
			return;
		}
		for (GenericList.Item gl : list.getItems()) {
			if (StringUtils.isNotBlank(refererDomain) && refererDomain.contains(gl.getTitle().trim())) {
				LOGGER.info("Key Matched with referrer: {} == {}", refererDomain, gl.getTitle());
				refererDomainValue = gl.getValue().trim();
				break;
			}
		}
		LOGGER.info("Referer domain: {} ", refererDomainValue);

	}

	/**
	 * Based on the current page, retrieve the language code.
	 */
	private void populateSiteLangue() {
		try {
			languageCode = getCurrentPage().getLanguage(false).getLanguage();
		} catch (Exception e) {
			LOGGER.error("Exception occured in retriving the current page language code: {}", e);
		}
	}

	/**
	 * @return thankYoupage URL
	 */
	private String populateThankyouPage() {

		String thankyouPageURL = getProperties().get("thankyoupage", String.class);

		BundleContext bundleContext = FrameworkUtil.getBundle(Externalizer.class).getBundleContext();
		Externalizer externalizer = (Externalizer) bundleContext.getService(bundleContext.getServiceReference(Externalizer.class.getName()));
		boolean isExternalUrlflag = true;
		if (thankyouPageURL != null) {
			Page thankYouPage = getPageManager().getPage(thankyouPageURL);
			isExternalUrlflag = (thankYouPage == null && (thankyouPageURL.contains("https") || thankyouPageURL.contains("http")));
			fullThankyouPagePath = externalizer.absoluteLink(getRequest(), (!isExternalUrlflag || thankyouPageURL.contains("http:")) ? "http" : "https", thankyouPageURL);
			fullThankyouPagePath = (!isExternalUrlflag) ? fullThankyouPagePath.replace("http:", "").replace("https:", "") + ".html" : fullThankyouPagePath;
		}
		return fullThankyouPagePath;
	}

	/**
	 * this method add hidden elements in form field
	 * 
	 * @return list of hidden elements as FormBEan
	 */
	List<FormBean> processHiddenFields() {

		JsonObject jObj;
		try {
			String[] itemsProps = getProperties().get("hiddenfields", String[].class);
			JsonParser jsonParser = new JsonParser();
			if (itemsProps != null) {
				for (int i = 0; i < itemsProps.length; i++) {
					jObj = (JsonObject) jsonParser.parse(itemsProps[i]);
					String name = jObj.get("hiddenname").getAsString();
					String value = jObj.get("hiddenvalue").getAsString();
					FormBean formBean = new FormBean();

					formBean.setName(name);
					formBean.setValue(value);

					hiddenFieldList.add(formBean);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception while Multifield data {}", e.getMessage(), e);
		}
		return hiddenFieldList;
	}

	/**
	 * @return languageCode
	 */
	public String getLanguageCode() {
		return languageCode;
	}

	/**
	 * @return fullThankyouPagePath
	 */
	public String getFullThankyouPagePath() {
		return fullThankyouPagePath;
	}

	/**
	 * @return hiddenFieldList
	 */
	public List<FormBean> getHiddenFieldList() {
		return hiddenFieldList;
	}

	/**
	 * @return refererDomainValue
	 */
	public String getRefererDomainValue() {
		return refererDomainValue;
	}

}