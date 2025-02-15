package com.brunodles.gurl

import com.brunodles.gurl.specs.KeyValueBuilder
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

/**
 * Main DSL for GURL
 *
 * Defines the main methods exposed for the users of gurl.
 *
 * Public methods here are available to be used as function,
 * most of them are just wrappers that delegates the execution into another object.
 * This is interesting as each method might have it's own DSL,
 * see {@link #aliases} that allows to create custom maps,
 * and {@link #metadata} that uses {@link JsonBuilder} for creating the RequestJson Schema.
 */
abstract class GurlDsl extends Script {
    /** Shared file metadata */
    private def metadataBuilder = new JsonBuilder()
    /** Common variables in case the user run this without any external properties */
    private def aliasesBuilder = new KeyValueBuilder()
    /** Client */
    private def httpClient = new HttpClientGateway()

    /** Create the metadata section, which is a json builder allowing a tree structure */
    def metadata(@DelegatesTo(JsonBuilder) Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        metadataBuilder.call(closure)
        println "Metadata = ${metadataBuilder.toString()}"
    }

    /** Create the key-value map that will be used for allowing aliases */
    def aliases(@DelegatesTo(KeyValueBuilder) Closure closure) {
        def code = closure.rehydrate(aliasesBuilder, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }

    /** Create the request json that should follow the Request Schema */
    def request(@DelegatesTo(JsonBuilder) Closure closure) {
        def builder = new JsonBuilder()
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        builder.call(closure)
//        def content = builder.content
//        println content.url
        // code to json -> replace strings -> from json
        def content = new JsonSlurper().parseText(replaceVars(builder.toString()))

        def result = httpClient.execute(content)
        println result.responseCode
        println result.resultStream.text
    }

    /** Get a environment variable from system */
    static def env(String name) {
        return System.getenv(name)
    }

    private String replaceVars(String original) {
        String result = original
        aliasesBuilder.content.forEach { key, value ->
            result = result.replaceAll("\\{\\{$key\\}\\}", value)
        }
        return result
    }
}
