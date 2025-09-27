package site.addzero

import org.babyfish.jimmer.client.EnableImplicitApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import site.addzero.easycode.inter.AbsCodegenContextProvider
import site.addzero.easycode.inter.CodeGenContextHolder
import site.addzero.lib_adpator.jimmer_easycode.user_need_impl.CodeGenEnum

@SpringBootApplication
@EnableImplicitApi
class SpringBootApp

val absCodegenContext = object : AbsCodegenContextProvider<CodeGenEnum>() {
    override val clazzEnum: Class<CodeGenEnum>
        get() = CodeGenEnum::class.java
}
fun main(args: Array<String>) {
    CodeGenContextHolder.initContext = {
    CodeGenContextHolder.absCodegenContextProvider=absCodegenContext
    }
    runApplication<SpringBootApp>(*args)
}
