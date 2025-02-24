package com.brunodles.ginq_cli


import static com.brunodles.ginq_cli.ExtensionFunctions.registerCustomExtensionFunctions

class CliMainClass {

    CliMainClass() {
    }

    /**
     * Run the script
     * @param args
     *
     * * Arg 0 = path to the script
     * * Arg 1..* = environment variables
     */
    static void main(String[] args) {
        registerCustomExtensionFunctions()

        try {
            def scriptArgs = args.drop(1)

            def file = new File(args.first())
            def scriptContent = file
                    .readLines()
                    .collect { (it.startsWith("#") ? "//$it" : it) }
                    .join("\n")

            def scriptEvaluator = new ScriptEvaluator(scriptContent, scriptArgs)
            def result = scriptEvaluator.evaluate()
            boolean shouldPrintResult = args.contains("-p")
            if (shouldPrintResult)
                println result
        } catch (Throwable t) {
            println "Error at ${t.cause} - ${t.message}."
            t.printStackTrace(System.out)
        }
    }
}
