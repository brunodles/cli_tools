package com.brunodles.gurl.specs;

import spock.lang.Specification;

import static org.junit.jupiter.api.Assertions.*;

class KeyValueBuilderTest extends Specification {
    def keyValueBuilder = new KeyValueBuilder()

    def "call with a single key-value, content should have the key-value"() {
        when:
        keyValueBuilder.call(key, value)

        then:
        def content = keyValueBuilder.content
        assert content.size() == 1
        assert content.containsKey(key)
        assert content.get(key) == value

        where:
        key              | value
        "name"           | "Bruno"
        "last name"      | "Lima"
        "something else" | "another long string that could be here"
    }

    def "multiple calls with a single key-value, content should have all the keys-values"() {
        def entries = [
                "host"         : "http://127.0.0.1:8080",
                "method"       : "GET",
                "path"         : "users/delete",
                "query-param-1": "username=bruno",
        ]

        when:
        entries.forEach { key, value ->
            keyValueBuilder.call(key, value)
        }

        then:
        def content = keyValueBuilder.content
        assert content.size() == entries.size()
        entries.forEach { key, value ->
            assert content.containsKey(key)
            assert content.get(key) == value
        }
    }

    def "call in a single closure, content should have the key-value"() {
        when:
        keyValueBuilder.with {
            myName "is Scrillex"
        }

        then:
        def content = keyValueBuilder.content
        assert content.size() == 1
        assert content.containsKey("myName")
        assert content.get("myName") == "is Scrillex"
    }

    def "multiple call in a single closure, content should have all the keys-values"() {
        def entries = [
                "key"          : "value",
                "another"      : "this value is different",
                "testing space": "is really interesting",
        ]

        when:
        keyValueBuilder.with {
            key "value"
            another "this value is different"
            "testing space" "is really interesting"
        }

        then:
        def content = keyValueBuilder.content
        assert content.size() == entries.size()
        entries.forEach { key, value ->
            assert content.containsKey(key)
            assert content.get(key) == value
        }
    }


    def "call of invokeMethod, content should have the key-value"() {
        when:
        keyValueBuilder."$key"(value)

        then:
        def content = keyValueBuilder.content
        assert content.size() == 1
        assert content.containsKey(key)
        assert content.get(key) == value

        where:
        key | value
        "1" | "1"
        "2" | "2"
        "3" | "fizz"
        "4" | "4"
        "5" | "buzz"
        "6" | "6"
    }

    def "call with string method name, content should have the key-value"() {
        def entries = [
                "drinks"     : "90",
                "meat"       : "200",
                "finger food": "50",
        ]

        when:
        entries.forEach { key, value ->
            keyValueBuilder."$key"(value)
        }

        then:
        def content = keyValueBuilder.content
        assert content.size() == entries.size()
        entries.forEach { key, value ->
            assert content.containsKey(key)
            assert content.get(key) == value
        }
    }

    def "call of invokeMethod with `null`, content should not be changed"() {
        given:
        def sample_key = "sample_key"

        when:
        keyValueBuilder.invokeMethod(sample_key, null)

        then:
        def content = keyValueBuilder.content
        assert content.size() == 0
        assert !content.containsKey(sample_key)
    }

    def "call of invokeMethod with empty collection, content should have the key"() {
        when:
        keyValueBuilder.invokeMethod(key, value)

        then:
        def content = keyValueBuilder.content
        assert content.size() == 1
        assert content.containsKey(key)

        where:
        key     | value
        "array" | new Object[0]
        "list"  | []
        "map"   | [:]
    }

    def "call of invokeMethod with empty list, content should have the key"() {
        when:
        keyValueBuilder.invokeMethod("something", [])

        then:
        def content = keyValueBuilder.content
        assert content.size() == 1
        assert content.containsKey("something")
    }

    def "call of invokeMethod with array, content should have the key-value"() {
        def entries = [
                "drinks"     : "90",
                "meat"       : "200",
                "finger_food": "50",
        ]

        when:
        entries.forEach { key, value ->
            keyValueBuilder.invokeMethod(key, new Object[]{value})
        }

        then:
        def content = keyValueBuilder.content
        assert content.size() == entries.size()
        entries.forEach { key, value ->
            assert content.containsKey(key)
            assert content.get(key) == value
        }
    }

    def "call of invokeMethod with values, content should contain the key-value"() {
        given:
        def sample_key = "sample_key"

        when:
        keyValueBuilder.invokeMethod(sample_key, null)

        then:
        def content = keyValueBuilder.content
        assert content.size() == 0
        assert !content.containsKey(sample_key)

        where:
        key          | value
        "name"       | "my-custom-faas"
        "server"     | "faas.homelab.myhouse"
        "port"       | 8080
        "function"   | "sync"
        "parameters" | "target=another_service"
    }
}
