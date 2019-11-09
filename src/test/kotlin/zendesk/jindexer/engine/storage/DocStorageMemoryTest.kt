package zendesk.jindexer.engine.storage

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import zendesk.jindexer.engine.Doc
import zendesk.jindexer.engine.DocType

@SpringBootTest
class DocStorageMemoryTest
    (@Autowired val docStorage: DocStorage) {

    @Test
    fun `search something does not exist`() = runBlocking<Unit> {
        val docs = docStorage.search("SSSSSSSSSSSSSS").toList()
        assertTrue(docs.isEmpty());
    }

    @Test
    fun `search for empty term`() = runBlocking<Unit> {
        val (userDoc, orgDoc, ticketDoc) = createDocs()
        val docIds = docStorage.search("").toList().map {it.id}.toSet()
        assertEquals(setOf(userDoc.id), docIds)
    }

    @Test
    fun `save and search on all fields`() = runBlocking<Unit> {
        val (userDoc, orgDoc, ticketDoc) = createDocs()
        var docIds = docStorage.search("software").map {it.id}.toSet()
        assertEquals(setOf(userDoc.id, orgDoc.id, ticketDoc.id), docIds)
    }

    @Test
    fun `save and search on single field`() = runBlocking<Unit> {
        val (userDoc, orgDoc, ticketDoc) = createDocs()
        val docIds = docStorage.search("occupation","software")
                .map {it.id}.toSet()
        assertEquals(setOf(userDoc.id, orgDoc.id), docIds)
    }

    @Test
    fun `save and search for single document type`() = runBlocking {
        val (userDoc, orgDoc, ticketDoc) = createDocs()
        val docIds = docStorage.search(DocType.ORGANIZATION,"occupation","software")
                .map {it.id}.toSet()
        assertEquals(setOf(orgDoc.id), docIds)
    }

    @Test
    fun `save and search on array field`() = runBlocking<Unit> {
        val (userDoc, orgDoc, ticketDoc) = createDocs()
        val docIds = docStorage.search("tag","top").map {it.id}.toSet()
        assertEquals(setOf(orgDoc.id), docIds)
    }

    private suspend fun createDocs(): Triple<Doc, Doc, Doc> {
        val userDoc = Doc("1", DocType.USER,
                        JsonObject(mapOf("name" to "SeRGey",
                                        "occupation" to "software",
                                        "empty-field" to "")))
        val orgDoc = Doc("2", DocType.ORGANIZATION,
                        JsonObject(mapOf("name" to "mvw",
                                        "occupation" to "software",
                                        "tag" to JsonArray("top", "small"))))
        val ticketDoc = Doc("3", DocType.TICKET,
                        JsonObject(mapOf("desc" to "bug in production env",
                                        "severity" to "high",
                                        "industry" to "software")))
        val docFlow = flowOf(userDoc, orgDoc, ticketDoc)
        docStorage.save(docFlow)
        return Triple(userDoc, orgDoc, ticketDoc)
    }

}