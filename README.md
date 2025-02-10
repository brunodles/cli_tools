# CLI Tools
A collection of CLI tools write in JVM Languages (Java, Groovy and Kotlin).
The modules here can be used as both CLI and libraries for other projects.

## Table Builder
Utility to create monospace tables using kotlin.
This could help to create tables for terminal application
Can also format then into other formats like MarkDown and Jira.
[More](table_builder)

# Future ideas
* [ ] Create the CLI for the Table Builder
* [ ] Create a shell interpreter for [GINQ](https://groovy-lang.org/using-ginq.html), so it can be used from CLI. Move content from [Query Interpreter](https://github.com/brunodles/AndroidShellUtils/tree/main/query_interpreter) 
* [ ] Create the GURL. A declarative shell interpreter for creating http requests

Combining all those tree would be possible to create interesting CLI and scripts.
Imagine the following:
1. Request something with gurl -> query the content in SQL like -> format as table
2. query content in multiple files -> format into jira -> copy into clipboard
