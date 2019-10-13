# URL Shortner App

### Prerequisites

* A local Redis instance instance should be up and running before running our service.

### Guides
The following guides illustrate how to use some features concretely:

* Import the project in IDE of choice 
* Build the project with mvn clean package

To test, send POST Request to ```http://localhost:8080/v1/urlconverter/shorturl/``` with a body of type application/json with body { 'url' : '' }
  
Other API's supported - ```http://localhost:8080/v1/urlconverter/longurl/``` 
with body - { 'url' : '' } 

&&
 
 ```http://localhost:8080/v1/urlconverter/shorturlcount/```
  with body - { "url":"www.tinyurl.com/e38fb238", "t1":"0", "t2":"1570952655364"}
