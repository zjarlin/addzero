import com.addzero.kmp.context.SettingContext
import com.addzero.kmp.context.SettingContext.settings
import com.addzero.kmp.context.isomorphicShareOutPutDir
import com.addzero.kmp.util.filterBaseEntity
import com.addzero.kmp.util.isJimmerEntity
import com.addzero.kmp.util.kotlinStdTypes
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import java.io.File


fun generateDslReceiver(classDef: KSClassDeclaration): String {
    val className = classDef.simpleName.asString()
    return """
        class ${className}FormDsl(
            val state: MutableState<${className}Iso>,
            private val renderMap: MutableMap<String, @Composable () -> Unit>
        ) {
            ${
        classDef.getAllProperties().joinToString("\n") { prop ->
            val propName = prop.simpleName.asString()
            val type = prop.type.resolve()
            var propType = getIsoTypeString(
                type,
                SettingContext.settings.isomorphicShareOutPutDir,
                mutableSetOf(),
                settings.isomorphicPackageName
            )
            propType = if (propName == SettingContext.settings.id) "Long?" else propType
            val nullableSuffix = if (type.isMarkedNullable) "?" else ""

            """
                    fun $propName(
                        hidden: Boolean = false,
                        render:  (@Composable ( MutableState<${className}Iso>) -> Unit)? = null
                    ) {
                        when {
                            hidden -> renderMap["$propName"] = {}
                            render != null -> renderMap["$propName"] = {
                                   render(state)
                            }
                        }
                    }
                    
                    
                    """.trimIndent()
        }
    }
            
            fun hide(vararg fields: String) {
                fields.forEach { renderMap[it] = {} }
            }
        }
    """.trimIndent()
}

fun generateIsomorphicForm(

    ksClass: KSClassDeclaration, outputDir: String = "composeApp/src/commonMain/kotlin/com/addzero/kmp/forms",
    generatedIsoClasses: MutableSet<String> = mutableSetOf<String>(), // 跟踪所有已生成同构体的FQCN
    packageName: String = settings.formPackageName // 生成的同构体文件所在的包名
) {
    val className = ksClass.simpleName.asString()

    val isoClassName = "${className}Iso"
    val dataClassName = "${className}Form"
    val currentIsoFqName = "$packageName.$dataClassName"

    // 如果该同构体已被生成，则直接返回，避免重复
    if (currentIsoFqName in generatedIsoClasses) {
        return
    }
    generatedIsoClasses += currentIsoFqName

    // 收集当前同构体文件需要的所��导入
//    val localImports = mutableSetOf<String>()

    val filterProps = ksClass.getAllProperties()
        .filter {
            val dDlRangeContext = it.simpleName.asString()
            filterBaseEntity(dDlRangeContext)
        }.filter { it.simpleName.asString() != "deleted" }


    val effectFormMaps = filterProps.map { prop ->
        val name = prop.simpleName.asString()
        """
            ${className}FormProps.$name to { ${prop.generateDifferentTypes()} }
        
       """.trimIndent()


    }.joinToString(",\n")

    val mapofeffectFormMaps = """
              val defaultRenderMap = mutableMapOf<String, @Composable () -> Unit>(
$effectFormMaps 
 )
       """.trimIndent()


    val rememberFun = """
        @Composable
        fun remember${className}FormState(current:${isoClassName}?=null): MutableState<${isoClassName}> {
            return remember (current){ mutableStateOf(current?: ${isoClassName} ()) }
        }
        """.trimIndent()


    val propconsts = """
object ${className}FormProps {
${
        filterProps.joinToString("\n") {

            "const val ${it.simpleName.asString()} = \"${it.simpleName.asString()}\""
        }.trimIndent()
    }
}
        """.trimIndent()


    val effectFormFun = """
        @Composable
        fun ${className}Form(
        state: MutableState<${isoClassName}>,
   visible: Boolean,
            title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
     dslConfig: ${className}FormDsl.() -> Unit = {}
        
        ) {
        
           val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    ${className}FormDsl(state, renderMap).apply(dslConfig) 
        
        
       $mapofeffectFormMaps 
       
          val finalItems = remember(renderMap) {
        defaultRenderMap
            .filterKeys { it !in renderMap } // 未被DSL覆盖的字段
            .plus(renderMap.filterValues { it != {} }) // 添加非隐藏的自定义字段
    }.values.toList() 
       
       
    val items = finalItems
 
        
           AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,

        ) {
            AddMultiColumnContainer(
                howMuchColumn = 2,
                items =items
            )
        }
 
        
        
        
        }
        """.trimIndent()


    val code = """
            package ${settings.formPackageName}
            import androidx.compose.material.icons.Icons
            import androidx.compose.foundation.layout.*
            import androidx.compose.material3.*
            import androidx.compose.runtime.*
            import androidx.compose.ui.Modifier
            import androidx.compose.ui.unit.dp
            import com.addzero.kmp.component.high_level.AddMultiColumnContainer
                       import com.addzero.kmp.component.drawer.AddDrawer
 
            import com.addzero.kmp.component.high_level.AddFormContainer
            import com.addzero.kmp.enums.RegexEnum
            import androidx.compose.material.icons.filled.*
            import com.addzero.kmp.component.form.*
           import com.addzero.kmp.component.form.number.*
import com.addzero.kmp.component.form.date.*
 
            import androidx.compose.ui.Alignment
            import com.addzero.kmp.core.ext.parseObjectByKtx
            import com.addzero.kmp.isomorphic.*
${generateDslReceiver(ksClass)}
$propconsts
$rememberFun
$effectFormFun
""".trimMargin()
    val file = File("$outputDir/${dataClassName}.kt")

    //这里form文件一定要存在就跳过
//    if (file.exists()) {
//        return
//    }

    file.parentFile?.mkdirs()
    file.writeText(code)
}

private fun autoImport(typeDecl: KSDeclaration, typeName: String, qualifiedName: String?, packageName: String, jimmerEntityPkg: String, localImports: MutableSet<String>, type: KSType) {
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
}
