
(function ($, $document) {
    'use strict';

    var  expfragmentPathsMethod = function(isCardTypeRequired) {
        var $expfragmentPathsArray = $('span.expfragPath input.js-coral-pathbrowser-input:visible');
        var $cardTypeArray = $expfragmentPathsArray;
        if(isCardTypeRequired){
            $cardTypeArray = $('span.cardType select:visible');
        }
        var componentResourcePath = $cardTypeArray.closest('form').attr('action'),
            jsonData = {},
            expFragArray = [];
        $expfragmentPathsArray.closest('form').find('input[type="hidden"].componentHidden').val(componentResourcePath);
     //   if ($expfragmentPathsArray.length > 0){
           $expfragmentPathsArray.each(function(index, element) {
               expFragArray.push( $(element).val());
           });
           jsonData["expfragPath"] = expFragArray;
           jsonData["componentResourcePath"] = componentResourcePath;
           $.ajax({
               type: "POST",
               url: "/bin/corteva/createdeletenode",
               data: jsonData,
               dataType: 'json',
               success: function () {
               //console.log('Nodes created');
               }
            });
      //  }
    },

    /* Function to populate Input fields dynamically
     * based on service response.
     */
    populateInputFields = function(responsePageJson, fieldJsonObj, fieldJsonObjKeys, $context) {
        var fieldJsonObjKeysLength = fieldJsonObjKeys.length;
        for (var i=0; i<fieldJsonObjKeysLength; i++) {
            var fieldName = fieldJsonObj[fieldJsonObjKeys[i]],
                valueToPolulate = responsePageJson[fieldJsonObjKeys[i]];
            if(typeof responsePageJson[fieldJsonObjKeys[i]] === 'undefined'){
                valueToPolulate = '';
            }
            if($(".coral-DatePicker input[name='"+fieldName+"']").length &&
                    (typeof responsePageJson[fieldJsonObjKeys[i]] !== 'undefined' || '' !== responsePageJson[fieldJsonObjKeys[i]])) {
                var displayFormatDate = responsePageJson[fieldJsonObjKeys[i]],
                    d = new Date(displayFormatDate),
                    curr_date = d.getDate(),
                    curr_month = d.getMonth();
                    curr_month++;
                var curr_year = d.getFullYear(),
                    storeFormatDate = curr_year + "-" + curr_month + "-" + curr_date;
                $("input[name='"+fieldName+"']").siblings('.coral-Textfield').val(displayFormatDate);
                $("input[name='"+fieldName+"']").val(storeFormatDate);
                if($("input[name='"+fieldName+"']").siblings('.coral-Textfield').attr('aria-required') === "true") {
                    $("input[name='"+fieldName+"']").siblings('.coral-Textfield').removeClass('is-invalid');
                    $("input[name='"+fieldName+"']").parent().siblings('.coral-Form-fielderror').hide();
                }
            } else if ($context.closest('.js-coral-Multifield-input').length || $context.closest('coral-multifield-item-content').length) {
                var $currentMultiFormSection = $context.closest('coral-multifield-item-content').length ? $context.closest('coral-multifield-item-content') : $context.closest('.js-coral-Multifield-input');
                if($('select[name="./videoType"]').length || $('coral-select[name="./mediaType"]').length){
                    if(fieldJsonObjKeys[i] === 'videoDescription'){
                        $currentMultiFormSection.find("textarea[name='"+fieldName+"']").val(valueToPolulate.substring(0, 250));
                    }
                    else{
                        $currentMultiFormSection.find("input[name='"+fieldName+"']").val(valueToPolulate);
                    }
                }
                else{
                    $currentMultiFormSection.find("input[name='"+fieldName+"']").val(valueToPolulate);
                }
                $currentMultiFormSection.find("input[name='"+fieldName+"']").change();
            } else {
                var $currentFormSection = $('.cq-dialog.foundation-form');
                if($('select[name="./videoType"]').length || $('coral-select[name="./mediaType"]').length){
                    if(fieldJsonObjKeys[i] === 'videoDescription'){
                        $currentFormSection.find("textarea[name='"+fieldName+"']").val(valueToPolulate.substring(0, 250));
                    }
                    else{
                        $currentFormSection.find("input[name='"+fieldName+"']").val(valueToPolulate);
                    }
                }
                else{
                    $currentFormSection.find("input[name='"+fieldName+"']").val(valueToPolulate);
                }
                $currentFormSection.find("input[name='"+fieldName+"']").change();

                if($currentFormSection.find('select[name="./contentType"]').val()==="page"
                    && $currentFormSection.find('.relatedContentFullWidthClass').length ){

                    if(fieldJsonObjKeys[i] === 'shortDescription'){
                        $currentFormSection.find('input[name="./shortDescription"]')
                            .parents('.richtext-container').children('.coral-Textfield--multiline')
                            .html('<p>'+ valueToPolulate +'</p>');
                    }
                }
            }
        }
    },

    /* Function to populate Input fields dynamically
     * based on service response.
     */
    populateImageFields = function(responseImageJson, imageJsonObj, imageJsonObjKeys) {

        var valueToPolulate = responseImageJson[imageJsonObjKeys];
            if(typeof responseImageJson[imageJsonObjKeys] === 'undefined'){
                valueToPolulate = '';
            }
        var $imageJsonObj = $("input[name='"+imageJsonObj[imageJsonObjKeys]+"']"),
            $fileUpload = $imageJsonObj.closest(".coral-FileUpload");

        if(valueToPolulate !== ''){
            $fileUpload
            .find("div.cq-FileUpload-thumbnail-img")
            .html('<img class="cq-dd-image" src="'+valueToPolulate+'">');
        }
        else{
            $fileUpload
            .find("div.cq-FileUpload-thumbnail-img")
            .html('');
        }

        $fileUpload
        .find('.cq-FileUpload-edit.coral-Button.coral-Button--quiet')
        .remove();

        $fileUpload
        .find("div.cq-FileUpload-thumbnail-img")
        .after('<button class="cq-FileUpload-edit coral-Button coral-Button--quiet" data-cq-fileupload-filereference="'+responseImageJson[imageJsonObjKeys]+'" data-cq-fileupload-viewinadminuri="/assetdetails.html{+item}" type="button" autocomplete="off" is="coral-button" variant="quiet" data-foundation-tracking-event="{&quot;feature&quot;:null,&quot;element&quot;:&quot;edit&quot;,&quot;type&quot;:&quot;button&quot;,&quot;widget&quot;:{&quot;name&quot;:null,&quot;type&quot;:&quot;button&quot;}}" size="M"><coral-button-label>Edit</coral-button-label></button>');

        $imageJsonObj.val(valueToPolulate).removeAttr('disabled');
        $imageJsonObj.trigger("change");

     },

    /* Function to reorder nodes of anchor navigation
     * component.
     */
    anchorLinksTitleMethod = function() {
        var $anchorLinksTitleArray = $('input[name="./anchorLinkLabel"]').closest('.coral-Form-fieldwrapper').parent().find('input[name="./anchorLinkLabelHidden"]'),
            anchorNavCompResPath = $anchorLinksTitleArray.closest('form').attr('action'),
            jsonData = {},
            anchorNavArray = [];

        $anchorLinksTitleArray.closest('form').find('input[name="./anchorNavHidden"]').val(anchorNavCompResPath);

        $anchorLinksTitleArray.each(function(index, element) {
           anchorNavArray.push( $(element).val());
        });

        jsonData["anchorLinkLabelHidden"] = anchorNavArray;
        jsonData["anchorNavResourcePath"] = anchorNavCompResPath;
        $.ajax({
            type: "POST",
            url: "/bin/corteva/reordernodes",
            data: jsonData,
            dataType: 'json',
            success: function () {
             //console.log('Nodes created');
            }
         });
    },

    /* impactTilesMethod
     * component.
     */
    impactTilesMethod = function() {
        var $firstExpandedTilePath = $('input[name="./expandedImpactFirstTilePath"]'),
            $lastExpandedTilePath = $('input[name="./expandedImpactLastTilePath"]'),
            $impactTilesArray = $('input[name="./impactTilePath"]'),
            $expandedTilesNumberSelection = $('.custom-dynamic-limit-selector select').not('.hide .custom-dynamic-limit-selector select'),
            expTilesLimit = parseInt($expandedTilesNumberSelection.val()),
            compResPath = $impactTilesArray.closest('form').attr('action'),
            jsonData = {}, impactTilesArr = [];

        $impactTilesArray.closest('form').find('input[name="./fixedGridHidden"]').val(compResPath);

        $impactTilesArray.each(function(index, element) {
            impactTilesArr.push( $(element).val());
        });

        if (expTilesLimit > 0) {
            jsonData["expandedImpactFirstTilePath"] = $firstExpandedTilePath.val();
        }
        if (expTilesLimit > 1) {
            jsonData["expandedImpactLastTilePath"] = $lastExpandedTilePath.val();
        }
        jsonData["impactTilePath"] = impactTilesArr;
        jsonData["fixedGridHidden"] = compResPath;

        $.ajax({
            type: "POST",
            url: "/bin/corteva/createdeletenode",
            data: jsonData,
            dataType: 'json',
            success: function () {
             //console.log('Nodes created');
            }
         });
     },

     imageAltPopulation = function(imagePath, $context) {
        var imgFieldStr = $context.closest('.assetMetadataClass').data('assetfieldstobepopulated'),
            imgSrc = imagePath,
            //imgFieldJsonObj = JSON.parse(imgFieldStr),
            imgFieldJsonObjKeys = Object.keys(imgFieldStr),
            imgFieldJsonObjKeysLength = imgFieldJsonObjKeys.length;

            $.ajax({
            type: "POST",
            url: "/bin/corteva/contentmetadata",
            data: {'assetPropertiesKeys': imgFieldJsonObjKeys, 'assetPath': imgSrc},
            dataType: 'json',
            success: function (response) {
                if(response) {
                    //console.log(response + "ImageTextResponse");
                    if(imgFieldJsonObjKeysLength > 0) {
                        populateInputFields(response, imgFieldStr, imgFieldJsonObjKeys, $context);
                    }
                }
            }
         });
     };


    $document.on('dialog-ready', function(){
        if(!checkAuthorFeatureFlag('jsFramework')){
            return;
        }
        /* Function create and delete experience fragment
         * nodes.
         */
        //events on dialog-submit
        $('body').on('click', '.cq-dialog-submit', function() {
            $('span.cardType select:visible').length > 0 ? expfragmentPathsMethod(true) : '';
            $(".anchorNavigationClass").length > 0 ? expfragmentPathsMethod(false) : '';
            $('input[name="./anchorLinkLabel"]').length > 0 ? anchorLinksTitleMethod() : '';
            $('input[name="./impactTilePath"]').length > 0 ? impactTilesMethod() : '';
        });

        $('body').on('change', 'input[name="./anchorLinkLabel"]', function() {
            var $this = $(this),
                $anchorlinkHidden = $this.closest('.coral-Form-fieldwrapper').parent().find('input[name="./anchorLinkLabelHidden"]'),
                //$anchorLinksHiddenTotal = $('input[name="./anchorLinkLabel"]').closest('.coral-Form-fieldwrapper').parent().find('input[name="./anchorLinkLabelHidden"]'),
                anchorHiddenLinkCounter = $(this).closest('.js-coral-Multifield-input').index()+1;

            if($anchorlinkHidden.val() === ""){
                $anchorlinkHidden.val($this.val().replace(/[^a-zA-Z0-9]+/gi, '')+'_'+anchorHiddenLinkCounter);
            }
        });

        $('body').on('change.videoID', '.js-focus-out', function() {
            var $this = $(this), videoId = $this.val();
            var jsonData = {};
            jsonData["videoId"] = videoId;

            var videoType = $('select[name="./videoType"]').val(),
                videoApi = "",
                youtubeApiKey = $($('#ContentFrame').contents()).find('.youtube-info').data('youtube-key');

                if($('coral-multifield-item-content').length){
                    videoType = $this.closest('coral-multifield-item-content').find('coral-select[name="./mediaType"]').find('input[name="./mediaType"]').val();
                }

            if(videoType === 'youtube' || videoType === 'youtubevideo'){
                videoApi = $($('#ContentFrame').contents()).find('.youtube-info').data('youtube-api')+videoId+"&key="+youtubeApiKey+"&format=json";
            }
            if(videoId!=""){
            $.ajax({
                type: "GET",
                url: videoApi,
                cache: false,
                data: jsonData,
                dataType: 'jsonp'
             }).done(function(response){
                if(response) {

                    if($this.closest('.js-coral-Multifield-input').length || $this.closest('coral-multifield-item-content').length){
                        var $currentMultiFormSection = $this.closest('coral-multifield-item-content').length ? $this.closest('coral-multifield-item-content'): $this.closest('.js-coral-Multifield-input');
                        $currentMultiFormSection.find('input[name="./videoTitle"]').val(response.items[0].snippet.title);
                        $currentMultiFormSection.find('textarea[name="./videoDescription"]').val(response.items[0].snippet.description.substring(0, 250));
                        $currentMultiFormSection.find('input[name="./videoAltText"]').val(response.items[0].snippet.title);
                        $currentMultiFormSection.find('input[name="./videoAltText"]').change();
                        $currentMultiFormSection.find('input[name="./videoThumbnail"]').val(response.items[0].snippet.thumbnails.medium.url);
                        $currentMultiFormSection.find('input[name="./videoThumbnail"]').change();
                    }
                    else{
                        var $currentFormSection = $this.closest('.cq-dialog.foundation-form');
                        $currentFormSection.find('input[name="./videoTitle"]').val(response.items[0].snippet.title);
                        $currentFormSection.find('textarea[name="./videoDescription"]').val(response.items[0].snippet.description.substring(0, 250));
                        $currentFormSection.find('input[name="./videoAltText"]').val(response.items[0].snippet.title);
                        $currentFormSection.find('input[name="./videoAltText"]').change();
                        $currentFormSection.find('input[name="./videoThumbnail"]').val(response.items[0].snippet.thumbnails.medium.url);
                        $currentFormSection.find('input[name="./videoThumbnail"]').change();
                    }

                }
            }).fail(function(jqXHR, textStatus){
                console.log("Error details [" + jqXHR + ", " + textStatus + "]");
            }).always(function(){

            });
        }
         });

        $('body').on('change', '.js-page-populate input.js-coral-pathbrowser-input', function() {
            var $this = $(this),
                $jsPagePopulate = $this.closest('.js-page-populate'),
                fieldJsonStr = $jsPagePopulate.data('fieldstobepopulated'),
                imageJsonStr = $jsPagePopulate.data('imagetobepopulated'),
                pathBrowser = $this.val(),
                fieldJsonObj = {},
                fieldJsonObjKeys = [],
                fieldJsonObjKeysLength = 0,
                imageJsonObj = {},
                imageJsonObjKeys = {}; //assuming image field can be only 1
                try {fieldJsonObj = JSON.parse(fieldJsonStr);}
                catch(e) {fieldJsonObj = fieldJsonStr;}
                try {imageJsonObj = JSON.parse(imageJsonStr);}
                catch(e) {imageJsonObj = imageJsonStr;}
                fieldJsonObjKeys = Object.keys(fieldJsonObj);
                fieldJsonObjKeysLength = fieldJsonObjKeys.length;
                if(typeof imageJsonObj !== 'undefined') {
                    imageJsonObjKeys = Object.keys(imageJsonObj)[0]; //assuming image field can be only 1
                }
                $.ajax({
                    type: "POST",
                    url: "/bin/corteva/contentmetadata",
                    data: {
                        "pagePropertiesKeys": fieldJsonObjKeys,
                        "imageKey": imageJsonObjKeys,
                        "pagePath":pathBrowser
                    },
                    dataType: 'json',
                success: function (response) {
                    if(response) {
                        //console.log(response);
                        if (fieldJsonObjKeysLength > 0) {
                            populateInputFields(response.pageJson, fieldJsonObj, fieldJsonObjKeys, $this);
                        }
                        if (imageJsonObjKeys.length > 0) {
                            populateImageFields(response.imageJson, imageJsonObj, imageJsonObjKeys);
                            $(".coral-FileUpload").addClass('is-filled');
                        }
                    }
                }
             });
         });

        //image alt population on image drag/drop
        //var fileUpload = '.coral-FileUpload',
            //cuiFileUpload = $(fileUpload).data('fileUpload'),
        var $dropTarget = $(".assetMetadataClass span.cq-droptarget");

        if ($dropTarget.length > 0) {
            $dropTarget.on("assetselected", function (event) {
//                imageAltPopulation(event.path, $(this));
                $(this).find('input[type="hidden"].cq-FileUpload-filereference').trigger("change");
            });

            // works on path browser click
            /*cuiFileUpload.$element.on("fileuploadsuccess", function (event) {
                if (event.item) {
                    imageAltPopulation(event.item.file.name, $(this));
                }
            });*/
        }

        //image alt population
        $('body').on('change', '.assetMetadataClass .js-coral-pathbrowser-input', function() {

            var $this = $(this);
            imageAltPopulation($this.val(), $this);
        });//$(".assetMetadataClass span input[type='hidden'").eq(1)
        $('body').on('change', '.assetMetadataClass input[type="hidden"].cq-FileUpload-filereference', function() {
            var $this = $(this);
            imageAltPopulation($this.val(), $this);
        });
    });
})($, $(document));