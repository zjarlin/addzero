plugins {
    id("kmp-core")
    id("site.addzero.buildlogic.kmp.libs.kmp-json")
}

kotlin {
dependencies {
            api(projects.lib.toolKmp.addzeroToolStr)
        }
    }
