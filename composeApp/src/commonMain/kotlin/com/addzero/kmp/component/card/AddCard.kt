
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AddCard(
    modifier: Modifier = Modifier, elevation: Dp = 4.dp, shape: Shape = MaterialTheme.shapes.medium, colors: CardColors = CardDefaults.cardColors(), paddingValues: PaddingValues = PaddingValues(16.dp), border: BorderStroke? = null, onClick: (() -> Unit)? = null, header: @Composable (() -> Unit)? = null, title: @Composable (() -> Unit)? = null, subtitle: @Composable (() -> Unit)? = null, footer: @Composable (() -> Unit)? = null, actionButtons: @Composable RowScope.() -> Unit = {}, content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = modifier.clickable(
            interactionSource = interactionSource, indication = if (onClick != null) LocalIndication.current else null, onClick = { onClick?.invoke() }), shape = shape, elevation = CardDefaults.cardElevation(defaultElevation = elevation), colors = colors, border = border) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            header?.let {
                Box(
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    it()
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(paddingValues)
            ) {
                title?.let {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        it()
                    }
                }

                subtitle?.let {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    ) {
                        it()
                    }
                }

                content()

                if (footer != null || actionButtons != {}) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                    ) {
                        footer?.let {
                            Box(modifier = Modifier.weight(1f)) {
                                it()
                            }
                        }

                        actionButtons()
                    }
                }
            }
        }
    }
}