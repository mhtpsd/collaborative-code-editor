package com.mohitprasad.codeeditor;

import com.mohitprasad.codeeditor.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
@Import(TestConfig.class)
class CodeEditorApplicationTests {

    @Test
    void contextLoads() {
    }
}
