/*******************************************************************************
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2014 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 ******************************************************************************/
package apps.granite.sightly.templates;

import javax.script.Bindings;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.corteva.model.component.models.PageMetaModel;
import com.corteva.core.utils.CommonUtils;
import com.day.cq.wcm.api.Page;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.scripting.sightly.pojo.Use;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import com.adobe.granite.ui.clientlibs.HtmlLibraryManager;


public class ClientLibUseObject implements Use {

    private static final String BINDINGS_CATEGORIES = "categories";
    private static final String BINDINGS_MODE = "mode";

    private HtmlLibraryManager htmlLibraryManager = null;
    private String[] categories;
    private String mode;
    private SlingHttpServletRequest request;
    private PrintWriter out;
    private Logger log;

    public void init(Bindings bindings) {
        Object categoriesObject = bindings.get(BINDINGS_CATEGORIES);
        log = (Logger) bindings.get(SlingBindings.LOG);
        if (categoriesObject != null) {
            if (categoriesObject instanceof Object[]) {
                Object[] categoriesArray = (Object[]) categoriesObject;
                categories = new String[categoriesArray.length];
                int i = 0;
                String siteName ="";
                request = (SlingHttpServletRequest) bindings.get(SlingBindings.REQUEST);
                String pathInfo = request.getRequestPathInfo().getResourcePath().toString();

                if(pathInfo.startsWith("/content/") && !(pathInfo.contains("/content/experience-fragments"))){
                    //This code will work for content pages, client lib inclussion.
                    siteName = pathInfo.substring(pathInfo.indexOf("/content/")+9, pathInfo.indexOf("/", 9));
                } else if(pathInfo.contains("/content/experience-fragments")) {
                    //This code will work for experienec fragments client lib inclussion.
                    siteName = pathInfo.substring(pathInfo.indexOf("/content/experience-fragments/")+30, pathInfo.indexOf("/", 30));
                }


                for (Object o : categoriesArray) {
                    if (o instanceof String) {
                        if(null != siteName && siteName.length() > 0 && !(siteName.equalsIgnoreCase("corteva"))){
                             String category = ((String) o).trim()+"-"+siteName;
                            categories[i++] = category;
                        } else {
                            categories[i++] = ((String) o).trim();
                        }
                    }
                }
            } else if (categoriesObject instanceof String) {
                categories = ((String) categoriesObject).split(",");
                int i = 0;
                for (String c : categories) {
                    categories[i++] = c.trim();
                }
            }
            if (categories != null && categories.length > 0) {
                mode = (String) bindings.get(BINDINGS_MODE);
                request = (SlingHttpServletRequest) bindings.get(SlingBindings.REQUEST);
                SlingScriptHelper sling = (SlingScriptHelper) bindings.get(SlingBindings.SLING);
                htmlLibraryManager = sling.getService(HtmlLibraryManager.class);
            }
        }
    }

    public String include() {

        StringWriter sw = new StringWriter();
        try {
            if (categories == null || categories.length == 0)  {
                log.error("'categories' option might be missing from the invocation of the /libs/granite/sightly/templates/clientlib.html" +
                        "client libraries template library. Please provide a CSV list or an array of categories to include.");
            } else {
                PrintWriter out = new PrintWriter(sw);
                if ("js".equalsIgnoreCase(mode)) {
                    htmlLibraryManager.writeJsInclude(request, out, categories);
                } else if ("css".equalsIgnoreCase(mode)) {
                    htmlLibraryManager.writeCssInclude(request, out, categories);
                } else {
                    htmlLibraryManager.writeIncludes(request, out, categories);
                }
            }
        } catch (IOException e) {
            log.error("Failed to include client libraries {}", categories);
        }
        return sw.toString();
    }
}
