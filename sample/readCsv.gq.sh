#!ginq_cli
# to execute this file, the `ginq_cli` need to be in the `PATH`.
# Check the `install.sh` file at the project root dir

/** See GQ docs https://groovy-lang.org/using-ginq.html */
sout csv(
  GQ {
      from user in tsvFile("./username.csv")
      where user.Username.startsWith("j")
      orderby user.Username
      select user
  }.toList()
)