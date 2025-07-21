import java.time.LocalDate
import com.addzero.Vars
plugins {
    id("com.vanniktech.maven.publish")
}
// 使用 Vars 中的配置

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()
    coordinates(project.group.toString(), project.name, project.version.toString())


    pom {
        name.set(Vars.projName)
        description.set(Vars.projectDescription)
        inceptionYear.set(LocalDate.now().year.toString())
        url.set(Vars.gitBaseUrl)
        licenses {
            license {
                name.set(Vars.licenseName)
                url.set(Vars.licenseUrl)
                distribution.set(Vars.licenseUrl)
            }
        }

        developers {
            developer {
                id.set(Vars.authorName)
                name.set(Vars.authorName)
                email.set(Vars.email)
            }
        }

        scm {
            connection.set("scm:git:git://${Vars.gitHost}/${Vars.gitRepoName}.git")
            developerConnection.set("scm:git:ssh://${Vars.gitHost}/${Vars.gitRepoName}.git")
            url.set(Vars.gitBaseUrl)
        }
    }
}
