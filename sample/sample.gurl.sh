#!gurl
# to execute this file, the `gurl` need to be in the `PATH`.
# Check the `install.sh` file at the project root dir

// This metadata can be decoded but it is not used
metadata {
    name "Hit simple file"
    description "Query a simple JSON file"
    version "0.1.0"
    owner {
        name "Bruno Lima"
        email "brunodles"
    }
}

aliases {
    host "raw.githubusercontent.com"
    "list-value" "as-another-random-thing", "something"
}

request {
    url "https://{{host}}/npm/init-package-json/refs/heads/main/package.json"
    headers {
      "content-type" "application/json"
    }
    method "GET"

    mock_response {
        body "wow, this will be crazy"
    }
}
