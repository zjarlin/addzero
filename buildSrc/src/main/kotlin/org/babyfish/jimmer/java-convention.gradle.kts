import org.babyfish.jimmer.Versions.javaVersion

plugins {
    `java-library`
    // 暂时移除发布相关插件
    // id("publish-convention")
}

extensions.configure<JavaPluginExtension> {
    val toVersion = JavaVersion.toVersion(javaVersion)
    sourceCompatibility = toVersion
    targetCompatibility = toVersion
    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    useJUnitPlatform()
}
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}
tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}
