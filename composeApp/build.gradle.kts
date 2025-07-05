import org.babyfish.jimmer.Versions

plugins {
    id("kmp")
    id("publish-convention")
//    alias(libs.plugins.compose.hot.reload)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Enables FileKit with Composable utilities
            implementation("io.github.vinceglb:filekit-compose:${Versions
                .fileKitVersion}")

        }
    }
}
