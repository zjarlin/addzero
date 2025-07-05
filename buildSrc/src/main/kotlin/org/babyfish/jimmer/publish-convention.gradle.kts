
import org.babyfish.jimmer.P.authorName
import org.babyfish.jimmer.P.giturl
import org.babyfish.jimmer.P.projName


// 临时注释发布相关配置，直到基本构建可工作
plugins {
    id("com.vanniktech.maven.publish")
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
