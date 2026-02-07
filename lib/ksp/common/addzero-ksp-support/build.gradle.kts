plugins {
    id("site.addzero.buildlogic.kmp.libs.kmp-ksp")
    id("site.addzero.buildlogic.kmp.composition.kmp-json-withtool")
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
