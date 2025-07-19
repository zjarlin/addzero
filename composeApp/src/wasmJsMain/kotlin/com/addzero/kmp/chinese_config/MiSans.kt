package com.addzero.kmp.chinese_config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.window.ComposeViewport
import com.addzero.kmp.App
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.w3c.fetch.Response

private const val MiSans = "./MiSans-Normal.ttf"


//private const val MiSans = "./Font Awesome 5 Brands-Regular-400.otf"
//private const val MiSans1 = "./JetBrains Mono Regular Nerd Font Complete.ttf"
//private const val MiSans2 = "./MiSans-Normal.ttf"


fun ArrayBuffer.toTheByteArray(): ByteArray {
    val source = Int8Array(this, 0, byteLength)
    return jsInt8ArrayToKotlinByteArray(source)
}

suspend fun loadRes(url: String): JsAny {
    val await = window.fetch(url).await<Response>().arrayBuffer().await<ArrayBuffer>()
    return await
}


lateinit var miFontFamily: FontFamily

@Composable
 fun ChineseApp() {
    val fontFamilyResolver = LocalFontFamilyResolver.current
    val fontLodaded = remember { mutableStateOf(false) }
    if (fontLodaded.value) {
        App()
    }
    LaunchedEffect(Unit) {
        val miSansBytes = loadRes(MiSans).unsafeCast<ArrayBuffer>().toTheByteArray()
        val fontFamily = FontFamily(listOf(Font("MiSans", miSansBytes)))
//            val fontFamily = FontFamily(listOf(Font("Font Awesome 5", miSansBytes)))
        fontFamilyResolver.preload(fontFamily)
        miFontFamily = fontFamily
        fontLodaded.value = true
    }
}
