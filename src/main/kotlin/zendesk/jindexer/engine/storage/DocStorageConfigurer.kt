package zendesk.jindexer.engine.storage

import kotlinx.coroutines.flow.Flow
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import zendesk.jindexer.engine.storage.impl.DocStorageMemory
import zendesk.jindexer.tokenizeFlow

@Configuration
class DocStorageConfigurer {

    private val delimiters = setOf(' ', '\t', '\n', ',', '.', ';', ':')

    fun tokenizeFlowWithDelimeters(charFlow: Flow<Char>) : Flow<String> {
        return tokenizeFlow(charFlow, delimiters)
    }

    @Bean
    fun docStorage(): DocStorage = DocStorageMemory(this::tokenizeFlowWithDelimeters)
}