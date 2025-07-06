
import org.babyfish.jimmer.Vars.authorName
import org.babyfish.jimmer.Vars.giturl
import org.babyfish.jimmer.Vars.projName


// 临时注释发布相关配置，直到基本构建可工作
plugins {
    signing
    id("com.vanniktech.maven.publish")
}
signing {
    // 推荐用 in-memory key，适合 CI/CD
    useInMemoryPgpKeys(
        findProperty("signing.keyId") as String?,
        findProperty("signing.password") as String?,
        findProperty("signing.secretKeyRingFile")?.let { file(it as String).readText() }
    )
    sign(publishing.publications)
}

afterEvaluate {
    tasks.findByName("plainJavadocJar")?.let { plainJavadocJarTask ->
        tasks.named("generateMetadataFileForMavenPublication") {
            dependsOn(plainJavadocJarTask)
        }
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()
    coordinates(project.group.toString(), project.name, project.version.toString())
    pom {
        name.set(projName)
        description.set(projName)
        inceptionYear.set("2025")
        url.set(giturl)
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set(authorName)
                name.set(authorName)
                url.set("https://gitee.com/$authorName")
            }
        }
        scm {
            url.set(giturl)
            connection.set("scm:git:git://${giturl.removePrefix("https://")}.git")
            developerConnection.set("scm:git:ssh://git@${giturl.removePrefix("https://")}.git")
        }
    }
}
