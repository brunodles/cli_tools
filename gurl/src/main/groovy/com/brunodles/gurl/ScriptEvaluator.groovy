package com.brunodles.gurl

import org.codehaus.groovy.control.CompilerConfiguration

/**
 * Evaluate the Script, forwarding the call into the {@link GurlDsl} script
 *
 * Used to evaluate script files, following the rules and the functions of declared at {@link GurlDsl}
 * using {@link GroovyShell}.
 *
 * Used by the {@link GurlMainClass}.
 */
class ScriptEvaluator {
    /** content of the script */
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
        )
        config.scriptBaseClass = "com.brunodles.gurl.GurlDsl"

        GroovyShell shell = new GroovyShell(classLoader, binding, config)
        shell.setVariable("args", args)

        Script script = shell.parse(scriptContent)

        return script.run()
    }
}
