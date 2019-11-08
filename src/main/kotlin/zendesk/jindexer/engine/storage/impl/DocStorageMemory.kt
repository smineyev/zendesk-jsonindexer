package zendesk.jindexer.engine.storage.impl

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import zendesk.jindexer.engine.Doc
import zendesk.jindexer.engine.storage.DocStorage

class DocStorageMemory
    (val tokenize: (Flow<Char>) -> Flow<String>)
        : DocStorage {

    private val fieldMap = HashMap<String, HashMap<String, MutableList<Doc>>>()

    override fun search(field: String, term: String): Flow<Doc> {
        val termMap = fieldMap[field]
        return termMap?.get(term)?.asFlow() ?: flow {}
    }

    override fun search(term: String): Flow<Doc> {
        val res = fieldMap.values
                    .map { it[term]?.asFlow() ?: flow {} }
                    .asFlow()
                    .flattenMerge()
        return res
    }

    override suspend fun save(docs: Flow<Doc>)  {
        docs.collect { doc: Doc ->
            for (field in doc.fields.entries) {
                val termMap = fieldMap.getOrPut(field.key, { HashMap<String, MutableList<Doc>>() })
                tokenize(field.value.asIterable().asFlow()).collect {
                    termMap.getOrPut(it, { mutableListOf<Doc>() }).add(doc)
                }
            }
        }
    }
}