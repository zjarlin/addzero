import BuildSettings.AUTH_NAME
import java.time.LocalDate

plugins {
    id("com.vanniktech.maven.publish")
}

// 注意：由于Maven Publish插件与Gradle配置缓存存在兼容性问题，
// 在使用publishToMavenLocal或publishToMavenCentral任务时，
// 请使用--no-configuration-cache参数禁用配置缓存
// 例如: ./gradlew publishToMavenLocal --no-configuration-cache

val pjVersion = project.version.toString()
mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()
    coordinates(project.group.toString(), project.name, pjVersion)

    pom {
        name.set(project.name)
        description.set(BuildSettings.PROJECT_DESCRIPTION)
        inceptionYear.set(LocalDate.now().year.toString())
        url.set(BuildSettings.gitBaseUrl)
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set(AUTH_NAME)
                name.set(AUTH_NAME)
                email.set(BuildSettings.email)
            }
        }

        scm {
            connection.set("scm:git:git://${BuildSettings.gitHost}/${BuildSettings.gitRepoName}.git")
            developerConnection.set("scm:git:ssh://${BuildSettings.gitHost}/${BuildSettings.gitRepoName}.git")
            url.set(BuildSettings.gitBaseUrl)
        }
    }
}