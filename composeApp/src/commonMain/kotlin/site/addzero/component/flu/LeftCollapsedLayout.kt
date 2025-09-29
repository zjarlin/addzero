package site.addzero.component.flu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.layout.IntervalList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import io.github.composefluent.animation.FluentDuration
import io.github.composefluent.animation.FluentEasing
import io.github.composefluent.background.BackgroundSizing
import io.github.composefluent.background.ElevationDefaults
import io.github.composefluent.background.Layer
import io.github.composefluent.background.MaterialContainer
import io.github.composefluent.background.MaterialDefaults

@Composable
 fun LeftCollapsedLayout(
    modifier: Modifier,
    state: NavigationState,
    contentPadding: PaddingValues,
    pane: @Composable () -> Unit,
    autoSuggestBox: @Composable (NavigationAutoSuggestBoxScope.() -> Unit)?,
    footerItemIntervals: IntervalList<NavigationViewMenuInterval>,
    displayMode: NavigationDisplayMode,
    menuItems: NavigationMenuScope.() -> Unit,
    title: @Composable () -> Unit,
    backButton: @Composable () -> Unit,
    color: Color,
    border: BorderStroke?,
    expandedButton: @Composable () -> Unit
) {
    MaterialContainer(modifier = modifier) {
        Box(
            content = {
                Layer(
                    color = color,
                    border = border,
                    shape = FluentTheme.shapes.intersectionEdge,
                    content = pane
                )
            },
            modifier = Modifier.Companion
                .behindMaterial()
                .then(
                    if (state.expanded) {
                        Modifier.Companion.pointerInput(Unit) {
                            awaitEachGesture {
                                awaitFirstDown(pass = PointerEventPass.Initial)
                                waitForUpOrCancellation(PointerEventPass.Initial)?.let {
                                    it.consume()
                                    state.expanded = false
                                }
                            }
                        }
                    } else {
                        Modifier.Companion
                    }
                ).padding(contentPadding)
        )
        val expandedTransition = updateTransition(state.expanded)

        var backButtonSize by remember { mutableStateOf(IntSize.Companion.Zero) }
        var expandedButtonSize by remember { mutableStateOf(IntSize.Companion.Zero) }
        AnimatedVisibility(
            visible = state.expanded,
            enter = expandHorizontally(
                animationSpec = tween(
                    durationMillis = FluentDuration.ShortDuration,
                    easing = FluentEasing.FastInvokeEasing,
                    delayMillis = FluentDuration.QuickDuration
                )
            ),
            exit = shrinkHorizontally(
                animationSpec = tween(
                    durationMillis = FluentDuration.ShortDuration,
                    easing = FluentEasing.FastDismissEasing,
                    delayMillis = FluentDuration.QuickDuration
                )
            )
        ) {
            Layer(
                backgroundSizing = BackgroundSizing.InnerBorderEdge,
                border = BorderStroke(1.dp, FluentTheme.colors.stroke.surface.flyout),
                color = Color.Companion.Transparent,
                shape = FluentTheme.shapes.overlay,
                elevation = expandedTransition.animateDp(
                    targetValueByState = { if (it) ElevationDefaults.flyout else 0.dp },
                    transitionSpec = {
                        tween(
                            durationMillis = FluentDuration.ShortDuration,
                            easing = FluentEasing.FadeInFadeOutEasing,
                            delayMillis = FluentDuration.QuickDuration
                        )
                    }
                ).value,
                modifier = Modifier.Companion.clickable(
                    interactionSource = null,
                    onClick = {},
                    indication = null
                )
            ) {
                FlyoutContentLayout(
                    shape = FluentTheme.shapes.overlay,
                    material = MaterialDefaults.acrylicDefault(),
                    contentPadding = PaddingValues()
                ) {
                    SideNav(
                        expanded = true,
                        autoSuggestionBox = autoSuggestBox,
                        onExpandStateChange = { state.expanded = it },
                        header = {
                            SideNavHeaderArea(
                                title = {},
                                backButton = {
                                    Spacer(
                                        modifier = Modifier.Companion.size(
                                            size = with(LocalDensity.current) {
                                                backButtonSize.toSize().toDpSize()
                                            }
                                        )
                                    )
                                },
                                expandButton = {
                                    Spacer(
                                        modifier = Modifier.Companion.size(
                                            size = with(LocalDensity.current) {
                                                expandedButtonSize.toSize().toDpSize()
                                            }
                                        )
                                    )
                                },
                                displayMode = NavigationDisplayMode.LeftCollapsed
                            )
                        },
                        indicatorState = state.indicatorState,
                        footer = if (footerItemIntervals.size > 0) {
                            {
                                val scope = ValueNavigationMenuItemScope(
                                    displayMode = displayMode,
                                    isFooter = true,
                                    onFlyoutDismissRequest = { state.expanded = false }
                                )
                                footerItemIntervals.forEachItem { item(scope, it) }
                            }
                        } else {
                            null
                        },
                        content = {
                            val menuItemIntervals = rememberNavigationMenuInterval(menuItems)
                            val scope =
                                ValueNavigationMenuItemScope(
                                    displayMode = displayMode,
                                    isFooter = true,
                                    onFlyoutDismissRequest = { state.expanded = false }
                                )
                            menuItemIntervals.forEachItem { item(scope, it) }
                        }
                    )
                }
            }
        }
        SideNavHeaderArea(
            title = title,
            backButton = {
                Box(
                    content = { backButton() },
                    modifier = Modifier.Companion.onSizeChanged { backButtonSize = it }
                )
            },
            expandButton = {
                Box(
                    content = { expandedButton() },
                    modifier = Modifier.Companion.onSizeChanged { expandedButtonSize = it }
                )
            },
            modifier = Modifier.Companion.padding(top = 4.dp),
            displayMode = NavigationDisplayMode.LeftCollapsed
        )
    }
}
