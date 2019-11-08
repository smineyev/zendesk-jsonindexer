package zendesk.jindexer

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.measureTimeMillis

@SpringBootApplication
class JsonIndexerApplication

fun main(args: Array<String>) {
	runApplication<JsonIndexerApplication>(*args)
}


//fun foo(): Flow<Int> = flow { // flow builder
//	var currThreadId = Thread.currentThread().id;
//	try {
//		for (i in 1..100) {
//			delay(100) // pretend we are doing something useful here
//			emit(i) // emit next value
//		}
//	} finally {
//		println("$currThreadId canceled")
//	}
//}
//
//suspend fun boo(): Int {
//	var currThreadId = Thread.currentThread().id;
//	println("$currThreadId is starting...")
//	delay(1000)
//	return Int.MAX_VALUE
//}
//
//fun main() = runBlocking<Unit> {
//	launch {
//		for (k in 1..100) {
//			var currThreadId = Thread.currentThread().id;
//			println("$currThreadId not blocked $k")
//			delay(100)
//		}
//	}
//
//	withTimeoutOrNull(250) {
//		foo().collect {
//			var currThreadId = Thread.currentThread().id;
//			println("$currThreadId received $it")
//		}
//	}
//
//	var currThreadId = Thread.currentThread().id;
//	println ("$currThreadId generated: " + boo())
//}

//fun events(): Flow<Int> = (1..3).asFlow().onEach { delay(100) }
//
//fun main() = runBlocking<Unit> {
//	val job = events()
//			.onEach { event -> println("Event: $event") }
//			.launchIn(this) // <--- Collecting the flow waits
//	delay(200)
//	job.cancel()
//	println("Done")
//}