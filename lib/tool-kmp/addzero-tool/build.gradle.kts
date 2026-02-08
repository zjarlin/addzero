plugins {
    id("site.addzero.buildlogic.kmp.kmp-core")
    id("site.addzero.buildlogic.kmp.kmp-json")
}

kotlin {
dependencies {
            api(projects.lib.toolKmp.addzeroToolStr)
        }
    }
