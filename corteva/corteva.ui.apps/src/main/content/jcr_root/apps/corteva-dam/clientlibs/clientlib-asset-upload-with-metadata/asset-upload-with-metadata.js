(function ($, $document) {
    var DIALOG_ROOT_PATH = "/apps/corteva-dam/asset-upload-metadata-dialogs",
        UPLOAD_LIST_DIALOG = "#uploadListDialog",
        ACTION_CHECK_DATA_VALIDITY = "ACTION_CHECK_DATA_VALIDITY",
        url = document.location.pathname;

    if (url.indexOf("/assets.html") == 0) {
        handleAssetsConsole();
    } else if (url.indexOf(DIALOG_ROOT_PATH) == 0) {
        handleMetadataDialog();
    }

    function handleAssetsConsole() {
        $document.on("foundation-contentloaded", handleFileAdditions);
    }

    function handleMetadataDialog() {
        $(function () {
            _.defer(styleMetadataIframe);
        });
    }

    function registerReceiveDataListener(handler) {
        if (window.addEventListener) {
            window.addEventListener("message", handler, false);
        } else if (window.attachEvent) {
            window.attachEvent("onmessage", handler);
        }
    }

    function styleMetadataIframe() {
        var $dialog = $("coral-dialog");

        if (_.isEmpty($dialog)) {
            return;
        }

        $dialog.css("overflow", "hidden");

        $dialog[0].open = true;

        var $header = $dialog.css("background-color", "#fff").find(".coral-Dialog-header");

        $header.find(".cq-dialog-actions").remove();

        $dialog.find(".coral-Dialog-wrapper").css("margin", "0").find(".coral-Dialog-content").css("padding", "0");

        registerReceiveDataListener(postMetadata);

        function postMetadata(event) {
            var message = JSON.parse(event.data);

            if (message.action !== ACTION_CHECK_DATA_VALIDITY) {
                return;
            }

            var $dialog = $(".cq-dialog"),
                $fields = $dialog.find("[name^='./']"),
                data = {},
                $field, $fValidation, name, value, values,
                isDataInValid = false;

            $fields.each(function (index, field) {
                $field = $(field);
                name = $field.attr("name");
                value = $field.val();

                $fValidation = $field.adaptTo("foundation-validation");

                if ($fValidation && !$fValidation.checkValidity()) {
                    isDataInValid = true;
                }

                $field.updateErrorUI();

                if (_.isEmpty(value)) {
                    return;
                }

                name = name.substr(2);

                if (!_.isEmpty(data[name])) {
                    if (_.isArray(data[name])) {
                        data[name].push(value);
                    } else {
                        values = [];
                        values.push(data[name]);
                        values.push(value);

                        data[name] = values;
                        data[name + "@TypeHint"] = "String[]";
                    }
                } else {
                    data[name] = value;
                }
            });

            sendValidityMessage(isDataInValid, data);
        }

        function sendValidityMessage(isDataInValid, data) {
            var message = {
                action: ACTION_CHECK_DATA_VALIDITY,
                data: data,
                isDataInValid: isDataInValid
            };

            parent.postMessage(JSON.stringify(message), "*");
        }
    }

    function handleFileAdditions() {

        var $fileUpload = $("coral-chunkfileupload"),
            $metadataIFrame, $uploadButton, validateUploadButton,
            metadata;

        $fileUpload.on('coral-fileupload:fileadded', addMetadataDialog);

        $fileUpload.on('coral-fileupload:loadend', postMetadata);

        function sendDataMessage(message) {
            $metadataIFrame[0].contentWindow.postMessage(JSON.stringify(message), "*");
        }



        function addMetadataDialog() {

            var $dialog = $(UPLOAD_LIST_DIALOG);

            url = document.location.pathname;
            var contentPath = url.replace('/assets.html', '');
            $.ajax({
                type: "POST",
                url: "/bin/corteva/getschematype",
                data: {
                    "folderPath": contentPath
                },
                dataType: 'json',
                success: function (response) {
                    if (response) {
                        if (typeof response.metadataSchema !== 'undefined' && '' !== response.metadataSchema) {
                            if (!_.isEmpty($dialog.find("iframe"))) {
                                return;
                            }
                            var METADATA_DIALOG = DIALOG_ROOT_PATH + '/' + response.metadataSchema + '/cq:dialog.html';
                            var iFrame = '<iframe id="metadata-iframe" class="metadata-dialog-iframe" style="border: none;" width="640px" height="1200px" src="' + METADATA_DIALOG + '"/>',
                                $dialogContent = $dialog.find("coral-dialog-content");
                            $dialogContent.css({
                                "max-height": "400px",
                                "overflow-x": "hidden"
                            });

                            $metadataIFrame = $(iFrame).appendTo($dialogContent);
                            $dialogContent.find("input").css("width", "30rem");


                            setTimeout(function () {
                                var dialogHeight = $($('.metadata-dialog-iframe').contents()).find('.coral-Dialog-wrapper').height();
                                if (null != dialogHeight && typeof dialogHeight !== 'undefined' && dialogHeight != 0) {
                                    $('.metadata-dialog-iframe').css('height', dialogHeight + 100 + 'px');
                                }
                            }, 2000);
                            addValidateUploadButton($dialog);

                        }
                    }
                }

            });


        }

        function addValidateUploadButton($dialog) {
            var $footer = $dialog.find("coral-dialog-footer");

            $uploadButton = $footer.find("coral-button-label:contains('Upload')").closest("button");

            validateUploadButton = new Coral.Button().set({
                variant: 'primary'
            });

            validateUploadButton.label.textContent = Granite.I18n.get('Upload');

            validateUploadButton.classList.add('dam-asset-upload-button');

            $footer[0].appendChild(validateUploadButton);

            $uploadButton.hide();

            validateUploadButton.hide();

            validateUploadButton.on('click', function () {
                checkDataValidity();
            });

            $metadataIFrame.on('load', function () {
                validateUploadButton.show();
            });

            registerReceiveDataListener(isMetadataValid);
        }

        function isMetadataValid(event) {
            var message = JSON.parse(event.data);

            if ((message.action !== ACTION_CHECK_DATA_VALIDITY)) {
                return;
            }

            if (message.isDataInValid) {
                return;
            }

            metadata = message.data;

            validateUploadButton.hide();

            $uploadButton.click();
        }

        function checkDataValidity() {
            var message = {
                action: ACTION_CHECK_DATA_VALIDITY
            };

            sendDataMessage(message);
        }

        function postMetadata(event) {
            var detail = event.originalEvent.detail,
                folderPath = detail.action.replace(".createasset.html", ""),
                assetMetadataPath = folderPath + "/" + detail.item.name + "/jcr:content/metadata";

            //jcr:content/metadata created by the DAM Update asset workflow may not be available when the below post
            //call executes; ideally, post the parameters to aem, a scheduler runs and adds the metadata when metadata
            //node becomes available
            $.ajax({
                type: 'POST',
                url: assetMetadataPath,
                data: metadata
            })
        }
    }

})(jQuery, jQuery(document));