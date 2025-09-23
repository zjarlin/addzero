plugins {
    id("org.jetbrains.dokka")
}

tasks {
    withType<Jar>().configureEach {
        if (archiveClassifier.get() == "javadoc") {
            from(named("dokkaHtml").map { (it as org.jetbrains.dokka.gradle.DokkaTask).outputDirectory })
        }
    }
}
