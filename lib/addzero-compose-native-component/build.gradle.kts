plugins {
    id("kmp")

    id("kmp-android-library")
}
kotlin{
   sourceSets{
        commonMain.dependencies {
            implementation(projects.lib.addzeroTool)
        }
    }
}

