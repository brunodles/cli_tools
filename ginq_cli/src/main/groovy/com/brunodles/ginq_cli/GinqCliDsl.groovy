package com.brunodles.ginq_cli

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.yaml.YamlBuilder
import groovy.yaml.YamlSlurper

import java.lang.reflect.Array

/**
 * Main DSL used for GINQ CLI files.
 *
 * This file just need to include how to include extra functions for the Script.
 * Parsing the directories and files into data for the GQ.*/
abstract class GinqCliDsl extends Script {

    boolean addMetaProperties = true

    /** Arguments passed into the script */
    String[] args = []

    /**
     * Access the content from pipe
     *
     * <p> Linux/unix use {@code Ctrl + D} to terminate
     * <p> Windows use {@code Ctrl + Z} or {@code F6} to terminate*/
    @Lazy(soft = true)
    String pipe = sysIn
    @Lazy
    String sysIn = { System.in.text }()

    /**
     * Prints the value and a newline to the current 'out' variable which should be a PrintWriter
     * or at least have a println() method defined on it.
     * If there is no 'out' property then print to standard out.*/
    def sout(Object text) {
        println text
    }

    /**
     * Read file text
     * @param path path to the file
     * @return a single String containing the file content
     */
    def textFile(String path) {
        return new File(path).text
    }

    /**
     * Use to read a single TSV File.
     *
     * <p>The result structure is similar to a JSON/YAML.
     *
     * <p>The input file should have the first line as header.
     *
     * @param path the path of the tsv file
     * @return a List of a List of String representing every row and every cell of the file
     */
    // Just a alias
    def tsvFile(String path) {
        return csvFile(path, "\t")
    }

    /**
     * Use to read a single CSV File.
     *
     * <p>The result structure is similar to a JSON/YAML.
     *
     * <p>The input file should have the first line as header.
     *
     * @param path the path of the csv file
     * @return a List of a List of String representing every row and every cell of the file
     */
    def csvFile(String path, String separator = ",") {
        return csv(textFile(path), separator)
    }

    /**
     * Use to read a single CSV File.
     *
     * <p>The result structure is similar to a JSON/YAML.
     *
     * <p>The input file should have the first line as header.
     *
     * @param path the path of the csv file
     * @return a List of a List of String representing every row and every cell of the file
     */
    def csv(String textContent, String separator = ",") {
        def lines = textContent
                .split("\n")

        def header = lines
                .first()
                .split(separator)
                .collect { it.trim() }

        def content = lines
                .drop(1)
//            .toList()    // required to use withIndex
//            .withIndex() // also not good as it also creates a new list
                .collect { line ->
                    def item = [:]
                    int index = 0
                    line.split(separator)
                            .collect { value -> item[header[index++]] = value.trim()
                            }
                    item
                }

        return content
    }

    String tsv(Object object) {
        return csv(object, "\t")
    }

    String csv(Object object, String separator = ",") {
        try {
            List<String> headers = object.collect { item -> item.keySet()
            }.flatten().unique()
            def encode = { value ->
                if (value == null)
                    return ''
                else if ((value instanceof String) && value.contains(separator))
                    return "\"$value\""
                else
                    return value
            }

            def builder = new StringBuilder()

            builder.append(headers.collect { encode(it) }.join(separator))
                    .append("\n")


            builder.append(object.collect { item ->
                headers.collect { headerName -> encode(item[headerName])
                }.join(separator)
            }.join("\n"))
            return builder.toString()
        } catch (Exception e) {
        }
        return "Unknown type"
    }

    /**
     * Read a directory for all json files inside it
     *
     * <p>This functions will use the file names as keys, mapping the result into a dictionary/map.
     *
     * <p>Tip: Use {@code jsonDir(...).values()} to drop the keys and use it as array.
     *
     * @param path pointing to a directory
     * @return an array of JSON
     */
    def jsonDir(String path) {
        def dir = new File(path)
        if (dir.isDirectory()) {
            FileFilter fileFilter = { file -> file.isFile() && file.path.endsWith(".json") }
            return dir.listFiles(fileFilter)
                    .collectEntries { file -> [(file.name): jsonFile(file.path)] }
        } else {
            return [jsonFile(path)]
        }
    }

    /**
     * Read a single json file
     * @param path pointing into a file
     * @return an object that allows to access the content of the file
     */
    def jsonFile(String path) {
        return json(textFile(path))
    }

    /**
     * Read a single json
     * @param text text content of the json
     * @return an object that allows to access the content of the file
     */
    def json(String text) {
        def readObject = new JsonSlurper().parseText(text)
        def result = MyProxy.create(readObject)
        if (addMetaProperties) {
            result.putExtraProperty("__sourceType", "json")
            result.putExtraProperty("__sourceFile", file)
        }
        return result
    }

    /**
     * Parse object to JSON string
     * @param object to be converted into JSON
     * @return a string of JSON
     */
    String json(Object object) {
        return new JsonBuilder(object).toString()
    }

    /**
     * Read a directory for all yaml files inside it
     *
     * <p>This functions will use the file names as keys, mapping the result into a dictionary/map.
     *
     * <p>Tip: Use {@code yaml(...).values()} to drop the keys and use it as array.
     *
     * @param path pointing to a directory
     * @return an map where key is the filename and the value yaml
     */
    def yamlDir(String path) {
        def dir = new File(path)
        if (dir.isDirectory()) {
            FileFilter fileFilter = { file -> file.isFile() && file.path.endsWith(".yaml") }
            return dir.listFiles(fileFilter)
                    .collectEntries { file -> [(file.name): jsonFile(file.path)] }
        } else {
            return [jsonFile(path)]
        }
    }

    /**
     * Read a single yaml file
     * @param path pointing into a file
     * @return an object that allows to access the content of the file
     */
    def yamlFile(String path) {
        return yaml(textFile(path))
        def file = new File(path)
        def readObject = new YamlSlurper().parse(file)
        def result = MyProxy.create(readObject)
        if (addMetaProperties) {
            result.putExtraProperty("__sourceType", "json")
            result.putExtraProperty("__sourceFile", file)
        }
        return result
    }

    /**
     * Read a yaml text
     * @param text of the yaml
     * @return an object that allows to access the content of the file
     */
    def yaml(String text) {
        def readObject = new YamlSlurper().parseText(text)
        def result = MyProxy.create(readObject)
        if (addMetaProperties) {
            result.putExtraProperty("__sourceType", "json")
            result.putExtraProperty("__sourceFile", file)
        }
        return result
    }

    /**
     * Parse object to YAML string
     * @param object to be converted into YAML
     * @return a string of YAML
     */
    String yaml(Object object) {
        def builder = new YamlBuilder()
        builder.call(object)
        return builder.toString()
    }

    /** Generate random array of integer */
    Integer[] randomArrayOf(Integer size, Integer min = 0, Integer max = 100) {
        def random = new Random()
        def result = new Integer[size]
        for (i in 0..<size) {
            result[i] = min + random.nextInt(max - min)
        }
        return result
    }
}
