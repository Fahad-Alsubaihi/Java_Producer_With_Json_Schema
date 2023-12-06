# Java_Producer_With_Json_Schema
Java Producer sends simple data [ id, name, email] to the Kafka Topic using JSON Schema type

## get Started 
navigate to the config file at the below path :
```
src/main/resources/config.properties
```
change the properties as per your environment

## Run the project

from the main class run the project

### If you want to add more sample records 

navigate to the below path and add more records to the file
```
src/main/java/org/example/sample.txt
```
like this 
```
{"name": "John Doe", "id": 30,"email": "John@example.com"}
{"name": "Jane Smith", "id": 25,"email": "jane.smith@example.com"}
{"name": "Fahad", "id": 25,"email": "fahad@example.com"}
```