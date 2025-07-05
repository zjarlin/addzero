plugins {
    id("kmp-ksp")
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.lib.addzeroRouteCore)

            implementation(projects.lib.addzeroKspSupport)
        }
    }
}
