#!/Users/bruno.lima/workspace/cli_tools/ginq_cli/build/install/ginq_cli/bin/ginq_cli
# to install: Add the bash executable script in the path, so you can replace the relative path above
# to execute: run `gradle ginq_cli:install` and then execute this file

/** See GQ docs https://groovy-lang.org/using-ginq.html */
GQ {
    from n in randomArrayOf(10, 10, 90)
    where n > 20 && n < 50
    orderby n
    select n
}
