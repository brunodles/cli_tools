#!ginq_cli
# to execute this file, the `ginq_cli` need to be in the `PATH`.
# Check the `install.sh` file at the project root dir

def data = (pipe.split(" ").collect { it.toInteger() })
#println "Input data= $data"

sout "number"

/** See GQ docs https://groovy-lang.org/using-ginq.html */
GQ {
    from n in data
#    where n > 20 && n < 50
    orderby n
    select n
}.stream()
  .forEach { sout it }
