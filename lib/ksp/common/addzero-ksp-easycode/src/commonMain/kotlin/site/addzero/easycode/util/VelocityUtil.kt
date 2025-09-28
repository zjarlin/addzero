package site.addzero.easycode.util

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.runtime.resource.loader.StringResourceLoader
import site.addzero.core.ext.bean2map
import java.io.StringWriter
import kotlin.reflect.KClass

val velocityEngine by lazy {
    // 创建VelocityEngine实例
    val velocityEngine = VelocityEngine()

    // 配置VelocityEngine
    velocityEngine.setProperty(
        "runtime.log.logsystem.class",
        "org.apache.velocity.runtime.log.SimpleLog4JLogSystem"
    )
    velocityEngine.setProperty("runtime.log.logsystem.log4j.category", "velocity")
    // 使用字符串资源加载器
    velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "string")
    velocityEngine.setProperty("string.resource.loader.class", StringResourceLoader::class.java.name)
    velocityEngine.setProperty("string.resource.loader.cache", false)
    velocityEngine.setProperty("string.resource.loader.modificationCheckInterval", 0)
    // 初始化引擎
    velocityEngine.init()
    velocityEngine
}
val velocityContext by lazy {
    // 创建上下文并添加数据
    val context = VelocityContext()
    context
}

object VelocityUtil {
    fun <T : Any> formatCode(
        templateConent: String,
        meta: T,
        kspOption: Map<String, String> = emptyMap(),
        kclass: KClass<T>,
        velocityEngineCallBack: (VelocityEngine) -> Unit = {},
        velocityContextCallBack: (VelocityContext) -> Unit = {}
    ): String {

        // 回调函数，允许外部配置引擎
        velocityEngineCallBack(velocityEngine)

        // 将元数据转换为Map
        val beanMap = meta.bean2map(kclass)


        // 添加元数据到上下文
        beanMap.forEach { (key, value) ->
            velocityContext.put(key, value)
        }

        // 添加一些常用的工具变量
        velocityContext.put("kspOption", kspOption)
        velocityContextCallBack(velocityContext)

        // 使用字符串资源库处理模板
        val templateName = "template_" + System.currentTimeMillis()
        StringResourceLoader.getRepository().putStringResource(templateName, templateConent)

        // 获取模板并合并上下文
        val template = velocityEngine.getTemplate(templateName)
        val writer = StringWriter()
        template.merge(velocityContext, writer)
        return writer.toString()
    }
}
