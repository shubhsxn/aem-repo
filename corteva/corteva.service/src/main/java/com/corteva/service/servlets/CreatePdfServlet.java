/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.service.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;

/**
 * The is the CreatePdfServlet which is triggered when the user clicks on the
 * download icon on a page. This servlet creates the pdf of a page using Phantom
 * JS.
 * 
 * @author Sapient
 */
@Component(immediate = true, service = Servlet.class, property = {
		CortevaConstant.SLING_SERVLET_RESOURCE_TYPES + CortevaConstant.BASE_PAGE_RESOURCE_TYPE,
		CortevaConstant.SLING_SERVLET_PATHS + CortevaConstant.GET,
		CortevaConstant.SLING_SERVLET_EXTENSIONS + CortevaConstant.PDF_EXTN })
public class CreatePdfServlet extends AbstractServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7815015307250280937L;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CreatePdfServlet.class);

	/** The base service. */
	private transient BaseConfigurationService configurationService;

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
		boolean featureFlag = true;
		if (configurationService != null) {
			featureFlag = configurationService.getToggleInfo("createPDFServlet",
					CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
		}
		if (featureFlag) {
			getCreatePdf(request, response);
		} else {
			LOGGER.debug("Feature Flag is turned off for {}", this.getClass().getName());
		}
	}

	/**
	 * Creates the pdf for the page.
	 *
	 * @param slingRequest
	 *            the sling request
	 * @param slingResponse
	 *            the sling response
	 */
	private void getCreatePdf(SlingHttpServletRequest slingRequest, SlingHttpServletResponse slingResponse) {
		LOGGER.debug("Entering getCreatePdf() method");
		String url;
		if (slingRequest.isSecure()) {
			url = CortevaConstant.HTTPS;
		} else {
			url = CortevaConstant.HTTP;
		}
		url = url.concat(slingRequest.getServerName());
		int port = slingRequest.getServerPort();
		if (port != -1) {
			url = url.concat(CortevaConstant.COLON).concat(Integer.toString(port));
		}
		url = url.concat(slingRequest.getRequestURI());
		String encodedUrl;
		try {
			encodedUrl = URLEncoder.encode(url, CortevaConstant.CHARACTER_ENCODING_UTF_8);
			File pdfFile = createPdf(encodedUrl, slingRequest);
			if (null != pdfFile) {
				byte[] pdfByteArray = FileUtils.readFileToByteArray(pdfFile);
				slingResponse.setContentType("application/pdf");
				slingResponse.getOutputStream().write(pdfByteArray);
				slingResponse.getOutputStream().flush();
				boolean flag = pdfFile.delete();
				if (!flag) {
					LOGGER.info("This pdf file is not deleted !!");
				}
			} else {
				slingResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (IOException e) {
			LOGGER.error("IO Exception occurred in getCreatePdf()", e);
		}
		LOGGER.debug("Exiting getCreatePdf() method");
	}

	/**
	 * Creates the pdf.
	 *
	 * @param url
	 *            the url
	 * @param request
	 *            the request
	 * @return the file
	 */
	private File createPdf(String url, SlingHttpServletRequest request) {
		Map<String, String> regionCountryLangMap = CommonUtils.getRegionCountryLanguage(request.getRequestURI(),
				request.getResourceResolver());
		LOGGER.debug("Region Country Language Map :: {}", regionCountryLangMap);
		String phantomExeLocation = configurationService.getPropertyValueFromConfiguration(regionCountryLangMap,
				CortevaConstant.PHANTOM_EXECUTABLE_LOCATION, CortevaConstant.PHANTOM_JS_CONFIG);
		String jsScriptLocation = configurationService.getPropertyValueFromConfiguration(regionCountryLangMap,
				CortevaConstant.PHANTOM_JS_LOCATION, CortevaConstant.PHANTOM_JS_CONFIG);
		String destPdfLocation = configurationService.getPropertyValueFromConfiguration(regionCountryLangMap,
				CortevaConstant.DESTINATION_PDF_LOCATION, CortevaConstant.PHANTOM_JS_CONFIG);
		String[] title = null;

		File file = null;
		// URL must end with pdf
		if (null != url && url.endsWith(CortevaConstant.PDF_EXTN)) {
			try {
				String decodedUrl = URLDecoder.decode(url, CortevaConstant.CHARACTER_ENCODING_UTF_8);
				String actualUrl = decodedUrl.replace(CortevaConstant.PDF_EXTN, CortevaConstant.HTML_EXTENSION);
				title = actualUrl.split(CortevaConstant.FORWARD_SLASH);
				String cmdString = phantomExeLocation + "  --ignore-ssl-errors=true  " + jsScriptLocation
						+ CortevaConstant.SPACE + actualUrl + CortevaConstant.SPACE + destPdfLocation
						+ StringUtils.replace(title[title.length - 1], CortevaConstant.HTML_EXTENSION, "")
						+ CortevaConstant.PDF_EXTN;
				LOGGER.debug("Phantom script command :: {}", cmdString);
				file = createFileFromPhantomCommand(title, cmdString, destPdfLocation);
			} catch (IOException e) {
				LOGGER.error("Exception occured in create pdf in Create Pdf Servlet :: {} ", e);
			}
		}
		return file;
	}

	/**
	 * Creates the file from phantom command.
	 *
	 * @param title
	 *            the title
	 * @param cmdString
	 *            the cmd string
	 * @param destPdfLocation
	 *            the dest pdf location
	 * @return the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	private File createFileFromPhantomCommand(String[] title, String cmdString, String destPdfLocation)
			throws IOException {
		LOGGER.debug("Inside createFileFromPhantomCommand() method");
		File file;
		Process process;
		process = Runtime.getRuntime().exec(cmdString);
		InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(),
				CortevaConstant.CHARACTER_ENCODING_UTF_8);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		try {
			String currentLine = null;
			currentLine = bufferedReader.readLine();
			while (currentLine != null) {
				currentLine = bufferedReader.readLine();
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred in createFileFromPhantomCommand()", e);
		} finally {
			bufferedReader.close();
			inputStreamReader.close();
		}
		file = new File(destPdfLocation + StringUtils.replace(title[title.length - 1], CortevaConstant.HTML_EXTENSION,
				CortevaConstant.PDF_EXTN));
		return file;
	}
}