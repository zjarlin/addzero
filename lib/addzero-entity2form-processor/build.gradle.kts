plugins {
    id("kmp-ksp")
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.lib.addzeroKspSupport)
            implementation(projects.lib.addzeroEntity2formCore)

            // 实体分析支持
            implementation(projects.lib.addzeroEntity2analysedSupport)
        }
    }
}
