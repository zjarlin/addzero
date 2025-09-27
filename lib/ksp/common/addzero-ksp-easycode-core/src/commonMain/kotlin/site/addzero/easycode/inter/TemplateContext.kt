package site.addzero.easycode.inter

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine

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

