plugins {
    id("spring-common")
}

//java {
//    toolchain {
//        languageVersion.set(JavaLanguageVersion.of(8))
//    }
//}
//
//kotlin {
//    jvmToolchain(8)
//}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

dependencies {
    implementation("org.springframework:spring-core")
    implementation("org.springframework:spring-context")
//    compileOnly("org.aspectj:aspectjweaver:1.9.9")
    compileOnly("org.aspectj:aspectjweaver")
}
