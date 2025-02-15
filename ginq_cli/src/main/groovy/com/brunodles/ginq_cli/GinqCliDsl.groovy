package com.brunodles.ginq_cli

/**
 * Main DSL used for GINQ CLI files.
 *
 * This file just need to include how to include extra functions for the Script.
 * Parsing the directories and files into data for the GQ.
 */
abstract class GinqCliDsl extends Script {

    /**
     * Use to read a single CSV File
     *
     * @return a List of a List of String representing every row and every cell of the file
     */
    public List<List<String>> csvFile(String path) {
        println path
        return Arrays.asList()
    }

    /** Generate random array of integer */
    public Integer[] randomArrayOf(Integer size, Integer min = 0, Integer max = 100) {
        def random = new Random()
        def result = new Integer[size]
        for (i in 0..<size) {
            result[i] = min + random.nextInt(max- min)
        }
        return result
    }
}

