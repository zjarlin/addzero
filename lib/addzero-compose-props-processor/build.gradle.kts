plugins {
    id("kmp-ksp")
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.lib.addzeroKspSupport)
            implementation(projects.lib.addzeroComposePropsAnnotations)
        }
        jvmMain.dependencies {
        }

    }
}
