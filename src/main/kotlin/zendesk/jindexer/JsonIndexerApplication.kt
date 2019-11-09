package zendesk.jindexer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JsonIndexerApplication

fun main(args: Array<String>) {
	runApplication<JsonIndexerApplication>(*args)
}