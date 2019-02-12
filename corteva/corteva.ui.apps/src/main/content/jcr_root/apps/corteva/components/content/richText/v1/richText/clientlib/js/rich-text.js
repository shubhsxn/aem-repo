'use strict';

var APP = window.APP = window.APP || {};

APP.richText = (function () {

    var bindEventsToUI = function () {

    };

    var bindOnResize = function () {
        /* Don't forget to uncomment APP.global.resizeRouteList.push('richText'); */
    };

    var init = function (element) {
        if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('richText')){
            return;
        }
        bindEventsToUI();
        //APP.global.resizeRouteList.push('richText');
        checkRTEAnalytics();

        //add download link on RTE
        addDownloadLink();
    };

     var addDownloadLink = function(){
        if($(".download-link a").length){
            $(".download-link a").attr('download','')
        }

        if($("a span.download-link").length){
             $("span.download-link").closest('a').attr('download','');
        }
    }

    var checkRTEAnalytics = function () {
        if($('.det16-rich-text-field').find('a').length){
            $('.det16-rich-text-field').find('a').attr('data-analytics-type','cta-link');
        }

        let emailLinkCheck = 'mailTo',
            linkHref = "" ;

        $('.det16-rich-text-field a').each((index,element) => {
            let $this = $(element);
            linkHref = ($this.attr('href')) ? $this.attr('href') : ' ';

            if(linkHref.indexOf(emailLinkCheck) !== -1){
                $this.attr('data-analytics-type','cta-contact')
                     .attr('data-analytics-value',$this.text());
            }
        });
    }

    /**
     * interfaces to public functions
     */
    return {
        init: init,
        bindOnResize: bindOnResize
    };

}());

$(document).ready(function(){
APP.richText.init();
});