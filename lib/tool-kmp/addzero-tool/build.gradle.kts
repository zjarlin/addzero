plugins {
    id("kmp-core")
    id("kmp-json")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.lib.toolKmp.addzeroToolStr)
        }
    }
}
