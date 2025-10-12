package site.addzero.util


actual fun isMobile(): Boolean {
    // iOS 设备通常是移动设备
    return true
}

actual fun getPlatform(): Platform = Platform.IOS
