<%
%><%@include file="/libs/granite/ui/global.jsp" %><%
%><%@page session="false"
          import="java.util.regex.Matcher,
                  org.apache.commons.lang.StringUtils,
                  org.apache.jackrabbit.util.Text,
                  org.apache.sling.api.resource.ResourceUtil,
                  org.json.JSONArray,
                  com.adobe.granite.ui.components.AttrBuilder,
                  com.adobe.granite.ui.components.Config,
                  com.adobe.granite.ui.components.Tag" %><%--###
FileUpload
==========

.. granite:servercomponent:: /libs/granite/ui/components/foundation/form/fileupload
   :supertype: /libs/granite/ui/components/foundation/form/field
   
   A field component to upload file.
   
   It extends :granite:servercomponent:`Field </libs/granite/ui/components/foundation/form/field>` component.
   
   It has the following content structure:

   .. gnd:gnd::

      [granite:FormFileUpload]
      
      /**
       * The id attribute.
       */
      - id (String)

      /**
       * The class attribute. This is used to indicate the semantic relationship of the component similar to ``rel`` attribute.
       */
      - rel (String)

      /**
       * The class attribute.
       */
      - class (String)

      /**
       * The title attribute.
       */
      - title (String) i18n
      
      /**
       * The label of the component.
       */
      - fieldLabel (String) i18n
      
      /**
       * The description of the component.
       */
      - fieldDescription (String) i18n

      /**
       * The name that identifies the field when submitting the form.
       */
      - name (String) = 'file'
      
      /**
       * A hint to the user of what can be entered in the field.
       */
      - emptyText (String) i18n
      
      /**
       * Indicates if the field is in disabled state.
       */
      - disabled (Boolean)
      
      /**
       * Indicates if the field is mandatory to be filled.
       */
      - required (Boolean)
      
      /**
       * The name of the validator to be applied. E.g. ``foundation.jcr.name``.
       * See :doc:`validation </jcr_root/libs/granite/ui/components/foundation/clientlibs/foundation/js/validation/index>` in Granite UI.
       */
      - validation (String) multiple
      
      /**
       * The text of the button.
       */
      - text (String) i18n
      
      /**
       * The icon of the button.
       */
      - icon (String)
      
      /**
       * The variant of the button.
       */
      - variant (String) multiple < 'secondary', 'quiet'
      
      /**
       * Indicates if multiple files can be uploaded.
       */
      - multiple (Boolean)
      
      /**
       * The parameter representing the file name, for instance "./fileName" or "./image/fileName".
       It will be used to determine the (relative) location where to store the name of the file.
       */
      - fileNameParameter (String)

      /**
       * The location where to store the reference of the file (when a file already uploaded on the server is used), usually ./fileReference or ./image/fileReference.
       NB: This is only working in the context of AEM Authoring
       */
      - fileReferenceParameter (String)

      /**
       * The URL where to upload the file, you can use ``${suffix.path}`` to use the current suffix
       */
      - uploadUrl (String)
      
      /**
       * The upload URL builder.
       */
      - uploadUrlBuilder (String)
      
      /**
       * The file size limit.
       */
      - sizeLimit (Long)
      
      /**
       * ``true`` to make the upload starts automatically once the file is selected.
       */ 
      - autoStart (String)
      
      /**
       * Prefers HTML5 to upload files (if browser allows it).
       */
      - useHTML5 (Boolean) = true
      
      /**
       * The drop zone selector to upload files from file system directly (if browser allows it).
       */
      - dropZone (String)
      
      /**
       * The browse and selection filter for file selection. E.g: [".png",".jpg"] or ["image/\*"].
       */
      - mimeTypes (String) multiple

	  /**
       * Flag to indicate if chunked upload is supported.
       */
      - chunkUploadSupported (Boolean) = false

      /**
       * Size of chunk.
       */
      - chunkSize (Long)

	  /**
       * Minimum file size which will use chunked upload.
       */
      - chunkUploadMinFileSize (Long)
###--%><%
    if (!cmp.getRenderCondition().check()) {
       return;
	}

    Config cfg = cmp.getConfig();
    
    String fieldLabel = cfg.get("fieldLabel", String.class);
    String fieldDesc = cfg.get("fieldDescription", String.class);
    String[] mimeTypes =  cfg.get("mimeTypes", new String[] { "*" });
    String icon = cfg.get("icon", String.class);
    
    String uploadUrl = cfg.get("uploadUrl", String.class);
    
    if (uploadUrl != null) {
        String suffix = slingRequest.getRequestPathInfo().getSuffix();
        if (suffix == null) {
            suffix = "";
        }
        // TODO it is better to expose a new property supporting EL for this
        uploadUrl = uploadUrl.replaceAll("\\$\\{suffix.path\\}", Matcher.quoteReplacement(Text.escapePath(suffix)));
    }
    
    Tag tag = cmp.consumeTag();
    
    AttrBuilder fieldAttrs = tag.getAttrs();
    
    fieldAttrs.add("id", cfg.get("id", String.class));
    fieldAttrs.addClass(cfg.get("class", String.class));
    fieldAttrs.addRel(cfg.get("rel", String.class));
    
    fieldAttrs.addClass("coral-FileUpload");
    
	boolean chunkUploadSupported = cfg.get("chunkUploadSupported", false);
    if (chunkUploadSupported) {
        fieldAttrs.add("data-init", "filechunkedupload");
    } else {
        fieldAttrs.add("data-init", "fileupload");
    }
    
    if (cfg.get("disabled", false)) {
        fieldAttrs.add("data-disabled", true);
    }
    
    AttrBuilder inputAttrs = new AttrBuilder(request, xssAPI);
    inputAttrs.add("type", "file");
    inputAttrs.add("name", cfg.get("name", String.class));
    inputAttrs.add("title", i18n.getVar(cfg.get("title", String.class)));
    inputAttrs.add("value", cmp.getValue().get("value", String.class));
    inputAttrs.add("placeholder", i18n.getVar(cfg.get("emptyText", String.class)));
    inputAttrs.addMultiple(cfg.get("multiple", false));
    inputAttrs.add("accept", StringUtils.join(mimeTypes, ','));
    
    inputAttrs.addClass("coral-FileUpload-input");
    inputAttrs.add("data-dropzone", cfg.get("dropZone", String.class));
    inputAttrs.add("data-usehtml5", cfg.get("useHTML5", true));
    inputAttrs.addHref("data-upload-url", uploadUrl);
    inputAttrs.add("data-upload-url-builder", cfg.get("uploadUrlBuilder", String.class));
    inputAttrs.add("data-size-limit", cfg.get("sizeLimit", String.class));
    inputAttrs.add("data-mime-types", new JSONArray(mimeTypes).toString());
    inputAttrs.add("data-auto-start", cfg.get("autoStart", false));
    inputAttrs.add("data-file-name-parameter", cfg.get("fileNameParameter", String.class));
    inputAttrs.add("data-chunk-size", cfg.get("chunkSize", String.class));
    inputAttrs.add("data-chunk-upload-min-file-size", cfg.get("chunkUploadMinFileSize", String.class));
    inputAttrs.add("data-chunk-upload-supported", cfg.get("chunkUploadSupported", false));
    
    if (cfg.get("required", false)) {
        inputAttrs.add("aria-required", true);
    }
    
    String validation = StringUtils.join(cfg.get("validation", new String[0]), " ");
    inputAttrs.add("data-validation", validation);

    ValueMap eventVM = ResourceUtil.getValueMap(cfg.getChild("events"));
    for (String eventName : eventVM.keySet()) {
        if (!eventName.startsWith("jcr:")) {
            Object eventHandler = eventVM.get(eventName);
            if (eventHandler instanceof String) {
                inputAttrs.add("data-event-" + eventName, (String) eventHandler);
            }
        }
    }

    inputAttrs.addOthers(cfg.getProperties(),
            "id", "rel", "class", "title",
            "name", "value", "multiple", "emptyText", "disabled", "required", "validation",
            "uploadUrl", "uploadUrlBuilder", "sizeLimit", "autoStart", "dropZone", "useHTML5", "fileNameParameter", "mimeTypes", "chunkSize", "chunkUploadMinFileSize", "chunkUploadSupported",
            "text", "icon", "variant", "fieldLabel", "fieldDescription");
    
    
    if (cmp.getOptions().rootField() && (fieldLabel != null || fieldDesc != null)) {
        %><div class="coral-Form-fieldwrapper"><%

        fieldAttrs.addClass("coral-Form-field");
        
        if (fieldLabel != null) {
            %><label class="coral-Form-fieldlabel"><%= outVar(xssAPI, i18n, fieldLabel) %></label><%
        }
    }
    
    %><span <%= fieldAttrs.build() %>><%
        AttrBuilder buttonAttrs = new AttrBuilder(request, xssAPI);
        buttonAttrs.addClass("coral-FileUpload-trigger coral-Button");
        
        for (String variant : cfg.get("variant", new String[0])) {
            if ("actionBar".equals(variant)) {
                variant = "graniteActionBar";
            }
            buttonAttrs.addClass("coral-Button--" + variant);
        }
    
        %><span <%= buttonAttrs.build() %>><%
		    if (icon != null) {
		        %><i class="coral-Icon <%= cmp.getIconClass(icon) %>"></i> <%
		    }
		    %><input <%= inputAttrs.build() %>>
		</span>
    </span><%
    
    if (cmp.getOptions().rootField() && (fieldLabel != null || fieldDesc != null)) {
        if (fieldDesc != null) {
            %><span class="coral-Form-fieldinfo coral-Icon coral-Icon--infoCircle coral-Icon--sizeS" data-init="quicktip" data-quicktip-type="info" data-quicktip-arrow="right" data-quicktip-content="<%= xssAPI.encodeForHTMLAttr(outVar(xssAPI, i18n, fieldDesc)) %>"></span><%
        }
        %></div><%
    }
%>