package com.brunodles.ginq_cli

import org.codehaus.groovy.control.CompilerConfiguration

/**
 * Evaluate the Script and return a TSV
 */
class ScriptEvaluator {
    /** content of the script containing the GQ*/
    final String scriptContent
    /** args used to initialize the script */
    final String[] args

    ScriptEvaluator(String scriptContent, String[] args) {
        this.scriptContent = scriptContent
        this.args = args
    }

    /**
     * Execute the script
     *
     * @return the last value returned by the script
     */
    def evaluate() {
        def binding = new Binding()
        def config = new CompilerConfiguration()
        def classLoader = new GroovyClassLoader(getClass().getClassLoader())
        classLoader.parseClass(
                """
                """.stripIndent()
        );
        config.scriptBaseClass = "com.brunodles.ginq_cli.GinqCliDsl"

        GroovyShell shell = new GroovyShell(classLoader, binding, config)
        shell.setVariable("args", MyProxy.create(args))

        Script script = shell.parse(scriptContent)

        return script.run()
    }
}
