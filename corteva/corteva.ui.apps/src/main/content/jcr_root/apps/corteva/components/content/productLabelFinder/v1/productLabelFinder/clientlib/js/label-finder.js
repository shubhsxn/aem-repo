//Label Finder
const labelFinder = function () {
    if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('productLabelFinder')) {
        return;
    }

    let $labelFinderWrap = $('.con21-label-finder'),
        $labelFinderForm = $('form.label-finder'),
        $labelFinderInput = $labelFinderForm.find('.label-finder-search'),
        $selectDrpdn = $labelFinderWrap.find('.label-finder-products'),
        hoverCl = 'is-hover',
        activeCl = 'is-active',
        searchBtn = 'button.submit-search-product',
        $currentCountry = $('html').attr('lang'),
        $stateDrpdn = $labelFinderWrap.find('.form-dropdown');

    /**
     * Initialization function
     */
    function initialize() {
        if ($labelFinderWrap.length) {
            eventBindings();
            labelSearchHandler();
            setDataOnPageLoad();

            $labelFinderForm.on('submit', (e)=> {
                e.preventDefault();
            });
        }
    }

    /**
     * eventBindings function
     */
    let eventBindings = function() {
        let $selectProductForm = $labelFinderWrap.find('.select-product-form'),
            $searchProductForm = $labelFinderWrap.find('.search-product-form'),
            hideCl = 'hide';

        $labelFinderWrap.find('.select-product').on('click', (e) => {
            e.preventDefault();

            populateProductData();
            $selectProductForm.removeClass(hideCl);
            $searchProductForm.addClass(hideCl);
            $('.no-result').remove();
        });
        $labelFinderWrap.find('.search-product').on('click', (e) => {
            e.preventDefault();
            let drpdwnVal = ($selectDrpdn.val() !== '') ? $selectDrpdn.find('option:selected').text() : '';

            $selectProductForm.addClass(hideCl);
            $searchProductForm.removeClass(hideCl);
            $labelFinderInput
            .typeahead('val', drpdwnVal)
            .next('pre').text(drpdwnVal).val(drpdwnVal);
        });
    },

    /**
     * getQueryParam
     */
    getQueryParam = function(name, url = window.location.href) {
        url = url.toLowerCase();
        name = name.replace(/[\[\]]/g, '\\$&');

        let regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
            results = regex.exec(url);

        if (!results) {
            return null;
        }
        if (!results[2]) {
            return '';
        }
        return decodeURIComponent(results[2].replace(/\+/g, ' '));
    },

    /**
     * * getSearchSelector
     * get search selector
     * */
    getFieldDetails = function() {
        let productId,
            productSource,
            pageUrl,
            productName,
            data = {};
        if ($labelFinderWrap.find('.select-product-form').is(':visible')) {
            let $selectedOption = $selectDrpdn.find('option:selected');
            productId = $selectDrpdn.val();
            productSource = $selectedOption.attr('data-product-source');
            productName = $selectedOption.text();
        } else {
            productId = $labelFinderInput.attr('data-product-id');
            productSource = $labelFinderInput.attr('data-product-source');
            productName = $labelFinderInput.val();
        }
        pageUrl = (productSource === 'nonagrian') ? '?currentPage=' + window.location.href : '';

        data = {
            productId : productId,
            productSource: productSource,
            productName: productName,
            pageUrl: pageUrl
        }
        return data;
    },

    /**
     * * searchResultTemplate
     * result listing template literals
     * */
    setDataOnPageLoad = function() {
        let finderData = $labelFinderForm.data('autocomplete-data'),
            filteredProduct = finderData.filter(item => item.productId === getQueryParam('pid')),
            searchKeyword = (filteredProduct.length > 0) ? decodeString(filteredProduct[0].productName) : '';

        if (searchKeyword && searchKeyword !== '') {
            $labelFinderInput.attr('data-product-id', getQueryParam('pid'))
                .attr('data-product-source', filteredProduct[0].productSource);

            $labelFinderInput.typeahead('val', searchKeyword)
                .next('pre').text(searchKeyword).val(searchKeyword).blur();
            $labelFinderInput.parents($labelFinderForm).addClass('is-active');

            //getProductData
            getProductData();

            if (getQueryParam('pdpsrc') === 'agrian') {
                $labelFinderWrap.find('.con21-label-finder-info').removeClass('hide');
            }
            $($labelFinderWrap).find('.band-content.results').removeClass('hide');
        }
    },

    /**
     * eventBindings function
     */

    populateProductData = function() {
        let $productDropdown = $labelFinderWrap.find('.label-finder-products'),
            productOptions = '';

        if ($productDropdown.find('option').length === 1 && $productDropdown.val() === '') {
            let productData = $labelFinderForm.data('autocomplete-data');

            productData.map((data) => {
                productOptions += `<option value = "${data.productId}" data-product-source="${data.productSource}">${data.productName}</option>`;
            });

            $productDropdown.append(productOptions);
            $productDropdown.select2({
                //disabled: true,
                width: '100%',
                minimumResultsForSearch: Infinity
            });

            $productDropdown.addClass('form-dropdown');
        }
        $productDropdown.val('').trigger('change.select2');
    },

    /**
     * * keyboardHandler
     * handle search on keyboard events
     * */
    labelSearchHandler = function() {
        //submitHandler
        submitHandler();

        //searchInputkeyup
        searchInputkeyup();

        //searchAutocomplete
        searchAutocomplete();

        //resetSearch
        resetSearch();

        //change event
        $labelFinderForm.find('input[type=search], select').on('change', (e) => {
            let $this = $(e.currentTarget), selectedProductLabelFinder = '';
            if(($this.closest('.label-finder').find('.label-finder-search').val().length > 0 || $('.label-finder').find('.label-finder-products option:selected').val().length > 0) && $labelFinderWrap.find('.no-result').length === 0) {
                $this.parents('.label-finder').find(searchBtn).removeAttr('disabled');

                if($this.hasClass('label-finder-products')){
                    selectedProductLabelFinder = $this.find('option:selected').html();
                }
                else{
                    selectedProductLabelFinder = $this.val();
                }

                $this.parents('.label-finder').find(searchBtn).attr('data-analytics-value',selectedProductLabelFinder);
            } else {
                $this.parents('.label-finder').find(searchBtn).attr('disabled', true);
            }
        });
    },

    /**
     * * submitHandler
     * submit to search page
     * */
    submitHandler = function() {
        $(searchBtn).on('click', (e) => {
            e.preventDefault();
            let $elm = $(e.currentTarget).parent().find($labelFinderInput),
                fieldVal = getFieldDetails().productName;

            if (fieldVal && fieldVal.length >= $labelFinderForm.data('minchars')) {
                $elm.typeahead('close');
                getProductData();
                $elm.blur();
            } else {
                $elm.focus();
            }
            $(e.currentTarget)
            .attr('disabled', true)
            .closest($labelFinderWrap)
            .find('.band-content.results')
            .attr('data-analytics-value', fieldVal)
            .attr('data-analytics-region', $stateDrpdn.find('option:selected').text());
        });
    },

    /**
     * * searchInputkeyup
     * search Input keyup handling
     * */
    searchInputkeyup = function() {
        $labelFinderInput.on('keyup', (e) => {
            let $this = $(e.currentTarget),
                fieldVal = $this.val();

            if ((e.keyCode || e.charCode) === 13) { //Capture Enter Key
                if (fieldVal.length > 2 || fieldVal.match(/^\s*$/)) {
                    $this.focus();
                } else {
                    if (!$this.parents('.search').is('.is-active') && $('.c-button.submit-search-product').attr('disabled') === "true") {
                        $this.typeahead('close').blur();
                        resetFilters();
                        getProductData();
                    }
                }
            }
            if ((e.keyCode || e.charCode) === 27) { //Capture Escape Key
                $labelFinderForm.trigger('reset');
                $labelFinderInput.blur();
                return;
            }
            $this.parents('.label-finder').find(searchBtn).attr('disabled', true);
        });
    },
    /**
    * decode String to html
    * */
    decodeString = function(str) {
        let parser = new DOMParser,
            dom = parser.parseFromString(
            '<!doctype html><body>' + str,
            'text/html'),
            decodedString = dom.body.textContent;

        return decodedString;
    },

    /**
     * * searchAutocomplete
     * autocomplete
     * */
    searchAutocomplete = function() {
        let $fieldset,
            searchQuery,
            disableBlur,
            ua = navigator.userAgent.toLowerCase(),
            isMobile = ua.indexOf("mobile") > -1,
            labelFinderData = JSON.parse("[" + $labelFinderForm.attr('data-autocomplete-data') + "]"),
            searchText = $labelFinderInput.data('searchtext'),
            suggestionTextLabel = $labelFinderInput.data('suggestiontext');

        $labelFinderInput.typeahead({
                hint: false,
                highlight: true,
                minLength: $labelFinderForm.data('minchars')
            }, {
                name: 'keywordSearch',
                limit: $labelFinderForm.data('suggestions-limit'),
                display: result => decodeString(result.productName),
                source: (query, sync, async) => {
                    searchQuery = query;
                    let result = labelFinderData[0].filter((product) => {
                        if(product.productName){
                            return decodeString(product.productName).toLowerCase().indexOf(decodeString(query).toLowerCase()) > -1;
                        }
                    });
                    sync(result);
                    async('');
                },
                templates: {
                    suggestion: (data) => {
                        return `<div data-product-id="${data.productId}" data-product-name="${data.productName}" data-product-source="${data.productSource}">
                            ${data.productName}
                        </div>`;
                    },
                    notFound: (data) => {
                        return `<div class="no-result"><p><strong>${searchText} "${data.query}"</strong></p><p>${suggestionTextLabel}<p></div>`
                    }
                }
            })
            .on('blur', (event) => {
                setTimeout(() => {
                    if ($fieldset && !disableBlur) {
                        if ($(event.currentTarget).val() !== '') {
                            $fieldset.addClass(activeCl);
                        }
                        $fieldset.removeClass(hoverCl);
                        //$fieldset.siblings(searchBtn).attr('disabled', true);
                    }
                }, 200);
            })
            .on('focus', (event, selected) => {
                let $this = $(event.currentTarget);

                $fieldset = $this.parents($labelFinderForm);
                $fieldset.addClass(hoverCl).removeClass(activeCl);

                if (isMobile) {
                    if ($this.val() !== '') {
                        $fieldset.addClass('is-text');
                    } else {
                        $fieldset.removeClass('is-text');
                    }
                    disableBlur = true;
                    setTimeout(() => {
                        disableBlur = false;
                    }, 500);
                }
                $this.parents('.label-finder').find(searchBtn).attr('disabled', true);
                return false;
            })
            .on('typeahead:cursorchanged .typeahead', (e) => {
                $(e.currentTarget).val(searchQuery);
            })
            .on('typeahead:selected', (event, selected, name) => {
                $(event.currentTarget).attr('data-product-id', selected.productId)
                .attr('data-product-source', selected.productSource);
                $('.label-finder').find(searchBtn).removeAttr('disabled');
            });
    },

    /**
     * * resetSearch
     * reset serach on reset button click
     * */
    resetSearch = function() {
        $labelFinderForm.on('reset', (e) => {
            e.preventDefault();
            let $this = $(e.currentTarget);

            setTimeout(() => {
                $this.find('select').val('');
                $this.find($labelFinderInput).val('')
                    .typeahead('val', '')
                    .next('pre').text('')
                    .typeahead('close');
                $this.removeClass(activeCl);
                $labelFinderForm.find(searchBtn).attr('disabled', true);
            }, 50);
        });
    },

    /** resetFilters
     * reset filters on search query change
     * */
    resetFilters = function() {
        $stateDrpdn.map((key, item) => {
            let $item = $(item);
            $item.find('option:eq(0)').prop('selected', true);
            //$item.trigger('change.select2');
        });
    },

    /** getQueryParam
     * query parameters for product api call
     * */
    productQueryParam = function() {
       let queryParams = `.${getFieldDetails().productId}.${getFieldDetails().productSource}.${$currentCountry}.json${getFieldDetails().pageUrl}`;

        return queryParams;
    },

    /**
     * * getProductData
     * search product data
     * */
    getProductData = function() {
        let servletUrl = $labelFinderForm.data('product-api') + productQueryParam(),
        $noResult = $('.con21-label-finder-noresults'),
        $result = $labelFinderWrap.find('.results');

        $.ajax({
            url: servletUrl,
            type: 'GET',
            dataType: 'json',
            success: function (responseData) {
                if (responseData && responseData.agrian_product && responseData.agrian_product !== null) {
                    mapDocsData(responseData, getFieldDetails().productSource);

                    $result.removeClass('hide');
                    $noResult.addClass('hide');
                    $('.label-finder').find(searchBtn).removeAttr('disabled');
                } else {
                    handleErrorState($result, $noResult);
                    $('.label-finder').find(searchBtn).attr('disabled', true);
                }
            },
            error:function(xhr) {
                handleErrorState($result, $noResult);
            }
        });
    },

    /**
     * * handleErrorState
     * search product data error state
     * */
    handleErrorState = function($result, $noResult) {
        $result.addClass('hide');
        getPlaceholderVal($noResult, '#placeholderProductLabel', getFieldDetails().productName);
        $noResult.removeClass('hide');
    },

    /**
     * * filteredData
     * filter label/supplementary data
     * */
    filteredData = function(response, docType) {
        let data = response.agrian_product.documents.document,
            filterdData = data.filter(item => item.type.toLowerCase() === docType.toLowerCase());

        return filterdData;
    },

    /**
     * * mapDocsData
     * Map product supplementary data
     * */
    mapDocsData = function(response, source) {
        let $stateDrpdn = $('.select-state .form-dropdown'),
            $agrianLinkElm = $labelFinderWrap.find('ul.primary-labels li .agrian');

        if (source === 'agrian') {
            renderAgrianData(response, $stateDrpdn);
            $labelFinderWrap.find('ul.primary-labels li .nonagrian').html('');
            $agrianLinkElm.fadeIn(500);
        } else {
            templateNonAgrian(response);
            $agrianLinkElm.fadeOut(500);
        }

        //unlicensedStateMsg
        unlicensedStateMsg(response, $stateDrpdn);
    },

    /**
     * * renderAgrianData
     * render product agrian data
     * */
    renderAgrianData = function(response, $stateDrpdn) {
        let $labelElm = $labelFinderWrap.find('ul.primary-labels li'),
            $suppElm = $labelFinderWrap.find('.con21-label-finder-info .item'),
            agrianPdfpath = $('.band-content.results').data('agrianpdfpath'),
            currentProductName = getFieldDetails().productName,
            currentProductNameEsc = currentProductName.replace(/®/g, '<sup>®</sup>').replace(/†/g, '<sup>†</sup>');

        $labelElm.map((idx, elm) => {
            let $this = $(elm),
                specimenLabel;

            $this.children('a.agrian').attr('href', agrianPdfpath+filteredData(response, $this.attr('data-type'))[0].filename);

            if ($this.attr('data-type') === "Label") {
                specimenLabel = $this.closest('.con21-label-finder-content').data('specimenlabel');
            }
            else if ($this.attr('data-type') === "MSDS") {
                specimenLabel = $this.closest('.con21-label-finder-content').data('safetylabel');
            }
            $this.children('a.agrian').html(`${currentProductNameEsc} ${specimenLabel}`)
            .attr('data-analytics-value',`${currentProductName} ${specimenLabel}`);
        });

        if ($stateDrpdn.val() !== '' && !response.agrian_product.licensedState[$stateDrpdn.val()]) {
            $labelFinderWrap.find('.con21-label-finder-info').fadeOut(500).addClass('hide');
        } else {
            $suppElm.map((idx, elm) => {
                let $this = $(elm),
                    filterByStates = filteredData(response, $this.attr('data-type'))
                    .filter(item => item.states.indexOf($stateDrpdn.val()) > -1);
                if (filterByStates.length) {
                    $this.find('.item-content')
                        .html(templateSuppData(filterByStates, $stateDrpdn.val())).parent().fadeIn(500);
                    $this.closest('.det01-accordion-list').find('h2').show();
                } else {
                    $this.find('.item-content').html('').parent().fadeOut(500);
                    $this.closest('.det01-accordion-list').find('h2').hide();
                }
            });
            if($('.item-content').find('.label-content').length) {
                $suppElm.closest('.det01-accordion-list').find('h2').show();
            }
            $labelFinderWrap.find('.con21-label-finder-info').fadeIn(500).removeClass('hide');
        }

    },

    /**
     * * templateSuppData
     * search product data
     * */
    templateSuppData = function(productData, state) {
        let agrianPdfpath = $('.band-content.results').data('agrianpdfpath');
        let template = resultsList => `
            ${resultsList.map((data, key) => `
            <div class="label-content">
                <a href="${agrianPdfpath}${data.filename}" title="${data.description}" target="_blank">
                    ${data.description}
                </a>
                ${data.states && data.states !== null ? `
                    <p>${data.states}</p>
                    ` : `
                    `} 
            </div>
        `).join('')}
    `;
        return template(productData);
    },

    /**
     * * templateSuppData
     * search product data
     * */
    templateNonAgrian = function(productData) {
        $labelFinderWrap.find('.con21-label-finder-info').addClass('hide');
        $('.primary-labels').find('h3').hide();

        let template = resultsList => `
            ${resultsList.map((data, key) => `
              <a href="${data.filename}" class="download" data-analytics-type="product-label" data-analytics-value="${data.description}" target="_blank">
                ${data.description}
              </a>
              ${data.displayDate !== null ? `
                <p>${data.displayDate}</p>
                ` : `
                `}
            `).join('')}
        `;
        $('.primary-labels-links.nonagrian').html('');
        $('.primary-labels-links').map((idx, elm) => {
            let $this = $(elm),
                filterByType = filteredData(productData, $this.data('type'));

            if (filterByType.length) {
                $this.html(template(filterByType));
                $this.parent().find('h3').show();
            }
        });
    },

    /**
     * * unlicensedStateMsg
     * unlicensed State notification Message
     * */

    unlicensedStateMsg = function(response, $state) {
        let errDiv = '.con21-label-finder-error';

        if ($state.length > 0 && $state.val() !== '' && !response.agrian_product.licensedState[$state.val()]) {
            getPlaceholderVal(errDiv, '#placeholderProductLabel', getFieldDetails().productName);
            getPlaceholderVal(errDiv, '#placeholderState', $state.find('option:selected').text());
            $labelFinderWrap.find(errDiv).removeClass('hide');
        } else {
            $labelFinderWrap.find(errDiv).addClass('hide');
        }
    },

    /**
     * * replacePlaceholderWithValues
     * replaceholder values with dynamic values
     * */
    getPlaceholderVal = function(item, placeholder, val) {
        let $item = $(item),
            updatedStr,
            placeholderClass = placeholder.replace('#', '').toLowerCase();

        val = '<span class="' + placeholderClass + '">' + val + '</span>';

        $item.map((key, value) => {
            let $this = $(value),
                str = value.innerHTML;

            if ($this.find('.' + placeholderClass).length === 0 && str.indexOf(placeholder) !== -1) {
                updatedStr = str.replace(placeholder, val);
                $this.html(updatedStr);
            } else {
                $this.find('.' + placeholderClass).html(val);
            }
        });
    }

    return initialize();
};


$(window).load(function() {
    labelFinder();
});