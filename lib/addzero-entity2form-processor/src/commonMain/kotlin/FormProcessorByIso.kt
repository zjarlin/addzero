
import com.addzero.kmp.context.SettingContext
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

/**
 * 处理器提供者
 */
class FormProcessorByIsoProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return FormProcessorByIso(environment)
    }
}

class FormProcessorByIso(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        SettingContext.initialize(environment.options)
        val entities = resolver.getSymbolsWithAnnotation("org.babyfish.jimmer.sql.Entity").filterIsInstance<KSClassDeclaration>()


        entities.forEach {
            generateIsomorphicForm(
                it,
            )

        }

        println("生成表单完成")


//        ret.addAll(entities.toList())
        return emptyList()
    }


//    override fun finish() {
//        ret.forEach {
//            generateIsomorphicForm(
//                it,
//            )
//
//        }
//
//    }
}
