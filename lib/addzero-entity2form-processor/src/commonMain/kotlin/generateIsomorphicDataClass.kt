import com.addzero.kmp.context.SettingContext
import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.isBlank
import com.addzero.kmp.util.isJimmerEntity
import com.addzero.kmp.util.kotlinStdTypes
import com.google.devtools.ksp.symbol.KSClassDeclaration
import java.io.File

// 生成同构体数据类的主函数
fun generateIsomorphicDataClass(
    ksClass: KSClassDeclaration, outputDir: String, generatedIsoClasses: MutableSet<String> = mutableSetOf<String>(), // 跟踪所有已生成同构体的FQCN
    packageName: String = "com.addzero.kmp.isomorphic", // 生成的同构体文件所在的包名
    jimmerEntityPkg: String = "com.addzero.web.modules",
    isoSuffix: String
) {
    val className = ksClass.simpleName.asString()
    val dataClassName = "${className}$isoSuffix"
    val currentIsoFqName = "$packageName.$dataClassName"

    // 如果该同构体已被生成，则直接返回，避免重复
    if (currentIsoFqName in generatedIsoClasses) {
        return
    }
    generatedIsoClasses += currentIsoFqName

    // 收集当前同构体文件需要的所��导入
    val localImports = mutableSetOf<String>()

    val props = ksClass.getAllProperties().map { prop ->
        val name = prop.simpleName.asString()
        val type = prop.type.resolve() // 属性的KSType
        val typeDecl = type.declaration // 属性类型的声明
        val typeName = typeDecl.simpleName.asString() // 属性类型的简单名称
        val qualifiedName = typeDecl.qualifiedName?.asString() // 属性类型的全限定名

        // 获取属性在同构体中的字符串表示（可能带Iso后缀）
        val typeStr = getIsoTypeString(type, outputDir, generatedIsoClasses, packageName)





        // 生成默认值
        val defaultValue = prop.defaultValue()


        // 判断是否需要导入
        when {
            // 如果是Jimmer实体，其Iso同构体在同一包下，无需导入。
            // 递归生成已在getIsoTypeString中处理。
            isJimmerEntity(typeDecl) -> {
            }
            // 如果是枚举或其他自定义类型，且不是Kotlin标准库类型
            !kotlinStdTypes.contains(typeName) && qualifiedName != null -> {
                val importPackage = qualifiedName.substringBeforeLast('.', "")
                // 确保导入的不是当前生成的包，也不是原始的Jimmer实体包
                // TODO: 这里的Jimmer实体包路径需要根据你的实际情况精确配置
                // 示例：如果你的Jimmer实体在 com.addzero.web.modules.xxx.entity 包下
                if (importPackage != packageName && !qualifiedName.startsWith(jimmerEntityPkg)) {
                    //如果qualifiedName是
                    if (qualifiedName.startsWith("java.time")) {
                        localImports.add("kotlinx.datetime.*")
                    } else {
                        localImports.add(qualifiedName)
                    }

                }
            }
            // 处理集合和Map的泛型参数导入
            (typeName == "List" || typeName == "Set") && type.arguments.isNotEmpty() -> {
                type.arguments.first().type?.resolve()?.let { argType -> // ✅ 获取 KSType
                    val argTypeDecl = argType.declaration
                    val argSimpleName = argTypeDecl.simpleName.asString()
                    val argQualifiedName = argTypeDecl.qualifiedName?.asString()
                    val argPackage = argQualifiedName?.substringBeforeLast('.', "")

                    if (argQualifiedName != null && !kotlinStdTypes.contains(argSimpleName) && argPackage != packageName && !isJimmerEntity(argTypeDecl)) {
                        localImports.add(argQualifiedName)
                    }
                }
            }

            typeName == "Map" && type.arguments.size == 2 -> {
                // 处理Map的Key类型导入
                type.arguments[0].type?.resolve()?.let { keyType -> // ✅ 获取 KSType
                    val keyTypeDecl = keyType.declaration
                    val keySimpleName = keyTypeDecl.simpleName.asString()
                    val keyQualifiedName = keyTypeDecl.qualifiedName?.asString()
                    val keyPackage = keyQualifiedName?.substringBeforeLast('.', "")
                    if (keyQualifiedName != null && !kotlinStdTypes.contains(keySimpleName) && keyPackage != packageName) {
                        localImports.add(keyQualifiedName)
                    }
                }
                // 处理Map的Value类型导入
                type.arguments[1].type?.resolve()?.let { valueType -> // ✅ 获取 KSType
                    val valueTypeDecl = valueType.declaration
                    val valueSimpleName = valueTypeDecl.simpleName.asString()
                    val valueQualifiedName = valueTypeDecl.qualifiedName?.asString()
                    val valuePackage = valueQualifiedName?.substringBeforeLast('.', "")
                    if (valueQualifiedName != null && !kotlinStdTypes.contains(valueSimpleName) && valuePackage != packageName && !isJimmerEntity(valueTypeDecl)) {
                        localImports.add(valueQualifiedName)
                    }
                }
            }
        }
        val mydoc = if (prop.docString.isBlank()) "" else """
            /*
             * ${prop.docString}
             */
        """.trimIndent()
        val thdoc = mydoc


            val trimIndent = """
            $thdoc
            val $name: $typeStr  = $defaultValue 
       """.trimIndent()
            trimIndent


        val id = SettingContext.settings.id
        val string = if (name == id) """
                       $thdoc
            val $id: Long?  = null
        """.trimIndent() else trimIndent
        string
    }.joinToString(",\n    ")

    val finalImportStr = localImports.filter { importFqName ->
        // 排除当前包下的类型
        !importFqName.startsWith(packageName)
    }.distinct().sorted().joinToString("\n") { "import $it" }

    val code = """
@file:OptIn(ExperimentalTime::class)
        |package $packageName
        |
        |            
        |$finalImportStr
        |import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
        | @Serializable
        |data class $dataClassName(
        |    $props
        |)
    """.trimMargin()

    val file = File("$outputDir/${dataClassName}.kt")

    if (SettingContext.settings.skipExistsFiles.toBoolean()) {
        if (file.exists()) {
            return
        }

    }
    file.parentFile?.mkdirs()
    file.writeText(code)
    println("Generated isomorphic data class: $dataClassName at ${file.absolutePath}")
}
