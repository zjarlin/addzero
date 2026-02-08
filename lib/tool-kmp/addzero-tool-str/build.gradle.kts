plugins {
    id("site.addzero.buildlogic.kmp.kmp-core")
    id("site.addzero.buildlogic.kmp.kmp-json")
}

kotlin {
dependencies {
            // put your Multiplatform dependencies here
            // Kotlinx Serialization 核心库
//            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${kotlinxSerializationVersion}")

//            implementation(projects.addzeroCore )
//            implementation(projects.addzeroTool )

            // Compose Multiplatform（跨平台 UI）
//            implementation("org.jetbrains.compose.runtime:runtime:+")
        }
    }

