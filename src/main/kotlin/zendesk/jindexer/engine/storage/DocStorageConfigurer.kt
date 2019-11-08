package zendesk.jindexer.engine.storage

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import zendesk.jindexer.engine.storage.impl.DocStorageMemory

@Configuration
class DocStorageConfigurer {

    @Bean
    fun docStorage(): DocStorage = DocStorageMemory()
}