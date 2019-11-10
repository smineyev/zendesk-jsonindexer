package zendesk.jindexer.engine.storage.impl

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import kotlinx.coroutines.flow.*
import zendesk.jindexer.engine.Doc
import zendesk.jindexer.engine.DocType
import zendesk.jindexer.engine.storage.DocStorage

class DocStorageMemory
    (val tokenize: (Flow<Char>?) -> Flow<String>)
        : DocStorage {

    private val fieldMap = HashMap<String, HashMap<String, MutableList<Doc>>>()

    override fun search(docType: DocType?, field: String, term: String): Flow<Doc> {
        val termInLower = term.toLowerCase()
        val termMap = fieldMap[field]
        val docs = termMap?.get(termInLower)
        val filteredDocFlow =
                if (docType != null)
                    docs?.asFlow()?.filter { it.type == docType }
                else
                    docs?.asFlow()
        return filteredDocFlow ?: flow{}
    }

    override fun search(field: String, term: String): Flow<Doc> {
        return search(null, field, term)
    }

    override fun search(docType: DocType?, term: String): Flow<Doc> = flow {
        val termInLower = term.toLowerCase()
        for (termMap in fieldMap.values) {
            termMap[termInLower]?.forEach {
                if (docType == null || it.type == docType) {
                    emit(it)
                }
            }
        }
//        val res = fieldMap.values
//                    .map { it[term]?.asFlow() ?: flow {} }
//                    .asFlow()
//                    .flattenMerge()
//        return res
    }

    override fun search(term: String): Flow<Doc> {
        return search(null as DocType, term)
    }

    override suspend fun save(docs: Flow<Doc>)  {
        docs.collect { doc: Doc ->
            for (field in doc.json.entries) {
                val termMap = fieldMap.getOrPut(field.key, { HashMap<String, MutableList<Doc>>() })
                if (field.value is JsonObject) {
                    error("Unsupported document format")
                }
                val charFlow: Flow<Char>?
                if (field.value is JsonArray<*>) {
                    charFlow = (field.value as JsonArray<String>).asFlow()
                            .flatMapMerge { (it.toLowerCase() + " ").asIterable().asFlow() }
                } else {
                    val fieldValStr = field.value.toString()
                    charFlow = if (fieldValStr.isNotBlank())
                                    fieldValStr.toLowerCase().asIterable().asFlow()
                                else
                                    null
                }
                tokenize(charFlow).collect {
                    termMap.getOrPut(it, { mutableListOf<Doc>() }).add(doc)
                }
            }
        }
    }

}