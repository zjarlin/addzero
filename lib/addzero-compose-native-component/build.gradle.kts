plugins {
    id("kmp-component")
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.lib.addzeroTool)
            implementation(projects.lib.addzeroComposeModelComponent)

        }
    }
}
// build.gradle.kts
kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}