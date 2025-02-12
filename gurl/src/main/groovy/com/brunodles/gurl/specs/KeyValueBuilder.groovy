package com.brunodles.gurl.specs

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

    Object invokeMethod(String name, Object args) {
        if (args == null)
            return null

        if (Object[].class.isAssignableFrom(args.getClass())) {
            Object[] arr = (Object[]) args
            if (arr.length == 0) {
                content[name] = ""
            } else if (arr.length == 1) {
                content[name] = arr[0].toString()
            } else {
                content[name] = args.toString()
            }
        }
        return null
    }
}
