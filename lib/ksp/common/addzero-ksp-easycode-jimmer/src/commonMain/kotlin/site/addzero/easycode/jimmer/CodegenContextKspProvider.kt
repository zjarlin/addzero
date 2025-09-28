package site.addzero.easycode.jimmer

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import site.addzero.easycode.analysis.model.EntityMetadata
import site.addzero.easycode.inter.CodeGenContextHolder
import site.addzero.easycode.jimmer.util.ServiceLoaderUtil
import site.addzero.easycode.ksp.AbstractTemplateProcessor


class CodegenContextKspProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        // 通过ServiceLoader加载用户自定义的AbsCodegenContextProvider
        ServiceLoaderUtil.loadAndSetCodegenContextProvider()

        val processor = object : AbstractTemplateProcessor<EntityMetadata>(
            CodeGenContextHolder.absCodegenContextProvider, environment
        ) {}
        return processor
    }
}
