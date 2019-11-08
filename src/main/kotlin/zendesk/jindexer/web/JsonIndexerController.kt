package zendesk.jindexer.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import zendesk.jindexer.engine.storage.DocStorage

@RestController
class JsonIndexerController
    (@Autowired val docStorage: DocStorage) {

    @GetMapping(path = ["/search"],
            produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun search(@RequestParam term: String) = Flux.range(1, 100)


    @GetMapping(path = ["/numbers"],
            produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun getNumbers() = Flux.range(1, 100)

}