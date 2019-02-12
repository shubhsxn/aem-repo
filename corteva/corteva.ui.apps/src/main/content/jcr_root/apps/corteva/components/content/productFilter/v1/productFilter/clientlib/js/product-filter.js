$(function() {
    let $labelFinderWrap = $('.con06-label-finder'),
        $searchedResultWrap = $labelFinderWrap.find('.result'),
        $resultCount = $labelFinderWrap.find('.result-count'),
        $noResult =  $labelFinderWrap.find('.no-result'),
        $loadMoreBtn = $labelFinderWrap.find('.loadmore-btn'),
        $sidebarForm = $labelFinderWrap.find('.product-label-finder'),
        $inputSearch = $sidebarForm.find('.search-products'),
        $searchLabel = $inputSearch.siblings('.search-label'),
        loadMoreisClicked = false,
        selectedValarray = [],
        idx = 0,
        productData = '',
        pagesize = $searchedResultWrap.data('pagesize'),
        pageNum = 0,
        windowURL = location.href,
        urlGet = windowURL.match(/([^\/]*)\/*$/)[1].split('.'),
        filteredData = '',
        firstdropdown = false,
        searchedData = '',
        finalArraybeforesearch = '',
        resetFromfilter = false;

    function initialize() {
        getFilterData();
        showhideFilter();
        loadAjax();
        filterSearch();
        handleFilters();
        clearonCancel();
    }

    /**
     * * showhideFilter
     * show hide filter
     * */
    let showhideFilter = function() {
        $labelFinderWrap.find('.reset-filter').on('click', () => {
            selectedValarray = [];
            searchedData = '';
            idx = 0;
            pageNum = 0;
            filteredData = '';
            loadMoreisClicked = false;
            $sidebarForm[0].reset();
            $sidebarForm.find('.form-dropdown').trigger('change.select2');
            loadArticleSnippets(productData.productList);
            handleLoadMore(productData.productList);
            $searchLabel.show();
            $resultCount.html(productData.productList.length);

            let weedsId = '#weeds',
                pestsId = '#pests',
                diseaseId = '#disease';

            updateFilterDrpdn([weedsId,pestsId,diseaseId]);

            if($('#productTypeFilter').length){
                //updateFilterDrpdn(['#cortevaproductTypescropProtection']);

                let $childFilterObject = $('form.product-label-finder').find('fieldset')
                    .first().find('.childfilter select');

                updateFilterDrpdn($childFilterObject);
                $childFilterObject.val('').trigger('change.select2');
            }
            $('.c-button.loadmore-btn').attr("href", urlGet[0] + "." + + 1 + ".html");
        });

        $labelFinderWrap.find('.filters-show').on('click',function () {
            $inputSearch.val('');
            $searchLabel.show();

            $sidebarForm.toggleClass("active");
        });

        $labelFinderWrap.find('.filters-done').on('click',function (e) {
            $sidebarForm.removeClass("active");
            e.preventDefault();
            /*  When a mobile user presses the "Done" button on the filters modal, this will run  */
        });
    },

    /**
     * * handleLoadMore
     * handle load more data
     * */
    handleLoadMore = function(filteredData) {
        if (filteredData.length > pagesize) {
            $loadMoreBtn.show();
        } else {
            $loadMoreBtn.hide();
        }
    },

    /**
     * * getFilterData
     * get filtered product data
     * */
    getFilterData = function() {
        let getAPI = $searchedResultWrap.data('url');

        if (!productData) {
            //ajax call
            $.ajax({
                type: "GET",
                url: getAPI,
                dataType: 'json',
                success: function (data) {
                    productData = data;
                }
            });
        }
    },

    /**
     * * replaceUnicodeChars
     * replace unicode characters
     * */
    replaceUnicodeChars = function(str) {
        str = str.replace(/&lt;/g,'<').replace(/&gt;/g,'>').replace(/&amp;/g,'&');
        return str;
    },

    /**
     * * replaceUnicodeChars
     * templating the ajax data
     * */
    loadArticleSnippets = function(feedData) {
        let feedDataArr;

        if (feedData){
            feedDataArr = feedData.filter((item, index) => index < pagesize);
        }

        let template = listData => `
            ${listData.map((data, index) => `
                <div class="search-result-item">
                    <a class="title" title="${data.productAnalyticsName}" href="${data.productPath}" data-analytics-type="product-finder-link" data-analytics-value=${data.productAnalyticsName}>${replaceUnicodeChars(data.productName)}</a>
                    <p class="type">${data.productTypeTags ?` ${data.productTypeTags.join(' | ')}  `:` `} </p>
                    <p class="description">${replaceUnicodeChars(data.description)}</p>
                </div>
            `).join('')
        }`;

        if (feedDataArr){
            if (loadMoreisClicked) {
                $searchedResultWrap.append(template(feedDataArr));
            } else{
                $searchedResultWrap.html(template(feedDataArr));
                $resultCount.html(feedData.length);
            }
            $noResult.addClass('hide');
        } else {
            $resultCount.html(0);
            $searchedResultWrap.empty();
            $noResult.removeClass('hide');
            $loadMoreBtn.hide();
        }
    },

    /**
     * * handleEnteredData
     * handle entered data
     * */
    handleEnteredData = function(searchedVal) {
        if (searchedVal.length) {
            $searchLabel.hide();
        } else {
            $searchLabel.show();
        }

        if (selectedValarray.length === 0) {
            resetFromfilter = false;
        }
        if (searchedVal.length > 2) {
            filterSearchData(searchedVal);
            loadMoreisClicked = false;
        } else if (searchedVal.length === 0) {
            $sidebarForm.find('.form-dropdown:eq(0)').trigger('change');
        }
        /*else{
           filteredData = '';
        }*/
    },

    /**
     * * filterSearchData
     * filter search data
     * */

    filterSearchData = (keyword) => {
        let filterData = (filteredData) ? filteredData : productData.productList,
            searchedItem = [];

        filterData.filter((element) => {
            if (element.productAnalyticsName.toLowerCase().indexOf(keyword) > -1) {
                searchedItem.push(element);
            }
        });

        if (searchedItem.length > 0) {
            filteredData = searchedItem;
            searchedData = searchedItem;

            loadArticleSnippets(searchedItem);
            $noResult.addClass('hide');
            pageNum = 0;
        } else {
            //filteredData = '';
            $searchedResultWrap.empty();
            $noResult.removeClass('hide');
            $resultCount.html(0);
        }
        //handleLoadMore
        handleLoadMore(searchedItem);
        clearonCancel();
    },

    /**
     * * filterSearch
     * search on input type text
     * */
    filterSearch = function() {
        $inputSearch.on('keyup keypress', (e) => {
            if (e.type === 'keyup' || e.keyCode === 13) {
                handleEnteredData($(e.currentTarget).val().toLowerCase());
            }
            if(e.keyCode === 8){
                filteredData = '';
            }
        });
    },

    /**
     * * updateFilterDrpdn
     * hide filter dropdown
     * */
    updateFilterDrpdn = function(filters) {
        $(filters).each((index,elm) => {
            $(elm).hide().trigger('change.select2')
            .next('.select2-container').hide();
        });
    },

    /**
     * * updateFilterDrpdnShow
     * show filter dropdown
     * */
    updateFilterDrpdnShow = function(filters) {
        $(filters).each((idx, elm) => {
            if (/iP(od|hone)/i.test(window.navigator.userAgent) || /Android/i.test(window.navigator.userAgent)) {
                $(elm).show();
            }
            else{
                $(elm).trigger('change.select2')
                    .next('.select2-container').show();
            }
        });
        $('.c-button.loadmore-btn').attr("href", urlGet[0] + "." + + 1 + ".html");
    },

     /**
     * * handleFilters
     * common filter selction
     * */
    handleFilters = function() {
        let $filter = $sidebarForm.find('.form-dropdown'),
            $childFilter = $sidebarForm.find('.childfilter'),
            cropId = '#cortevaproductTypescropProtection',
            //$cropProtection = $childFilter.find(cropId),
            weedsId = '#weeds',
            pestsId = '#pests',
            diseaseId = '#disease',
            cropProtectionVal = 'cortevaproductTypescropProtection'.toLowerCase(),
            $currentVal = '',
            $newVal = '';

        $inputSearch.val('');

        let $childFilterObject = $('form.product-label-finder').find('fieldset')
                .first().find('.childfilter select');

        updateFilterDrpdn($childFilterObject);
        updateFilterDrpdn(cropId +', '+ weedsId +', '+ pestsId +', '+ diseaseId);

        $filter.on('change',function() {
            let $this = $(this),
                filterProductData;

            $newVal = $this.val();
            $currentVal = $newVal.toLowerCase();
            pageNum = 0;
            selectedValarray = [];

            if ($inputSearch.val() !== '') {
                $searchLabel.hide();
            } else {
                $searchLabel.show();
            }

        // for sequential type filter
        if($('#productTypeSubFilter').length){
            switch ($('#productTypeSubFilter').val().toLowerCase()) {
                case 'cortevaproducttypescropprotectionherbicide':
                    updateFilterDrpdnShow(weedsId);
                    $(pestsId).val('').trigger('change.select2');
                    updateFilterDrpdn(pestsId);
                    $(diseaseId).val('').trigger('change.select2');
                    updateFilterDrpdn(diseaseId);
                    break;
                case 'cortevaproducttypescropprotectioninsecticide':
                    updateFilterDrpdnShow(pestsId);
                    $(weedsId).val('').trigger('change.select2');
                    updateFilterDrpdn(weedsId);
                    $(diseaseId).val('').trigger('change.select2');
                    updateFilterDrpdn(diseaseId);
                    break;
                case 'cortevaproducttypescropprotectionfungicide':
                    updateFilterDrpdnShow(diseaseId);
                    $(weedsId).val('').trigger('change.select2');
                    updateFilterDrpdn(weedsId);
                    $(pestsId).val('').trigger('change.select2');
                    updateFilterDrpdn(pestsId);
                    break;
                case 'cortevaproducttypescropprotectionnematicide':
                    updateFilterDrpdnShow(pestsId);
                    $(weedsId).val('').trigger('change.select2');
                    updateFilterDrpdn(weedsId);
                    $(diseaseId).val('').trigger('change.select2');
                    updateFilterDrpdn(diseaseId);

                    break;
                default:
                    updateFilterDrpdn(weedsId);
                    $(weedsId).val('').trigger('change.select2');
                    updateFilterDrpdn(pestsId);
                    $(pestsId).val('').trigger('change.select2');
                    updateFilterDrpdn(diseaseId);
                    $(diseaseId).val('').trigger('change.select2');
            }
        }

        if($this.closest('fieldset').find('#productTypeFilter') && $this.attr('id') === 'productTypeFilter'){
           $this.siblings('.childfilter').find('select').val('').trigger('change.select2');
        }

            $filter.map(function(i, el) {
                if ($(el).val() !== '' && $(el).next('.select2-container').css('display')!=='none') {
                    selectedValarray.push($(el).val());
                }
            });

            loadMoreisClicked = false;
            resetFromfilter = true;


            if (productData) {
                if (selectedValarray.length > 0) {
                    sortDataonDropdown(selectedValarray);
                } else {
                    if ($inputSearch.length === 0 || $inputSearch.val() === '') {
                        filterProductData = productData.productList;
                        filteredData = filterProductData;
                    } else {
                        filteredData = '';
                        filterProductData = searchedData;
                    }
                    firstdropdown = false;
                    loadArticleSnippets(filterProductData);
                    handleLoadMore(filterProductData);
                }
            }

            // if ($newVal === 'cortevaproductTypescropProtection') {
            //     firstdropdown = ($currentVal === cropProtectionVal) ? true : false;
            // }
            if($this.closest('fieldset').find('#productTypeFilter').length){
                firstdropdown = true;
            }
            else{
                firstdropdown = false;
            }
            let $productTypeEl = $this.val() && $this.val() !=='' ? $childFilter.find('#'+$this.val()) : null;
            //filterMapping
            filterMapping(
                //{$newVal, $currentVal, selectedValarray, cropId, weedsId, pestsId, diseaseId, cropProtectionVal, $cropProtection}
                {$newVal, $currentVal, selectedValarray, cropId, weedsId, pestsId, diseaseId, cropProtectionVal, $productTypeEl},
                $this
            );
        });
    },

    /**
     * * filterMapping
     * first level filter show  hide
     * */
    filterMapping = function(options,$currentElement) {
        let $childFilter = $sidebarForm.find('.childfilter');

        if ($childFilter.find('#productTypeFilter').val() === '' && options.selectedValarray === '') {
            $childFilter.find('#cortevaproductTypescropProtection, #productTypeSubFilter, #stateFilter, #pestsFilter, #weedsFilter')
            .val('').trigger('change.select2');

            loadArticleSnippets(productData.productList);
        }

        switch (options.$newVal) {
            case 'cortevaproductTypescropProtectionfumigant':
            case 'cortevaproductTypescropProtectionnitrogenStabilizer':
                updateFilterDrpdn(options.weedsId +', '+ options.pestsId +', '+ options.diseaseId);
                updateFilterDrpdn();
                break;
            case 'cortevaproductTypesurbanPestManagement':
            case 'cortevaproductTypesservices':
            case 'cortevaproductTypesoils':
                updateFilterDrpdn(options.cropId);
                break;
            default:
            if ($currentElement.closest('fieldset').find('#producTypeSubfilter').length) {
                updateFilterDrpdn(options.cropId+ ', '+ options.weedsId +', '+ options.pestsId +', '+ options.diseaseId);
            }
        }

        //show hide filter based on selection Product Type filter
        if($currentElement.closest('fieldset').find('#productTypeFilter').length){
           producTypefilter(options.$newVal, options.cropProtectionVal, options.$productTypeEl);
        }
        //producTypefilter(options.$newVal, options.cropProtectionVal, options.$productTypeEl);

        //Product Sub-Type filte pests/Weeds/Diseases sequential filter
        producTypeSubfilter(options.$newVal, 'cortevaproductTypescropProtectionherbicide', options.weedsId);
        producTypeSubfilter(options.$newVal, 'cortevaproductTypescropProtectionnematicide', options.pestsId);
        producTypeSubfilter(options.$newVal, 'cortevaproductTypescropProtectionfungicide', options.diseaseId);
        producTypeSubfilter(options.$newVal, 'cortevaproductTypescropProtectioninsecticide', options.pestsId);
    },

     /**
     * * producTypefilter
     * first level filter show  hide
     * */
    producTypefilter = function(selectedVal, val, $domelement){
        // if (selectedVal === val || selectedVal === val.toLowerCase()) {
        //     updateFilterDrpdnShow([domelement]);
        // } else{
        //     updateFilterDrpdn([domelement]);
        // }
        //$productTypeEl.closest('fieldset').find('.childfilter select')

        //domelement.parent();
        //updateFilterDrpdnShow([domelement]);

            let $childFilterObject = $('form.product-label-finder').find('fieldset')
                .first().find('.childfilter select');

            updateFilterDrpdn($childFilterObject);
            $childFilterObject.val('').trigger('change.select2');
        if($domelement !== null){

            //check for cortevaproducttypescropprotection to show only 1 child
            if(selectedVal.toLowerCase() === 'cortevaproducttypescropprotection'){
                updateFilterDrpdnShow([$domelement]);
            }
            //updateFilterDrpdnShow([$domelement]);
        }

        $sidebarForm.find('.form-dropdown').trigger('change.select2');
    },

    /**
     * * producTypeSubfilter
     * second level filter show hide
     * */
    producTypeSubfilter = function(selectedVal, val, domelement) {
        let weedsId = '#weeds',
            pestsId = '#pests',
            diseaseId = '#disease';

        if (selectedVal.toLowerCase() === val.toLowerCase()) {
            switch ($(domelement).attr('id')) {
                case 'weeds':
                    updateFilterDrpdnShow(weedsId);
                    updateFilterDrpdn(pestsId, diseaseId);
                break;
                case 'pests':
                    updateFilterDrpdnShow(pestsId);
                    updateFilterDrpdn(weedsId, diseaseId);
                break;
                case 'disease':
                    updateFilterDrpdnShow(diseaseId);
                    updateFilterDrpdn(weedsId, pestsId);
                break;
            }
            updateFilterDrpdnShow(domelement)
        } else {
            updateFilterDrpdn(domelement);
        }

        if (firstdropdown === true) {
            //updateFilterDrpdnShow('#cortevaproductTypescropProtection, #cortevaproductTypescropProtection_chosen,#cropFilter');
            let isChildFilterLength = $('form.product-label-finder')
                .find('select option[value="'+selectedVal+'"]')
                .closest('.childfilter').length,
                selectedValCheck = selectedVal.toLowerCase();

            //check for cortevaproducttypescropprotection to show only 1 child
            if(isChildFilterLength && selectedValCheck.indexOf('cortevaproducttypescropprotection') > -1){
                updateFilterDrpdnShow(['#'+$('#productTypeFilter').val()]);
            }
            //updateFilterDrpdnShow(['#'+$('#productTypeFilter').val()]);

            if(isChildFilterLength){
                $('#'+$('#productTypeFilter').val()).val(selectedVal).trigger('change.select2');
            }
        }
        // for sequential type filter
        if($('#productTypeSubFilter').length){
            switch ($('#productTypeSubFilter').val().toLowerCase()) {
                case 'cortevaproducttypescropprotectionherbicide':
                    updateFilterDrpdnShow(weedsId);
                    $(pestsId).val('').trigger('change.select2');
                    updateFilterDrpdn(pestsId);
                    $(diseaseId).val('').trigger('change.select2')
                    updateFilterDrpdn(diseaseId);
                    break;
                case 'cortevaproducttypescropprotectioninsecticide':
                    updateFilterDrpdnShow(pestsId);
                    $(weedsId).val('').trigger('change.select2');
                    updateFilterDrpdn(weedsId);
                    $(diseaseId).val('').trigger('change.select2');
                    updateFilterDrpdn(diseaseId);
                    break;
                case 'cortevaproducttypescropprotectionfungicide':
                    updateFilterDrpdnShow(diseaseId);
                    $(weedsId).val('').trigger('change.select2');
                    updateFilterDrpdn(weedsId);
                    $(pestsId).val('').trigger('change.select2');
                    updateFilterDrpdn(pestsId);
                    break;
                case 'cortevaproducttypescropprotectionnematicide':
                    updateFilterDrpdnShow(pestsId);
                    $(weedsId).val('').trigger('change.select2');
                    updateFilterDrpdn(weedsId);
                    $(diseaseId).val('').trigger('change.select2');
                    updateFilterDrpdn(diseaseId);

                    break;
                default:
                    updateFilterDrpdn(weedsId);
                    $(weedsId).val('').trigger('change.select2');
                    updateFilterDrpdn(pestsId);
                    $(pestsId).val('').trigger('change.select2');
                    updateFilterDrpdn(diseaseId);
                    $(diseaseId).val('').trigger('change.select2');
            }
        }
    },

    /**
     * * clearonCancel
     * input search reset on click inside search
     * */
    clearonCancel = function() {
        $inputSearch.on('search', function () {
            $searchLabel.show();
            if (resetFromfilter === true) {
                loadArticleSnippets(finalArraybeforesearch);
                handleLoadMore(finalArraybeforesearch);
                filteredData = finalArraybeforesearch;
            }else{
                filteredData = '';
                loadArticleSnippets(productData.productList);
                handleLoadMore(productData.productList);
            }
        });
    },

    /**
     * * sortDataonDropdown
     * filtering data on selected value
     * */
    sortDataonDropdown = function(chosenValue) {
        let valTextfield = ($inputSearch.length === 0 || $inputSearch.val() === '') ? true :  false,
            modifiedData = searchedData,
            filteredVal =  valTextfield ? productData.productList : modifiedData,
            productArr = [],
            flag = true,
            finalArr = [],
            filterArray;

        filterArray = function (filteredVal, filterKey) {
            filteredVal.filter((element) => {
                return filterKey.filter(function(e, index) {
                    if (element.allTags.indexOf(e) > -1) {
                        if (index === 0) {
                            productArr.push(element);
                        }
                    }
                });
            });

            if (productArr.length === 0) {
                idx = chosenValue.length;
                finalArr = [];
            }

            if (productArr.length && flag && chosenValue.length > idx) {
                let refacturedArr = productArr;
                productArr = [];
                finalArr = [];
                idx++;

                if (chosenValue.length === idx) {
                    finalArr.push(refacturedArr);
                } else {
                    finalArr.push(filterArray(refacturedArr, [chosenValue[idx]]));
                }
            }
        }

        filterArray(filteredVal, chosenValue);

        if (chosenValue.length === idx) {
            loadArticleSnippets(finalArr[0]);
            filteredData = finalArr[0];
            if(valTextfield) {
                finalArraybeforesearch = finalArr[0];
            }
            if (finalArr.length > 0) {
                if (finalArr[0].length > pagesize) {
                    $loadMoreBtn.show();
                } else {
                    $loadMoreBtn.hide();
                }
            }
            idx = 0;
            flag = false;
        }
        filterSearch();
    },

    /**
     * * paginate
     * loading data based on check
     * */
    paginate = function(array, pageSize, pageNumber) {
        return array.slice(pageNumber * pageSize, (pageNumber + 1) * pageSize);
    },

    /**
     * * loadAjax
     * paginating data
     * */
    loadAjax = function() {
        let urlGetValue = parseInt(urlGet[1], 10);
            pageNum = (urlGet.length > 2) ? urlGetValue : 0;
        $loadMoreBtn.on('click',function(e) {
            e.preventDefault();
            let filterData = (filteredData) ? filteredData : productData.productList,
                loadmoreData = paginate(filterData, pagesize, pageNum = pageNum + 1);
            if (filterData.length - (pageNum*10) <= pagesize) {
                $loadMoreBtn.hide();
            }

            loadMoreisClicked = true;
            loadArticleSnippets(loadmoreData);
            filterSearch();
            $(this).attr("href", urlGet[0] + "." + + (pageNum + 1) + ".html");
        });
    }

    return initialize();
});
