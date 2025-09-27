package site.addzero.easycode.jimmer

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import site.addzero.easycode.inter.CodeGenContextHolder
import site.addzero.easycode.ksp.AbstractTemplateProcessor
import site.addzero.entity.analysis.model.EntityMetadata


class CodegenContextKspProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        CodeGenContextHolder.initContext()
        val processor = object : AbstractTemplateProcessor<EntityMetadata>(
            CodeGenContextHolder.absCodegenContextProvider, environment
        ) {}
        return processor
    }
}
