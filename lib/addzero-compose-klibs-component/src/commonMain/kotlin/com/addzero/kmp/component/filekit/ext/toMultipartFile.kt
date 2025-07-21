package com.addzero.kmp.component.filekit.ext

import com.addzero.kmp.util.WebUtil.getContentType
import io.github.vinceglb.filekit.core.PlatformFile
import io.ktor.client.request.forms.*
import io.ktor.http.*

suspend fun PlatformFile.toMultipartFile(): MultiPartFormDataContent {
    val file = this
    val fileBytes = file.readBytes() // 正确处理挂起函数

    val content = MultiPartFormDataContent(
        formData {
            append("fileName", file.name)
            append(
                "file", fileBytes, Headers.build {
                    // 根据文件类型设置正确的ContentType，而不是硬编码为image/png
                    append(HttpHeaders.ContentType, getContentType(file.name))
                    append(
                        HttpHeaders.ContentDisposition, "filename=${file.name}"
                    )
                })
        }
    )
    return content
}
