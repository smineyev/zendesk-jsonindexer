package zendesk.jindexer.engine.storage

import kotlinx.coroutines.flow.Flow
import zendesk.jindexer.engine.Doc

interface DocStorage {
    fun search(term: String) : Flow<Doc>;
    suspend fun save(docs: Flow<Doc>);
}