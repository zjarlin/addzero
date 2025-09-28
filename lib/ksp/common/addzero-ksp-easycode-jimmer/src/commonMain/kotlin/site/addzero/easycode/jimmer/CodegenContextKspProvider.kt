package site.addzero.easycode.jimmer

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import site.addzero.easycode.analysis.model.EntityMetadata
import site.addzero.easycode.jimmer.util.ServiceLoaderUtil
import site.addzero.easycode.ksp.AbstractTemplateProcessor


class CodegenContextKspProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        val loadAndSetCodegenContextProvider = ServiceLoaderUtil.loadCodegenContextProvider()
        val processor = object : AbstractTemplateProcessor<EntityMetadata>(
            loadAndSetCodegenContextProvider, environment
        ) {}
        return processor
    }
}
