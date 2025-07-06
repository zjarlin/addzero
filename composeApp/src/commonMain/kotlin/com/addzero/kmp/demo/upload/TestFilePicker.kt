package com.addzero.kmp.demo.upload

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.addzero.kmp.processor.annotation.Route
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode


@Composable
@Route("组件示例", title = "文件选择器")
fun TestPicker() {
    // Pick files from Compose
    val launcher = rememberFilePickerLauncher(mode = PickerMode.Multiple()) { files ->
        val map = files?.map { it.name }
        println(map)
        // Handle picked files
    }


// Use the pickerLauncher
    Button(onClick = { launcher.launch() }) {
        Text("Pick files")
    }
}