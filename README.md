# Mashup

Preliminary requirements
* Maven, you can get it here : https://maven.apache.org/download.cgi
* java 11, free version here : https://openjdk.java.net/ 

How to start application

1) (Optional) Specify port to use in "src/main/resources/application.yml", default is 8112.
2) Run mvn clean install in root directory
3) From root directory run "java -jar target\mashup-0.0.1-SNAPSHOT.jar"

When started you should be able to navigate to the swagger link provided by the console log, or use this default link : http://localhost:8112/swagger-ui.html

Here are two mbid examples that could be used for testing: 
* 5b11f4ce-a62d-471e-81fc-a69a8278c7da
* d8df96ae-8fcf-4997-b3e6-e5d1aaf0f69e (Should bypass wikidata)

TODO check list
- 
* Look over error handling
* Look over logging
