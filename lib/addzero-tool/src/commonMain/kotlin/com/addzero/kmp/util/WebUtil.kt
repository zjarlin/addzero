package com.addzero.kmp.util

object WebUtil {
    /**
     * 根据文件名获取ContentType
     */
    fun getContentType(fileName: String): String {
        return when {
            fileName.endsWith(
                ".xlsx",
                ignoreCase = true
            ) -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

            fileName.endsWith(".xls", ignoreCase = true) -> "application/vnd.ms-excel"
            fileName.endsWith(".csv", ignoreCase = true) -> "text/csv"
            fileName.endsWith(".png", ignoreCase = true) -> "image/png"
            fileName.endsWith(".jpg", ignoreCase = true) || fileName.endsWith(
                ".jpeg",
                ignoreCase = true
            ) -> "image/jpeg"

            fileName.endsWith(".pdf", ignoreCase = true) -> "application/pdf"
            else -> "application/octet-stream" // 默认二进制流
        }
    }

}