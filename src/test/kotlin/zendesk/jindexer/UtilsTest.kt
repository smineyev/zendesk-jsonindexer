package zendesk.jindexer

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UtilsTest {

    @Test
    fun tokenizeFlow() = runBlocking {
        val contentFlow = "everyone loves    Kotlin".asIterable().asFlow()
        val terms = tokenizeFlow(contentFlow, ' ').toSet()
        Assertions.assertEquals(setOf("everyone", "loves", "Kotlin"), terms)
    }

}