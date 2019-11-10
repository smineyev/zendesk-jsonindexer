package zendesk.jindexer.cli

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.budhash.cliche.Command
import com.budhash.cliche.ShellFactory
import kotlinx.coroutines.reactor.asFlux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import zendesk.jindexer.engine.DocType
import zendesk.jindexer.engine.storage.DocStorage

@Component
class JsonIndexerCLI(
        @Autowired val docStorage: DocStorage) : CommandLineRunner {

    @Command (description="search any documents that contain given term(p1) in any field")
    fun search(term: String): String {
        val resBuilder = StringBuilder()
        docStorage.search(if (term != "''") term else "")
                .asFlux().subscribe {
                    resBuilder.append(it.json.toJsonString(prettyPrint = true))
                    resBuilder.append('\n')
        }
        return resBuilder.toString()
    }

    @Command (description="search documents by given type(p1) that contain term(p3) in given field(p2)")
    fun search(docTypeStr: String, field: String, term: String): String {
        val docType: DocType
        try {
            docType = DocType.valueOf(docTypeStr.toUpperCase())
        } catch (e: IllegalArgumentException) {
            println("Unknown document type: $docTypeStr")
            println("Supported document types: "
                    + DocType.values()
                        .map { it.name }
                        .reduce{res, elem -> res+" "+elem})
            return ""
        }
        val resBuilder = StringBuilder()
        docStorage.search(docType, field, if (term != "''") term else "")
                .asFlux().subscribe {
                    resBuilder.append(it.json.toJsonString(prettyPrint = true))
                    resBuilder.append('\n')
                }
        return resBuilder.toString()
    }

    @Command(description="retrieve meta information (doc types -> fields)")
    fun meta(): String {
        val meta = docStorage.getMeta()
        val jsonObj = JsonObject()
        for ((docType, fieldSet) in meta.typeFieldMap) {
            jsonObj.set(docType.name, JsonArray(fieldSet.toList()))
        }
        return jsonObj.toJsonString(prettyPrint = true)
    }

    @Command
    fun exit() {
        println("Bye.")
        System.exit(0)
    }

    override fun run(vararg args: String?) {
        if ("cli" in args) {
            val shell = ShellFactory.createConsoleShell("cmd>>",
                    "Welcome to Json Indexer CLI\n"
                            + "To list all available commands enter ?list. "
                            + "To get detailed info on a command enter ?help command-name.",
                    this)
            shell.commandLoop()
        }
    }
}