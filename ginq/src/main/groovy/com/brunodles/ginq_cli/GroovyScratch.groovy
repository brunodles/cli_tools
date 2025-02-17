package com.brunodles.ginq_cli

// todo: extract into a proper test class
class GroovyScratch {

    static void main(String[] args) {
        def script = """
                    GQ {
                        from n in randomArrayOf(10, 10, 90)
                        where n > 20 && n < 50
                        orderby n
                        select n
                    }
                """.stripIndent()

        def result = new ScriptEvaluator(script, new String[]{}).evaluate()

        println result
    }
}
