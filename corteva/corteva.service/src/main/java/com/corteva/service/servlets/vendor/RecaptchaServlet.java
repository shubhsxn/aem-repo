package com.corteva.service.servlets.vendor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author Sapient *
 */

@Component(immediate = true, service = Servlet.class, property = {
		CortevaConstant.SLING_SERVLET_PATHS + "/bin/corteva/verifyRecaptcha",
		CortevaConstant.SLING_SERVLET_METHODS + "{" + CortevaConstant.GET + "," + CortevaConstant.POST + "}" })
@Designate(ocd = RecaptchaServlet.CaptchaConfiguration.class)
public class RecaptchaServlet extends SlingAllMethodsServlet {
	/** serial version id */
	private static final long serialVersionUID = 3743867149355611706L;
	/** secret key */
	private String secretKey;
	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RecaptchaServlet.class);

	/**
	 * @author Sapient This is a configuration to hold the captcha secret key
	 */
	@ObjectClassDefinition(name = "Privacy Captcha Configuration", description = "Privacy Captcha Configuration")
	public @interface CaptchaConfiguration {
		/**
		 * @return secret key
		 */
		@AttributeDefinition(name = "Captcha Secret key", description = "Captcha Secret Key", defaultValue = "6LdMTV0UAAAAAH-DeB0sqLY9L_tOMfjyLvdOcqbM")
		String getCaptchaSecretKey();
	}

	/**
	 * @param config
	 *            the config
	 */
	@Activate
	public void activate(CaptchaConfiguration config) {
		this.secretKey = config.getCaptchaSecretKey();
	}

	/**
	 * site URL
	 */
	private static final String SITEURL = "https://www.google.com/recaptcha/api/siteverify";

	/**
	 * user agent
	 */
	private static final String USERAGENT = "Mozilla/5.0";
	
	/**
	 * g-recaptcha-response request parameter
	 */
	private static final String PARAMETER_RECAPTCHA_RESPONSE = "g-recaptcha-response";
	
	/**
	 * response
	 */
	private static final String RESPONSE = "response";
	
	/**
	 * response status
	 */
	private static final String RESPONSE_STATUS = "status";
	
	/**
	 * response success status
	 */
	private static final String RESPONSE_SUCCESS = "success";
	
	/**
	 * response failure status
	 */
	private static final String RESPONSE_FAILURE = "failure";
	
	/** secret */
	private static final String SECRET = "secret";
	
	/** secret key */
	private static final String SECRET_KEY = "secretKey";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.api.servlets.SlingSafeMethodsServlet#doPost(org.apache.sling
	 * .api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)
	 */
	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
		this.doIt(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.api.servlets.SlingSafeMethodsServlet#doGet(org.apache.sling.
	 * api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)
	 */
	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
		this.doIt(request, response);
	}

	/**
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 */
	private void doIt(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
		String gRecaptchaResponse = request.getParameter(PARAMETER_RECAPTCHA_RESPONSE);
		response.setContentType(CortevaConstant.CONTENT_TYPE_JSON);
		JsonObject object = new JsonObject();
		PrintWriter out = null;
		try {
			String status = verifyResponse(gRecaptchaResponse) ? RESPONSE_SUCCESS : RESPONSE_FAILURE;
			out = response.getWriter();
			object.addProperty(RESPONSE_STATUS, status);
			if (null != out) {
				out.print(object.toString());
			}
		} catch (IOException e) {
			LOGGER.error("IOException occured {}", e);
		}

	}

	/**
	 * @param recaptchaResponse
	 *            the response
	 * @return the success
	 */
	public boolean verifyResponse(String recaptchaResponse) {
		if (recaptchaResponse == null || "".equals(recaptchaResponse)) {
			return false;
		}
		try {
			HttpsURLConnection httpConnection = getConnection();
			httpConnection.setRequestMethod(CortevaConstant.POST);
			httpConnection.setRequestProperty(CortevaConstant.REQUEST_PROPERTY_USER_AGENT, USERAGENT);
			String postParams = SECRET + CortevaConstant.EQUAL + SECRET_KEY + CortevaConstant.AMPERSAND + RESPONSE + CortevaConstant.EQUAL + recaptchaResponse;
			httpConnection.setDoOutput(true);
			StringBuilder response = new StringBuilder();
			try (OutputStream os = httpConnection.getOutputStream(); DataOutputStream wr = new DataOutputStream(os)) {

				wr.writeBytes(postParams);
			}
			try (BufferedReader inputReader = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream(), CortevaConstant.CHARACTER_ENCODING_UTF_8))) {
				String inputLine;
				while ((inputLine = inputReader.readLine()) != null) {
					response.append(inputLine);
				}
			}
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(response.toString());
			return jsonObject.get(RESPONSE_SUCCESS).getAsBoolean();
		} catch (IOException e) {
			LOGGER.error("Exception occured while verifiying captcha :", e);
			return false;
		}
	}

	/**
	 * @return HTTP connection
	 ** @throws IOException
	 *             the IOException
	 */
	public HttpsURLConnection getConnection() throws IOException {
		URL obj = new URL(SITEURL);
		return (HttpsURLConnection) obj.openConnection();
	}
}
