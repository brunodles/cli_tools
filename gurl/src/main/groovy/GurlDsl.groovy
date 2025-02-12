import com.brunodles.gurl.specs.KeyValueBuilder
import groovy.json.JsonBuilder

/**
 * Main DSL for GURL
 */
abstract class GurlDsl extends Script {
    /** Shared metadata */
    private def metadataBuilder = new JsonBuilder()
    /** common variables in case the user run this without any external properties */
    private def aliasesBuilder = new KeyValueBuilder()

    def metadata(@DelegatesTo(JsonBuilder) Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        metadataBuilder.call(closure)
        println "Metadata = ${metadataBuilder.toString()}"
    }

    def aliases(@DelegatesTo(KeyValueBuilder) Closure closure) {
        def code = closure.rehydrate(aliasesBuilder, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }

//    def request(@DelegatesTo(RequestSpec) Closure closure) {
//        def spec = new RequestSpec(aliasesBuilder.content())
//        def code = closure.rehydrate(spec, this, this)
//        code.resolveStrategy = Closure.DELEGATE_ONLY
//        code()
//    }

    def request(@DelegatesTo(JsonBuilder) Closure closure) {
        def builder = new JsonBuilder()
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        builder.call(closure)
        def content = builder.content
//        println content.url
        def connection = replaceVars(content.url).toURL().openConnection() as HttpURLConnection
        connection.with {
            setRequestProperty("User-Agent", "GURL")
            setRequestMethod(content.method ?: "GET")
        }
        def responseCode = connection.responseCode
        def response = connection.inputStream.text
        println "responseCode: $responseCode"
        println response
    }

    /** Get a environment variable from system */
    static def env(String name) {
        return System.getenv(name)
    }

    private String replaceVars(String original) {
        String result = original
        aliasesBuilder.content.forEach { key, value ->
            result = result.replaceAll("\\{\\{$key\\}\\}", value)
        }
//            def variablePattern = Pattern.compile(/\{\{(.+?)}}/)
//            def matcher = variablePattern.matcher(original)
//            println matcher.count
//            while (matcher.find()) {
//                def variableDeclaration = matcher.group(0)
//                def variableName = matcher.group(1)
//                println "found: $variableDeclaration = $variableName"
//                result = result.replace(variableDeclaration, aliases[variableName])
//            }
        return result
    }

    class RequestSpec extends GroovyObjectSupport {

        String url
        private Map<String,String> headers = [:]
        private final Map<String,String> aliases

        RequestSpec(Map<String, String> aliases = [:]) {
            this.aliases = aliases
        }

        def headers(@DelegatesTo(KeyValueBuilder) Closure closure) {
            def values = new KeyValueBuilder()
            def code = closure.rehydrate(values, this, this)
            code.resolveStrategy = Closure.DELEGATE_ONLY
            code()
            this.headers = values.content()
        }

        def get() {
            println "GET ${replaceVars(url)} "
        }


        Object invokeMethod(String name, Object args) {
            if (args == null)
                return null

            if (Object[].class.isAssignableFrom(args.getClass())) {
                Object[] arr = (Object[]) args
                if (arr.length == 0) {
//                    map[name] = ""
                } else if (arr.length == 1) {
                    if (this.properties.containsKey(name)) {
                        this.setProperty(name, arr[0])
                    }
//                    map[name] = arr[0].toString()
                } else {
//                    map[name] = args.toString()
                }
            }
            return null
        }

        def methodMissing(String name, def args) {
            if (this.properties.containsKey(name))
                invokeMethod(name, args)
            else
                super.methodMissing(name, args)
        }
    }
}
