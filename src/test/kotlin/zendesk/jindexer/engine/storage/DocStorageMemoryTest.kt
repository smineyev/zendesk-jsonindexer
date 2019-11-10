package zendesk.jindexer.engine.storage

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestComponent
import zendesk.jindexer.engine.Doc
import zendesk.jindexer.engine.DocType
import zendesk.jindexer.engine.storage.impl.DocStorageMemory

class DocStorageMemoryTest {
    val docStorageConfigurer = DocStorageConfigurer()
    lateinit var docStorage: DocStorage
    lateinit var userDoc: Doc
    lateinit var orgDoc: Doc
    lateinit var ticketDoc: Doc

    @BeforeEach
    fun beforeEach() = runBlocking {
        docStorage = docStorageConfigurer.docStorage()
        createDocs()
    }

    @Test
    fun `search something does not exist`() = runBlocking<Unit> {
        val docs = docStorage.search("SSSSSSSSSSSSSS").toList()
        assertTrue(docs.isEmpty());
    }

    @Test
    fun `search for empty term`() = runBlocking<Unit> {
        val docIds = docStorage.search("").toList().map {it.id}.toSet()
        assertEquals(setOf(userDoc.id), docIds)
    }

    @Test
    fun `save and search on all fields`() = runBlocking<Unit> {
        var docIds = docStorage.search("software").map {it.id}.toSet()
        assertEquals(setOf(userDoc.id, orgDoc.id, ticketDoc.id), docIds)
    }

    @Test
    fun `save and search on single field`() = runBlocking<Unit> {
        val docIds = docStorage.search("occupation","software")
                .map {it.id}.toSet()
        assertEquals(setOf(userDoc.id, orgDoc.id), docIds)
    }

    @Test
    fun `save and search for single document type`() = runBlocking {
        val docIds = docStorage.search(DocType.ORGANIZATION,"occupation","software")
                .map {it.id}.toSet()
        assertEquals(setOf(orgDoc.id), docIds)
    }

    @Test
    fun `save and search on array field`() = runBlocking<Unit> {
        val docIds = docStorage.search("tag","top").map {it.id}.toSet()
        assertEquals(setOf(orgDoc.id), docIds)
    }

    @Test
    fun `retrieval of meta information`() = runBlocking {
        val meta = docStorage.getMeta()
        assertEquals(3, meta.typeFieldMap.size)
        assertEquals(userDoc.json.keys, meta.typeFieldMap.get(DocType.USER))
    }

    private suspend fun createDocs() {
        userDoc = Doc("1", DocType.USER,
                        JsonObject(mapOf("name" to "SeRGey",
                                        "occupation" to "software",
                                        "empty-field" to "")))
        orgDoc = Doc("2", DocType.ORGANIZATION,
                        JsonObject(mapOf("name" to "mvw",
                                        "occupation" to "software",
                                        "tag" to JsonArray("top", "small"))))
        ticketDoc = Doc("3", DocType.TICKET,
                        JsonObject(mapOf("desc" to "bug in production env",
                                        "severity" to "high",
                                        "industry" to "software")))
        val docFlow = flowOf(userDoc, orgDoc, ticketDoc)
        docStorage.save(docFlow)
//        return Triple(userDoc, orgDoc, ticketDoc)
    }

}