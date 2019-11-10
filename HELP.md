# Getting Started

To build and start the service, execute:
>bash# ./runCLI

It will start web service on port 8080 and expose shell interface.
 
*note that search is case insensitive 

### HTTP end-points

Retrieve meta information about stored data, i.e. document types and corresponding field names
>/meta

Search documents
>/search?type=<doc-type>&field=<field-name>&term=<string-to-search>

Examples:
> curl 'http://localhost:8080/search?term=oHiO'

> curl 'http://localhost:8080/search?type=user&field=signature&term=Worry'

> curl 'http://localhost:8080/meta'
 

### Shell interface
List all commands:
> cmd>>> ?list

Help on a particular command
> cmd>>> ?help search

Retrieve meta information about stored data
> cmd>>> meta

Search all documents that have word Ohio:
> cmd>>> search Ohio

Search tickets with empty description:
> cmd>>> search ticket description ''

### Docker
If runCLI build fails due to issues with local environment, there docker image available at smineyev/jsonsearch.
To use it, execute:
> bash# sudo docker run -p 8080:8080 smineyev/jsonsearch:v2

*note that in-app shell interface won't be available in this scenario


### Reference Documentation
These might help to better understand tools and concepts that were used in this application 

* [Docker documentation](https://docs.docker.com/)
* [Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/gradle-plugin/reference/html/)
* [Coroutines section of the Spring Framework](https://docs.spring.io/spring/docs/5.2.1.RELEASE/spring-framework-reference/languages.html#coroutines)

