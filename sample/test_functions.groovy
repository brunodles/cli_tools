/** Generate random array of integer */

static Integer[] randomArrayOf(Integer size, Integer min = 0, Integer max = 100) {
    def random = new Random()
    def result = new Integer[size]
    for (i in 0..<size) {
        result[i] = min + random.nextInt(max- min)
    }
    return result
}
