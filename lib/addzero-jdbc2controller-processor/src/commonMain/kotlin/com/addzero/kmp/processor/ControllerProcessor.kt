package com.addzero.kmp.processor


import com.addzero.kmp.context.SettingContext
import com.addzero.kmp.entity.JdbcTableMetadata
import com.addzero.kmp.util.JdbcMetadataExtractor
import com.addzero.kmp.util.toBigCamelCase
import com.addzero.kmp.util.toLowCamelCase
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import java.io.File

class ControllerProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        SettingContext.initialize(environment.options)
//        resolver.getde
        val tables = JdbcMetadataExtractor.initAndGetJdbcMetaDataTables(environment.options)
        // 通过KSP options获取JDBC配置
        // 获取所有表的元数据
        println("解析到${tables.size}张表元数据")
        // 为每个表生成表单和formState
        tables.forEach { table ->
            generateFormFile(table)
        }
        return emptyList()
    }


    private fun generateFormFile(table: JdbcTableMetadata) {
        val tableName = table.tableName
        val entityName = tableName.toBigCamelCase()
        val dir = SettingContext.settings.serverSourceDir
//        println("后端源码目录为: $dir")

        val pkgGang = "com/addzero/web/modules/controller"

//        val pkg = StrUtil.replace(pkgGang, "//", ".")

        val pkg = pkgGang.replace("/", ".")

        val fileName = "${entityName}Controller.kt"

        val className = "${entityName}Controller"

        val targetFile = File("$dir/$pkgGang/$fileName")
        targetFile.parentFile?.mkdirs()
        if (targetFile.exists()) {
            environment.logger.info("Controller文件已存在，跳过生成: ${targetFile.path}")
            return
        }

        // 写入文件
        targetFile.writeText(
            """
package $pkg 
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/${tableName.toLowCamelCase()}")
class $className {

    @GetMapping("/page")
    fun page(): Unit {
        // TODO: 
    }

    @PostMapping("/save")
    fun save(): Unit {
        // TODO: 
    }

}
            """.trimMargin()
        )
    }
}

/**
 * 处理器提供者
 */
class ControllerProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ControllerProcessor(environment)
    }
}
