package com.brunodles.gurl

import org.apache.groovy.ginq.provider.collection.runtime.NamedRecord

class GurlMainClass {

    GurlMainClass() {
    }

    /**
     * Run the script
     * @param args
     *
     * * Arg 0 = path to the script
     * * Arg 1..* = environment variables
     */
    static void main(String[] args) {

        def scriptArgs = args.drop(1)

        def file = new File(args.first())
        def scriptContent = file
                .readLines()
                .collect { (it.startsWith("#") ? "//$it" : it) }
                .join("\n")

        def scriptResult = new ScriptEvaluator(scriptContent, scriptArgs).evaluate()

        switch (scriptResult.getClass()) {
            case List.class:
                buildListResult((List) scriptResult)
                break
//            case String.class:
            default:
                println scriptResult
                break
        }
    }

    static void buildListResult(List scriptResult) {
        List<NamedRecord> resultList = scriptResult

        if (resultList.isEmpty()) {
            println "Empty. No data available."
            return
        }

        def resultBuilder = new StringBuilder()
        resultList.forEach { record ->
            resultBuilder.append(record.join("\t"))
                    .append("\n")
        }

        println resultBuilder.toString()
    }
}
