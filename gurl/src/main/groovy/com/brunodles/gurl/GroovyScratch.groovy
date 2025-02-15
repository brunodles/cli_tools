package com.brunodles.gurl

// todo: extract into a proper test class
class GroovyScratch {

    static void main(String[] args) {
//        def builder = new YamlBuilder()
        def script = """
metadata {
    name "Postman Echo"
    description "Echo service"
    version "0.1.0"
    owner {
        name "Bruno Lima"
        email "brunodles"
    }
}

aliases {
    host "https://postman-echo.com"
}

request {
    url "{{host}}"
    path "post"  
    headers {
      "content-type" "application/json"
    }
    method "POST"
    
    "body:multipart_form" {
      artist "Skrillex" 
      song "First of the Year"
      album "Nice sprites and Scary Monsters"
      "this is the name" {
//        "Content-Type" "application/json"
//        "Content-Transfer-Encoding" "binary"
//        charset "UTF-8"
        filename "this-is.txt"
        content(
          "wow look at him!",
          "this content is Jason inside the multipart"          
        )
      }       
    }
    
    mock_response {
        body "wow, this will be crazy"
    }
    
//    test {
//      assert response.body == ""
//    }
}

""".stripIndent()

//        def yamlContent = new YamlSlurper().parseText(script)
//        if (ArrayList.isInstance(yamlContent)) {
//            for (content in yamlContent)
//                println content
//        } else {
//            println yamlContent
//        }
        def result = new ScriptEvaluator(script, new String[]{}).evaluate()
        println "Script result: $result"
    }
}
