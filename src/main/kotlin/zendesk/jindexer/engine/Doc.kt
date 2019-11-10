package zendesk.jindexer.engine

import com.beust.klaxon.JsonObject
import java.util.*
import kotlin.collections.HashMap

enum class DocType {
    USER, TICKET, ORGANIZATION
}

data class Doc (val id: String, val type: DocType, val json: JsonObject)

data class Meta (val typeFieldMap: HashMap<DocType, TreeSet<String>> = HashMap())
