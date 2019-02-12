/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.core.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;

/**
 * The Class LinkUtil for processing the link urls being authored from component
 * dialogs across the Corteva site.
 *
 * @author Sapient
 */
public final class LinkUtil {

	/**
	 * The logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LinkUtil.class);

	/**
	 * This method gets the href for a URL authored by the author.
	 *
	 * @param url
	 *            the url
	 * @return the href
	 */
	public static String getHref(String url) {
		LOGGER.debug("Inside getHref() method");
		String href = StringUtils.EMPTY;

		if (url == null || url.isEmpty()) {
			return href;
		}

		if (StringUtils.startsWith(url.trim(), CortevaConstant.CONTENT_DAM_PATH)) {
			href = url.trim();
			if (StringUtils.contains(href, CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION)) {
				href = StringUtils.substringBefore(href, CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION);
			}
		} else {
			if ("#".equals(url.trim())) {
				href = url.trim();
			} else {
				href = createHref(url.trim());
			}
		}

		LOGGER.debug("The Href Is :: {}", href);
		return href;
	}

	/**
	 * Creates the href.
	 *
	 * @param trimmedUrl
	 *            the trimmed url
	 * @return the string
	 */
	private static String createHref(String trimmedUrl) {
		boolean jpgPngGifImage = StringUtils.endsWithIgnoreCase(trimmedUrl, ".png")
				|| StringUtils.endsWithIgnoreCase(trimmedUrl, "gif")
				|| StringUtils.endsWithIgnoreCase(trimmedUrl, "jpg");
		boolean jpegBmpTifImage = StringUtils.endsWithIgnoreCase(trimmedUrl, "jpeg")
				|| StringUtils.endsWithIgnoreCase(trimmedUrl, "bmp")
				|| StringUtils.endsWithIgnoreCase(trimmedUrl, "tif");
		return getHref(trimmedUrl, jpgPngGifImage, jpegBmpTifImage);
	}

	/**
	 * Gets the href.
	 *
	 * @param trimmedUrl
	 *            the trimmed url
	 * @param jpgPngGifImage
	 *            the jpg png gif image
	 * @param jpegBmpTifImage
	 *            the jpeg bmp tif image
	 * @return the href
	 */
	private static String getHref(String trimmedUrl, boolean jpgPngGifImage, boolean jpegBmpTifImage) {
		String href;
		if (StringUtils.startsWith(trimmedUrl, CortevaConstant.CONTENT_ROOT) && !(jpgPngGifImage || jpegBmpTifImage
				|| StringUtils.endsWithIgnoreCase(trimmedUrl, "vcf"))) {
			href = trimmedUrl;
			if (!StringUtils.contains(href, CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION)) {
				href = href.concat(CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION);
			}
		} else {
			href = trimmedUrl;
			LOGGER.debug("href:: {}", href);
			if (!(StringUtils.startsWith(href, CortevaConstant.HTTP)
					|| StringUtils.startsWith(href, CortevaConstant.HTTPS)) && !href.startsWith("/")) {
				href = CortevaConstant.HTTP.concat(href);
			}
		}
		return href;
	}
}