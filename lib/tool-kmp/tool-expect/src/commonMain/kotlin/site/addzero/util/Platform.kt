package site.addzero.util

// 预期函数 - 由各平台实现
expect fun isMobile(): Boolean

enum class Platform {
    Jvm,
    IOS,
    Mac,
    Web
}

expect fun getPlatform(): Platform
