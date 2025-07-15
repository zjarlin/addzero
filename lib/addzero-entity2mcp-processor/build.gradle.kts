plugins {
    id("kmp-ksp")
}

kotlin {

    sourceSets {
        commonMain.dependencies {
            // 基础工具
            implementation(projects.lib.addzeroKspSupport)

            // 实体分析支持
            implementation(projects.lib.addzeroEntity2analysedSupport)

        }
    }
}
