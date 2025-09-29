package site.addzero.component.flu

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import io.github.composefluent.component.IndicatorState
import site.addzero.component.flu.scheme.PentaVisualScheme

internal val LocalNavigationExpand = compositionLocalOf { false }
internal val LocalNavigationLevel = compositionLocalOf { 0 }
internal val LocalIndicatorState = compositionLocalOf<IndicatorState?> { null }

typealias NavigationItemColorScheme = PentaVisualScheme<NavigationItemColor>

/**
 * Represents the color scheme for a navigation item.
 *
 * @property fillColor The fill color of the navigation item.
 * @property contentColor The color of the content (text, icons) within the navigation item.
 * @property indicatorColor The color of the indicator associated with the navigation item.
 * @property trailingColor The color of trailing elements in the navigation item.
 */
@Immutable
data class NavigationItemColor(
    val fillColor: Color,
    val contentColor: Color,
    val indicatorColor: Color,
    val trailingColor: Color
)

/**
 * Enumeration class for defining the display mode of the navigation view.
 *
 * - **Top**: The navigation menu is displayed at the top of the screen.
 * - **Left**: The navigation menu is displayed on the left side of the screen.
 * - **LeftCompact**: The navigation menu is displayed in a compact mode on the left side.
 * - **LeftCollapsed**: The navigation menu is displayed in a collapsed mode on the left side.
 */
enum class NavigationDisplayMode {
    Top,
    Left,
    LeftCompact,
    LeftCollapsed
}
