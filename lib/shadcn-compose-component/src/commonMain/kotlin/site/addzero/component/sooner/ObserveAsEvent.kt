package site.addzero.component.sooner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * 一个观察 [Flow] 并将每次发射视为一次性事件的可组合函数。
 *
 * 这对于需要响应来自 ViewModel 或应用程序其他部分的事件而无需重新组合整个
 * 可组合函数的场景很有用。事件仅在可组合函数处于 `STARTED` 生命周期状态时被消费。
 *
 * @param T 流发出的事件类型。
 * @param flow 要观察的事件 [Flow]。
 * @param key1 可选的键，如果其值发生变化则重新启动效果。
 * @param key2 另一个可选的键，如果其值发生变化则重新启动效果。
 * @param onEvent 将为 [flow] 发出的每个事件调用的 lambda 函数。
 */
@Composable
fun <T> ObserveAsEvent(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2, flow) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}
