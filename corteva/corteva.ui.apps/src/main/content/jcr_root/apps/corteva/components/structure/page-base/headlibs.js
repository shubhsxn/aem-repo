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

// Hint: for performance and simplicity, use the synchronous JS APIs instead. We keep this code as is for compatibility.
use(["/libs/wcm/foundation/components/utils/ResourceUtils.js", "/libs/sightly/js/3rd-party/q.js"], function (ResourceUtils, Q) {

    var staticDesignPath = this.designPath + "/static.css";
    var designPath = this.designPath ? this.designPath + ".css" : undefined;

    var staticDesignDefer = Q.defer();

    granite.resource.resolve(staticDesignPath).then(function (staticDesignResource) {
        staticDesignDefer.resolve(staticDesignPath);
    }, function () {
        staticDesignDefer.resolve("");
    });

    return {
        staticDesign: staticDesignDefer.promise,
        design: designPath
    };
});
