package com.brunodles.gurl.specs

import groovy.json.JsonBuilder

/**
 * A builder for creating Maps
 *
 * Inspired by {@code groovy.json.JsonBuilder}
 */
class KeyValueBuilder extends GroovyObjectSupport {
    private Map<String, String> content = [:]

    public Map<String, String> getContent() {
        return Collections.unmodifiableMap(content)
    }

    def call(String key, String value) {
        content[key] = value
    }

    def call(Closure c) {
        c.delegate = this
        c.resolveStrategy = Closure.DELEGATE_ONLY
        c.call()
    }

    Object invokeMethod(String name, Object args) {
        if (args == null)
            return null

        if (Collection.isInstance(args)) {
            def arr = (Collection) args
            dealWithCollection(name, args, arr)
        } else if (Object[].class.isAssignableFrom(args.getClass())) {
            Object[] arr = (Object[]) args
            dealWithCollection(name, args, arr)
        } else if (Map.isInstance(args)) {
            content[name] = new JsonBuilder(args).toString()
        } else {
            //todo: add a custom logger?
            println "Type not found: $name ${args}"
            content[name] = args.toString()
        }
        return null
    }

    private def dealWithCollection(String name, Object args, def arr) {
        content[name] = switch (arr.length) {
            case (0) -> ""
            case (1) -> arr[0].toString()
            default -> args.toString()
        }
    }
}
