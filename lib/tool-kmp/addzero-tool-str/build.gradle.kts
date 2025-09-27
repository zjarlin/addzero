plugins {
    id("kmp-core")
    id("kmp-json")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            // Kotlinx Serialization 核心库
//            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${kotlinxSerializationVersion}")

//            implementation(projects.addzeroCore )
//            implementation(projects.addzeroTool )

            // Compose Multiplatform（跨平台 UI）
//            implementation("org.jetbrains.compose.runtime:runtime:+")
        }
    }

}
