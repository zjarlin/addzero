package site.addzero.entity.analysis.analyzer

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import site.addzero.entity.analysis.model.EntityMetadata
import site.addzero.entity.analysis.model.EnumMetadata
import site.addzero.entity.analysis.model.PropertyMetadata
import site.addzero.entity.analysis.model.TypeAnalysisResult
import site.addzero.util.defaultValue
import site.addzero.util.getArgStr
import site.addzero.util.isJimmerEntity

/**
 * Jimmer 实体分析器
 *
 * 统一的 Jimmer 实体元数据解析工具
 * 负责分析实体结构、属性类型、注解等信息
 */
class JimmerEntityAnalyzer(
) {



    /**
     * 分析单个实体 (KSP原生版本)
     */
    fun analyzeEntity(entity: KSClassDeclaration): EntityMetadata {
            val className = entity.simpleName.asString()
            val packageName = entity.packageName.asString()
            val qualifiedName = entity.qualifiedName?.asString() ?: ""

            // 收集导入信息
            val imports = mutableSetOf<String>()

            // 提取实体描述
            val description = extractEntityDescription(entity)
            // 分析属性
            val properties = entity.getAllProperties().map { prop ->
                analyzeProperty(prop, imports)
            }.toList()
            return EntityMetadata(
                className = className,
                packageName = packageName,
                qualifiedName = qualifiedName,
                properties = properties,
                imports = imports,
                description = description
            )

    }


    /**
     * 提取实体描述信息 (KSP原生版本)
     */
    private fun extractEntityDescription(entity: KSClassDeclaration): String {
        entity.annotations.forEach { annotation ->
            val annotationName = annotation.shortName.asString()

            // 支持的描述注解类型（使用简单名称）
            when (annotationName) {
                "Schema" -> {
                    // Swagger/OpenAPI @Schema(description = "...")
                    val description = annotation.getArgStr("description")
                    if (description.isNotBlank()) return description
                }

                "ApiModel" -> {
                    // Swagger @ApiModel(description = "...")
                    val description = annotation.getArgStr("description")
                    if (description.isNotBlank()) return description
                }

                "Entity" -> {
                    // JPA @Entity(description = "...")
                    val description = annotation.getArgStr("description")
                    if (description.isNotBlank()) return description
                }

                "Table" -> {
                    // JPA @Table(comment = "...")
                    val description = annotation.getArgStr("comment")
                    if (description.isNotBlank()) return description
                }

                "Comment" -> {
                    // 自定义 @Comment("...")
                    val description = annotation.getArgStr("value")
                    if (description.isNotBlank()) return description
                }

                "Description" -> {
                    // 自定义 @Description("...")
                    val description = annotation.getArgStr("value")
                    if (description.isNotBlank()) return description
                }

                "JsonPropertyDescription" -> {
                    // Jackson @JsonPropertyDescription("...")
                    val description = annotation.getArgStr("value")
                    if (description.isNotBlank()) return description
                }
            }
        }

        // 如果没有找到注解描述，使用预定义的映射规则
        return getDefaultEntityDescription(entity.simpleName.asString())
    }

    /**
     * 获取默认的实体描述
     */
    private fun getDefaultEntityDescription(className: String): String {
        return when (className) {
            "SysDict" -> "字典"
            "SysUser" -> "用户"
            "SysDept" -> "部门"
            "SysRole" -> "角色"
            "BizNote" -> "笔记"
            "BizTag" -> "标签"
            "BizDotfiles" -> "配置文件"
            "SysWeather" -> "天气"
            "SysDictItem" -> "字典项"
            "SysAiPrompt" -> "AI提示词"
            else -> {
                // 默认转换：去掉Sys/Biz前缀，转换为中文描述
                val simpleName = className
                    .removePrefix("Sys")
                    .removePrefix("Biz")

                // 这里可以添加更多的转换规则
                simpleName.lowercase()
            }
        }
    }


    /**
     * 分析属性 (KSP原生版本)
     */
    fun analyzeProperty(prop: KSPropertyDeclaration, imports: MutableSet<String>): PropertyMetadata {
        val name = prop.simpleName.asString()
        val type = prop.type.resolve()
        val typeName = type.declaration.simpleName.asString()
        val qualifiedTypeName = type.declaration.qualifiedName?.asString()
        // ID 字段在同构体中强制可空，不论原实体中是否可空
        val isNullable = type.isMarkedNullable || name.equals("id", ignoreCase = true)

        // 收集注解
        val annotations = prop.annotations.map { it.shortName.asString() }.toList()

        // 类型分析（处理无法解析的类型）
        val typeAnalysis = try {
            analyzeType(type)
        } catch (e: Exception) {
            // 返回一个默认的类型分析结果
            TypeAnalysisResult(
                isJimmerEntity = false,
                isEnum = false,
                isCollection = false,
                isBasicType = false,
                isDateTimeType = false
            )
        }

        // 生成 ISO 类型名
        val isoTypeName = generateIsoTypeName(type, typeAnalysis)

        // 生成默认值
        val enumMetadata = if (typeAnalysis.isEnum) {
            try {
                analyzeEnum(type.declaration as KSClassDeclaration)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else null
        val defaultValue =prop.defaultValue

//         generateDefaultValue(type, isNullable, typeAnalysis, enumMetadata)

        // 生成标签
        val label = generateLabel(name)

        // 判断是否必填
        val isRequired = !isNullable && !hasDefaultValue(prop)

        // 查找 @LabelProp 字段
        val labelPropField = findLabelPropField(type, typeAnalysis)

        // 添加必要的导入（排除 Jimmer 实体）
        addImports(imports, qualifiedTypeName, typeAnalysis)

        return PropertyMetadata(
            name = name,
            typeName = typeName,
            qualifiedTypeName = qualifiedTypeName,
            isNullable = isNullable,
            annotations = annotations,
            isJimmerEntity = typeAnalysis.isJimmerEntity,
            isEnum = typeAnalysis.isEnum,
            isCollection = typeAnalysis.isCollection,
            isBasicType = typeAnalysis.isBasicType,
            genericType = typeAnalysis.genericType,
            genericQualifiedType = typeAnalysis.genericQualifiedType,
            isoTypeName = isoTypeName,
            defaultValue = defaultValue,
            label = label,
            isRequired = isRequired,
            labelPropField = labelPropField
        )
    }


    /**
     * 分析类型 (KSP原生版本)
     */
    fun analyzeType(type: KSType): TypeAnalysisResult {
        val declaration = type.declaration
        val typeName = declaration.simpleName.asString()
        val isJimmerEntity = isJimmerEntity(declaration)
        val isEnum = try {
            declaration is KSClassDeclaration &&
                    declaration.classKind == ClassKind.ENUM_CLASS
        } catch (e: Exception) {
            // 如果无法解析类型，根据名称判断是否是枚举
            typeName.startsWith("Enum") || typeName.endsWith("Enum")
        }
        val isCollection = isCollectionType(typeName)
        val isBasicType = isBasicType(typeName)
        val isDateTimeType = isDateTimeType(typeName)

        // 泛型信息
        val (genericType, genericQualifiedType) = if (isCollection && type.arguments.isNotEmpty()) {
            val genericArg = type.arguments.first().type?.resolve()
            genericArg?.declaration?.simpleName?.asString() to genericArg?.declaration?.qualifiedName?.asString()
        } else {
            null to null
        }

        return TypeAnalysisResult(
            isJimmerEntity = isJimmerEntity,
            isEnum = isEnum,
            isCollection = isCollection,
            isBasicType = isBasicType,
            isDateTimeType = isDateTimeType,
            genericType = genericType,
            genericQualifiedType = genericQualifiedType
        )
    }

    /**
     * 分析枚举
     */
    fun analyzeEnum(enumClass: KSClassDeclaration): EnumMetadata {
        val className = enumClass.simpleName.asString()
        val packageName = enumClass.packageName.asString()
        val qualifiedName = enumClass.qualifiedName?.asString() ?: ""

        val enumConstants = enumClass.declarations
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.classKind == ClassKind.ENUM_ENTRY }
            .map { it.simpleName.asString() }
            .toList()

        val firstValue = enumConstants.firstOrNull()?.let { "$className.$it" }

        return EnumMetadata(
            className = className,
            packageName = packageName,
            qualifiedName = qualifiedName,
            values = enumConstants,
            firstValue = firstValue
        )
    }

    // ========== 辅助方法 ==========

    private fun isCollectionType(typeName: String): Boolean {
        return typeName in setOf("List", "Set", "MutableList", "MutableSet", "Collection")
    }

    private fun isBasicType(typeName: String): Boolean {
        return typeName in setOf(
            "String", "Int", "Long", "Double", "Float", "Boolean",
            "BigDecimal"
        )
    }

    private fun isDateTimeType(typeName: String): Boolean {
        return typeName in setOf("LocalDate", "LocalDateTime", "Instant")
    }


    private fun generateIsoTypeName(type: KSType, typeAnalysis: TypeAnalysisResult): String {
        return when {
            typeAnalysis.isCollection && typeAnalysis.genericType != null -> {
                val genericDecl = type.arguments.first().type?.resolve()?.declaration
                val genericIsoType = if (genericDecl != null && isJimmerEntity(genericDecl)) {
                    // Jimmer 实体类型：转换为同构体类型
                    "${typeAnalysis.genericType}Iso"
                } else {
                    // 处理日期时间类型
                    when (typeAnalysis.genericType) {
                        "LocalDateTime", "LocalDate", "Instant" -> "kotlinx.datetime.${typeAnalysis.genericType}"
                        else -> typeAnalysis.genericType!!
                    }
                }
                "List<$genericIsoType>"
            }

            typeAnalysis.isJimmerEntity -> {
                // Jimmer 实体类型：转换为同构体类型
                "${type.declaration.simpleName.asString()}Iso"
            }

            else -> {
                val typeName = type.declaration.simpleName.asString()
                // 处理日期时间类型
                when (typeName) {
                    "LocalDateTime", "LocalDate", "Instant" -> "kotlinx.datetime.$typeName"
                    else -> typeName
                }
            }
        }
    }



    private fun generateLabel(name: String): String {
        // 简单的驼峰转换为可读标签
        return "\"${name.replace(Regex("([a-z])([A-Z])"), "$1 $2").lowercase().replaceFirstChar { it.uppercase() }}\""
    }

    private fun hasDefaultValue(prop: KSPropertyDeclaration): Boolean {
        // 简化实现
        return prop.hasBackingField && prop.getter == null
    }

    private fun findLabelPropField(type: KSType, typeAnalysis: TypeAnalysisResult): String? {
        return when {
            typeAnalysis.isJimmerEntity -> {
                findLabelPropInClass(type.declaration as KSClassDeclaration)
            }

            typeAnalysis.isCollection && typeAnalysis.genericType != null -> {
                // 对于集合类型，查找泛型类型的 @LabelProp
                type.arguments.firstOrNull()?.type?.resolve()?.declaration?.let { decl ->
                    if (isJimmerEntity(decl)) {
                        findLabelPropInClass(decl as KSClassDeclaration)
                    } else null
                }
            }

            else -> null
        }
    }

    private fun findLabelPropInClass(classDecl: KSClassDeclaration): String? {
        return try {
            val labelProperties = classDecl.getAllProperties().filter { property ->
                property.annotations.any { annotation ->
                    annotation.shortName.asString() == "LabelProp"
                }
            }.toList()

            if (labelProperties.isNotEmpty()) {
                val selectedProperty = labelProperties.firstOrNull { property ->
                    property.simpleName.asString().isNotBlank()
                } ?: labelProperties.first()

                selectedProperty.simpleName.asString()
            } else {
                "name" // 默认值
            }
        } catch (e: Exception) {
            println("查找 @LabelProp 失败: ${classDecl.simpleName.asString()}, 使用默认值")
            "name" // 出错时使用默认值
        }
    }

    private fun addImports(imports: MutableSet<String>, qualifiedTypeName: String?, typeAnalysis: TypeAnalysisResult) {
        // 只添加非 Jimmer 实体的导入
        qualifiedTypeName?.let { qualifiedType ->
            if (!typeAnalysis.isJimmerEntity && !isJimmerEntityByName(qualifiedType)) {
                imports.add(qualifiedType)
            }
        }

        // 泛型类型：只添加非 Jimmer 实体的导入
        typeAnalysis.genericQualifiedType?.let { genericQualifiedType ->
            // 检查泛型类型是否是 Jimmer 实体
            if (!isJimmerEntityByName(genericQualifiedType)) {
                imports.add(genericQualifiedType)
            }
        }
    }

    /**
     * 根据类名判断是否是 Jimmer 实体
     */
    private fun isJimmerEntityByName(qualifiedName: String): Boolean {
        // 简单的启发式判断：Jimmer 实体通常在 entity 包下
        return qualifiedName.contains(".entity.") ||
                qualifiedName.contains(".modules.") ||
                qualifiedName.startsWith("site.addzero.web.modules.")
    }

    /**
     * 根据枚举名称生成默认值（用于无法解析的枚举）
     */
    private fun generateEnumDefaultByName(enumName: String): String {
        // 对于无法解析的枚举，生成一个通用的默认值
        // 这里可以根据具体的枚举名称进行特殊处理
        return " $enumName.entrys.first()"
    }
}
