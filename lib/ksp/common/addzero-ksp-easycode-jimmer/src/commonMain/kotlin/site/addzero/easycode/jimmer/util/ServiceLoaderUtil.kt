package site.addzero.easycode.jimmer.util

import site.addzero.easycode.inter.AbsCodegenContextProvider
import java.util.ServiceLoader

/**
 * 通过ServiceLoader机制加载用户自定义的AbsCodegenContextProvider
 */
object ServiceLoaderUtil {
    fun loadCodegenContextProvider(): AbsCodegenContextProvider<*> {
        val loader = ServiceLoader.load(AbsCodegenContextProvider::class.java)
        val provider = loader.firstOrNull()?: throw IllegalStateException("No AbsCodegenContextProvider implementation found. Please provide your own implementation via ServiceLoader.")
        return provider
    }
}
