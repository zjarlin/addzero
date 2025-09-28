package site.addzero.easycode.jimmer.util

import site.addzero.easycode.inter.AbsCodegenContextProvider
import site.addzero.easycode.inter.CodeGenContextHolder
import java.util.ServiceLoader

/**
 * 通过ServiceLoader机制加载用户自定义的AbsCodegenContextProvider
 */
object ServiceLoaderUtil {
    fun loadAndSetCodegenContextProvider() {
        val loader = ServiceLoader.load(AbsCodegenContextProvider::class.java)
        val provider = loader.firstOrNull()
        
        if (provider != null) {
            CodeGenContextHolder.absCodegenContextProvider = provider
        } else {
            // 如果没有找到用户实现，则使用默认实现或抛出异常
            throw IllegalStateException("No AbsCodegenContextProvider implementation found. Please provide your own implementation via ServiceLoader.")
        }
    }
}