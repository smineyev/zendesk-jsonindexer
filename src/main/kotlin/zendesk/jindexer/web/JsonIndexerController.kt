package zendesk.jindexer.web

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withTimeoutOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import zendesk.jindexer.engine.Doc
import zendesk.jindexer.engine.DocType
import zendesk.jindexer.engine.storage.DocStorage

@RestController
class JsonIndexerController
    (@Autowired val docStorage: DocStorage,
     val httpTimeoutInMs: Long) {


    @GetMapping(path = ["/search"],
            produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun search(@RequestParam term: String) : Flow<Doc> = flow {
        withTimeoutOrNull(httpTimeoutInMs) {
            val docFlow = docStorage.search(term)
        }
    }


    @GetMapping(path = ["/numbers"],
            produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun getNumbers():Flow<Doc> {
        val userDoc = Doc("1", DocType.USER,
                mapOf("name" to "sergey",
                        "occupation" to "software"))
        val orgDoc = Doc("2", DocType.ORGANIZATION,
                mapOf("name" to "mvw",
                        "occupation" to "software"))
        return flowOf(userDoc, orgDoc)
    }

}