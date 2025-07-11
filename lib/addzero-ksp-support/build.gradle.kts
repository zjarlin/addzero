import org.babyfish.jimmer.Versions

plugins {
    id("kmp-ksp")
}
kotlin {
    sourceSets {
        commonMain.dependencies {

        }
        jvmMain.dependencies {
            implementation(libs.pinyin4j)
//            implementation("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlinVersion}")


        }
    }
}
