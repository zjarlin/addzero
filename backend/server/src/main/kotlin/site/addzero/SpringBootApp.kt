package site.addzero

import org.babyfish.jimmer.client.EnableImplicitApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableImplicitApi
class SpringBootApp
fun main(args: Array<String>) {
    runApplication<SpringBootApp>(*args)
}
