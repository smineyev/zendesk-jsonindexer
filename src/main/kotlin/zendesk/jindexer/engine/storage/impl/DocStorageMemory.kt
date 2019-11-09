package zendesk.jindexer.engine.storage.impl

import com.beust.klaxon.Json
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
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

    override fun search(term: String): Flow<Doc> = flow {
        for (termMap in fieldMap.values) {
            termMap[term]?.forEach { emit(it) }
        }
//        val res = fieldMap.values
//                    .map { it[term]?.asFlow() ?: flow {} }
//                    .asFlow()
//                    .flattenMerge()
//        return res
    }

    override suspend fun save(docs: Flow<Doc>)  {
        docs.collect { doc: Doc ->
            for (field in doc.json.entries) {
                val termMap = fieldMap.getOrPut(field.key, { HashMap<String, MutableList<Doc>>() })
                if (field.value is JsonObject) {
                    error("Unsupported document format")
                }
                val charFlow: Flow<Char>
                if (field.value is JsonArray<*>) {
                    charFlow = (field.value as JsonArray<String>).asFlow()
                            .flatMapMerge { (it + " ").asIterable().asFlow() }
                } else {
                    charFlow = field.value.toString().asIterable().asFlow()
                }
                tokenize(charFlow).collect {
                    termMap.getOrPut(it, { mutableListOf<Doc>() }).add(doc)
                }
            }
        }
    }
}