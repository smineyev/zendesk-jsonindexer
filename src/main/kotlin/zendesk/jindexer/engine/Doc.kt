package zendesk.jindexer.engine

enum class DocType {
    USER, TICKET, ORGANIZATION
}

data class Doc (val id: Long, val type: DocType, val fields: Map<String, String>);

