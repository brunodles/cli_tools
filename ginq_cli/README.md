# GINQ CLI
This is a sql-like querying data files using [GINQ](https://groovy-lang.org/using-ginq.html) or GQ, Groovy Integrated Query.

This project does not introduce any new query function for GINQ. The main objectives are:
1. Add custom functions for query files, so basically providing the collections into the GINQ
2. Create a extension for ginq queries it can be executed from terminal

With the objectives above, this project will allow users to query data in file stores.

## Context
I implement my own interpreter for the [file_query] using the groovy, then I discovered that the language itself has 
a library that is a lot better.
Because of that, I just decided to use it and adjust the points that were missing for my own usage. 
Initially it would also include the [table_builder].

This module is a Groovy-only, it is better for dealing with dynamic runtime and parsing multiple formats. 
Groovy works really well with dynamic properties.

# Next steps
Just ideas, there is no guarantee that I will include these options
* [ ] Support other input formats
  * [x] YAML
  * [x] CSV
  * [x] TSV
  * [ ] [maybe] XML
* [ ] [maybe] Pass the fatjar through proguard. _Not sure if it would relly work, based on runtime/reflection properties of Groovy_
* [ ] Create a way to "import" other script file. It will allow users to share the extra functions on a separate file.
* [ ] Simplify the `database` function to accept parameters
* [ ] Improve the query debugging
  * [ ] Find a way to merge imports without concatenation, because it affects the query debugging
  * [ ] Do not remove commented lines(`//`, `#`), just clean them up, but do not skip them
