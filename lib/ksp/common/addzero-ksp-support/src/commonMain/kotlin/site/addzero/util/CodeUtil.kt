package site.addzero.util

import java.io.File

fun genCode(pathname: String, code: String, skipExistFile: Boolean = false) {

    val targetFile = File(pathname)
    targetFile.parentFile?.mkdirs()
    if (skipExistFile) {
        if (targetFile.exists()) {
            return
        }
    }
    targetFile.writeText(code)
}
