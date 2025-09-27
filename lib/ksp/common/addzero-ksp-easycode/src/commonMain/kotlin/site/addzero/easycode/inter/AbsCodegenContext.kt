package site.addzero.easycode.inter

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSFile
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
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
interface TemplateContext<T> {
    /**
    跳过存在的文件
     */
    val skipExistFile: Boolean
        get() = false

    val useKspCodeGenerator: Boolean
        get() = true
    val getModulePath: String
    val getPkg: String
    val getFileName: (T, Map<String, String>) -> String
    val getFileSuffix: String
        get() = ".kt"

    /**
    模板绝对路径
     */
    val templatePath: String

    val velocityEngineCallBack: (VelocityEngine) -> Unit
        get() = {}

    val velocityContextCallback: (VelocityContext) -> Unit
        get() = {}

}

