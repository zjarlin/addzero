package com.addzero.kmp.demo.upload

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.addzero.kmp.annotation.Route

import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher


/**
 * wasm不支持
 * @return [Unit]
 */
@Composable
@Route("组件示例", title = "文件夹选择器")
fun TestDirectoryPicker() {
    // Pick files from Compose
    val launcher = rememberDirectoryPickerLauncher(
        title = "Pick a directory",
        initialDirectory = "/custom/initial/path"


    ) { directory ->
        directory?.path

        // Handle picked files
    }

// Use the pickerLauncher
    Button(onClick = { launcher.launch() }) {
        Text("Pick files")
    }
}