package io.github.composefluent.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import site.addzero.component.flu.NavigationDisplayMode

@Immutable
 class TopNavigationMenuScope(
    private val overflowRowScope: OverflowRowScope,
    onFlyoutDismissRequest: () -> Unit = {}
) : NavigationMenuScope {

    private val overflowItemScope =
        ValueNavigationMenuItemScope(
            displayMode = NavigationDisplayMode.Left,
            isFooter = false,
            onFlyoutDismissRequest = onFlyoutDismissRequest
        )

    private val normalItemScope = ValueNavigationMenuItemScope(
        displayMode = NavigationDisplayMode.Top,
        isFooter = false
    )

    override fun item(
        key: Any?,
        contentType: Any?,
        content: @Composable NavigationMenuItemScope.() -> Unit
    ) {
        overflowRowScope.item(
            key = key,
            contentType = contentType,
            content = {
                if (isOverflow) {
                    content(overflowItemScope)
                } else {
                    content(normalItemScope)
                }
            }
        )
    }

    override fun items(
        count: Int,
        key: ((index: Int) -> Any)?,
        contentType: (index: Int) -> Any?,
        itemContent: @Composable NavigationMenuItemScope.(index: Int) -> Unit
    ) {
        overflowRowScope.items(
            count = count,
            key = key,
            contentType = contentType,
            itemContent = { index ->
                if (isOverflow) {
                    itemContent(overflowItemScope, index)
                } else {
                    itemContent(normalItemScope, index)
                }
            }
        )
    }
}
