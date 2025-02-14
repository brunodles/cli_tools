package com.brunodles.gurl

import jdk.internal.misc.Unsafe

import java.lang.reflect.Field

class GurlMainClass {

    GurlMainClass() {
    }

    /**
     * Run the script
     * @param args for the script
     * <p>* Arg 0 = path to the script
     * <p>* Arg 1..* = environment variables
     */
    static void main(String[] args) {
        disableStdError()
//        resetInternalLogger()

        def scriptArgs = args.drop(1)

        def file = new File(args.first())
        def scriptContent = file
                .readLines()
                .collect { (it.startsWith("#") ? "//$it" : it) }
                .join("\n")

        def scriptResult = new ScriptEvaluator(scriptContent, scriptArgs).evaluate()
        if (scriptResult != null)
            println scriptResult
    }

    /**
     * Disable StdErr to prevent warnings
     *
     * <p>Source: <a href="https://stackoverflow.com/questions/46454995/how-to-hide-warning-illegal-reflective-access-in-java-9-without-jvm-argument">apangin at StackOverflow thread<a>
     */
    private static void disableStdError() {
        System.err.close();
        System.setErr(System.out);
    }

    /**
     * Reset {@link jdk.internal.module.IllegalAccessLogger} internal Logger
     *
     * <p>Source: <a href="https://stackoverflow.com/questions/46454995/how-to-hide-warning-illegal-reflective-access-in-java-9-without-jvm-argument">apangin at StackOverflow thread<a>
     */
    private static void resetInternalLogger() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe u = (Unsafe) theUnsafe.get(null);

            Class cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
        } catch (Exception e) {
            // ignore
        }
    }

}
