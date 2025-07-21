plugins {
    id("kmp-component")
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.lib.addzeroTool)
//            implementation(projects.lib.addzeroComposeModelComponent)
            implementation("com.seanproctor:data-table-material3:0.11.4")

              implementation("io.github.aleksandar-stefanovic:composematerialdatatable:1.2.1")

            implementation(libs.filekit.compose)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)


        }
    }
}
// build.gradle.kts
kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
