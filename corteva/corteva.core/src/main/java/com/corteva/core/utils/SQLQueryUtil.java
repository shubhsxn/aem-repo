package com.corteva.core.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;

/**
 * The Class SQLQueryUtil.
 */
public class SQLQueryUtil {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(SQLQueryUtil.class);

	/**
	 * Gets the documents.
	 * 
	 * @param resourceResolver
	 *            the resource resolver
	 * @param searchInput
	 *            the search input
	 * @return the documents
	 */
	public static Iterator<Resource> getDocuments(final ResourceResolver resourceResolver,
			final SearchInput searchInput) {
		LOGGER.info("Inside getDocuments of SQLQueryUtil");
		Iterator<Resource> resourceList = null;
		List<Resource> documentList = new ArrayList<>();
		String sqlQuery = prepareSQLQuery(searchInput);
		try {
			Session session = resourceResolver.adaptTo(Session.class);
			Query query = null;
			if (null != session) {
				QueryManager queryManager = session.getWorkspace().getQueryManager();
				query = queryManager.createQuery(sqlQuery, Query.JCR_SQL2);
				query.setOffset(searchInput.getOffset());
				LOGGER.debug("getDocuments :: query - {} :: query toString - {}", query, query.toString());
				QueryResult result = query.execute();
				NodeIterator nodes = result.getNodes();
				
				LOGGER.debug("results size :: {}", nodes.getSize());
				while (nodes.hasNext()) {
					Node node = nodes.nextNode();
					LOGGER.debug("node path :: {}", node.getPath());
					documentList.add(resourceResolver.getResource(node.getPath()));
				}
			}
			resourceList = documentList.iterator();
		} catch (RepositoryException e) {
			LOGGER.error("RepositoryException occurred in getDocuments()", e);
		}
		LOGGER.info("Exiting getDocuments of SQLQueryUtil");
		return resourceList;
	}

	/**
	 * Prepare SQL query.
	 * 
	 * @param searchInput
	 *            the search input
	 * @return the string
	 */
	public static String prepareSQLQuery(final SearchInput searchInput) {

		LOGGER.info("Inside prepareSQLQuery of SQLQueryUtil");
		final StringBuilder sql = new StringBuilder();

		sql.append("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([").append(searchInput.getRootPath())
				.append("])");
		if (null != searchInput.getType()) {
			sql.append("AND ([sling:resourceType] = '").append(searchInput.getType()).append("')");
		}
		if (null != searchInput.getTagList()) {
			String[] tagFields = new String[] {"cq:productType", "cq:tags"};
			sql.append(" AND (");
			for (int i = 0; i < tagFields.length; i++) {
				if (i > 0) {
					sql.append(" OR ");
				}
				sql.append("CONTAINS('").append(tagFields[i]).append("','");
				int count = 0;
				for (String tag : searchInput.getTagList()) {
					sql.append(tag);
					if (count != searchInput.getTagList().size() - 1) {
						sql.append(" OR ");
					} else {
						sql.append("') ");
					}
					count++;
				}
			}
			sql.append(")");
		}
		sql.append(" AND CONTAINS('hideProduct','false') order by [").append(CortevaConstant.PRODUCT_NAME_PROPERTY).append("]");
		String sqlQuery = sql.toString();
		LOGGER.info("Inside prepareSQLQuery of SQLQueryUtil : {}", sqlQuery);
		return sqlQuery;
	}
}