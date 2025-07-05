plugins {
    id("kmp")
    id("publish-convention")
    alias(libs.plugins.compose.hot.reload )
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Enables FileKit with Composable utilities
            implementation(libs.filekit.compose)

        }
    }
}
