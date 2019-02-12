<%@page session="false"
        import="com.adobe.acs.commons.errorpagehandler.ErrorPageHandlerService"%>
<%@include file="/libs/foundation/global.jsp" %>
<%  
ErrorPageHandlerService errorPageHandlerService = sling.getService(ErrorPageHandlerService.class);
final String path = errorPageHandlerService.findErrorPage(slingRequest, resource);
slingResponse.sendRedirect(path); 
%>
