/* eslint-disable */
/**
 * 'path' library
 * @type {posix}
 */
const path = require('path');

/**
 * Imports the corteva component definitions
 * @type {{"../js/vendor/babel-polyfill": string[], style: *, "../../../clientlib/toplib-pioneer/js/dist/app": string, "../../../content/accordionList/v1/accordionList/clientlib/js/dist/accordion-list": string, "../../../content/anchorNavigation/v1/anchorNavigation/clientlib/js/dist/anchor-links-container": string, "../../../content/anchorNavigation/v1/anchorNavigation/clientlib/js/dist/in-page-nav-vertical": string, "../../../content/anchorNavigation/v1/anchorNavigation/clientlib/js/dist/timeline": string, "../../../content/articleFilter/v1/articleFilter/clientlib/js/dist/articleFilter": string, "../../../content/bioDetailCardContainer/v1/bioDetailCardContainer/clientlib/js/dist/biography-filter": string, "../../../content/breadcrumb/v1/breadcrumb/clientlib/js/dist/breadcrumb": string, "../../../content/cardsContainer/v1/cardsContainer/clientlib/js/dist/cardsContainer": string, "../../../content/cardsContainer/v1/cardsContainer/clientlib/js/dist/container-responsive": string, "../../../content/containerSearch/v1/containerSearch/clientlib/js/dist/containerSearch": string, "../../../content/eloquaForm/v1/eloquaForm/clientlib/js/dist/eloqua-form": string, "../../../content/galleryImage/v1/galleryImage/clientlib/js/dist/gallery-image": string, "../../../content/galleryVideoPlayer/v1/galleryVideoPlayer/clientlib/js/dist/gallery-video-player": string, "../../../content/galleryVideoPlayer/v2/galleryVideoPlayer/clientlib/js/dist/gallery-video-player": string, "../../../content/globalHeader/v1/globalHeader/clientlib/js/dist/globalHeader": string, "../../../content/globalHeader/v1/globalHeader/clientlib/js/dist/skipNavigation": string, "../../../content/heroCarousel/v1/heroCarousel/clientlib/js/dist/hero-carousel": string, "../../../content/navHeaderTabs/v1/navHeaderTabs/clientlib/js/dist/header-tabs": string, "../../../content/navigationDropdown/v1/navigationDropdown/clientlib/js/dist/nav-dropdown": string, "../../../content/navSearch/v1/navSearch/clientlib/js/dist/navSearch": string, "../../../content/relatedContentFullWidthCard/v1/relatedContentFullWidthCard/clientlib/js/dist/related-content-full-width-card": string, "../../../content/richText/v1/richText/clientlib/js/dist/rich-text": string, "../../../content/scrollToTop/v1/scrollToTop/clientlib/js/dist/back-to-top": string, "../../../content/comparativeImageSlider/v1/comparativeImageSlider/clientlib/js/dist/comparativeImageSlider": string, "../../../content/countrySelector/v1/countrySelector/clientlib/js/dist/countrySelector": string, "../../../content/cropList/v1/cropList/clientlib/js/dist/crop-list": string, "../../../content/productHeader/v1/productHeader/clientlib/js/dist/product-header": string, "../../../content/productLabel/v1/productLabel/clientlib/js/dist/product-label": string, "../../../content/productRegistration/v1/productRegistration/clientlib/js/dist/product-registrations": string, "../../../content/productTechSpecs/v1/productTechSpecs/clientlib/js/dist/productTechSpecs": string, "../../../content/productEfficacyList/v1/productEfficacyList/clientlib/js/dist/product-efficacy-list": string, "../../../content/productFilter/v1/productFilter/clientlib/js/dist/product-filter": string, "../../../content/productLabelFinder/v1/productLabelFinder/clientlib/js/dist/label-finder": string, "../../../content/productLabelFinder/v1/productLabelFinder/clientlib/js/dist/product-label-accordion": string, "../../../content/hotspotimage/v1/hotspotimage/clientlib/js/dist/hotspot": string, "../../../content/planterSettings/v1/planterSettings/clientlib/js/dist/planter-settings": string, "../../../content/plantingRateEstimator/v1/plantingRateEstimator/clientlib/js/dist/planting-rate-estimator": string, "../../../content/galleryImage/v2/galleryImage/clientlib/js/dist/gallery-image-v2": string, "../../../content/cornReplantCalculator/v1/cornReplantCalculator/clientlib/js/dist/corn-replant-calculator": string}}
 */
const common = require('./config-common');

/**
 * Sets the app directory to resolve to the content components
 */
const APP_DIR = path.resolve(__dirname, 'src/main/content/jcr_root/apps/corteva/components/content/');

/**
 * Extending the Corteva components with the Pioneer specific components
 * @type {*}
 */
const config = Object.assign({}, common, {
    'style': path.resolve(__dirname, 'src/main/content/jcr_root/apps/corteva/components/clientlib/toplib/styles/style.js'),
});

module.exports = config;
