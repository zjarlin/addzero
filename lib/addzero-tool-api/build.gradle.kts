plugins {
    id("kmp-shared")
}


kotlin {
    sourceSets {
        commonMain.dependencies {
//            implementation(projects.lib.addzeroTool)
            implementation(projects.lib.addzeroNetworkStarter)
        }
    }
}

