package io.github.composefluent.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.layout.IntervalList
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import site.addzero.component.flu.NavigationDisplayMode

@Composable
 fun LeftLayout(
    menuItems: NavigationMenuScope.() -> Unit,
    modifier: Modifier,
    autoSuggestBox: @Composable (NavigationAutoSuggestBoxScope.() -> Unit)?,
    title: @Composable () -> Unit,
    backButton: @Composable () -> Unit,
    expandedButton: @Composable () -> Unit,
    state: NavigationState,
    footerItemIntervals: IntervalList<NavigationViewMenuInterval>,
    displayMode: NavigationDisplayMode,
    contentPadding: PaddingValues,
    color: Color,
    border: BorderStroke?,
    pane: @Composable () -> Unit
) {
    val menuItemIntervals = rememberNavigationMenuInterval(menuItems)
    Row(modifier = modifier) {
        SideNav(
            autoSuggestionBox = autoSuggestBox,
            header = {
                SideNavHeaderArea(
                    title = title,
                    backButton = backButton,
                    expandButton = expandedButton
                )
            },
            expanded = state.expanded,
            onExpandStateChange = { state.expanded = it },
            indicatorState = state.indicatorState,
            footer = if (footerItemIntervals.size > 0) {
                {
                    val scope = ValueNavigationMenuItemScope(
                        displayMode = displayMode,
                        isFooter = true
                    )
                    footerItemIntervals.forEachItem { item(scope, it) }
                }
            } else {
                null
            },
            modifier = Modifier.Companion.fillMaxHeight()
        ) {
            val scope = ValueNavigationMenuItemScope(
                displayMode = displayMode,
                isFooter = false
            )
            menuItemIntervals.forEachItem { item(scope, it) }
        }
        Box(
            content = {
                Layer(
                    color = color,
                    border = border,
                    shape = RoundedCornerShape(topStart = FluentTheme.cornerRadius.overlay),
                    content = pane
                )
            },
            modifier = Modifier.Companion.weight(1f).padding(contentPadding)
        )
    }
}
