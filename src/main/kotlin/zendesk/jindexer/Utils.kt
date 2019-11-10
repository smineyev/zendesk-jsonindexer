package zendesk.jindexer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

const val EMPTY_TERM = ""

fun tokenizeFlow(contentFlow: Flow<Char>?, delimeters: Set<Char>) : Flow<String> = flow {
    if (contentFlow != null) {
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
    } else {
        emit(EMPTY_TERM)
    }
}
