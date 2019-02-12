/**
 * Util to handle geocoding tasks
 */
import { makeRequest } from './serviceRequester';

/**
 * getLatLng
 * takes a location (address, city and state, or zip) and
 * requests the latitude & longitude from the Bing Maps API and then returns the result
 *
 * @param { string } location The location to be geocoded
 * @param { string } countryRegion The countryRegion of the location
 * @param { string } apiUrl The URL for the Client API that we get the lat & lng from
 * @return { promise } A promise that resolves with the latitude & longitude ({latitude: Number, longitude: Number})
 */
export function getLatLng(location, countryRegion, apiUrl) {
    const requestUri =
        `${apiUrl}&countryRegion=${encodeURIComponent(countryRegion)}&postalCode=${encodeURIComponent(location)}`;

    return makeRequest(requestUri, 'GET')
        .then(data => {
            if(data instanceof Error) {
                throw new Error('Latitude/Longitude not found');
            }
            const latitude = data.resourceSets[0].resources[0].point.coordinates[0];
            const longitude = data.resourceSets[0].resources[0].point.coordinates[1];
            return {latitude, longitude};
        }).catch(error => {
            console.log(error);
            return error;
        });
}

/**
 * getDistance
 * returns the distance between two latitude/longitude points
 * (uses the havershine formula to get the shortest distance)
 *
 * @param { object } point1 The first point
 * @param { object } point2 The second point
 * @return { number } The distance between the two points in miles
 */
export function getDistance(point1, point2) {
    if(!point1.latitude || !point1.longitude || !point2.latitude || !point2.longitude ) { return null; }
    const earthRadius = 6371;
    const distanceLat = degreesToRadians(point2.latitude-point1.latitude);
    const distanceLng = degreesToRadians(point2.longitude-point1.longitude);
    const a =
        Math.sin(distanceLat/2) * Math.sin(distanceLat/2) +
        Math.cos(degreesToRadians(point1.latitude)) * Math.cos(degreesToRadians(point2.latitude)) *
        Math.sin(distanceLng/2) * Math.sin(distanceLng/2)
    ;
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    const distance = earthRadius * c;
    return kilometersToMiles(distance);
}

/**
 * degreesToRadians
 * converts degrees to radians
 *
 * @param { number } degrees The degrees
 * @return { number } The radians
 */
export function degreesToRadians(degrees) {
    return degrees * (Math.PI/180);
}

/**
 * kilometersToMiles
 * converts kilometers to miles
 *
 * @param { number } km The kilmeters
 * @return { number } The miles
 */
export function kilometersToMiles(km) {
    return km * 0.621371;
}