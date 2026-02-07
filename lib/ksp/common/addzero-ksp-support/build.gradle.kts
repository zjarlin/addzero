plugins {
    id("site.addzero.buildlogic.kmp.libs.kmp-ksp")
    id("site.addzero.buildlogic.kmp.composition.kmp-json-withtool")
}
kotlin {

    sourceSets {
      commonMain.dependencies {
        api(libs.tool.str)
      }
        jvmMain.dependencies {
            implementation(libs.pinyin4j)
        }
    }
}
