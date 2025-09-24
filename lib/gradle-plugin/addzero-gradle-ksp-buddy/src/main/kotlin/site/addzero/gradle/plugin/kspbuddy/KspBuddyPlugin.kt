package site.addzero.gradle.plugin.kspbuddy

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import java.io.File

/**
 * KspBuddyPlugin Gradle 插件主类
 * 该插件用于生成KSP处理器所需的配置脚本
 */
class KspBuddyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // 创建扩展对象，用于配置mustMap
        val extension = project.extensions.create<KspBuddyExtension>("kspBuddy")

        // 设置默认值
        val value = "build-logic/src/main/kotlin/conventions/generated"
        extension.kspScriptOutputDir.convention(value)

        // 创建生成脚本的任务
        val generateTask = project.tasks.register<GenerateKspScriptTask>("generateKspScript") {
            description = "Generates KSP configuration script based on mustMap"
            group = "build"

            // 设置输出文件路径，使用模块名称作为文件名的一部分
            val outputDirPath = extension.kspScriptOutputDir.getOrElse(value)
            val generatedDir = File(project.rootProject.projectDir, outputDirPath)
            val moduleName = getModuleName(project)
            outputFile = File(generatedDir, "ksp-config4${moduleName}.gradle.kts")

            // 传递mustMap给任务
            mustMap.set(extension.mustMap)

            // 传递SettingContext配置
            settingContextConfig.set(extension.settingContext)
            targetProject.set(project)
        }

        // 在项目配置完成后自动生成配置文件
        project.afterEvaluate {
            // 检查是否启用预编译文件生成
            if (extension.enablePrecompile.getOrElse(true)) {
                // 只有当mustMap有配置时才生成
                if (extension.mustMap.isPresent && extension.mustMap.get().isNotEmpty() ) {
                    generateTask.get().generate()
                }
            } else {
                println("KSP预编译文件生成已禁用")
            }
        }
    }

    /**
     * 获取模块名称，用于生成文件名
     */
    private fun getModuleName(project: Project): String {
        // 获取项目路径并转换为合适的文件名格式
        val path = project.path
        return if (path == ":") {
            "root"
        } else {
            // 移除冒号并替换为连字符
            path.substring(1).replace(":", "-")
        }
    }
}

/**
 * 生成KSP配置脚本的任务
 */
abstract class GenerateKspScriptTask: DefaultTask() {
    @get:org.gradle.api.tasks.Input
    abstract val mustMap: MapProperty<String, String>

    @get:org.gradle.api.tasks.Input
    abstract val settingContextConfig: Property<SettingContextConfig>

    @get:org.gradle.api.tasks.Input
    abstract val targetProject: Property<Project>

    @get:org.gradle.api.tasks.OutputFile
    abstract var outputFile: File

    @org.gradle.api.tasks.TaskAction
    fun generate() {
        // 确保输出目录存在
        outputFile.parentFile.mkdirs()

        // 生成脚本内容
        val content = buildString {
            appendLine("plugins {")
            appendLine("    id(\"com.google.devtools.ksp\")")
            appendLine("}")
            appendLine()
            appendLine("ksp {")

            // 添加所有配置项
            mustMap.get().forEach { (key, value) ->
                appendLine("    arg(\"$key\", \"$value\")")
            }

            appendLine("}")
        }

        // 写入文件
        outputFile.writeText(content)

        logger.lifecycle("Generated KSP configuration script to: ${outputFile.absolutePath}")

        // 生成Settings数据类和SettingContext对象
        generateSettingsAndContext()
    }

    private fun generateSettingsAndContext() {
        val config = settingContextConfig.get()
        if (!config.enabled) {
            logger.lifecycle("SettingContext generation is disabled")
            return
        }

        val project = targetProject.get()
        val mustMap = mustMap.get()

        // 创建输出目录在build目录下，避免污染源码
        val outputDir = File(project.buildDir, "generated/ksp-buddy")
        val packageDir = File(outputDir, config.packageName.replace(".", "/"))
        packageDir.mkdirs()

        logger.lifecycle("Generating Settings and SettingContext in: ${packageDir.absolutePath}")
        logger.lifecycle("Package name: ${config.packageName}")
        logger.lifecycle("MustMap content: $mustMap")

        // 生成Settings数据类
        generateSettingsDataClass(packageDir, config.settingsClassName, mustMap, config.packageName)

        // 生成SettingContext对象
        generateSettingContextObject(packageDir, config.contextClassName, config.settingsClassName, config.packageName, mustMap)
    }

    private fun generateSettingsDataClass(packageDir: File, className: String, properties: Map<String, String>, packageName: String) {
        val file = File(packageDir, "${className}.kt")

        // 如果没有属性，不需要生成文件
        if (properties.isEmpty()) {
            logger.lifecycle("No properties provided, skipping Settings data class generation")
            return
        }

        // 使用字符串模板生成代码，只根据mustMap生成属性（不包含序列化注解）
        val template = """
            package $packageName

            data class $className(
            <%properties%>
            )
        """.trimIndent()

        // 生成属性列表，完全基于传入的properties map
        val propertyLines = properties.map { (key, value) -> "    val $key: String = \"$value\"," }

        // 组合最终内容
        val propertiesContent = propertyLines.joinToString("\n")
        val finalContent = template.replace("<%properties%>", propertiesContent)
            .replace(",\n            )", "\n            )")  // 处理最后一个逗号

        file.writeText(finalContent)
        logger.lifecycle("Generated Settings data class to: ${file.absolutePath}")
    }

    private fun generateSettingContextObject(packageDir: File, objectName: String, settingsClassName: String, packageName: String, properties: Map<String, String>) {
        val file = File(packageDir, "${objectName}.kt")

        // 如果没有属性，不需要生成文件
        if (properties.isEmpty()) {
            logger.lifecycle("No properties provided, skipping SettingContext object generation")
            return
        }

        // 使用字符串模板生成代码
        val template = """
            package $packageName

            import java.util.concurrent.atomic.AtomicReference

            object $objectName {
                private val settingsRef = AtomicReference<$settingsClassName?>()

                val settings: $settingsClassName
                    get() = settingsRef.get() ?: $settingsClassName()

                fun initialize(op: Map<String, String>) {
                    // 使用简单的map到对象转换，避免依赖外部库
                    val mapToBean = $settingsClassName(
            <%properties%>
                    )
                    settingsRef.compareAndSet(null, mapToBean)
                }
            }
        """.trimIndent()

        // 生成属性列表，完全基于传入的properties map
        val propertyLines = properties.map { (key, _) -> "            $key = op[\"$key\"] ?: \"\"," }

        // 组合最终内容
        val propertiesContent = propertyLines.joinToString("\n")
        val finalContent = template.replace("<%properties%>", propertiesContent)
            .replace(",\n                    )", "\n                    )")  // 处理最后一个逗号

        file.writeText(finalContent)
        logger.lifecycle("Generated SettingContext object to: ${file.absolutePath}")
    }
}
