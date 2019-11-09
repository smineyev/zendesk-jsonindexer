package zendesk.jindexer.cli

import com.budhash.cliche.Command
import com.budhash.cliche.ShellFactory
import kotlinx.coroutines.reactor.asFlux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import zendesk.jindexer.engine.storage.DocStorage

@Component
class JsonIndexerCLI(
        @Autowired val docStorage: DocStorage) : CommandLineRunner {

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

    override fun run(vararg args: String?) {
        val shell = ShellFactory.createConsoleShell("cmd>>",
                "Welcome to Json Indexer CLI\n"
                        + "To list all available commands enter ?list. "
                        + "To get detailed info on a command enter ?help command-name.",
                this)
        shell.commandLoop()
    }
}