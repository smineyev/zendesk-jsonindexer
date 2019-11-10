package zendesk.jindexer.engine.storage

import kotlinx.coroutines.flow.Flow
import zendesk.jindexer.engine.Doc
import zendesk.jindexer.engine.DocType
import zendesk.jindexer.engine.Meta

interface DocStorage {
    fun search(term: String) : Flow<Doc>;
    fun search(docType: DocType?, term: String) : Flow<Doc>;
    fun search(field: String, term: String) : Flow<Doc>;
    fun search(docType: DocType?, field: String, term: String) : Flow<Doc>;
    suspend fun save(docs: Flow<Doc>);
    fun getMeta(): Meta
}