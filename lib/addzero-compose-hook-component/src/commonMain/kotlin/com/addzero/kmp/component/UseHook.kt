package  com.addzero.kmp.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * @author zjarlin
 * @date 2025/04/02
 * @constructor 创建[UseHook]
 */
interface UseHook<T : UseHook<T>> {

    //      var  xxxxxx  by    mutableStateOf(value)

    val modifier: Modifier
        get() = Modifier

    val state: T
        get() = this as T


    @Composable
    fun rememberState(): T {
        val remember = remember { state }
        return remember
    }

    val render: @Composable () -> Unit

    val onDispose: (T) -> Unit
        get() = {}

    @Composable
    fun render(block: @Composable T.() -> Unit) {
        val state = rememberState()

        // 使用DisposableEffect确保在组件离开组合时调用dispose方法
        DisposableEffect(state) {
            onDispose {
                onDispose(state)
            }
        }

        block(state)
        state.render()
    }
}
