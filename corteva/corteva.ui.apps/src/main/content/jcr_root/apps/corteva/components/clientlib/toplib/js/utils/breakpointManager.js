const DESKTOP_MIN_RESOLUTION = 1200;
const TABLET_MIN_RESOLUTION = 992;
const MOBILE_MAX_RESOLUTION = 768;

/**
 * isDesktop
 *
 * @return { boolean } The boolean for whether or not the resolution is desktop size
 */
export function isDesktop() {
    // if documentElement not available, return default value
    return document && document.documentElement ?
        document.documentElement.clientWidth >= DESKTOP_MIN_RESOLUTION :
        true;
}

/**
 * isTablet
 *
 * @return { boolean } The boolean for whether or not the resolution is tablet size
 */
export function isTablet() {
    // if documentElement not available, return default value
    return document && document.documentElement ?
        document.documentElement.clientWidth >= TABLET_MIN_RESOLUTION :
        false;
}

/**
 * isMobile
 *
 * @return { boolean } The boolean for whether or not the resolution is mobile size
 */
export function isMobile() {
    // if documentElement not available, return default value
    return document && document.documentElement ?
        document.documentElement.clientWidth <= MOBILE_MAX_RESOLUTION :
        false;
}