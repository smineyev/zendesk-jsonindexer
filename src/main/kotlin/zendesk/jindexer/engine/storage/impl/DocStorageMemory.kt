package zendesk.jindexer.engine.storage.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import zendesk.jindexer.engine.Doc
import zendesk.jindexer.engine.storage.DocStorage

class DocStorageMemory: DocStorage {

    private val termMap = HashMap<String, MutableList<Doc>>()

    override fun search(term: String): Flow<Doc> {
        val docs = termMap[term]
        return docs?.asFlow() ?: flow {}
    }

    override fun save(docs: Flow<Doc>) = runBlocking<Unit> {
        // TODO
//        docs.collect {
//            val doc = it
//            doc.fields.values.forEach {
//                termMap.getOrPut(term, {mutableListOf<Doc>()}).add(doc)
//            }
//        }
    }
}