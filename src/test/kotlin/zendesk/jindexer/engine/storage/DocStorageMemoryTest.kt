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
    fun `search on empty storage`() = runBlocking<Unit> {
        var docs = docStorage.search("a").toList()
        assertTrue(docs.isEmpty());
        docs = docStorage.search("").toList()
        assertTrue(docs.isEmpty());
    }

    @Test
    fun `save and search on all fields`() = runBlocking<Unit> {
        val (userDoc, orgDoc, ticketDoc) = createDocs()
        var docIds = docStorage.search("software").map {it.id}.toSet()
        assertEquals(setOf(userDoc.id, orgDoc.id, ticketDoc.id), docIds)

        docIds = docStorage.search("blah").map {it.id}.toSet()
        assertTrue(docIds.isEmpty())
    }

    @Test
    fun `save and search on single field`() = runBlocking<Unit> {
        val (userDoc, orgDoc, ticketDoc) = createDocs()
        val docIds = docStorage.search("occupation","software").map {it.id}.toSet()
        assertEquals(setOf(userDoc.id, orgDoc.id), docIds)
    }

    private suspend fun createDocs(): Triple<Doc, Doc, Doc> {
        val userDoc = Doc("1", DocType.USER,
                mapOf("name" to "sergey",
                        "occupation" to "software"))
        val orgDoc = Doc("2", DocType.ORGANIZATION,
                mapOf("name" to "mvw",
                        "occupation" to "software"))
        val ticketDoc = Doc("3", DocType.TICKET,
                mapOf("desc" to "bug in production env",
                        "severity" to "high",
                        "industry" to "software"))
        val docFlow = flowOf(userDoc, orgDoc, ticketDoc)
        docStorage.save(docFlow)
        return Triple(userDoc, orgDoc, ticketDoc)
    }

}