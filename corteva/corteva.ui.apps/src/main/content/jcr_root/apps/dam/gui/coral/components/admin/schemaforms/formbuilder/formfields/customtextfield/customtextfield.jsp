<%--

  ADOBE CONFIDENTIAL
  __________________

   Copyright 2012 Adobe Systems Incorporated
   All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.

--%><%
%><%@include file="/libs/granite/ui/global.jsp" %><%
%><%@ page session="false" contentType="text/html" pageEncoding="utf-8"
         import="org.apache.sling.api.resource.ValueMap" %><%

	ValueMap fieldProperties = resource.adaptTo(ValueMap.class);
	String key = resource.getName();

%>

<div class="formbuilder-content-form">
  <!--This customization is done to support textarea field while creating a new custom metadata ,this new field will appear as Multi Line Text in custom metadata editor -->    
    <label class="fieldtype coral-Form-fieldlabel">
    <coral-icon icon="text" size="XS"></coral-icon><%= i18n.get("Multi Line Text") %>
    </label>
    <sling:include resource="<%= resource %>" resourceType="granite/ui/components/coral/foundation/form/textarea"/>
  <!--end of  customization which is done to support textarea field  while creating a new custom metadata ,this new field will appear as Multi Line Text in custom metadata editor -->  
</div>
<div class="formbuilder-content-properties">

    <input type="hidden" name="<%= xssAPI.encodeForHTMLAttr("./items/" + key) %>">
    <input type="hidden" name="<%= xssAPI.encodeForHTMLAttr("./items/" + key + "/jcr:primaryType") %>" value="nt:unstructured">
  <!-- This line is added so that custom added textarea is visible as textarea while opening second time otherwise it becomes invisble-->  
    <input type="hidden" name="<%= xssAPI.encodeForHTMLAttr("./items/" + key + "/sling:resourceType") %>" value="granite/ui/components/coral/foundation/form/textarea">
	<input type="hidden" name="<%= xssAPI.encodeForHTMLAttr("./items/" + key + "/granite:data/metaType") %>" value="customtext">
  <!-- end of custmoization done to make custom added textarea visible as textarea while opening schema editor for second time-->    
	<input type="hidden" name="<%= xssAPI.encodeForHTMLAttr("./items/" + key + "/defaultValue") %>" value=" "> <!-- value is sent with space to ensure it get saved -->

    <%
        String resourcePathBase = "dam/gui/coral/components/admin/schemaforms/formbuilder/formfieldproperties/";
        String[] settingsList = {"labelfields", "metadatamappertextfield", "placeholderfields", "requiredfields", "disableineditmodefields", "showemptyfieldinreadonly", "titlefields"};
        for(String settingComponent : settingsList){
            %>
            <sling:include resource="<%= resource %>" resourceType="<%= resourcePathBase + settingComponent %>"/>
            <%
        }
    %>

    <coral-icon class="delete-field" icon="delete" size="L" href="" data-target-id="<%= xssAPI.encodeForHTMLAttr(key) %>" data-target="<%= xssAPI.encodeForHTMLAttr("./items/" + key + "@Delete") %>"></coral-icon>

</div>
