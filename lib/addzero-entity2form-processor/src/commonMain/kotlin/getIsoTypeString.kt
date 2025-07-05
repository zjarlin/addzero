import com.addzero.kmp.util.isJimmerEntity
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType

// 获取类型在同构体中的字符串表示
// 它不再收集导入，只返回类型字符串
internal fun getIsoTypeString(
    type: KSType, outputDir: String, generatedIsoClasses: MutableSet<String>, // 跟踪所有已生成同构体的FQCN
    packageName: String // 生成的同构体文件所在的包名
): String {


    val typeDecl = type.declaration
    val typeName = typeDecl.simpleName.asString()
    val isNullable = type.isMarkedNullable
    val isJimmer = isJimmerEntity(typeDecl)
    val typeArgs = type.arguments

    // 递归处理��型参数
    val typeArgsStr = if (typeArgs.isNotEmpty()) {
        typeArgs.joinToString(", ") { arg ->
            arg.type?.resolve()?.let {
                getIsoTypeString(it, outputDir, generatedIsoClasses, packageName)
            } ?: "Any"
        }
    } else ""

    return when {
        // 处理集合类型，泛型参数会被递归处理并附带Iso后缀
        (typeName == "List" || typeName == "Set" || typeName == "Map") && typeArgs.isNotEmpty() -> {
            "${typeName}<${typeArgsStr}>${if (isNullable) "?" else ""}"
        }
        // 处理Jimmer实体类型，递归生成同构体并返回带Iso后缀的类型名
        isJimmer -> {
            generateIsomorphicDataClass(typeDecl as KSClassDeclaration, outputDir, generatedIsoClasses, packageName, isoSuffix = "Iso")
            "${typeName}Iso${if (isNullable) "?" else ""}"
        }
        // 其他类型（包括枚举和普通类），直接返回其简单名称
        else -> {
            "${typeName}${if (isNullable) "?" else ""}"
        }
    }
}