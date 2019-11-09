package zendesk.jindexer.web

import com.beust.klaxon.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withTimeoutOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zendesk.jindexer.engine.Doc
import zendesk.jindexer.engine.DocType
import zendesk.jindexer.engine.storage.DocStorage

@RestController
class SearchController
    (@Autowired val docStorage: DocStorage,
     @Value(value = "\${spring.httpTimeoutInMs}") val httpTimeoutInMs: Long) {

    @GetMapping(path = ["/search"],
                produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    suspend fun search(@RequestParam term: String) : Flow<Doc> {
        lateinit var docFlow: Flow<Doc>
        withTimeoutOrNull(httpTimeoutInMs) {
            docFlow = docStorage.search(term)
        }
        return docFlow
    }

}