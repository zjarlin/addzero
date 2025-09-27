plugins {
    id("kmp-ksp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // KSP 依赖
//            implementation(libs.ksp.symbol.processing.api)

            // 基础工具
            implementation(projects.lib.ksp.common.addzeroKspSupport)


//            implementation(projects.lib.kld.addzeroKaleidoscopeKsp)

        }

        jvmMain.dependencies {
            // JVM 特定依赖
        }
    }
}
