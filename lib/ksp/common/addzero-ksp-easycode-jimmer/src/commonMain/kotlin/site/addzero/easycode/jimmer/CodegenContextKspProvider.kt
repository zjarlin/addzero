package site.addzero.easycode.jimmer

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import site.addzero.easycode.analysis.model.EntityMetadata
import site.addzero.easycode.inter.AbsCodegenContextProvider
import site.addzero.easycode.ksp.AbstractTemplateProcessor


class CodegenContextKspProvider(
    private val absCodegenContextProvider: AbsCodegenContextProvider<*>
) : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        val processor = object : AbstractTemplateProcessor<EntityMetadata>(
            absCodegenContextProvider, environment
        ) {}
        return processor
    }
}
