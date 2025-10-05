package site.addzero.kcp.i18n.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration

@OptIn(ExperimentalCompilerApi::class)
class I18NCompilerPlugin : CompilerPluginRegistrar() {
    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val targetLocale = I18NPluginConfiguration.getTargetLocale(configuration)
        val resourceBasePath = I18NPluginConfiguration.getResourceBasePath(configuration)
        IrGenerationExtension.registerExtension(I18NIrGenerationExtension(targetLocale, resourceBasePath))
    }
}