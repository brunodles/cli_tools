package com.brunodles.gurl

import groovy.json.JsonBuilder

import static java.net.URLEncoder.encode

/**
 * Wrapper for the HttpClient
 *
 * <p>Used to map the {@code RequestModel} that uses the {@code RequestSchema} into a real http request.
 * <p><a href="https://github.com/brunodles/SlackUpload/blob/master/src/main/java/com/brunodles/util/WebClient.java">Multi-Part example brunodles/slackupload</a>
 * <p><a href="https://blog.morizyun.com/blog/android-httpurlconnection-post-multipart/index.html">Multi-Part example from blog.morizyun.com</a>
 * <p><a href="https://github.com/janbodnar/Groovy-Examples/blob/main/httpclient.md">Checkout Groovy-Examples</p>
 */
class HttpClientGateway {

    private static final String HEADER_USER_AGENT = "User-Agent"
    private static final String UTF8 = "utf8"
    private static final String HEADER_CONTENT_TYPE = "Content-Type"
    private static final String CONTENT_TYPE_FORM_URL_ENCODED = "application/x-www-form-urlencoded"
    private static final String CONTENT_TYPE_JSON = "application/json"
    private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain"
    private static final String CONTENT_TYPE_MULTIPART = "multipart/form-data;boundary=$BOUNDARY"

    private static final String CRLF = "\r\n"
    private static final String TWOHYPHENS = "--"
    private static final String BOUNDARY = "*****"

    // todo: should i use httpclient?Ø
    // HttpClient.newHttpClient()
    /** Execute the request */
    @SuppressWarnings('GrMethodMayBeStatic')
    def execute(Object requestModel) {
        println requestModel
        def finalUrl = buildFinalUrl(requestModel).toURL()
        def connection = finalUrl.openConnection() as HttpURLConnection
        connection.with {
            setRequestProperty(HEADER_USER_AGENT, requestModel[HEADER_USER_AGENT] ?: "GURL")
            (requestModel.headers as Map<String, String>)?.forEach { key, value ->
                setRequestProperty(key, value)
            }
            setRequestMethod(requestModel.method ?: "GET")
        }
        connection.setDoInput(true)
        doOutputIfNeeded(requestModel, connection)

        return [
                responseCode: connection.responseCode,
                headers: connection.getHeaderFields(),
                resultStream: connection.inputStream
        ]
    }

    private static String buildFinalUrl(requestModel) {
        String result = requestModel.url
        if (requestModel.path != null) {
            result += "/${requestModel.path}"
        }
        if (requestModel.query_params != null) {
            result += '?' + urlEncodeKeyValue(requestModel.query_params as Map<String, String>)
        }
        println "finalUrl $result"
        return result
    }

    private static void doOutputIfNeeded(Object requestModel, HttpURLConnection connection) {
        def bodyPropertyName = requestModel.keySet()?.find { it.startsWith("body") }
        println "bodyPropertyName: $bodyPropertyName"
        if (bodyPropertyName == null) return

        String bodyContent = switch (bodyPropertyName) {
            case ("body") -> requestModel["body"].toString()
            case ("body:text") -> requestModel["body:text"].toString()
            case ("body:data") -> urlEncodeKeyValue(requestModel["body:data"])
            case ("body:json") -> {
                connection.setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON)
                new JsonBuilder(requestModel["body:json"]).toString()
            }
            case ("body:form_urlencoded") -> {
                connection.setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE_FORM_URL_ENCODED)
                urlEncodeKeyValue(requestModel["body:form_urlencoded"])
            }
            case ("body:multipart_form") -> {
                connection.setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE_MULTIPART)
                connection.setRequestProperty("Connection", "Keep-Alive")
                connection.setRequestProperty("Cache-Control", "no-cache")
                String result = ""
                requestModel["body:multipart_form"].forEach { name, value ->
                    if (value.class in [String, Integer, Long, Float, Double]) {
                        result += "${TWOHYPHENS}${BOUNDARY}${CRLF}" +
                                "Content-Disposition: form-data; name=\"$name\"${CRLF}" +
                                "Content-Type: text/plain; charset=UTF-8${CRLF}" +
                                "${CRLF}${value}${CRLF}"
                    } else { //  if (value.class == Map)
                        try {
                            def contentType = value["Content-Type"] ?: CONTENT_TYPE_TEXT_PLAIN
                            def charset = value.charset ?: "UTF-8"
                            String content = switch (contentType) {
                                case CONTENT_TYPE_JSON -> new JsonBuilder(value.content ?: value).toString()
                                case CONTENT_TYPE_TEXT_PLAIN -> (value.content ?: value).join("\n")
                                default -> (value.content ?: value).toString()
                            }

                            result += "${TWOHYPHENS}${BOUNDARY}${CRLF}"
                            if (value.filename)
                                result += "Content-Disposition: form-data; name=\"$name\"; filename=\"${value.filename}\"${CRLF}"
                            else
                                result += "Content-Disposition: form-data; name=\"$name\"${CRLF}"
                            result += "Content-Type: ${contentType}; charset=${charset}${CRLF}"
                            if (value["Content-Transfer-Encoding"] != null)
                                result += "Content-Transfer-Encoding: ${value["Content-Transfer-Encoding"]}${CRLF}"
                            result += "${CRLF}${content}${CRLF}"
                        } catch (Throwable e) {
                            println "Failed to parse multipart field (\"$name\"). Cause: ${e.message}."
                        }
                    }
                }
                result += "${TWOHYPHENS}${BOUNDARY}${TWOHYPHENS}${CRLF}"
                result
            }
            default -> null
        }
        println "bodyContent: $bodyContent"
        if (bodyContent == null) return

        connection.doOutput = true
        def outputStream = new DataOutputStream(connection.outputStream)
        outputStream.writeBytes(bodyContent)
        outputStream.flush()
        outputStream.close()
    }

    private static String urlEncodeKeyValue(Map<String, String> map) {
        return map
                .collect { key, value -> encode(key, UTF8) + '=' + encode(value, UTF8) }
                .join('&')
    }
}
