package com.corteva.service.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * The Class APIClient.
 */
public class APIClient {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(APIClient.class);

	/** The Constant REQUEST_TYPE. */
	private static final String REQUEST_TYPE = "GET";

	/** The Constant ACCEPT_HEADER. */
	private static final String ACCEPT_HEADER = "Accept";

	/** The Constant APPLICATION_XML. */
	private static final String APPLICATION_XML = "application/xml";

	/** The Constant CONNECTION_TIMEOUT. */
	private static final int CONNECTION_TIMEOUT = 180000;

	/** The Constant PRODUCT_ROOT_NODE. */
	private static final String PRODUCT_ROOT_NODE = "string";

	/**
	 * Call API.
	 *
	 * @param <T>
	 *            the generic type
	 * @param apiUrl
	 *            the api url
	 * @param responseClassType
	 *            the response class type
	 * @return the input stream
	 * @throws ApiException
	 *             the api exception
	 */
	public static <T> ApiResponse<T> callAPI(String apiUrl, Class<T> responseClassType) throws ApiException {
		LOGGER.debug("Entering callAPI method of APIClient");
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		ApiResponse<T> response = null;
		try {
			URL url = new URL(apiUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod(REQUEST_TYPE);
			httpURLConnection.setRequestProperty(ACCEPT_HEADER, APPLICATION_XML);
			httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);

			inputStream = httpURLConnection.getInputStream();
			response = processProductResponse(inputStream, responseClassType);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new ApiException(e);
		} finally {
			if (null != httpURLConnection && null != inputStream) {
				httpURLConnection.disconnect();
				try {
					inputStream.close();
				} catch (IOException e) {
					LOGGER.error("Exception while closing the input stream ", e);
				}
			}
		}
		LOGGER.debug("Exiting callAPI method of APIClient");
		return response;
	}

	/**
	 * Process product response.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param inputStream
	 *            the input stream
	 * @param responseClassType
	 *            the response class type
	 * @return the agrian products
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the SAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static <T> ApiResponse<T> processProductResponse(InputStream inputStream, Class<T> responseClassType)
			throws ParserConfigurationException, SAXException, IOException {
		LOGGER.debug("Entering processProductResponse method of APIClient");
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = dBuilder.parse(inputStream);
		doc.getDocumentElement().normalize();

		T data = null;

		NodeList nList = doc.getElementsByTagName(PRODUCT_ROOT_NODE);
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				JacksonXmlModule xmlModule = new JacksonXmlModule();
				xmlModule.setDefaultUseWrapper(false);
				ObjectMapper mapper = new XmlMapper(xmlModule);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

				data = mapper.readValue(eElement.getTextContent(), responseClassType);
			}

		}
		LOGGER.debug("Exiting processProductResponse method of APIClient");
		return new ApiResponse<>(data);
	}

}
