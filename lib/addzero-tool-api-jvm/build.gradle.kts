plugins {
    id("kotlin-convention")
//    kotlin("plugin.serialization")
}

// 配置 Kotlin 编译器选项以启用新特性

dependencies {
    implementation("org.jsoup:jsoup:1.10.2")
    // OkHttp 核心库
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation(libs.hutool.all)
    implementation(libs.fastjson2.kotlin)

    // Kotlinx Serialization 用于 JSON 序列化
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    // Kotlin 反射库（用于运行时访问注释）
//    implementation("org.jetbrains.kotlin:kotlin-reflect")
}

