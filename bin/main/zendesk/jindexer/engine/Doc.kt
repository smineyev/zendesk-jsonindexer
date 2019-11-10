package zendesk.jindexer.engine

import com.beust.klaxon.JsonObject

enum class DocType {
    USER, TICKET, ORGANIZATION
}

data class Doc (val id: String, val type: DocType, val json: JsonObject)

