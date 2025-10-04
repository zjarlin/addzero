plugins {
    id("kmp-ksp")
    id("kmp-json-withtool")
}
kotlin {
    dependencies{
        api(projects.lib.toolKmp.addzeroToolStr)
    }

    sourceSets {
        jvmMain.dependencies {
            implementation(libs.pinyin4j)
        }
    }
}
