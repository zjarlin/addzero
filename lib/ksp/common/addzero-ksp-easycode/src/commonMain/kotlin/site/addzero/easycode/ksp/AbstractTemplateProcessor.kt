package site.addzero.easycode.ksp

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import site.addzero.easycode.inter.AbsCodegenContext
import site.addzero.util.genCode
import site.addzero.util.str.withFileName
import site.addzero.util.str.withFileSuffix
import site.addzero.util.str.withPkg
import java.io.File
import java.nio.file.Files

/**
 * 负责处理基于模板的代码生成任务，使用 Apache Velocity 模板引擎
 *
 * @param T 元数据类型
 */
abstract class AbstractTemplateProcessor<T : Any>(
    protected val absCodegenContext: AbsCodegenContext<T, *>,
    environment: SymbolProcessorEnvironment,
) : SymbolProcessor {
    protected val options = environment.options
    protected val codeGenerator = environment.codeGenerator
    private var metadataCache: List<T> = emptyList()
    private val dependenciesFiles = mutableSetOf<KSFile>()

    /**
     * 处理KSP符号，提取元数据
     *
     * @param resolver KSP解析器
     * @return 未处理的符号列表
     */
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val extract = absCodegenContext.extract(resolver)
        metadataCache = extract.metadata
        dependenciesFiles.addAll(extract.dependencies)
        return extract.notValid
    }

    /**
     * 完成处理，生成代码文件
     *
     * 在此阶段，所有元数据已经收集完毕，开始根据模板生成代码文件
     */
    override fun finish() {
        val javaClass = absCodegenContext.clazzEnum

//        absCodegenContext.clazzEnum.java.
        val clazz = absCodegenContext.clazz

        // 处理每个模板上下文
        val metadata = metadataCache

        javaClass.enumConstants.forEach { templateContext ->
            val templatePath = templateContext.templatePath
            val skipExistFile = templateContext.skipExistFile
            val templateContent = Files.readString(File(templatePath).toPath())
            println("tttttttt$templateContent")

            metadata.forEach { meta ->
                val fileName = templateContext.getFileName(meta, options)
                val withPkg = templateContext.getModulePath.withPkg(templateContext.getPkg)
                val pathName = withPkg.withFileName(fileName).withFileSuffix(templateContext.getFileSuffix)
                val formatCode: String = VelocityUtil.formatCode(
                    templateConent = templateContent,
                    meta = meta,
                    kspOption = options,
                    kclass = clazz,
                    velocityContextCallBack = templateContext.velocityContextCallback,
                    velocityEngineCallBack = templateContext.velocityEngineCallBack
                )

                if (!templateContext.useKspCodeGenerator) {
                    genCode(
                        pathname = pathName, code = formatCode, skipExistFile = skipExistFile
                    )
                } else {
                    val fileNameWithSuffix = fileName.withFileSuffix(templateContext.getFileSuffix)
                    codeGenerator.createNewFile(
                        dependencies = Dependencies(true, *dependenciesFiles.toTypedArray()),
                        packageName = templateContext.getPkg,
                        fileName = fileNameWithSuffix
                    ).use { stream ->
                        stream.write(formatCode.toByteArray())
                    }
                }
            }


        }


    }
}
