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

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The is a utility class that will contain methods that perform operations on
 * JCR Nodes. This class will be used by the sling models and the Sling
 * Servlets.
 * 
 * @author Sapient
 */
public final class AEMUtils {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AEMUtils.class);

	/**
	 * This method gets the JSON list from property.
	 *
	 * @param property
	 *            the property
	 * @return the JSON list from property
	 */
	public static List<JsonObject> getJSONListfromProperty(Property property) {
		LOGGER.debug("Inside getJSONListfromProperty() method.");
		final List<JsonObject> datalist = new ArrayList<>();
		if (property != null) {
			JsonObject jsonObject = null;
			Value[] values = null;
			try {
				if (property.isMultiple()) {
					values = property.getValues();
				} else {
					values = new Value[1];
					values[0] = property.getValue();
				}
				for (final Value val : values) {
					jsonObject = new Gson().fromJson(val.getString(), JsonObject.class);
					datalist.add(jsonObject);
				}
			} catch (IllegalStateException | RepositoryException e) {
				LOGGER.error("Exception occurred in getJSONListfromProperty", e);
			}
		}
		return datalist;
	}

	/**
	 * This method gets the string from property.
	 *
	 * @param property
	 *            the property
	 * @return the string from property
	 */
	public static String getStringfromProperty(Property property) {
		LOGGER.debug("Inside getStringfromProperty() method.");
		String propertyString = StringUtils.EMPTY;
		if (null != property) {
			try {
				Value val = property.getValue();
				propertyString = val.getString();
			} catch (IllegalStateException | RepositoryException e) {
				LOGGER.error("Exception occurred in getStringfromProperty()", e);
			}
		}
		return propertyString;
	}

	/**
	 * @param property
	 *            theproperty
	 * @param propertyNameToFetchFromJSON
	 *            the propertyNameToFetchFromJSON
	 * @return the Array List Form property
	 */
	public static List<String> getListFromMultiFieldJSONProperty(Property property,
			String propertyNameToFetchFromJSON) {
		List<String> propertyAsArrayList = new ArrayList<>();
		try {
			Value[] values = null;
			JsonObject jsonObject = null;
			if (property.isMultiple()) {

				values = property.getValues();

				for (final Value val : values) {
					jsonObject = new Gson().fromJson(val.getString(), JsonObject.class);
					propertyAsArrayList.add(jsonObject.get(propertyNameToFetchFromJSON).getAsString());
				}
			} else {
				propertyAsArrayList.add(property.getValue().getString());
			}
		} catch (RepositoryException e) {
			LOGGER.error("RepositoryException occurred in getJSONListfromProperty", e);
		}
		return propertyAsArrayList;
	}

	/**
	 * @param <E>
	 *            the type parameter for the method
	 * @param res
	 *            the parent resource of multifield items
	 * @param type
	 *            the Model class for the multifield item to adapt to
	 * @return List the list of multifield item model objects
	 */
	public static <E> List<E> getMultifieldItemList(Resource res, Class<E> type) {
		List<E> multifieldItemList = new ArrayList<>();
		for (Resource item : res.getChildren()) {
			E e = item.adaptTo(type);
			if (null != e) {
				multifieldItemList.add(e);
			}
		}
		return multifieldItemList;
	}
	
	/**
	   * Use this method to get a property from a JCR Node - it will return the property as a string regardless of property data type
	   *    String Arrays will return as a comma separated string 
	   * @param originalNode The Node to look for the property on
	   * @param propertyName The name of the property to get
	   * @return The property value as String
	   */
		public static String getNodePropertyAsString(Node originalNode, String propertyName) {
			if (originalNode != null) {
				try {
					Property property = null;
					if (originalNode.hasProperty(propertyName)) {
						property = originalNode.getProperty(propertyName);
					}
					if (property != null && property.getValue() != null) {
						return property.getValue().getString();
					}
				} catch (PathNotFoundException e) {
					LOGGER.error("PathNotFoundException getNodePropertyAsString", e.getMessage());
				} catch (RepositoryException e) {
					LOGGER.error("RepositoryException getNodePropertyAsString", e.getMessage());
				}
			}
			return null;
		}
}

