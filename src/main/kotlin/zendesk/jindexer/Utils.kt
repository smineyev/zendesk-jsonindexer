package zendesk.jindexer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun tokenizeFlow(contentFlow: Flow<Char>, delimeters: Set<Char>) : Flow<String> = flow {
    val strBuilder = StringBuilder()
    contentFlow.collect {
        if (it in delimeters) {
            if (strBuilder.isNotEmpty()) {
                emit(strBuilder.toString())
                strBuilder.clear()
            }
        } else {
            strBuilder.append(it)
        }
    }
    if (strBuilder.isNotEmpty()) {
        emit(strBuilder.toString())
    }
}

fun isJUnitTest(): Boolean {
    val stackTrace = Thread.currentThread().stackTrace
    for (element in stackTrace) {
        if (element.getClassName().startsWith("org.junit.")) {
            return true
        }
    }
    return false
}