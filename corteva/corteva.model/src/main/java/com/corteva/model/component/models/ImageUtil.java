package com.corteva.model.component.models;

import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.model.component.bean.ImageRenditionBean;

/**
 * The is a common utility class that will contain methods that will be used to
 * get Scene 7 Image URL
 * 
 * @author Sapient
 */
public class ImageUtil {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

	/** The Constant DOLLAR_SIGN. */
	private static final String DOLLAR_SIGN = "$";

	/**
	 * This method returns a list of Image rendition bean that contains the desktop,
	 * tablet and mobile image paths.
	 *
	 * @param imagePath
	 *            the image path
	 * @param componentName
	 *            the component name
	 * @param request
	 *            the request
	 * @param baseService
	 *            the baseService
	 * @param regCountryLangMap
	 *            the regCountryLangMap
	 * @return the image rendition list
	 */
	public static ImageRenditionBean getImageRenditionList(String imagePath, String componentName,
			SlingHttpServletRequest request, BaseConfigurationService baseService,
			Map<String, String> regCountryLangMap) {
		String[] imageRenditionArr = null;
		String scene7ImgPath;
		boolean isImageFromS7 = Boolean.parseBoolean(baseService.getPropertyValueFromConfiguration(regCountryLangMap,
				CortevaConstant.IS_ASSET_FROM_S7, CortevaConstant.IMAGE_CONFIG_NAME));
		scene7ImgPath = getSceneSevenImagePath(imagePath, request, baseService, regCountryLangMap);
		if (isImageFromS7) {
			String imageRenditionPropertyValue = baseService.getPropertyValueFromConfiguration(regCountryLangMap,
					componentName, CortevaConstant.IMAGE_CONFIG_NAME);
			imageRenditionArr = StringUtils.split(imageRenditionPropertyValue, CortevaConstant.COMMA);
		}
		return populateImageBean(imageRenditionArr, scene7ImgPath);
	}

	/**
	 * Gets the scene seven image path.
	 *
	 * @param imagePath
	 *            the image path
	 * @param request
	 *            the request
	 * @param baseService
	 *            the baseService
	 * @param regCountryLangMap
	 *            the regCountryLangMap
	 * @return the scene seven image path
	 */
	public static String getSceneSevenImagePath(String imagePath, SlingHttpServletRequest request,
			BaseConfigurationService baseService, Map<String, String> regCountryLangMap) {
		String sceneSevenImgPath = StringUtils.EMPTY;
		StringBuilder sb = new StringBuilder();
		boolean isImageFromS7 = Boolean.parseBoolean(baseService.getPropertyValueFromConfiguration(regCountryLangMap,
				CortevaConstant.IS_ASSET_FROM_S7, CortevaConstant.IMAGE_CONFIG_NAME));
		if (isImageFromS7) {
			Resource imageResource = request.getResourceResolver().resolve(imagePath);
			try {
				if (!ResourceUtil.isNonExistingResource(imageResource)) {
					Node imgNode = imageResource.adaptTo(Node.class);
					if (null != imgNode && StringUtils.isNotBlank(imgNode.getName())) {
						String sSevenImgName = CommonUtils.getFileNameWithoutExtn(imgNode.getName());
						String sceneSevenDomain = baseService.getPropertyValueFromConfiguration(regCountryLangMap,
								"sceneSevenImageRoot", CortevaConstant.IMAGE_CONFIG_NAME);

						sceneSevenImgPath = sb.append(sceneSevenDomain).append(CortevaConstant.FORWARD_SLASH)
								.append(sSevenImgName).toString();
					}
				}
			} catch (RepositoryException e) {
				LOGGER.debug("Repository Exception occurred in getSceneSevenImagePath()", e);
			}
		} else {
			sceneSevenImgPath = imagePath;
		}
		LOGGER.debug("Scene7 Image Path :: {}", sceneSevenImgPath);
		return sceneSevenImgPath;
	}

	/**
	 * Populate image bean.
	 *
	 * @param imageRenditionArr
	 *            the image rendition arr
	 * @param sSevenImgPath
	 *            the s seven img path
	 * @return the image rendition bean
	 */
	private static ImageRenditionBean populateImageBean(String[] imageRenditionArr, String sSevenImgPath) {
		ImageRenditionBean imgRenditionBean = new ImageRenditionBean();
		StringBuilder sbDesk = new StringBuilder();
		StringBuilder sbTab = new StringBuilder();
		StringBuilder sbMob = new StringBuilder();
		String desktopImagePath = sSevenImgPath;
		String tabletImagePath = sSevenImgPath;
		String mobileImagePath = sSevenImgPath;
		if (null != imageRenditionArr && imageRenditionArr.length > 0) {
			desktopImagePath = sbDesk.append(desktopImagePath).append(CortevaConstant.QUESTION_MARK)
					.append(ImageUtil.DOLLAR_SIGN)
					.append(StringUtils.replace(imageRenditionArr[0], CortevaConstant.SPACE, ""))
					.append(ImageUtil.DOLLAR_SIGN).toString();
			tabletImagePath = sbTab.append(tabletImagePath).append(CortevaConstant.QUESTION_MARK)
					.append(ImageUtil.DOLLAR_SIGN)
					.append(StringUtils.replace(imageRenditionArr[1], CortevaConstant.SPACE, ""))
					.append(ImageUtil.DOLLAR_SIGN).toString();
			mobileImagePath = sbMob.append(mobileImagePath).append(CortevaConstant.QUESTION_MARK)
					.append(ImageUtil.DOLLAR_SIGN)
					.append(StringUtils.replace(imageRenditionArr[2], CortevaConstant.SPACE, ""))
					.append(ImageUtil.DOLLAR_SIGN).toString();
		}
		imgRenditionBean.setDesktopImagePath(desktopImagePath);
		imgRenditionBean.setTabletImagePath(tabletImagePath);
		imgRenditionBean.setMobileImagePath(mobileImagePath);
		return imgRenditionBean;
	}
}
