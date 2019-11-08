package zendesk.jindexer.engine.storage.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import zendesk.jindexer.engine.Doc
import zendesk.jindexer.engine.storage.DocStorage

class DocStorageMemory
    (val tokenize: (Flow<Char>) -> Flow<String>)
        : DocStorage {

    private val termMap = HashMap<String, MutableList<Doc>>()

    override fun search(term: String): Flow<Doc> {
        val docs = termMap[term]
        return docs?.asFlow() ?: flow {}
    }

    override suspend fun save(docs: Flow<Doc>)  {
        docs.collect { doc: Doc ->
            for (fieldVal in doc.fields.values) {
                tokenize(fieldVal.asIterable().asFlow()).collect {
                    termMap.getOrPut(it, { mutableListOf<Doc>() }).add(doc)
                }
            }
        }
    }
}