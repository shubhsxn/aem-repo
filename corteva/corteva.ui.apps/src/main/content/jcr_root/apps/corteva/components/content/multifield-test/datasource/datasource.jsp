<%@page session="false" import="
                  org.apache.sling.api.resource.Resource,
                  org.apache.sling.api.resource.ResourceResolver,
				  com.adobe.granite.ui.components.ds.DataSource,
				  com.adobe.granite.ui.components.ds.EmptyDataSource,
				  com.corteva.core.utils.DataSourceUtils"%><%
%><%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %><%
%><cq:defineObjects/><%
  
// set fallback
request.setAttribute(DataSource.class.getName(), EmptyDataSource.instance());

ResourceResolver resolver = resource.getResourceResolver();

//Create a DataSource that is used to populate the drop-down control
DataSourceUtils.getStateDropdownValues(slingRequest, resource, resolver);
%>