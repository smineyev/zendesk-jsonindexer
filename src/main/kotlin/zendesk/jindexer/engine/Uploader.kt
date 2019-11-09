package zendesk.jindexer.engine

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import zendesk.jindexer.engine.storage.DocStorage
import java.io.InputStream

private const val EXTERNAL_ID_FIELD = "external_id"

@Component
class Uploader
    (@Autowired val docStorage: DocStorage) {

    init {
        initialUpload()
    }

    fun initialUpload() = runBlocking {
        val parser: Parser = Parser.default()
        this::class.java.classLoader.getResourceAsStream("users.json").use {
            val docFlow = processJsonStrem(parser, it, DocType.USER)
            docStorage.save(docFlow)
        }
        this::class.java.classLoader.getResourceAsStream("organizations.json").use {
            val docFlow = processJsonStrem(parser, it, DocType.ORGANIZATION)
            docStorage.save(docFlow)
        }
        this::class.java.classLoader.getResourceAsStream("tickets.json").use {
            val docFlow = processJsonStrem(parser, it, DocType.TICKET)
            docStorage.save(docFlow)
        }
    }

    private fun processJsonStrem(parser: Parser, jsonInputStream: InputStream, docType: DocType): Flow<Doc> = flow {
        val jsonDocs = parser.parse(jsonInputStream)
        if (jsonDocs is JsonArray<*>) {
            for (jsonDoc in jsonDocs as JsonArray<JsonObject>) {
                val doc = convertToDoc(jsonDoc, docType)
                emit(doc)
            }
        } else {
            error("Unsupported json format")
        }
    }

    private fun convertToDoc(jsonDoc: JsonObject, docType: DocType) : Doc {
        val externalId = jsonDoc[EXTERNAL_ID_FIELD] as String
        return Doc(externalId, docType,  jsonDoc)
    }

}