# Monospace Table Builder
Utility to create monospace tables using kotlin.
This could help to create tables for terminal application
Can also format then into other formats like MarkDown and Jira.

## Usage
```kotlin
val table = TableBuilder(FormatDefault.simple) // or FormatDefault.markdown 
    .columns {
        add("col1", ColumnDirection.left)
        add("col2", ColumnDirection.center)
        add("col3", ColumnDirection.right)
    }
    .newRow {
        add("r1")
        add("r2")
        add("r3")
    }
    .newRow {
        add("r1")
        add("r2")
        add("r3")
    }
    .build()
```

Simple Result
```
col1 | col2 | col3
---- | ---- | ----
r1   |  r2  |   r3
r1   |  r2  |   r3
```

MarkDown
```
| col1 |  col2 | col3 |
| :--- | :---: | ---: |
| r1   |   r2  |   r3 |
| r1   |   r2  |   r3 |
```

## Next Steps
* [ ] Parameterized version of the existing formats
* [ ] Create computation/aggregation cells
* [ ] Create formula cells?
* [ ] Create a terminal CLI that could receive input from Pipe `|`

## How to install?
Download the source code and install on maven local.
```
# gradle publishToMavenLocal
```

### Will it be published?
I have no time to dig into it right now.
It may work with [jitpack](https://jitpack.io/).


## Where does this idea come from?
I always kept myself running some scripts to format some files or content for presenting for other engineers.
On chat, slack or even other tools like Jira.
I also get information from spreadsheets/csv/tsv.
Because of that I wrote this to format some of those contents.
Another use-case was for creating scripts that could present something in terminal.

### History
1. This is a scrap code
2. Commited into personal project
3. Got moved into [Android Shell Utils](https://github.com/brunodles/AndroidShellUtils) / [Table Builder](https://github.com/brunodles/AndroidShellUtils/tree/main/table_builder)
4. Now it is here
