import { isDesktop } from '../../../../../../clientlib/toplib/js/utils/breakpointManager';
import { getLatLng, getDistance } from '../../../../../../clientlib/toplib/js/utils/geocoder';
import { makeRequest } from '../../../../../../clientlib/toplib/js/utils/serviceRequester';

class RepSearch {
    constructor() {
        this.initProps();
    }

    /**
     * initProps
     * initializes class properties (scoped variables)
     */
    initProps() {
        this.SALES_REP_MODE = 'salesrep';
        this.RETAILER_MODE = 'retailer';
        this.TAB_CONTAINER = '.navHeaderTabs .band-content';
        this.TABS = '.navHeaderTabs .band-content a';
        this.SALES_REP_CONTAINER = '.findRep';
        this.submitButtons = '.findRep .submit-button';
        this.RESET_BUTTON = '.findRep .reset-search';
        this.MOBILE_NUM_RESULTS = 10;
        this.DESKTOP_NUM_RESULTS = 21;
        this.DISTANCE_RADII = [10, 20, 40, 50, 100];
        this.hasLocationFilter = true;
        this.isFiltered = false;
        this.shouldHideAddress = false;
        // set defaults
        this.numResults = this.DESKTOP_NUM_RESULTS;
        this.totalResults = [];
        this.currentResults = [];
        this.cachedData = {};
        // tracks the number corresponding to the number of times Load More was clicked
        // until the total number of results has been reached
        this.currentResultsIteration = 0;
        this.region = $("html").data("region");
        this.country = $("html").data("country");
        this.language = $("html").attr("lang");
        this.countryRegion = '';
    }

    /**
     * init
     * initializes component - should be called when document & window are available
     */
    init() {
        this.setActiveMode();
        this.setElements();
        this.setEventHandlers();
        this.resetEventHandlers();
        this.checkResolution();
        this.requestData();
    }

    /**
     * setActiveMode
     * checks which mode (search type) is active
     */
    setActiveMode() {
        // find which is active and set the mode based upon it
        if ($(this.TAB_CONTAINER).length > 0) {
            this.activeTabID = $(this.TAB_CONTAINER).find(".active").attr("href").replace("#","");
            this.mode = $("[id='"+this.activeTabID+"']").find(this.SALES_REP_CONTAINER).data("search-type");
        } else {
            this.mode = $(this.SALES_REP_CONTAINER).data("search-type");
        }
    }

    /**
     * setElements
     * select commonly-used elements and save them as props
     */
    setElements() {
        this.el = document.querySelector(`.findRep[data-search-type="${this.mode}"]`);
        this.resultsWrapper = this.el.getElementsByClassName('results-wrapper')[0];
        this.resultsRow = this.el.getElementsByClassName('results-row')[0];
        this.resultsError = this.el.getElementsByClassName('results-error')[0];
        this.invalidLocationError = this.el.getElementsByClassName('invalid-location-error')[0];
        this.loadMoreButton = this.el.getElementsByClassName('load-more-button')[0];
        this.findRepForm = this.el.getElementsByClassName('find-rep-form')[0];
        this.locationInput = this.el.getElementsByClassName('location-input')[0];
        this.submitButton = this.el.getElementsByClassName('submit-button')[0];
        this.shouldHideAddress = this.mode !== this.RETAILER_MODE;

        this.hasLocationFilter = this.findRepForm ? true : false;
    }

    /**
     * setEventHandlers
     * set event handlers for buttons, forms, etc.
     */
    setEventHandlers() {
        this.onModeButtonClick = this.onModeButtonClick.bind(this);
        this.onSubmitButtonClick = this.onSubmitButtonClick.bind(this);
        this.onResetButtonClick = this.onResetButtonClick.bind(this);
        $(this.TABS).on("click", this.onModeButtonClick);
        $(this.submitButtons).on("click", this.onSubmitButtonClick);
        $(this.RESET_BUTTON).on("click", this.onResetButtonClick);
    }

    /**
     * resetEventHandlers
     * reset event handlers for buttons, forms, etc.
     */
    resetEventHandlers() {
        this.onLoadMoreClick = this.onLoadMoreClick.bind(this);
        this.onLocationSubmit = this.onLocationSubmit.bind(this);
        this.loadMoreButton.addEventListener('click', this.onLoadMoreClick);
        if(this.findRepForm) { $(this.findRepForm).off('submit').on('submit', this.onLocationSubmit); }
    }

    /**
     * requestData
     * make request to API to get all data (to be filtered/sorted)
     */
    requestData() {
        makeRequest(`/bin/corteva/getsalesrepjson.${this.region}.${this.country}.${this.language}.${this.mode}.json`, 'GET')
            .then(data => {
                if(data instanceof Error) {
                    throw new Error('Data not found');
                }
                this.onDataLoaded(data);
            }).catch(error => {
                this.renderError();
            });
    }

    /**
     * renderResults
     * renders the results according to the current props
     */
    renderResults() {
        // handle instance that error is shown
        this.resultsError.style.display = 'none';
        this.resultsWrapper.style.display = 'block';
        this.clearInvalidLocationError();

        // and create the list from the num that has been rendered
        // to the lesser of numResults*(currentResultsIteration+1) or this.currentResults.length
        const startNum = this.numResults*(this.currentResultsIteration);
        const totalResults = Math.min(
            this.numResults*(this.currentResultsIteration+1), this.currentResults.length
        );

        for(let i=startNum;i<totalResults;i++){
            // if no name, do not show the result at all
            if(!this.currentResults[i].name || (!this.currentResults[i].zipCode
            && !this.currentResults[i].city && !this.currentResults[i].state
            && !this.currentResults[i].addressLine1)) {
                continue;
            }

            const listNode = document.createElement('ul');
            listNode.setAttribute('class', 'col');
            this.resultsRow.appendChild(listNode);

            // name
            const nameListItemNode = document.createElement('li');
            nameListItemNode.setAttribute('class', 'bold');
            nameListItemNode.textContent = this.currentResults[i].name;
            listNode.appendChild(nameListItemNode);

            // title
            if(this.currentResults[i].title) {
                const titleListItemNode = document.createElement('li');
                titleListItemNode.textContent = this.currentResults[i].title;
                listNode.appendChild(titleListItemNode);
            }

            // address
            if(!this.shouldHideAddress && this.currentResults[i].addressLine1) {
                const addrListItemNode = document.createElement('li');
                addrListItemNode.textContent = this.currentResults[i].addressLine1;
                listNode.appendChild(addrListItemNode);
            }

            // cityStateZip
            let cityStateZip = this.currentResults[i].city || '';
            cityStateZip = this.currentResults[i].state ?
                `${cityStateZip} ${this.currentResults[i].state}` :
                cityStateZip;
            if(!this.shouldHideAddress && this.currentResults[i].zipCode) {
                cityStateZip = this.currentResults[i].zipCode && cityStateZip ?
                    `${cityStateZip}, ${this.currentResults[i].zipCode}` :
                    this.currentResults[i].zipCode;
            }
            if(cityStateZip) {
                const cityStateZipListItemNode = document.createElement('li');
                cityStateZipListItemNode.textContent = cityStateZip;
                listNode.appendChild(cityStateZipListItemNode);
            }

            // phone
            if(this.currentResults[i].phone) {
                const phoneItemNode = document.createElement('li');
                const phoneLink = document.createElement('a');
                phoneLink.setAttribute('class', 'phone-link');
                phoneLink.setAttribute('href', `tel:${this.currentResults[i].phone}`);
                phoneLink.textContent = this.currentResults[i].phone;
                phoneItemNode.appendChild(phoneLink);
                listNode.appendChild(phoneItemNode);

                if(this.mode === this.SALES_REP_MODE) {
                    phoneLink.setAttribute('data-analytics-type', 'sales-rep-card');
                } else if(this.mode === this.RETAILER_MODE) {
                    phoneLink.setAttribute('data-analytics-type', 'retailer-card');
                }

                phoneLink.setAttribute('data-analytics-value', this.currentResults[i].phone);
            }

            // phone2
            if(this.currentResults[i].phone2) {
                const phoneItemNode = document.createElement('li');
                const phoneLink = document.createElement('a');
                phoneLink.setAttribute('class', 'phone-link');
                phoneLink.setAttribute('href', `tel:${this.currentResults[i].phone}`);
                phoneLink.textContent = this.currentResults[i].phone2;
                phoneItemNode.appendChild(phoneLink);
                listNode.appendChild(phoneItemNode);

                if(this.mode === this.SALES_REP_MODE) {
                    phoneLink.setAttribute('data-analytics-type', 'sales-rep-card');
                } else if(this.mode === this.RETAILER_MODE) {
                    phoneLink.setAttribute('data-analytics-type', 'retailer-card');
                }

                phoneLink.setAttribute('data-analytics-value', this.currentResults[i].phone);
            }

            // email
            if(this.currentResults[i].email) {
                const emailListItemNode = document.createElement('li');
                const emailLink = document.createElement('a');
                emailLink.setAttribute('class', 'bold');
                emailLink.setAttribute('href', `mailto:${this.currentResults[i].email}`);
                emailLink.textContent = this.currentResults[i].email;
                emailListItemNode.appendChild(emailLink);
                listNode.appendChild(emailListItemNode);

                if(this.mode === this.SALES_REP_MODE) {
                    emailLink.setAttribute('data-analytics-type', 'sales-rep-card');
                } else if(this.mode === this.RETAILER_MODE) {
                    emailLink.setAttribute('data-analytics-type', 'retailer-card');
                }

                emailLink.setAttribute('data-analytics-value', this.currentResults[i].email);
            }

            // website
            if(this.currentResults[i].website) {
                const websiteListItemNode = document.createElement('li');
                const websiteLink = document.createElement('a');
                websiteLink.setAttribute('class', 'bold');
                websiteLink.setAttribute('href', this.currentResults[i].website);
                websiteLink.setAttribute('target', '_blank');
                websiteLink.textContent = this.currentResults[i].website;
                websiteListItemNode.appendChild(websiteLink);
                listNode.appendChild(websiteListItemNode);

                if(this.mode === this.SALES_REP_MODE) {
                    websiteLink.setAttribute('data-analytics-type', 'sales-rep-card');
                } else if(this.mode === this.RETAILER_MODE) {
                    websiteLink.setAttribute('data-analytics-type', 'retailer-card');
                }

                websiteLink.setAttribute('data-analytics-value', this.currentResults[i].website);
            }
        }

        // if we've rendered all, hide the load more button
        // otherwise, make sur it is shown
        if(totalResults === this.currentResults.length) {
            this.hideLoadMoreButton();
        } else {
            this.showLoadMoreButton();
        }
    }

    /**
     * reset
     * clears the form, resets the elements, and currentResults
     */
    reset() {
        this.isFiltered = false;
        this.setElements();
        this.resetEventHandlers();

        if(this.locationInput) {
            this.clearForm();
        }

        // and fetch the new data
        if(!this.cachedData[this.mode] || !this.cachedData[this.mode].length) { this.requestData(); }
        else {
            this.currentResults = this.totalResults = this.cachedData[this.mode].slice();
            this.resetResults();
        }
    }

    /**
     * clearForm
     * clears inputs
     */
    clearForm() {
        this.locationInput.value = '';
    }

    /**
     * resetResults
     * resets the results (clears and rerenders them)
     */
    resetResults() {
        this.clearResults();
        this.renderResults();
    }

    /**
     * clearResults
     * removes any results
     */
    clearResults() {
        this.currentResultsIteration = 0;
        while(this.resultsRow.hasChildNodes()) {
            this.resultsRow.removeChild(this.resultsRow.lastChild);
        }
    }

    /**
     * sortResults
     * sorts the results
     */
    sortResults() {
        // sort the results
        // first sort by name
        this.currentResults = this.currentResults.sort((resultA, resultB) => {
            if(!resultA.name || !resultB.name) { return 0; }

            return resultA.name.localeCompare(resultB.name);
        });

        // then if hasLocationFilter & isFiltered, sort by distance
        if(this.isFiltered && this.hasLocationFilter) {
            this.currentResults = this.currentResults.sort((resultA, resultB) => {
                return resultA.distance - resultB.distance;
            });
        }
    }

    /**
     * renderError
     * renders the results error message
     */
    renderError() {
        this.resultsError.style.display = 'block';
        this.resultsWrapper.style.display = 'none';
        this.clearInvalidLocationError();
    }

    /**
     * renderInvalidLocationError
     * renders the error for when an invalid location is entered
     */
    renderInvalidLocationError() {
        this.invalidLocationError.style.display = 'block';
        this.resultsError.style.display = 'none';
        this.resultsWrapper.style.display = 'none';
        this.locationInput.classList.add('invalid');
    }

    /**
     * clearInvalidLocationError
     * clears the error for when an invalid location is entered
     */
    clearInvalidLocationError() {
        if(this.locationInput) {
            this.invalidLocationError.style.display = 'none';
            this.locationInput.classList.remove('invalid');
        }
    }

    /**
     * checkResolution
     * checks the current resolution and sets properties accordingly
     */
    checkResolution() {
        this.numResults = isDesktop() ?
            this.DESKTOP_NUM_RESULTS :
            this.MOBILE_NUM_RESULTS;
    }

    /**
     * hideLoadMoreButton
     * hides the Load More button; for when there are no more results to render
     */
    hideLoadMoreButton() {
        this.loadMoreButton.style.display = 'none';
    }

    /**
     * showLoadMoreButton
     * shows the Load More button; for when it's been hidden and needs to be displayed
     */
    showLoadMoreButton() {
        this.loadMoreButton.style.display = 'block';
    }

    /**
     * filterResults
     * actually filters & sorts the results, also resets the num results shown
     *
     * @param { string } latlng The latlng the results are compared against
     */
    filterResults(latlng) {
        let filteredResults = [];
        this.currentResults = this.totalResults;

        // traverse the data list and calculate the distance from the latlng
        // to each latlng in the list. map them and add distance param
        this.resultsWithDistance = this.currentResults.map(result => {
            const latitude = result && result.latitude || null;
            const longitude = result && result.longitude  || null;
            const distance = getDistance(latlng, {latitude, longitude});
            return $.extend(result, {distance: distance});
        });

        // then filter based upon the DISTANCE_RADII
        // start with the first radius in the list
        // if the filtered results are >= numResults, break out
        // if not, move to the next radius in the list

        for(var i = 0; i<this.DISTANCE_RADII.length; i++) {
            let radius = this.DISTANCE_RADII[i];
            filteredResults = this.resultsWithDistance.filter(result => {
                return (result.distance || result.distance === 0) && result.distance <= radius;
            });

            if(filteredResults.length > 0) { break; }
        }

        if(!filteredResults.length) {
            this.renderError();
            return;
        }

        this.currentResults = filteredResults.slice();
        this.isFiltered = true;
        this.sortResults();
        this.renderResults();
    }

    /**
     * onLocationSubmit
     * handler for filter by location form submission
     */
    onLocationSubmit(event) {
        event.preventDefault();
        let searchValue = $.trim($(this.locationInput).val());

        if (!(searchValue.match(/^[a-z0-9][a-z0-9\-\s]*[a-z0-9]$/i))) {
            this.renderInvalidLocationError();
            return;
        }

        if (!searchValue) {
            this.renderError();
            return;
        }

        getLatLng(searchValue, this.countryRegion, this.el.dataset['apiDomain'])
            .then(latlng => {
                if(latlng instanceof Error) {
                    this.renderInvalidLocationError();
                    return;
                }
                this.clearResults();
                if(!latlng) { this.renderInvalidLocationError(); }
                else { this.filterResults(latlng); }
            });
    }

    /**
     * onDataLoaded
     * handler for when data has loaded
     */
    onDataLoaded(data) {
        this.currentResults = this.totalResults = data.slice();
        if(this.currentResults.length) {
            this.countryRegion = this.currentResults[0].country || '';
            this.sortResults();
            this.cachedData[this.mode] = this.currentResults.slice();
            this.renderResults();
        } else {
            this.renderError();
        }
    }

    /**
     * onLoadMoreClick
     * handler for click of Load More button
     */
     onLoadMoreClick(event) {
        this.currentResultsIteration++;
        this.renderResults();
    }

    /**
     * onModeButtonClick
     * handler for click of mode (sales rep || retailer) button
     */
    onModeButtonClick(event) {
        // event.preventDefault();
        // set the mode accordingly
        this.setActiveMode();

        // reset all including the elements since our parent element has changed
        this.reset();
    }

    onSubmitButtonClick(event) {
        if($(this.locationInput).val() === "") {
            this.currentResults = this.totalResults = this.cachedData[this.mode].slice();
            this.resetResults();
        }
    }

    onResetButtonClick(event) {
        this.reset();
        $(this.locationInput).focus();
    }
}

const repSearch = new RepSearch();
document.addEventListener("DOMContentLoaded", function() {
    let $repsearchContainer = $(".repsearch");
    if ($repsearchContainer.length !== 0) {
        repSearch.init();
    }
});
