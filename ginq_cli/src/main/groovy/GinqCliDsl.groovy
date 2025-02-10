/**
 * Main DSL used for GINQ CLI files.
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

    public Integer[] randomArrayOf(Integer size, Integer min = 0, Integer max = 100) {
        def random = new Random()
        def result = new Integer[size]
        for (i in 0..<size) {
            result[i] = min + random.nextInt(max- min)
        }
        return result
    }

//    /** Forward the call to the GQ */
//    public def query(Closure<groovy.ginq.transform.GQ> closure) {
//        return GQ {
//            closure()
//        }
//    }
}

