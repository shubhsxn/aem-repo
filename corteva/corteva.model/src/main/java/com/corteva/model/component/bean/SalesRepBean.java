package com.corteva.model.component.bean;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.exception.InvalidCsvException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;



/**
 * The is the Sales Rep Bean class
 *
 * @author Sapient
 */

public class SalesRepBean {

    /**
     * The constant for Bing Map Response - Coordinate Constant
     **/
    public static final String COORDINATE_CONSTANT = "coordinates";
    /**
     * The constant for Bing Map Response - Resources Set Constant
     **/
    public static final String RESOURCE_SETS_CONSTANT = "resourceSets";
    /**
     * The constant for Bing Map Response - Resources Constant
     **/
    public static final String RESOURCES_CONSTANT = "resources";
    /**
     * The constant for Bing Map Response Points Constant
     **/
    public static final String POINTS_CONSTANT = "point";
    /**
     * Logger Instantiation.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesRepBean.class);
    /**
     * The  variable zipcode.
     */
    private String zipCode;
    /**
     * The  variable state.
     */
    private String state;
    /**
     * The variable  country.
     */
    private String country;
    /**
     * The variable salesrepName .
     */
    private String name;
    /**
     * The variable title .
     */
    private String title;
    /**
     * The variable addressLine1 .
     */
    private String addressLine1;
    /**
     * The variable city .
     */
    private String city;
    /**
     * The variable phone .
     */
    private String phone;
    /**
     * The variable email .
     */
    private String email;
    /**
     * The variable phone2 .
     */
    private String phone2;
    /**
     * The variable website .
     */
    private String website;
    /**
     * The variable latitude .
     */
    private String latitude;
    /**
     * The variable longitude .
     */
    private String longitude;


    /**
     * This method makes a rest call to get latitude and Longitude for each Sales Rep Object
     *
     * @param country        the country
     * @param zipCode        the zipcode
     * @param apiUrl         the apiurl     *
     * @param city           the city
     * @param addressLine1   the addressLine1
     * @param state          the state
     * @param encodingFormat the encodingFormat
     * @throws InvalidCsvException the InvalidCsvException
     */
    public void setLatitudeLongitude(String apiUrl, String country, String zipCode, String city, String addressLine1, String state, String encodingFormat) throws InvalidCsvException {
        HttpClient client = new HttpClient();
        GetMethod get = null;
        try {
            addressLine1 = URLEncoder.encode(addressLine1, encodingFormat);
            state = URLEncoder.encode(state, encodingFormat);
            city = URLEncoder.encode(city, encodingFormat);
            country = URLEncoder.encode(country, encodingFormat);
            zipCode = URLEncoder.encode(zipCode, encodingFormat);

            StringBuilder sb = new StringBuilder();

            sb.append("&countryRegion=" + country);
            sb.append("&postalCode=" + zipCode);
            sb.append("&locality=" + city);
            sb.append("&adminDistrict=" + state);
            sb.append("&addressLine=" + addressLine1);

            apiUrl = apiUrl + sb.toString();
            get = new GetMethod(apiUrl);
            LOGGER.debug("Fetching latitude - longitude : {}", apiUrl);
        } catch (UnsupportedEncodingException e) {
            throw new InvalidCsvException("Unsupported Encoding Exception Corrupted Data -could not retrieve latitide and longitude " + e.getMessage());
        }
        try {
            client.executeMethod(get);
            JsonObject jsonObj = new JsonParser().parse(get.getResponseBodyAsString()).getAsJsonObject();
            JsonArray arrLatLong = bingMapParsedJson(jsonObj);
            if (null != arrLatLong) {
                this.latitude = arrLatLong.get(CortevaConstant.ZERO).toString();
                this.longitude = arrLatLong.get(CortevaConstant.ONE).toString();
            } else {
                throw new InvalidCsvException("Incorrect response recieved from API while fetching latitude/longitude");
            }
        } catch (IOException e) {
            throw new InvalidCsvException("Error occured while hitting API latitude and Longitude for Sales Rep :" + name);
        } catch (Exception e) {
            throw new InvalidCsvException("Some Exception occured while fetching Latitude /Longitude from Bing Map for Sales Rep:" + name + " : " + e.getMessage());
        } finally {
            get.releaseConnection();
        }
    }

    /**
     * This method returns of the JSON received from Bing Map API Response is valid
     *
     * @param jsonObject the JSONObject
     * @return jsonArray
     */
    private static JsonArray bingMapParsedJson(JsonObject jsonObject) {

        JsonArray arrLatLong = null;
        JsonObject resourceSet = checkAndGetResourceSet(jsonObject);
        if (resourceSet != null) {
            JsonObject resources = checkAndGetResources(resourceSet);
            if (resources != null) {
                JsonObject geocodePoints = checkAndGetGeoPoints(resources);
                if (null == geocodePoints || null == geocodePoints.getAsJsonArray(COORDINATE_CONSTANT).get(CortevaConstant.ZERO)
                        || null == geocodePoints.getAsJsonArray(COORDINATE_CONSTANT).get(CortevaConstant.ONE)) {
                    return null;
                }
                arrLatLong = geocodePoints.getAsJsonArray(COORDINATE_CONSTANT);
            }

        }
        return arrLatLong;
    }

    /**
     * Get resource Set from Api
     *
     * @param jsonObject the JSONObject
     * @return JSONObject
     */
    private static JsonObject checkAndGetResourceSet(JsonObject jsonObject) {

        if (null == jsonObject.getAsJsonArray(RESOURCE_SETS_CONSTANT)
                || null == jsonObject.getAsJsonArray(RESOURCE_SETS_CONSTANT).get(CortevaConstant.ZERO)) {
            return null;
        }
        return (JsonObject) jsonObject.getAsJsonArray(RESOURCE_SETS_CONSTANT).get(CortevaConstant.ZERO);
    }

    /**
     * Get resources from Api
     *
     * @param resourceSet the JSONObject
     * @return JSONObject
     */
    private static JsonObject checkAndGetResources(JsonObject resourceSet) {

        if (null == resourceSet.getAsJsonArray(RESOURCES_CONSTANT)
                || null == resourceSet.getAsJsonArray(RESOURCES_CONSTANT).get(CortevaConstant.ZERO)) {
            return null;
        }
        return (JsonObject) resourceSet.getAsJsonArray(RESOURCES_CONSTANT).get(CortevaConstant.ZERO);
    }

    /**
     * Get geo point from Api
     *
     * @param resources the JSONObject
     * @return JSONObject
     */
    private static JsonObject checkAndGetGeoPoints(JsonObject resources) {

        if (null == resources || !resources.has(POINTS_CONSTANT)) {
            return null;
        }
        return resources.getAsJsonObject(POINTS_CONSTANT);
    }

    /**
     * This method returns a zip code
     *
     * @return zip code the zip code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * This method sets a zip code
     *
     * @param zipCode the zip code
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * This method returns state value
     *
     * @return state the state
     */
    public String getState() {
        return state;
    }

    /**
     * This method sets state value
     *
     * @param state the state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * This method returns country value
     *
     * @return country the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * This method sets country value
     *
     * @param country the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * This method returns the sales rep name
     *
     * @return name the sales rep name
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the sales rep name
     *
     * @param name the sales rep name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * this method returns the title
     *
     * @return title the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * this method sets the title
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This method gets Address line 1
     *
     * @return the addressline1
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * This method sets Address line 1
     *
     * @param addressLine1 the addressLIne1
     */
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }


    /**
     * This method returns city value
     *
     * @return city value
     */
    public String getCity() {
        return city;
    }

    /**
     * This methods sets the city value
     *
     * @param city the city value
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * This method gets the phone value
     *
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method sets the phone value
     *
     * @param phone the phone value
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * This method returns the email
     *
     * @return email the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method sets the email
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method gets Phone2
     *
     * @return phone1 the phone1
     */
    public String getPhone2() {
        return phone2;
    }

    /**
     * This methods sets the phone 2
     *
     * @param phone2 the phone2
     */
    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    /**
     * This method gets website
     *
     * @return website the website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * This method sets website variable
     *
     * @param website the website variable
     */
    public void setWebsite(String website) {
        this.website = website;
    }


    /**
     * This methid returns latitude
     *
     * @return latitude the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * This method sets latitude
     *
     * @param latitude the latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Thsi method returns longitude
     *
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * This method sets longitude
     *
     * @param longitude the longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}
