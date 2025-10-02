import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("kmp-compose-common")

}
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

kotlin {
    jvm()
    sourceSets {
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

compose.desktop {
    application {
        mainClass = BuildSettings.Desktop.MAIN_CLASS

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = BuildSettings.PACKAGE_NAME

            packageVersion = "1.0.0"
        }
    }
}
