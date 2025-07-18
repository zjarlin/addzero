plugins {
    id("kmp")

    id("kmp-android-library")
}
kotlin{
   sourceSets{
        commonMain.dependencies {
            implementation(projects.lib.addzeroTool)
            implementation(project.dependencies.platform(libs.koin.bom)) 
            implementation(libs.koin.annotations) 
            implementation(libs.koin.core) 
            implementation(libs.koin.compose) 
            implementation(libs.koin.compose.viewmodel) 
            implementation(libs.koin.compose.viewmodel.navigation) 

        }
    }
}

