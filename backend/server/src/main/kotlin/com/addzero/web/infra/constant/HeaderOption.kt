package com.addzero.web.infra.constant

enum class HeaderOption(val type: String, val postfix: String) {
    显示("Content-Disposition", ".inline;fileName="),
    下载("Content-attachment", ".inline;fileName=");
}