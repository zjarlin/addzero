package site.addzero.easycode.user_need_impl

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
import site.addzero.easycode.inter.AbsCodegenContextProvider
import site.addzero.easycode.inter.CodeGenContextHolder
import site.addzero.easycode.inter.TemplateContext
import site.addzero.entity.analysis.model.EntityMetadata

val absCodegenContext = object : AbsCodegenContextProvider<CodeGenEnum>() {
    override val clazzEnum: Class<CodeGenEnum>
        get() = CodeGenEnum::class.java
}

//init
/**
 * 代码生成清单
 * @author zjarlin
 * @date 2025/09/27
 * @constructor 创建[CodeGenEnum]
 */
enum class CodeGenEnum : TemplateContext<EntityMetadata> {
    Entity2Iso {
        /** 跳过已存在的文件 */
        override val skipExistFile: Boolean
            get() = false

        /**跨目录生成需false  */
        override val useKspCodeGenerator: Boolean
            get() = false

        override val getModulePath: String
            get() = "/Users/zjarlin/IdeaProjects/addzero/backend/server/src/main/kotlin"
        override val getPkg: String
            get() = "site.addzero.gencodetest"
        override val getFileName: (EntityMetadata, Map<String, String>) -> String
            get() = { metadata, kspOptions ->
                "${metadata.className}IsoTest"
            }
        override val templatePath: String
            get() = run {
                ClasspathResourceLoader::class.java.getResource("/templates/entity.vm")?.file?.let {
                    it.substring(it.lastIndexOf("/") + 1)
                } ?: run {
                    println("templatePath not found")
                    ""
                }
                ""
            }
        override val velocityEngineCallBack: (VelocityEngine) -> Unit
            get() = {

            }
        override val velocityContextCallback: (VelocityContext) -> Unit
            get() = {

                println("tttt$it")
                it.put("packageName", "site.addzero.gencodetest")
//                it.put("imports", "site.addzero.gencodetest")
                it.put("hasId", true)
                it.put("generatedBy", "zjarlin")

//                imports
            }
    },
//    Entity2Dto,
//    Entity2Vo,
//    Entity2Po,
//    Entity2DtoList,
//    Entity2VoList,
//    Entity2PoList,
}
