plugins {
    id("kmp-ksp")
}
kotlin {
    sourceSets {
        commonMain.dependencies {

        }
        jvmMain.dependencies {
            implementation(libs.pinyin4j)


        }
    }
}
