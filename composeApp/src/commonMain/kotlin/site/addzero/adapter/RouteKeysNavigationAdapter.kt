package site.addzero.adapter

import site.addzero.generated.RouteKeys
import site.addzero.youtrack.components.NavigationMetadata
import site.addzero.youtrack.components.NavigationMetadataProvider

/**
 * RouteKeys 到通用导航元数据的适配器
 * 将现有的 RouteKeys.allMeta 转换为 NavigationMetadata 接口
 */
class RouteKeysNavigationAdapter : NavigationMetadataProvider {
    override fun getAllNavigationItems(): List<NavigationMetadata> {
        return RouteKeys.allMeta.map { route ->
            RouteKeysNavigationItem(
                id = route.routePath,
                title = route.title,
                routePath = route.routePath,
                icon = route.icon,
                group = route.value,
                order = (route.order as? Number)?.toDouble() ?: 0.0,
                qualifiedName = route.qualifiedName
            )
        }
    }
}

/**
 * RouteKeys 导航项的实现
 */
data class RouteKeysNavigationItem(
    override val id: String,
    override val title: String,
    override val routePath: String,
    override val icon: String,
    override val group: String,
    override val order: Double,
    override val qualifiedName: String
) : NavigationMetadata