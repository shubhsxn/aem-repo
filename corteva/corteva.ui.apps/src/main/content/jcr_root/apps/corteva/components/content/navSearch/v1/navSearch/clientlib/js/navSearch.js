//Nav search
const NavSearch = function() {
    const searchCl = 'form.search',
        searchInputCl = '.top-search',
        hoverCl = 'is-hover',
        activeCl = 'is-active',
        searchBtn = 'button.submit-search'
    /**
     * Initialization function
    */
    function initialize() {
        if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('navSearch')){
            return;
        }
        searchHandler();
    }

    /**
     * * keyboardHandler
     * handle search on keyboard events
    * */
    let searchHandler = function() {
        let $searchForm = $(searchCl),
            $searchInput = $searchForm.find(searchInputCl);

        $(searchCl).find(searchBtn).on('click', (e) => {
            e.preventDefault();
            submitHandler($(e.currentTarget).parent().find(searchInputCl), $searchForm);
        });

        $searchInput.on('keyup', (e) => {
            let $this = $(e.currentTarget);

                $this.parent().siblings(searchBtn).removeAttr('disabled');
                if ((e.keyCode || e.charCode) === 13) { //Capture Enter Key
                submitHandler($this, $searchForm);
                return false;
                }
                if ((e.keyCode || e.charCode) === 27) { //Capture Escape Key
                    $searchForm.trigger("reset");
                    $searchInput.blur();
                    return;
                }
            });

        //searchAutocomplete
            searchAutocomplete($searchInput, $searchForm);
        //resetSearch
        resetSearch($searchInput, $searchForm);
    },

    /**
     * * submitHandler
     * submit to search page
    * */
    submitHandler = function($elm, $form) {
        let fieldVal = $elm.val();

        if (fieldVal.length && fieldVal.length >= $form.data('minchars')) {
            $elm.typeahead('close');
            let countryVal = $('html').attr('data-country');
            if(countryVal){
                $form.find('#search-country').val(countryVal);
            }
            $form.submit();
        } else {
          $elm.focus();
        }
    },

    /**
     * * searchAutocomplete
     * autocomplete
    * */
    searchAutocomplete = function($searchInput, $searchFilter) {
      let $fieldset,
          searchQuery,
          disableBlur,
          autoCompleteUrl,
          ua = navigator.userAgent.toLowerCase(),
          isMobile = ua.indexOf("mobile") > -1;

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
            $searchInput.on('blur', (event) => {
            setTimeout(() => {
                if ($fieldset && !disableBlur) {
                    if ($(event.currentTarget).val() !== '') {
                        $fieldset.addClass(activeCl);
                    }
                    $fieldset.removeClass(hoverCl);
                    $fieldset.siblings(searchBtn).attr('disabled', true);
                }
            }, 200);
        })
        .on('focus', (event) => {
            let $this = $(event.currentTarget);

            $fieldset = $this.parents(searchCl);
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
            return false;
        })
        .on('typeahead:cursorchanged .typeahead', (e) => {
            $(e.currentTarget).val(searchQuery);
        });
    },

    /**
     * * resetSearch
     * reset serach on reset button click
    * */
    resetSearch = function($searchInput, $searchFilter) {
        $searchFilter.on('reset', (e) => {
          e.preventDefault();
          setTimeout(() => {
              $searchInput.val('')
              .typeahead('val', '')
              .next('pre').text('')
              .typeahead('close');
              $(e.currentTarget).removeClass(activeCl);
          }, 50);
        });

        //close Search Input on mobile
        $searchFilter.find('.reset-search').on('click', (e) => {
            e.preventDefault();

            $searchFilter[0].reset();
        });
    }
    return initialize();
};

NavSearch();