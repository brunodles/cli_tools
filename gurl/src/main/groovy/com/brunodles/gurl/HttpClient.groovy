package com.brunodles.gurl

/**
 * Wrapper for the HttpClient
 *
 * <p>Used to map the {@code RequestModel} that uses the {@code RequestSchema} into a real http request.
 */
class HttpClient {

    /** Execute the request */
    @SuppressWarnings('GrMethodMayBeStatic')
    def execute(def requestModel) {
        def connection = requestModel.url.toURL().openConnection() as HttpURLConnection
        connection.with {
            setRequestProperty("User-Agent", "GURL")
            setRequestMethod(requestModel.method ?: "GET")
        }
        def responseCode = connection.responseCode
        def response = connection.inputStream.text
        println "responseCode: $responseCode"
        println response
    }
}
