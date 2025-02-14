package com.brunodles.gurl.specs

import groovy.json.JsonBuilder

/**
 * A builder for creating Maps
 *
 * Even if we can create maps in Groovy using {@code [:]},
 * it breaks the flow of declaration used by invoking closures and setting fields.
 * Because of that the {@link KeyValueBuilder} will provide the same syntax for creating maps.
 *
 * Inspired by {@link groovy.json.JsonBuilder}, which also allows to set maps in using the same syntax.
 * The key difference is that the {@link groovy.json.JsonBuilder} also allows nested maps,
 * which is a characteristic that is not presented in the {@link KeyValueBuilder}.
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
