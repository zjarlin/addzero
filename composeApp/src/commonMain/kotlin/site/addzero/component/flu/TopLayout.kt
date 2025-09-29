package site.addzero.component.flu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.layout.IntervalList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.composefluent.component.TopNavigationMenuScope

@Composable
 fun TopLayout(
    modifier: Modifier,
    autoSuggestBox: @Composable (NavigationAutoSuggestBoxScope.() -> Unit)?,
    state: NavigationState,
    footerItemIntervals: IntervalList<NavigationViewMenuInterval>,
    displayMode: NavigationDisplayMode,
    menuItems: NavigationMenuScope.() -> Unit,
    contentPadding: PaddingValues,
    color: Color,
    border: BorderStroke?,
    pane: @Composable () -> Unit
) {
    Column(
        content = {
            val expandedState = remember { mutableStateOf(false) }
            TopNav(
                expanded = expandedState.value,
                onExpandedChanged = { expandedState.value = it },
                autoSuggestBox = autoSuggestBox,
                indicatorState = state.indicatorState,
                footer = if (footerItemIntervals.size > 0) {
                    {
                        val scope = ValueNavigationMenuItemScope(
                            displayMode = displayMode,
                            isFooter = true,
                            onFlyoutDismissRequest = { expandedState.value = false }
                        )
                        footerItemIntervals.forEachItem { item(scope, it) }
                    }
                } else {
                    null
                }
            ) {
                TopNavigationMenuScope(
                    overflowRowScope = this,
                    onFlyoutDismissRequest = { expandedState.value = false }
                ).menuItems()
            }
            Layer(
                modifier = Modifier.Companion.weight(1f).padding(contentPadding),
                shape = FluentTheme.shapes.intersectionEdge,
                color = color,
                border = border,
                content = pane
            )
        },
        modifier = modifier
    )
}
