plugins {
    id("kmp-core")
    id("kmp-json")
}

kotlin {
dependencies {
            api(projects.lib.toolKmp.addzeroToolStr)
        }
    }
