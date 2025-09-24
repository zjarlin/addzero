plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
//    implementation(libs.addzero.gradle.tool)
//    implementation(project(":lib:gradle-plugin:addzero-gradle-tool"))
    implementation(projects.lib.gradlePlugin.addzeroGradleTool)

}

gradlePlugin {
    plugins {
        create(project.name) {
            id = "site.addzero.modules-buddy"
            implementationClass = "site.addzero.gradle.plugin.automodules.AutoModulesPlugin"
        }
    }
}
