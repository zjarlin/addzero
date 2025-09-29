package mock_data

import org.babyfish.jimmer.sql.kt.KSqlClient
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import site.addzero.SpringBootApp

@SpringBootTest(classes = [SpringBootApp::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MockDataTests(val kSqlClient: KSqlClient) {

    @Test
    fun `test`() {
    }

}
