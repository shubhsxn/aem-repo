<%@page session="false"%><%
if (request.getServerName().contains("brevant")){
    response.setStatus(404); 
} else {
%><%@include file="/apps/acs-commons/components/utilities/errorpagehandler/404.jsp" %>
<% } %>