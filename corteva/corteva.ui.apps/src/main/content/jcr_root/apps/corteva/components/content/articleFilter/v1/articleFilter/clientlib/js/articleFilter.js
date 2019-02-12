$(function() {
    let $loadmoreWrapper = $('.con18-article-feed'),
        $itemWrapper = $('.con18-article-feed'),
        $loadmorebtn = $('.loadmore'),
        $yearfilter = $('#years-dropdown'),
        $dropdown = $('.form-dropdown-filter'),
        $monthDropdown = $('#month-dropdown'),
        $topicfilter = $('#topicfilter-dropdown'),
        $firstdropdownVal = '',
        $yeardropwdownVal = '',
        $monthdropdownVal = '',
        $loadmoreFlag = false,
        dropdownData = {},
        resetDropdowndata = false,
        dropdownLoadeddata = {},
        $triggeredfromArticle = false,
        $monthfilterlabel =  $('.month-filter'),
        $getLoadMoreUrl = $('.loadmore').attr("href"),
        $loadMoreUrlValue = $('.loadmore').length === 0 ? 0 : $getLoadMoreUrl.match(/([^\/]*)\/*$/)[1].split('.'),
        $getLoadMoreUrlPageload = $('.loadmore').attr("href"),
        $loadMoreUrlValuePageload = $('.loadmore').length === 0 ? 0 : $getLoadMoreUrlPageload.match(/([^\/]*)\/*$/)[1].split('.');
    /**
     * Initialization function
    */
    function initialize() {
        if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('articlefilter')){
            return;
        }
        if ($loadmoreWrapper.length) {
            monthFilter();
            topicFilter();
            loadmoreClick();
            populateDatayearDropdown();
            yearfilterChange();
            $monthfilterlabel.hide();
        }
    }

    let populateDatayearDropdown = function(data) {
        $loadmoreFlag = false;

        if (resetDropdowndata === false) {
            dropdownData = $yearfilter.data('datedropdown');
        } else {
            dropdownData = data;
        }
        //pushing the values in the year  dropdown
        $yearfilter.empty();
        $yearfilter.append('<option value=""> Years </option>');

        if (jQuery.isEmptyObject(dropdownData) === false) {
            $.each(dropdownData, function(k, v) {
                $yearfilter.append('<option value="' + v.year + '"  data-analytics-value="'+ v.year +'">' + v.year + '</option>');
                $yearfilter.trigger('change.select2');
            });
        } else {
            $monthDropdown.empty().append('<option value=""> Month </option>');
            $yearfilter.empty().append('<option value=""> Years </option>');
        }
    },

    //yearfilter
    yearfilterChange = function() {
         //month filter dropdown function
         $yearfilter.on('change',function() {
            let currentVal = $(this).val();
            $yeardropwdownVal =  $(this).val();
            $triggeredfromArticle = false;

            let param1 = $firstdropdownVal === '' ? 'All' : $firstdropdownVal,
                param2 =  $yeardropwdownVal === '' ? 'All' : $yeardropwdownVal,
                param3 =  'All';

            $monthDropdown.empty();

            if (currentVal !== '') {
                $('.loadmore').attr("href", $loadMoreUrlValue[0] + "." + + 0 + ".html");
                loadmoreArticle(param1,param2,param3,0);

                $.each(dropdownData, function(i, ob) {
                    if(currentVal === ob.year){
                        //pushing the values in the month dropdown
                        $.each(ob.month, function(index, object) {
                            for (let month in object){
                                if(object.hasOwnProperty(month)) {
                                    $monthfilterlabel.show();
                                    $monthDropdown.append('<option value="' + month + '" data-analytics-value="'+ month +'">' + object[month] + '</option>');
                                    $dropdown.trigger('change.select2');
                                }
                            }
                        });
                    }
                });
            } else {
                if($firstdropdownVal === 'All') {
                    $('.loadmore').attr("href", $loadMoreUrlValue[0] + "." + + 0 + ".html");
                    loadmoreArticle('All','All','All',0);
                    //loadmoreArticle('ALL','ALL','ALL');
                }
                resetDropdowndata = false;
                $monthfilterlabel.hide();
                $monthdropdownVal = 'All'
                $monthDropdown.empty().append('<option value=""> Month </option>');
                $dropdown.trigger('change.select2');
            }
        });
    },

    //month filter dropdown function
    monthFilter = function() {
        $loadmoreFlag = false;
        $triggeredfromArticle = false;

        $monthDropdown.on('change',function() {
            $monthdropdownVal = $(this).val();

            let param1 = $firstdropdownVal === '' ? 'All' : $firstdropdownVal,
                param2 = $yeardropwdownVal === '' ? 'All' : $yeardropwdownVal,
                param3 = $monthdropdownVal === '' ? 'All' : $monthdropdownVal;

            $('.loadmore').attr("href", $loadMoreUrlValue[0] + "." + + 0 + ".html");
            loadmoreArticle(param1,param2,param3,0);
        });
    },

    //article filter first dropdown function

    topicFilter = function() {
        $loadmoreFlag = false;
        let articledropdownData = $topicfilter.data('articletypedropdown');

        $.each(articledropdownData, function(i, v) {
            $topicfilter.append('<option value="' + i + '" data-analytics-value="'+i+'">' + v + '</option>');
            $dropdown.trigger('change.select2');
        });

        $topicfilter.on('change',function() {
            $firstdropdownVal = $(this).val();
            $yeardropwdownVal = 'All';

            let param1 = $firstdropdownVal === '' ? 'All' : $firstdropdownVal,
                param2 = 'All',
                param3 = 'All';

            if ($firstdropdownVal === 'All') {
                resetDropdowndata = false;
            } else {
                resetDropdowndata = true;
                $triggeredfromArticle = true;
                $yearfilter.empty().append('<option value=""> Years </option>');
            }
            $monthDropdown.empty().append('<option value=""> Month </option>');
            $dropdown.trigger('change.select2');
            $('.loadmore').attr("href", $loadMoreUrlValue[0] + "." + + 0 + ".html");
            loadmoreArticle(param1,param2,param3,0);
            populateDatayearDropdown();
          });
    },

    //load more function
    loadmoreClick = function() {
      let param1 = $firstdropdownVal === '' ? 'All' : $firstdropdownVal,
            param2 = $yeardropwdownVal === '' ? 'All' : $yeardropwdownVal,
            param3 = $monthdropdownVal === '' ? 'All' : $monthdropdownVal,
            UrlGetvalue = parseInt($loadMoreUrlValue[1], 10);
        $loadmorebtn.on('click',function(e) {
            e.preventDefault();
            let count = $('.con18-article-feed').data('articledisplaycount');
            loadmoreArticle(param1,param2,param3,count);
            $loadmoreFlag = true;
            $(this).attr("href", $loadMoreUrlValue[0] + "." + + (UrlGetvalue + 1) + ".html");
        });
    },

    //ajax function to call servelet
    loadmoreArticle = function(topic,year,month,count) {
      let pageName = $itemWrapper.data('pagename'),
        $getLoadMoreUrl = $('.loadmore').attr("href"),
        $loadMoreUrlValue = $('.loadmore').length === 0 ? 0 : $getLoadMoreUrl.match(/([^\/]*)\/*$/)[1].split('.'),
        UrlGetvalue = $loadMoreUrlValue === 0 ? 0 : parseInt($loadMoreUrlValue[1], 10),
        currUrlValue = $('.loadmore').length === 0 ? $loadMoreUrlValuePageload[0] + "." + + (UrlGetvalue + 1) + ".html" : $loadMoreUrlValue[0] + "." + + (UrlGetvalue + 1) + ".html",
        getCount = count *  UrlGetvalue,
        dataURL = '/bin/corteva/articleFilter.'+topic+'.'+year+'.'+month+'.'+getCount+'.'+pageName+'.json';

        $itemWrapper.append("<div class='loader search-loader'></div>");
        $.ajax({
            type: "GET",
            url: dataURL,
            dataType: 'json',
            success: function (data) {
                if (data) {
                    $('.loader').fadeOut(500);

                    if($loadmoreFlag === true){
                        $itemWrapper.append(articleFeedtemplate(data.articlePagesList));
                        $loadmoreFlag = false;
                        if (Object.keys(data.articlePagesList).length === 0){
                            $itemWrapper.append("<div class='no-result'><h3>No Result</h3></div>");
                        } else{
                            $itemWrapper.remove('.no-result');
                        }
                    } else{
                        $itemWrapper.html(articleFeedtemplate(data.articlePagesList));
                        if (Object.keys(data.dateMapFilter).length !== 0) {
                            if ($triggeredfromArticle === true) {
                              dropdownLoadeddata = data.dateMapFilter;
                              populateDatayearDropdown(dropdownLoadeddata);
                            }
                        }
                        if (Object.keys(data.articlePagesList).length === 0) {
                            $itemWrapper.append("<div class='no-result'><h3>No Result</h3></div>");
                        } else {
                            $itemWrapper.remove('.no-result');
                        }
                    }
                    if (data.showLoadMore === false) {
                        $('.loadmore').remove();
                    } else {
                        $('.loadmore').remove();
                        $loadmoreWrapper.append(`<a href='${currUrlValue}' class='c-button  secondary loadmore'  data-analytics-type='cta-button' data-analytics-value='Load More '> Load More </a>`);
                        $('.loadmore').on('click',function(e){
                            e.preventDefault();
                            let countFeed = $('.con18-article-feed').data('articledisplaycount'),
                                param1 = $firstdropdownVal === '' ? 'All' : $firstdropdownVal,
                                param2 = $yeardropwdownVal === '' ? 'All' : $yeardropwdownVal,
                                param3 = $monthdropdownVal === '' ? 'All' : $monthdropdownVal;

                            loadmoreArticle(param1,param2,param3,countFeed);
                            $loadmoreFlag = true;
                        });
                        $loadmorebtn.show();
                    }
                }
            },
            error:function(xhr) {
              console.log('There is an error with service: ' + xhr.status);
              $itemWrapper.remove('.no-result');
              $itemWrapper.remove('.loader');
            }
         });
      },

    //json template
    articleFeedtemplate = function(feedData) {
        let testDisplayArticleType = $('.con18-article-feed').data('testdisplayarticletype'),
        testDisplayDate = $('.con18-article-feed').data('testdisplaydate');
        let template = responseData => `
            ${responseData.map(data => `
              <div class="item row">
                ${data.hasOwnProperty('primaryImage') ? `
                <div class="article-image col s12 l2 no-pad">
                    <picture>
                        <source srcset="${data.primaryImage.desktopImagePath}" media="(min-width: 1200px)">
                        <source srcset="${data.primaryImage.tabletImagePath}" media="(min-width: 992px)">
                        ${data.primaryImageMobile ? `<source srcset="${data.primaryImageMobile.mobileImagePath}" media="(max-width: 991px)"> `:` `}
                        <img class="" src="${data.primaryImage.desktopImagePath}"  alt="${data.shortDescription}">
                    </picture>
                  </div>
                    ` : `
                    `} 
                <div class="col s12 no-pad  ${data.primaryImage ?` article-info l10 `:` `} ">
                    <span class="eyebrow"> 
                        ${testDisplayArticleType ?` ${data.articleType} `:` `} 
                        ${testDisplayArticleType && testDisplayDate ?` •  `:` `}
                        ${testDisplayDate ?` ${data.customDisplayDate} `:` `}
                    </span>
                   
               
                <h3>
                ${data.isExternalURL === true ? `
                    <a href="${data.articlePagePath}" target="_blank" rel="noopener noreferrer" data-analytics-type="cta-link" class="external">${data.articleTitle}</a>
                    <a href="${data.articlePagePath}" target="_blank" rel="noopener noreferrer" class="domain">${data.externalURLLabel}</a>
                    ` : `
                    <a href="${data.articlePagePath}" data-analytics-type="cta-link" target="_self">${data.articleTitle}</a> 
                    `}
                </h3>               
                <p>${data.shortDescription}</p>
                </div>
            </div>               
            `).join('')}
        `;
        return template(feedData);
    }
    return initialize();

});
