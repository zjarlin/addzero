import com.vanniktech.maven.publish.SonatypeHost
import org.babyfish.jimmer.Vars

plugins {
    id("com.vanniktech.maven.publish")
}


mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
//    signAllPublications()
//    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    coordinates(project.group.toString(), project.name, project.version.toString())

    pom {
        name.set("addzero")
        description.set("The most advanced ORM of JVM, for both java & kotlin")
        inceptionYear.set("2025")
        url.set(Vars.giturl)
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
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
            connection.set("scm:git:git://gitee.com/zjarlin/addzero.git")
            developerConnection.set("scm:git:ssh://gitee.com/zjarlin/addzero.git")
            url.set("https://gitee.com/zjarlin/addzero")
        }
    }
}
afterEvaluate {
    tasks.findByName("plainJavadocJar")?.let { plainJavadocJarTask ->
        tasks.named("generateMetadataFileForMavenPublication") {
            dependsOn(plainJavadocJarTask)
        }
    }
}
