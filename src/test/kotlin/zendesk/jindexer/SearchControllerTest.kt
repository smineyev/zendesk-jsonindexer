package zendesk.jindexer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchControllerTest
	(@Autowired val restTemplate: TestRestTemplate) {

	@Test
	fun `test web controller`() {
		val entity = restTemplate.getForEntity<String>("/search?term=incident")
		println (entity.body)
		assertEquals(entity.statusCode, HttpStatus.OK)
	}

}