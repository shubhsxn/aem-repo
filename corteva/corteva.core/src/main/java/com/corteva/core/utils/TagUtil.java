package com.corteva.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.corteva.core.constants.CortevaConstant;
import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

/**
 * This is the utility class for tags.
 * 
 * @author Sapient
 */
public final class TagUtil {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TagUtil.class);

	/** The Constant INSIDE_GETRESOURCES. */
	private static final String INSIDE_GET_RESOURCES = "Inside getResources method with parameter :: {}";

	/** The Constant INSIDE_LIST_ALL_SUBTAGS. */
	private static final String INSIDE_LIST_ALL_SUBTAGS = "Inside listAllSubTags method";

	/** The Constant INSIDE_LIST_CHILDREN. */
	private static final String INSIDE_LIST_CHILDREN = "Inside listChildren method";

	/**
	 * Instantiates a new tag util.
	 */
	private TagUtil() {
	}

	/**
	 * Gets the resources.
	 *
	 * @param tagManager
	 *            the tag manager
	 * @param tag
	 *            the tag
	 * @param basePath
	 *            the base path
	 * @return the resources
	 */
	public static List<Resource> getResources(final TagManager tagManager, final Tag tag, String basePath) {
		LOGGER.debug(INSIDE_GET_RESOURCES, basePath);
		if (null == tag) {
			return Collections.emptyList();
		}
		return getResources(tagManager, tag.getTagID(), basePath);
	}

	/**
	 * Gets the resources.
	 *
	 * @param tagManager
	 *            the tag manager
	 * @param tagIds
	 *            the tag ids
	 * @param basePath
	 *            the base path
	 * @return the resources
	 */
	public static List<Resource> getResources(final TagManager tagManager, String[] tagIds, String basePath) {
		LOGGER.debug(INSIDE_GET_RESOURCES, basePath);
		final List<Resource> resources = new ArrayList<>();
		final RangeIterator<Resource> iterator = tagManager.find(basePath, tagIds);
		while (iterator.hasNext()) {
			resources.add(iterator.next());
		}
		return resources;
	}

	/**
	 * Gets the resources.
	 *
	 * @param tagManager
	 *            the tag manager
	 * @param tagId
	 *            the tag id
	 * @param basePath
	 *            the base path
	 * @return the resources
	 */
	public static List<Resource> getResources(final TagManager tagManager, String tagId, String basePath) {
		LOGGER.debug(INSIDE_GET_RESOURCES, tagId, basePath);
		final String[] tagIds = new String[1];
		tagIds[0] = tagId;
		return getResources(tagManager, tagIds, basePath);
	}

	/**
	 * List all sub tags.
	 *
	 * @param rootTag
	 *            the root tag
	 * @return the list
	 */
	public static List<Tag> listAllSubTags(final Tag rootTag) {
		LOGGER.debug(INSIDE_LIST_ALL_SUBTAGS);
		if (null == rootTag) {
			return Collections.emptyList();
		}
		return createTagList(rootTag.listAllSubTags());
	}

	/**
	 * List all sub tags.
	 *
	 * @param tagManager
	 *            the tag manager
	 * @param rootTagPath
	 *            the root tag path
	 * @return the list
	 */
	public static List<Tag> listAllSubTags(final TagManager tagManager, final String rootTagPath) {
		LOGGER.debug(INSIDE_LIST_ALL_SUBTAGS, rootTagPath);
		final Tag rootTag = tagManager.resolve(rootTagPath);
		if (null == rootTag) {
			return Collections.emptyList();
		}
		return createTagList(rootTag.listAllSubTags());
	}

	/**
	 * List children.
	 *
	 * @param tagManager
	 *            the tag manager
	 * @param rootTagPath
	 *            the root tag path
	 * @return the list
	 */
	public static List<Tag> listChildren(final TagManager tagManager, final String rootTagPath) {
		LOGGER.debug(INSIDE_LIST_CHILDREN, rootTagPath);
		final Tag rootTag = tagManager.resolve(rootTagPath);
		if (null == rootTag) {
			return Collections.emptyList();
		}
		return createTagList(rootTag.listChildren());
	}

	/**
	 * List children.
	 *
	 * @param rootTag
	 *            the root tag
	 * @return the list
	 */
	public static List<Tag> listChildren(final Tag rootTag) {
		LOGGER.debug(INSIDE_LIST_CHILDREN);
		if (null == rootTag) {
			return Collections.emptyList();
		}
		return createTagList(rootTag.listChildren());
	}

	/**
	 * Creates the tag list.
	 *
	 * @param iterator
	 *            the iterator
	 * @return the list
	 */
	public static List<Tag> createTagList(final Iterator<Tag> iterator) {
		LOGGER.debug("Inside createTagList method ");
		final List<Tag> list = new ArrayList<>();
		while (iterator.hasNext()) {
			final Tag tag = iterator.next();
			list.add(tag);
		}
		return list;
	}

	/**
	 * Gets the tag.
	 *
	 * @param tagManager
	 *            the tag manager
	 * @param tagId
	 *            the tag id
	 * @return the tag
	 */
	public static Tag getTag(final TagManager tagManager, final String tagId) {
		LOGGER.debug("Inside getTag method with parameter :: {}", tagId);
		return tagManager.resolve(tagId);
	}

	/**
	 * Gets the tag name.
	 *
	 * @param tagManager
	 *            the tag manager
	 * @param tagIdorPath
	 *            the tag idor path
	 * @return the tag name
	 */
	public static String getTagName(final TagManager tagManager, final String tagIdorPath) {
		LOGGER.debug("Inside getTagName method with parameter :: {}", tagIdorPath);
		String tagName = StringUtils.EMPTY;
		if (null != getTag(tagManager, tagIdorPath)) {
			tagName = getTag(tagManager, tagIdorPath).getName();
		}
		LOGGER.debug("getTagName | Tag Name :: {}", tagName);
		return tagName;
	}

	/**
	 * Gets the tag title.
	 *
	 * @param tagManager
	 *            the tag manager
	 * @param tagIdorPath
	 *            the tag idor path
	 * @return the tag title
	 */
	public static String getTagTitle(final TagManager tagManager, final String tagIdorPath) {
		LOGGER.debug("Inside getTagTitle method with parameter :: {}", tagIdorPath);
		String tagTitle = StringUtils.EMPTY;
		if (null != getTag(tagManager, tagIdorPath)) {
			tagTitle = getTag(tagManager, tagIdorPath).getTitle();
		}
		LOGGER.debug("getTagTitle | Tag Name :: {}", tagTitle);
		return tagTitle;
	}

	/**
	 * Gets the localized tag title.
	 *
	 * @param tagManager
	 *            the tag manager
	 * @param locale
	 *            the locale
	 * @param tagIdorPath
	 *            the tag id or path
	 * @return the localized tag title
	 */
	public static String getLocalizedTagTitle(final TagManager tagManager, final Locale locale,
			final String tagIdorPath) {
		LOGGER.debug("Inside getLocalizedTagTitle method with parameter :: {}", tagIdorPath);
		String localizedTagTitle = StringUtils.EMPTY;
		if (null != getTag(tagManager, tagIdorPath)) {
			localizedTagTitle = getTag(tagManager, tagIdorPath).getTitle(locale);
		}
		LOGGER.debug("getLocalizedTagTitle | Tag Name :: {}", localizedTagTitle);
		return localizedTagTitle;
	}

	/**
	 * Gets the tag value.
	 *
	 * @param tag
	 *            the tag
	 * @return the tag value
	 */
	public static String getTagValue(final Tag tag) {
		LOGGER.debug("Inside getTagValue {}", tag);
		if (null == tag) {
			return StringUtils.EMPTY;
		}
		final Resource tagResource = tag.adaptTo(Resource.class);
		String value = StringUtils.EMPTY;
		if (null != tagResource) {
			final ValueMap valueMap = tagResource.getValueMap();
			value = valueMap.get(CortevaConstant.VALUE, StringUtils.EMPTY);
		}
		if (value.isEmpty()) {
			value = tag.getName();
		}
		LOGGER.debug("Exit getTagValue = {}", value);
		return value;
	}

	/**
	 * Gets the tag value.
	 *
	 * @param currentRes
	 *            the current res
	 * @param tagId
	 *            the tag id
	 * @return the tag value
	 */
	public static String getTagValueFromTagId(Resource currentRes, String tagId) {
		LOGGER.debug("Inside getTagValueFromTagId {}", tagId);
		TagManager tm = currentRes.getResourceResolver().adaptTo(TagManager.class);
		String tagValue = StringUtils.EMPTY;
		if (null != tm) {
			Tag tag = tm.resolve(tagId);
			if (null != tag) {
				tagValue = getTagValue(tag);
			}
		}
		LOGGER.debug("Exit getTagValueFromTagId {}", tagValue);
		return tagValue;
	}

	/**
	 * Gets the List of all Pages of specific template within the specified content
	 * path.
	 *
	 * @param resourceResolver
	 *            the resourceResolver
	 * @param tagPath
	 *            the tagPath
	 * @return Iterator
	 */
	public static List<Resource> getMapFromTags(ResourceResolver resourceResolver, String tagPath) {
		List<Resource> resourceList = new ArrayList<>();
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		if (null != tagManager) {
			List<Tag> tags = TagUtil.listChildren(tagManager, tagPath);
			for (final Tag tag : tags) {
				ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());
				vm.put(CortevaConstant.VALUE, tag.getName());
				vm.put(CortevaConstant.TEXT, tag.getTitle());
				resourceList.add(
						new ValueMapResource(resourceResolver, new ResourceMetadata(), CortevaConstant.NODE_TYPE, vm));
			}
		}
		return resourceList;
	}

	/**
	 * Creates the product state map from tags.
	 *
	 * @param states
	 *            the states
	 * @param resolver
	 *            the resolver
	 * @param locale
	 *            the locale
	 * @return the map
	 */
	public static Map<String, String> createProductStateMapFromTags(String[] states, ResourceResolver resolver,
			Locale locale) {
		LOGGER.debug("Inside createProductStateMapFromTags() method");
		Map<String, String> prodStatesMap = new HashMap<>();
		TagManager tagManager = resolver.adaptTo(TagManager.class);
		if (null != tagManager && null != states) {
			for (String state : states) {
				Tag tag = tagManager.resolve(state);
				prodStatesMap.put(tag.getName(), getLocalizedTagTitle(tagManager, locale, tag.getTagID()));
			}
		}
		LOGGER.debug("Product States Map :: {}", prodStatesMap);
		return prodStatesMap;
	}
}