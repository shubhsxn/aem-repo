/** searchModule */
const searchModule = function() {
    let $searchWrapper = $('.con07-container-search'),
        $searchFilter = $searchWrapper.find('.search-filters'),
        $searchFilterDrpdn = $searchFilter.find('select.form-dropdown'),
        $searchResultsWrap = $searchWrapper.find('.search-results-wrap'),
        $searchInput = $searchFilter.find('.search-field'),
        searchKeyword,
        $loader = $searchWrapper.find('.loader'),
        $paginationWrap = $searchWrapper.find('.c-con06-pagination__wrapper'),
        $paginationList = $paginationWrap.find('.pagination'),
        $noResults = $searchWrapper.find('.no-search-results');

    /**
     * Initialization function
    */
    function initialize() {
        if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('containerSearch')){
            return;
        }
        if ($searchWrapper.length) {
            //set keyword in search input if qyery exists
            setKeywordValOnLoad();

            // search event handlers
            searchResultHandler();

            inputSearchHandler();

            // show suggestions
                searchAutocomplete();

            //StartsWith polyfll for IE
            /* eslint-disable */
            if (!String.prototype.startsWith) {
                String.prototype.startsWith = function(search, pos) {
                    return this.substr(!pos || pos < 0 ? 0 : +pos, search.length) === search;
                };
            }
            /* eslint-enable */
        }
    }

    /**
     * * searchResultHandler
     * get data from search handler and render the template
     * */
    let searchResultHandler = function(param, callback) {
      let queryParams = (param) ? param : (searchQueryParams() ? '?'+searchQueryParams()  : ''),
            servletUrl = $searchFilter.attr('action') + queryParams;

        $searchResultsWrap.fadeOut(400);
        $loader.fadeOut(500);
        $noResults.fadeOut(500).addClass('hide');

        if ($searchInput.val() !=='' && servletUrl) {
            $loader.fadeIn(500);

            fetch(servletUrl)
              .then(response => {
                return response.json();
              })
              .then(responseData => {
                if (responseData) {
                    const suggestions = responseData.searchSuggestions.suggestions;
                    $loader.fadeOut(500);
                    //update result count
                    $searchWrapper.find('.results-count').text(responseData.resultcount.total);

                    if (typeof digitalData !== 'undefined') {
                        digitalData.searchNum = responseData.resultcount.total;
                    }

                    if (responseData.results.length) {
                        setTimeout(() => {
                            $searchResultsWrap.html(searchResultTemplate(responseData)).fadeIn(500);
                            //pagination
                            paginationTemplate(responseData);
                            callback && callback();
                        }, 500);
                    } else {
                        $paginationWrap.addClass('hide');
                        if (suggestions.length) {
                          replacePlaceholderWithValues($noResults.find('p'), '#placeholderForDidYouMean', suggestions[0].value, true);
                            $noResults.find('.js_didyoumean').parent().show();
                        } else {
                            $noResults.find('p:eq(0)').hide();
                        }
                        $noResults.fadeIn(800).removeClass('hide');
                        replacePlaceholderWithValues($noResults.find('h2'), '#placeholderForKeyword', responseData.general.query);
                    }
                }
            })
            .catch(error => {
                if (error) {
                  console.log('There is an error with service: ' + error);
                }
            });
        }
    },

    /**
     * * searchQueryParams
     * prepare query for search
     * */

    searchQueryParams = function() {
        let searchVal = $searchInput.val(),
            filterVal = $searchFilter.find('[name="filter"] option:selected').attr('value'),
            sortVal = $searchFilter.find('[name="sort"] option:selected').attr('value'),
            q = (searchVal !== '') ? 'q=' + encodeURIComponent(searchVal) : '',
            filter = (filterVal) ? '&' + filterVal : '',
            sort = (sortVal) ? '&' + sortVal : '',
            queryParam = q + filter + sort,
            countryVal = $('html').attr('data-country'),
            country = (countryVal) ? '&country=' + countryVal : '';

            if(countryVal){
                queryParam += country;
            }

        if (typeof digitalData !== 'undefined') {
            digitalData.searchTerm = searchVal;
        }

        return queryParam;
    },

    /**
     * * decodeString
     * Decode string
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
     * * searchResultTemplate
     * result listing template literals
     * */
    searchResultTemplate = function(responseData) {
        let searchResults = responseData.results,
            pagelower = parseInt(responseData.resultcount.pagelower),
            pageupper = parseInt(responseData.resultcount.pageupper),
            pagination = responseData.pagination,
            pageNum;

        if (pagelower === pageupper) {
            pageNum = pagination.pages[pagination.pages.length - 1].page;
        } else {
            pageNum = Math.ceil(pageupper/pagination.pages.length);
        }

        let template = resultsList => `
            ${resultsList.map((data, key) => `
                <div class="card det13-card-search-result">
                    <span class="eyebrow">${data.contentType}</span>
                    <a href="${data.url}" data-analytics-type="cta-search-results" data-analytics-value="${data.contentType}" data-analytics-position="${pageNum}:${key+1}" class="title ${data.external.startsWith(`true`) ? `external` : ``}"
                    ${data.external.startsWith(`true`) ? `target="_blank" rel="noopener noreferrer"` : ``}>
                        ${decodeString(data.title)}
                    </a>
                    <p>${decodeString(data.desc)}</p>
                </div>
            `).join('')}
        `;
        return template(searchResults);
    },

    /**
     * * paginationTemplate
     * pagination template literals
     * */
    paginationTemplate = function(searchResponse) {
        let template = results => `
            <li>
                ${results.pagination.previous ? `<a class="previous page" href="${results.pagination.previous}" title="">` : `<span class="previous page">`}
                <i class="material-icons">trending_flat</i>
                ${results.pagination.previous ? `</a>` : `</span>`}
            </li>
            <li>
                 <strong>${results.resultcount.pagelower}-${results.resultcount.pageupper}</strong> ${$('.c-con06-pagination__wrapper').data('paginationseparator')} <span class="results-count">${results.resultcount.total}</span>
            </li>
            <li>
                ${results.pagination.next ? `<a class="next page" href="${results.pagination.next}" title="">` : `<span class="next page">`}
                <i class="material-icons">trending_flat</i>
                ${results.pagination.next ? `</a>` : `</span>`}
            </li>
        `;

        $paginationWrap.removeClass('hide');
        $paginationList.html(template(searchResponse));
    },

    /**
     * * Get query param
     * create/get/delete Cookie
     * */
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
     * * searchResultTemplate
     * result listing template literals
     * */
    setKeywordValOnLoad = function() {
        searchKeyword = getQueryParam('q');
        if (searchKeyword && searchKeyword !== '') {
            $searchInput.val(searchKeyword).blur();
            $searchInput.parents('.search').addClass('is-active');

            if (typeof digitalData !== 'undefined') {
                digitalData.searchTerm = searchKeyword;
            }
        }
    },

    /**
     * * inputSearchHandler
     * handle input search submit/click handler
    * */
    inputSearchHandler = function() {
        //search button handler
        submitHandler();
        //didyoumean link handling
        didYouMeanSearch();
        // search next page results on pagination next/previous link click
        navigateToPage();
        //search keyword handler
        keyboardHandler();
        // reset fields on reset button click
        resetSearch();
        //filterSearch
        filterSearch();
    },

    /**
     * * submitHandler
     * trigger search on search button click
    * */
    submitHandler = function() {
        $('.submit--search').on('click', (e) => {
            e.preventDefault();
            let $this = $(e.currentTarget),
                $fieldVal = $this.parent().find('.search-field').val();

            if ($fieldVal.length) {
                resetFilters();
                searchResultHandler();
                $this.blur();
              } else {
                $this.focus();
            }
        });
    },

    /**
     * * didYouMeanSearch
     * trigger search on didyoumean link click
    * */
    didYouMeanSearch = function() {
        // did you mean link search
        $noResults.on('click', '.js_didyoumean', (e) => {
            e.preventDefault();
            let $fieldVal = $(e.currentTarget).attr('href');

            if ($fieldVal.length) {
                $searchInput.val($fieldVal);
                searchResultHandler();
            }
        });
    },

    /**
     * * navigateToPage
     * navigate to next set of page
    * */
    navigateToPage = function() {
        // pagination handler
        $paginationList.on('click', 'a.page', (e) => {
            e.preventDefault();

            searchResultHandler($(e.currentTarget).attr('href'), function() {
                $('html, body').animate({
                    scrollTop: $searchWrapper.offset().top - 100
                }, 1000);
            });
        });
    },

    /**
     * * filterSearch
     * trigger search on filter dropdown change
    * */
    filterSearch = function() {
        // pagination handler
        $searchFilterDrpdn.on('change', (e) => {
            e.preventDefault();

            if ($searchInput.val() !=='') {
                searchResultHandler();
            }
        });
    },

    /**
     * * keyboardHandler
     * handle search on keyboard events
    * */
    keyboardHandler = function() {
        $searchInput.on('keypress', (e) => {
            let kCode = e.keyCode || e.charCode, //for cross browser
                $this = $(e.currentTarget),
                fieldVal = $this.val();

            if (kCode === 13) { //Capture Enter Key
                if (fieldVal === '' || fieldVal.match(/^\s*$/)) {
                    $this.focus();
                } else {
                  if (!$this.parents('.search').is('.is-active')) {
                      $this.typeahead('close').blur();
                      resetFilters();
                      searchResultHandler();
                  }
                }
                return false;
            }
        });
    },

    /** resetFilters
        * reset filters on search query change
    * */
    resetFilters = function() {
        $searchFilterDrpdn.map((key, item) => {
          let $item = $(item);
            $item.find('option:eq(0)').prop('selected', true);
            $item.trigger('change.select2');
        });
    },

    /**
     * * resetSearch
     * reset serach on reset button click
    * */

    resetSearch = function() {
        $searchFilter.on('reset', (e) => {
          e.preventDefault();
          setTimeout(() => {
              $searchFilter.find('select').trigger('change.select2');
              $searchInput.val('')
              .typeahead('val', '')
              .next('pre').text('')
              .typeahead('close');
              $(e.currentTarget).find('.search').removeClass('is-active');
          }, 50);
        });
    },

    /**
     * * replacePlaceholderWithValues
     * replaceholder values with dynamic values
    * */
    replacePlaceholderWithValues = function(item, placeholder, val, isLink) {
        let $item = $(item),
            updatedStr,
            placeholderClass = placeholder.replace('#', '').toLowerCase();

        if (isLink) {
            val = '<a class="js_didyoumean ' + placeholderClass + '" href="'+val+'">' + val + '</span>';
        } else {
            val = '<span class="' + placeholderClass + '">' + val + '</span>';
        }

        $item.map((key, value) => {
            let $this = $(value),
                str = value.innerText;

            if ($this.find('.' + placeholderClass).length === 0 && str.indexOf(placeholder) !== -1) {
                updatedStr = str.replace(placeholder, val);
                $this.html(updatedStr);
            } else {
                $this.find('.' + placeholderClass).replaceWith(val);
            }
        });
      },

    /**
     * * searchAutocomplete
     * autocomplete
    * */
    searchAutocomplete = function() {
        let $fieldset,
            searchQuery,
            autoCompleteUrl,
            hoverCl = 'is-hover',
            activeCl = 'is-active';
            $searchInput.typeahead({
                hint: false,
                highlight: true,
                minLength: $searchFilter.data('minchars')
            }, {
                name: 'keywordSearch',
                limit: $searchFilter.data('suggestions-limit'),
                source: (query, sync, async) => {
                    searchQuery = query;
                    if ($searchFilter.data('autocomplete-url').indexOf('&site=') > 0) {
                        autoCompleteUrl = $searchFilter.data('autocomplete-url') + '&q=' + encodeURIComponent(searchQuery) + '*' + '&count=' + $searchFilter.data('suggestions-limit');
                    }
                    else{
                        autoCompleteUrl = $searchFilter.data('autocomplete-url') + '?query=' + encodeURIComponent(searchQuery) + '&max_results=' + $searchFilter.data('suggestions-limit');
                    }
                    fetch(autoCompleteUrl)
                    .then(response => {
                        return response.text();
                        })
                        .then(responseData => {
                            if (responseData) {
                                let data = responseData;
                                data = data.replace("( [", '[').replace("] )", ']');
                                data = JSON.parse(data);
                                async(data);
                            }
                        })
                        .catch(error => {
                            if (error) {
                                console.log('There is an error with service: ' + error);
                            }
                        })
                }
            })
            $searchInput.on('keyup', (e) => {
            let kCode = e.keyCode || e.charCode;
            if ($(e.currentTarget).val() !== '' && kCode !== 13) {
              $fieldset.removeClass(activeCl);
            }
        }).on('focus', (event) => {
            $fieldset = $(event.currentTarget).parents('.search');
            $fieldset.addClass(hoverCl);
          }).on('blur', (event) => {
            setTimeout(() => {
                if ($fieldset) {
                    if ($(event.currentTarget).val() !== '') {
                        $fieldset.addClass(activeCl);
                    }
                    $fieldset.removeClass(hoverCl);
                }
            }, 200);
        }).on('typeahead:cursorchanged .typeahead', (e) => {
            $(e.currentTarget).val(searchQuery);
        });
    }
    return initialize();
};

searchModule();