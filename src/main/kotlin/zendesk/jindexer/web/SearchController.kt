package zendesk.jindexer.web

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeoutOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zendesk.jindexer.engine.Doc
import zendesk.jindexer.engine.DocType
import zendesk.jindexer.engine.Meta
import zendesk.jindexer.engine.storage.DocStorage
import java.lang.IllegalArgumentException

@RestController
class SearchController
    (@Autowired val docStorage: DocStorage,
     @Value(value = "\${spring.httpTimeoutInMs}") val httpTimeoutInMs: Long) {

    @GetMapping(path = ["/search"],
                produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    suspend fun search(@RequestParam term: String,
                       @RequestParam(required = false) field: String,
                       @RequestParam(required = false) type: String) : Flow<Doc> {
        var docType: DocType? = null
        if (type != null) {
            try {
                docType = DocType.valueOf(type.toUpperCase())
            } catch (e: IllegalArgumentException) {
                println("Illegal type $type")
                return flow() {}
            }
        }

        lateinit var docFlow: Flow<Doc>
        withTimeoutOrNull(httpTimeoutInMs) {
            docFlow = if (field == null)
                            docStorage.search(docType, term)
                        else
                            docStorage.search(docType, field, term)
        }
        return docFlow
    }

    @GetMapping(path = ["/meta"],
            produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun getMeta(): Meta {
        return docStorage.getMeta()
    }

}