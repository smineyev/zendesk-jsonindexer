package zendesk.jindexer.cli

import com.budhash.cliche.ShellFactory
import com.budhash.cliche.Command
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactor.asFlux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import zendesk.jindexer.engine.Doc
import zendesk.jindexer.engine.Uploader
import zendesk.jindexer.engine.storage.DocStorage
import zendesk.jindexer.engine.storage.DocStorageConfigurer

@Configuration
class JsonIndexerCLI {
    private val docStorage = DocStorageConfigurer().docStorage()
    private val uploader = Uploader(docStorage)

    @Command (description="search documents that contain given term")
    fun search(term: String): String {
        val resBuilder = StringBuilder()
        docStorage.search(term)
                .asFlux().subscribe {
                    resBuilder.append(it.json.toJsonString(prettyPrint = true))
                    resBuilder.append('\n')
        }
        return resBuilder.toString()
    }


    @Command
    fun fields(): List<String> {
        return listOf()
    }
}

fun main(args: Array<String>) {
    ShellFactory.createConsoleShell("cmd>>",
            "Welcome to Json Indexer CLI\n"
                    + "To list all available commands enter ?list. "
                    + "To get detailed info on a command enter ?help command-name",
                    JsonIndexerCLI())
            .commandLoop()
}