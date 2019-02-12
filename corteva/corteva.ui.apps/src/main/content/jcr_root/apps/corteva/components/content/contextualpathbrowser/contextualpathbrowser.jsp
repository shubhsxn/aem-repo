	<%--
  This is the jsp for rendering the pathbrowser. Contextual Path Browser is overlayed from acs commons
  to set the root path of the path browser dynamically.
--%><%
%><%@include file="/libs/granite/ui/global.jsp" %><%
%><%@page session="false" %><%
%><%@ page import="org.apache.sling.api.request.RequestDispatcherOptions" %><%
%><%@ page import="org.apache.sling.api.resource.Resource"%><%
%><%@ page import="com.corteva.core.utils.CommonUtils" %><%
%><%@ page import="com.day.text.Text" %><%
 %><%@ page import="com.adobe.granite.ui.components.Config" %><%
%><%
    Config cfg = cmp.getConfig();
    String contentPath = slingRequest.getRequestPathInfo().getSuffix();
        if(contentPath==null){
            if(null!=slingRequest.getParameter("item")){
            contentPath=slingRequest.getParameter("item").toString();
            }else{
                contentPath="";
            }
        }

	String contentRootPath = "";
	if(null!=cfg.get("contentRootPath", String.class))
    contentRootPath = cfg.get("contentRootPath", String.class);
 	Resource resourceWrapper = CommonUtils.getResource(slingRequest,Text.getAbsoluteParent(contentPath, 5), resource, resource.getResourceResolver(),contentRootPath);
    RequestDispatcherOptions options = new RequestDispatcherOptions();
    options.setForceResourceType("/libs/granite/ui/components/foundation/form/pathbrowser");
    RequestDispatcher dispatcher = slingRequest.getRequestDispatcher(resourceWrapper, options);
    dispatcher.include(slingRequest, slingResponse);
%>