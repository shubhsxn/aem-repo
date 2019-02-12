/*******************************************************************************
 * Copyright 2016 Adobe Systems Incorporated
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
"use strict";

var global = this;

use(["/libs/wcm/foundation/components/utils/AuthoringUtils.js"], function (AuthoringUtils) {

    var _getTemplateCategories = function () {
        var categories = [];

        // Get the categories from the clientLibs property of the page policy in case the page is authored templated
        var template = currentPage.template;
        if (template && template.hasStructureSupport()) {
            var templateResource = template.adaptTo(Packages.org.apache.sling.api.resource.Resource);

            // Default template editor clientlibs
            // When currentPage is a node under the current template
            if (currentPage.getPath().startsWith(templateResource.getPath())) {
                categories.push("wcm.foundation.components.parsys.allowedcomponents");
            }

            var pageContentPolicyMapping = templateResource.getChild("policies/jcr:content");

            if (pageContentPolicyMapping != null) {
                var policyMapping = pageContentPolicyMapping.adaptTo(Packages.com.day.cq.wcm.api.policies.ContentPolicyMapping);

                if (policyMapping != null) {
                    var pagePolicy = policyMapping.getPolicy();

                    if (pagePolicy != null) {
                        var clientLibs = pagePolicy.properties["clientlibs"];

                        if (clientLibs && clientLibs != null) {
                            // Check if it's an array xor a string
                            if (clientLibs.getClass && clientLibs.length && !clientLibs.getClass().equals(java.lang.String)) {
                                for (var i = 0, length = clientLibs.length; i < length; i++) {
                                    categories.push(clientLibs[i]);
                                }
                            } else {
                                categories.push(clientLibs);
                            }
                        }
                    }
                }
            }
        }

        return categories;
    };
    
    var _getUndoConfig = function () {
        var undoCfg = undefined;
        if (global.Packages && global.sling) {
            var undoConfigService = global.sling.getService(global.Packages.com.day.cq.wcm.undo.UndoConfigService);
            var javaStringWriter = new java.io.StringWriter();
            undoConfigService.writeClientConfig(javaStringWriter);
            undoCfg = javaStringWriter.toString();
        }
        return undoCfg;
    };
    
    var _getDialogPath = function () {
        var dialogPath = undefined;
        if (global.editContext) {
            dialogPath = global.editContext.getComponent().getDialogPath(); 
        }
        return dialogPath;
    };
    
    return {
        isTouchAuthoring: AuthoringUtils.isTouch,
        dialogPath: _getDialogPath(),
        undoConfig: _getUndoConfig(),
        wcmmode: global.wcmmode,
        templateCategories: _getTemplateCategories()
    };
});
