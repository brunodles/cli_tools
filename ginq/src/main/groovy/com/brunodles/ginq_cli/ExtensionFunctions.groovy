package com.brunodles.ginq_cli

import groovy.json.JsonBuilder
import groovy.yaml.YamlBuilder

class ExtensionFunctions {

    private ExtensionFunctions() {
    }

    static void registerCustomExtensionFunctions() {
        Collection.metaClass.random = { ->
            def random = new Random()
            return delegate.get(random.nextInt(delegate.size()))
        }
        Collection.metaClass.toJson = { ->
            return new JsonBuilder(delegate).toString()
        }
        Collection.metaClass.toYaml = { ->
            def yamlBuilder = new YamlBuilder()
            yamlBuilder.call(delegate)
            return yamlBuilder.toString()
        }
    }

    static <T> T first(Closure<T>... closures) {
        for (final def closure in closures) {
            def result = tryOrNull { closure() }
            if (result != null) {
                return result
            }
        }
        return null
    }

    static <T> T tryOrNull(Closure<T> closure) {
        try {
            return closure()
        } catch (Throwable ignore) {
        }
        return null
    }
}
