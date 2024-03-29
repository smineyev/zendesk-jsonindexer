package zendesk.jindexer.web

import jdk.javadoc.internal.doclets.formats.html.markup.DocType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withTimeoutOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zendesk.jindexer.engine.Doc
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
        val docType
        try {
            docType = DocType.valueOf(type)
        } catch (e: IllegalArgumentException) {
            
        }

        lateinit var docFlow: Flow<Doc>
        withTimeoutOrNull(httpTimeoutInMs) {
            docFlow = if (field == null)
                            docStorage.search(term)
                        else
                            docStorage.search(term, field)
        }
        return docFlow
    }

}