package com.addzero.kmp.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import com.addzero.kmp.annotation.Route


@Composable
@Route("组件示例", "图片加载")
fun ImageLoad(
    modifier: Modifier = Modifier
) {
    val listOf = listOf<String>(
        "https://i.loli.net/2019/11/10/T7Mu8Aod3egmC4Q.png",
        "https://pic-go-bed.oss-cn-beijing.aliyuncs.com/img/20220316151929.png"
    )

    Column {
        listOf.map {
            AsyncImage(
                model = it,
                contentDescription = null,

                )
        }
    }


}
