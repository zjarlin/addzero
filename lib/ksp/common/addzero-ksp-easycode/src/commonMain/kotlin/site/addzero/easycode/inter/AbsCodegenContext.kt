package site.addzero.easycode.inter

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSFile
import kotlin.reflect.KClass

data class MetadataContext<T>(
    val metadata: List<T>,
    val dependencies: List<KSFile>,
    val notValid: List<KSFile>,
)

/**
 * 元数据提取器接口
 *
 * 负责从KSP符号中提取元数据，与代码生成逻辑解耦
 *
 * @param T 元数据类型
 */
interface AbsCodegenContext<T : Any, E> where E : TemplateContext<T>, E : Enum<E> {
    val clazz: KClass<T>
    val clazzEnum: Class<E>
    fun extract(resolver: Resolver): MetadataContext<T>

}

