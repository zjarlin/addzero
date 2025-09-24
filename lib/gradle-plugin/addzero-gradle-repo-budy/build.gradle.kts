
plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
//    id("publish-convention")

}
dependencies {
    implementation(gradleApi())
}
gradlePlugin {
    plugins {
        register(project.name) {
            id = "site.addzero.repo-buddy"
            implementationClass = "site.addzero.RepoConfigPlugin"
        }
    }
}
