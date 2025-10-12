package site.addzero.youtrack.components

/**
 * 导航项元数据接口
 * 实现此接口可以解耦对具体 RouteKeys 的依赖
 */
interface NavigationMetadata {
    val id: String
    val title: String
    val routePath: String
    val icon: String
    val group: String
    val order: Double
    val qualifiedName: String
}

/**
 * 导航元数据提供者接口
 */
interface NavigationMetadataProvider {
    fun getAllNavigationItems(): List<NavigationMetadata>
}

