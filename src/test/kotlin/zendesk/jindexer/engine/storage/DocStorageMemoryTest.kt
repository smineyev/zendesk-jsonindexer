package zendesk.jindexer.engine.storage

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import zendesk.jindexer.engine.Doc
import zendesk.jindexer.engine.DocType

@SpringBootTest
class DocStorageMemoryTest
    (@Autowired val docStorage: DocStorage) {

    @Test
    fun searchOnEmptyStorage() = runBlocking<Unit> {
        var docs = docStorage.search("a").toList()
        assertTrue(docs.isEmpty());
        docs = docStorage.search("").toList()
        assertTrue(docs.isEmpty());
    }

    @Test
    fun saveAndSearch() = runBlocking<Unit> {
        var userDoc = Doc("1", DocType.USER,
                mapOf("name" to "sergey", "occupation" to "software"))
        var orgDoc = Doc("2", DocType.ORGANIZATION,
                mapOf("name" to "mvw", "occupation" to "software"))
        var ticketDoc = Doc("3", DocType.TICKET,
                mapOf("desc" to "bug in production env", "severity" to "high"))
        val docFlow = flowOf(userDoc, orgDoc, ticketDoc)
        docStorage.save(docFlow)

        val docIds = docStorage.search("sergey").map {it.id}.toSet()
        assertEquals(setOf(userDoc.id, orgDoc.id, ticketDoc.id), docIds)
    }

}