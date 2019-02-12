/**
 * Util to handle ServiceRequests
 */

/**
 * makeRequest
 * Makes a service request, expecting JSON as the content of the response
 *
 * @param { string } uri The URI of the
 * @param { string } options.method  The method of the request (GET, POST, PUT, DELETE)
 * @param { object } [params] Optional - any params you want to append to the URI
 * @param { object } [options]  Optional - the additional options for the request.
 * @param { string } [options.headers] Any headers you want to add to the request
 * @param { string } [options.body]  Any body you want to add to the request (no body for GET)
 * @param { string } [options.mode]  Request mode (no-cors, cors, *same-origin)
 * can also add to the options obj any other options found here:
 * https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/fetch#Parameters
 * @return { promise } The promise that resolves with the data
 */
export function makeRequest(uri, method, options = {}) {
    const optionsWithMethod = Object.assign({
        method,
        mode: 'cors',
        headers: { 'Accept': 'application/json' }
    }, options);
    const request = new Request(uri, optionsWithMethod);
    return fetch(request)
         .then(function (response) {
            const contentType = response.headers.get("content-type");
            if(response.ok) {
                if (contentType && contentType.includes("application/json")) {
                    return response.json();
                }
                throw new TypeError("Content of response is not JSON");
            }
            throw new Error("Service not found");
        }).catch(error => {
            console.log(error);
            return error;
        });
}