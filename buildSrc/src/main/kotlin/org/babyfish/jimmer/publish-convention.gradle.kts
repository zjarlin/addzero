import org.babyfish.jimmer.Vars

plugins {
//       signing
    id("com.vanniktech.maven.publish")
}
//val keystorePropertiesFile = rootProject.file("local.properties")
//
//val keystoreProperties = Properties()
//
//keystoreProperties.load(FileInputStream(keystorePropertiesFile))

//signing {
//    val mavenCentralUsername = findProperty("mavenCentralUsername")?.toString()
//    val mavenCentralPassword = findProperty("mavenCentralPassword")?.toString()
//    val keyId = findProperty("signing.keyId")?.toString()
//    val password = findProperty("signing.password")?.toString()
//    val keyFilePath = findProperty("signing.secretKeyRingFile")?.toString()
//
//    println("mavenCentralUsername: $mavenCentralUsername")
//    println("mavenCentralPassword: $mavenCentralPassword")
//    println("signing.keyId: $keyId")
//    println("signing.password: $password")
//    println("signing.secretKeyRingFile: $keyFilePath")
//
//    val secretKeyContent = file(keyFilePath!!).readText(Charsets.UTF_8)
//    println("Secret Key Content: $secretKeyContent")
//
//    useInMemoryPgpKeys(keyId, secretKeyContent, password)
//    sign(publishing.publications)
//}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()
}


mavenPublishing {
//    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    coordinates(project.group.toString(), project.name, project.version.toString())

    pom {
        name.set("addzero")
        description.set("jimmer kmp")
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
