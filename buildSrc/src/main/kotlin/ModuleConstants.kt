/**
 * 项目模块名称常量
 * 这个文件可以在 settings.gradle.kts 中使用
 */
object ModuleConstants {
    // 主要模块
    const val BACKEND = "backend"
    const val COMPOSE_APP = "composeApp"
    const val SHARED = "shared"
    const val LIB = "lib"

    // KSP 支持模块
    val KSP_SUPPORT = listOf(
        "addzero-ksp-support",
        "addzero-ksp-support-jdbc"
    )

    // 实体分析与代码生成
    val ENTITY_ANALYSIS = listOf(
        "addzero-entity2analysed-support",
        "addzero-entity2iso-processor",
        "addzero-entity2form-processor",
        "addzero-entity2form-core"
    )

    // JDBC 相关处理器
    val JDBC_PROCESSORS = listOf(
        "addzero-jdbc2controller-processor",
        "addzero-jdbc2enum-processor"
    )

    // API 相关处理器
    val API_PROCESSORS = listOf(
        "addzero-controller2api-processor",
        "addzero-apiprovider-processor"
    )

    // 路由模块
    val ROUTE_MODULES = listOf(
        "addzero-route-processor",
        "addzero-route-core"
    )

    // Compose 相关模块
    val COMPOSE_MODULES = listOf(
        "addzero-compose-props-annotations",
        "addzero-compose-props-processor"
    )

    // 工具模块
    val TOOL_MODULES = listOf(
        "addzero-tool"
    )

    // 网络模块（暂时注释）
    val NETWORK_MODULES = listOf(
        "addzero-network-starter"
    )
}
