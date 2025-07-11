

plugins {
    id("kmp-ksp")
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.lib.addzeroKspSupportJdbc)
            implementation(projects.lib.addzeroKspSupport)

        }
        jvmMain.dependencies {
        }

    }
}
