plugins {
    id("kmp-json")

}
kotlin {

    sourceSets {
        commonMain.dependencies {
            implementation("site.addzero:addzero-tool-json:+")

        }
    }

}
