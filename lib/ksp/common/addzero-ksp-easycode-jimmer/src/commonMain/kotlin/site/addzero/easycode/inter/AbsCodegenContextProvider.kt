package site.addzero.easycode.inter

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import site.addzero.entity.analysis.analyzer.JimmerEntityAnalyzer
import site.addzero.entity.analysis.model.EntityMetadata
import kotlin.reflect.KClass

abstract class AbsCodegenContextProvider<T> : AbsCodegenContext<EntityMetadata, T> where T : TemplateContext<EntityMetadata>, T : Enum<T> {
    override val clazz: KClass<EntityMetadata>
        get() = EntityMetadata::class

    override fun extract(resolver: Resolver): MetadataContext<EntityMetadata> {
        val entitySymbols = resolver
            .getSymbolsWithAnnotation("org.babyfish.jimmer.sql.Entity")
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.validate() }

        val dependencies = entitySymbols.mapNotNull { it.containingFile }

        val jimmerEntityAnalyzer = JimmerEntityAnalyzer()
        val metadatas = entitySymbols.map {
            val analyzeEntity = jimmerEntityAnalyzer.analyzeEntity(it)
            analyzeEntity
        }
        return MetadataContext(
            metadata = metadatas.toList(),
            dependencies = dependencies.toList(),
            notValid = dependencies.filterNot { it.validate() }.toList()
        )
    }
}
