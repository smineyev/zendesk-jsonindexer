package zendesk.jindexer

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import zendesk.jindexer.engine.Uploader

@SpringBootTest
class UploaderTest
    (@Autowired val uploader: Uploader) {

    @Test
    fun initialUpload() {
        uploader.initialUpload()
    }
}