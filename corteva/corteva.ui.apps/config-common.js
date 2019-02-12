/* eslint-disable */

/**
 * 'path' library
 * @type {posix}
 */
const path = require('path');

/**
 * Sets the app directory to resolve to the content components
 */
const APP_DIR = path.resolve(__dirname, 'src/main/content/jcr_root/apps/corteva/components/content/');

/**
 * List of individual components to compile in webpack
 * @type {{"../js/vendor/babel-polyfill": string[], style: *, "../../../clientlib/toplib-pioneer/js/dist/app": string, "../../../content/accordionList/v1/accordionList/clientlib/js/dist/accordion-list": string, "../../../content/anchorNavigation/v1/anchorNavigation/clientlib/js/dist/anchor-links-container": string, "../../../content/anchorNavigation/v1/anchorNavigation/clientlib/js/dist/in-page-nav-vertical": string, "../../../content/anchorNavigation/v1/anchorNavigation/clientlib/js/dist/timeline": string, "../../../content/articleFilter/v1/articleFilter/clientlib/js/dist/articleFilter": string, "../../../content/bioDetailCardContainer/v1/bioDetailCardContainer/clientlib/js/dist/biography-filter": string, "../../../content/breadcrumb/v1/breadcrumb/clientlib/js/dist/breadcrumb": string, "../../../content/cardsContainer/v1/cardsContainer/clientlib/js/dist/cardsContainer": string, "../../../content/cardsContainer/v1/cardsContainer/clientlib/js/dist/container-responsive": string, "../../../content/containerSearch/v1/containerSearch/clientlib/js/dist/containerSearch": string, "../../../content/eloquaForm/v1/eloquaForm/clientlib/js/dist/eloqua-form": string, "../../../content/galleryImage/v1/galleryImage/clientlib/js/dist/gallery-image": string, "../../../content/galleryVideoPlayer/v1/galleryVideoPlayer/clientlib/js/dist/gallery-video-player": string, "../../../content/galleryVideoPlayer/v2/galleryVideoPlayer/clientlib/js/dist/gallery-video-player": string, "../../../content/globalHeader/v1/globalHeader/clientlib/js/dist/globalHeader": string, "../../../content/globalHeader/v1/globalHeader/clientlib/js/dist/skipNavigation": string, "../../../content/heroCarousel/v1/heroCarousel/clientlib/js/dist/hero-carousel": string, "../../../content/navHeaderTabs/v1/navHeaderTabs/clientlib/js/dist/header-tabs": string, "../../../content/navigationDropdown/v1/navigationDropdown/clientlib/js/dist/nav-dropdown": string, "../../../content/navSearch/v1/navSearch/clientlib/js/dist/navSearch": string, "../../../content/relatedContentFullWidthCard/v1/relatedContentFullWidthCard/clientlib/js/dist/related-content-full-width-card": string, "../../../content/richText/v1/richText/clientlib/js/dist/rich-text": string, "../../../content/scrollToTop/v1/scrollToTop/clientlib/js/dist/back-to-top": string, "../../../content/comparativeImageSlider/v1/comparativeImageSlider/clientlib/js/dist/comparativeImageSlider": string, "../../../content/countrySelector/v1/countrySelector/clientlib/js/dist/countrySelector": string, "../../../content/cropList/v1/cropList/clientlib/js/dist/crop-list": string, "../../../content/productHeader/v1/productHeader/clientlib/js/dist/product-header": string, "../../../content/productLabel/v1/productLabel/clientlib/js/dist/product-label": string, "../../../content/productRegistration/v1/productRegistration/clientlib/js/dist/product-registrations": string, "../../../content/productTechSpecs/v1/productTechSpecs/clientlib/js/dist/productTechSpecs": string, "../../../content/productEfficacyList/v1/productEfficacyList/clientlib/js/dist/product-efficacy-list": string, "../../../content/productFilter/v1/productFilter/clientlib/js/dist/product-filter": string, "../../../content/productLabelFinder/v1/productLabelFinder/clientlib/js/dist/label-finder": string, "../../../content/productLabelFinder/v1/productLabelFinder/clientlib/js/dist/product-label-accordion": string, "../../../content/hotspotimage/v1/hotspotimage/clientlib/js/dist/hotspot": string, "../../../content/planterSettings/v1/planterSettings/clientlib/js/dist/planter-settings": string, "../../../content/plantingRateEstimator/v1/plantingRateEstimator/clientlib/js/dist/planting-rate-estimator": string, "../../../content/galleryImage/v2/galleryImage/clientlib/js/dist/gallery-image-v2": string, "../../../content/cornReplantCalculator/v1/cornReplantCalculator/clientlib/js/dist/corn-replant-calculator": string}}
 */
const config = {
    //'../js/vendor/babel-polyfill': ['babel-polyfill'],
    '../../../clientlib/toplib-pioneer/js/dist/app': APP_DIR + '../../clientlib/toplib/js/app.js',

    // Common components
    '../../../clientlib/toplib/js/dist/app': APP_DIR + '../../clientlib/toplib/js/app.js',
    '../../../clientlib/toplib/js/utils/dist/breakpointManager': APP_DIR + '../../clientlib/toplib/js/utils/breakpointManager.js',
    '../../../clientlib/toplib/js/utils/dist/geocoder': APP_DIR + '../../clientlib/toplib/js/utils/geocoder.js',
    '../../../clientlib/toplib/js/utils/dist/serviceRequester': APP_DIR + '../../clientlib/toplib/js/utils/serviceRequester.js',
    '../../../content/accordionList/v1/accordionList/clientlib/js/dist/accordion-list': APP_DIR + '/accordionList/v1/accordionList/clientlib/js/accordion-list.js',
    '../../../content/anchorNavigation/v1/anchorNavigation/clientlib/js/dist/anchor-links-container': APP_DIR + '/anchorNavigation/v1/anchorNavigation/clientlib/js/anchor-links-container.js',
    '../../../content/anchorNavigation/v1/anchorNavigation/clientlib/js/dist/in-page-nav-vertical': APP_DIR + '/anchorNavigation/v1/anchorNavigation/clientlib/js/in-page-nav-vertical.js',
    '../../../content/anchorNavigation/v1/anchorNavigation/clientlib/js/dist/timeline': APP_DIR + '/anchorNavigation/v1/anchorNavigation/clientlib/js/timeline.js',
    '../../../content/articleFilter/v1/articleFilter/clientlib/js/dist/articleFilter': APP_DIR + '/articleFilter/v1/articleFilter/clientlib/js/articleFilter.js',
    '../../../content/bioDetailCardContainer/v1/bioDetailCardContainer/clientlib/js/dist/biography-filter': APP_DIR + '/bioDetailCardContainer/v1/bioDetailCardContainer/clientlib/js/biography-filter.js',
    '../../../content/breadcrumb/v1/breadcrumb/clientlib/js/dist/breadcrumb': APP_DIR + '/breadcrumb/v1/breadcrumb/clientlib/js/breadcrumb.js',
    '../../../content/cardsContainer/v1/cardsContainer/clientlib/js/dist/cardsContainer': APP_DIR + '/cardsContainer/v1/cardsContainer/clientlib/js/cardsContainer.js',
    '../../../content/cardsContainer/v1/cardsContainer/clientlib/js/dist/container-responsive': APP_DIR + '/cardsContainer/v1/cardsContainer/clientlib/js/container-responsive.js',
    '../../../content/hybridTable/v1/hybridTable/clientlib/js/dist/hybridTable': APP_DIR + '/hybridTable/v1/hybridTable/clientlib/js/hybridTable.js',
    '../../../content/characteristicsChart/v1/characteristicsChart/clientlib/js/dist/characteristicsChart': APP_DIR + '/characteristicsChart/v1/characteristicsChart/clientlib/js/characteristicsChart.js',
    '../../../content/containerSearch/v1/containerSearch/clientlib/js/dist/containerSearch': APP_DIR + '/containerSearch/v1/containerSearch/clientlib/js/containerSearch.js',
    '../../../content/eloquaForm/v1/eloquaForm/clientlib/js/dist/eloqua-form': APP_DIR + '/eloquaForm/v1/eloquaForm/clientlib/js/eloqua-form.js',
    '../../../content/galleryImage/v1/galleryImage/clientlib/js/dist/gallery-image': APP_DIR + '/galleryImage/v1/galleryImage/clientlib/js/gallery-image.js',
    '../../../content/galleryVideoPlayer/v1/galleryVideoPlayer/clientlib/js/dist/gallery-video-player': APP_DIR + '/galleryVideoPlayer/v1/galleryVideoPlayer/clientlib/js/gallery-video-player.js',
    '../../../content/globalHeader/v1/globalHeader/clientlib/js/dist/globalHeader': APP_DIR + '/globalHeader/v1/globalHeader/clientlib/js/globalHeader.js',
    '../../../content/globalHeader/v1/globalHeader/clientlib/js/dist/skipNavigation': APP_DIR + '/globalHeader/v1/globalHeader/clientlib/js/skipNavigation.js',
    '../../../content/heroCarousel/v1/heroCarousel/clientlib/js/dist/hero-carousel': APP_DIR + '/heroCarousel/v1/heroCarousel/clientlib/js/hero-carousel.js',
    '../../../content/navHeaderTabs/v1/navHeaderTabs/clientlib/js/dist/header-tabs': APP_DIR + '/navHeaderTabs/v1/navHeaderTabs/clientlib/js/header-tabs.js',
    '../../../content/navigationDropdown/v1/navigationDropdown/clientlib/js/dist/nav-dropdown': APP_DIR + '/navigationDropdown/v1/navigationDropdown/clientlib/js/nav-dropdown.js',
    '../../../content/navSearch/v1/navSearch/clientlib/js/dist/navSearch': APP_DIR + '/navSearch/v1/navSearch/clientlib/js/navSearch.js',
    '../../../content/relatedContentFullWidthCard/v1/relatedContentFullWidthCard/clientlib/js/dist/related-content-full-width-card': APP_DIR + '/relatedContentFullWidthCard/v1/relatedContentFullWidthCard/clientlib/js/related-content-full-width-card.js',
    '../../../content/repsearch/v1/repsearch/clientlib/js/dist/RepSearch': APP_DIR + '/repsearch/v1/repsearch/clientlib/js/RepSearch.js',
    '../../../content/richText/v1/richText/clientlib/js/dist/rich-text': APP_DIR + '/richText/v1/richText/clientlib/js/rich-text.js',
    '../../../content/scrollToTop/v1/scrollToTop/clientlib/js/dist/back-to-top': APP_DIR + '/scrollToTop/v1/scrollToTop/clientlib/js/back-to-top.js',
    '../../../content/comparativeImageSlider/v1/comparativeImageSlider/clientlib/js/dist/comparativeImageSlider': APP_DIR + '/comparativeImageSlider/v1/comparativeImageSlider/clientlib/js/comparativeImageSlider.js',
    '../../../content/countrySelector/v1/countrySelector/clientlib/js/dist/countrySelector': APP_DIR + '/countrySelector/v1/countrySelector/clientlib/js/countrySelector.js',
    '../../../content/cropList/v1/cropList/clientlib/js/dist/crop-list': APP_DIR + '/cropList/v1/cropList/clientlib/js/crop-list.js',
    '../../../content/productHeader/v1/productHeader/clientlib/js/dist/product-header': APP_DIR + '/productHeader/v1/productHeader/clientlib/js/product-header.js',
    '../../../content/productLabel/v1/productLabel/clientlib/js/dist/product-label': APP_DIR + '/productLabel/v1/productLabel/clientlib/js/product-label.js',
    '../../../content/productRegistration/v1/productRegistration/clientlib/js/dist/product-registrations': APP_DIR + '/productRegistration/v1/productRegistration/clientlib/js/prduct-registrations.js',
    '../../../content/productTechSpecs/v1/productTechSpecs/clientlib/js/dist/productTechSpecs': APP_DIR + '/productTechSpecs/v1/productTechSpecs/clientlib/js/productTechSpecs.js',
    '../../../content/productEfficacyList/v1/productEfficacyList/clientlib/js/dist/product-efficacy-list': APP_DIR + '/productEfficacyList/v1/productEfficacyList/clientlib/js/product-efficacy-list.js',
    '../../../content/productFilter/v1/productFilter/clientlib/js/dist/product-filter': APP_DIR + '/productFilter/v1/productFilter/clientlib/js/product-filter.js',
    '../../../content/productLabelFinder/v1/productLabelFinder/clientlib/js/dist/label-finder': APP_DIR + '/productLabelFinder/v1/productLabelFinder/clientlib/js/label-finder.js',
    '../../../content/productLabelFinder/v1/productLabelFinder/clientlib/js/dist/product-label-accordion': APP_DIR + '/productLabelFinder/v1/productLabelFinder/clientlib/js/product-label-accordion.js',
    '../../../content/hotspotimage/v1/hotspotimage/clientlib/js/dist/hotspot': APP_DIR + '/hotspotimage/v1/hotspotimage/clientlib/js/hotspot.js'
};

module.exports = config;