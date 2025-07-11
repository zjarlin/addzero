import org.babyfish.jimmer.Versions.ktorVersion

plugins {
    id("kmp-core")
}
kotlin {
    sourceSets {
        jvmMain.dependencies {
            implementation("io.ktor:ktor-client-cio:${ktorVersion}")
        }
        commonMain.dependencies {
            implementation("io.ktor:ktor-client-core:${ktorVersion}")
            implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
            implementation("io.ktor:ktor-serialization-kotlinx-json:${ktorVersion}")
            implementation("io.ktor:ktor-client-logging:${ktorVersion}")
        }
    }
}
