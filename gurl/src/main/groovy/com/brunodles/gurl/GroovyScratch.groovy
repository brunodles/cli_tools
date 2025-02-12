package com.brunodles.gurl

import groovy.yaml.YamlBuilder
import groovy.yaml.YamlSlurper

// todo: extract into a proper test class
class GroovyScratch {

    static void main(String[] args) {
//        def builder = new YamlBuilder()
        def script = """
metadata {
    name "Hit simple file"
    description "Query a simple JSON file"
    version "0.1.0"
    owner {
        name "Bruno Lima"
        email "brunodles"
    }
}

aliases {
    host "raw.githubusercontent.com"
    "list-value" "as-another-random-thing", "something"
}

request {
    url "https://{{host}}/npm/init-package-json/refs/heads/main/package.json"  
    headers {
      "content-type" "application/json"
    }
    method: "GET"
}

""".stripIndent()

//        def yamlContent = new YamlSlurper().parseText(script)
//        if (ArrayList.isInstance(yamlContent)) {
//            for (content in yamlContent)
//                println content
//        } else {
//            println yamlContent
//        }
        def result = new ScriptEvaluator(script, new String[]{}).evaluate()
        println "Script result: $result"
    }
}
