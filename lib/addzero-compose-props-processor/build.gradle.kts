plugins {
    id("kmp-ksp")
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.lib.addzeroKspSupport)
            implementation(projects.lib.addzeroComposePropsAnnotations)
        }
        jvmMain.dependencies {
//            implementation("com.belerweb:pinyin4j:${Versions.pinyin4jVersion}")
        }

    }
}
