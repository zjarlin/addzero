plugins {
    id("kotlin-convention")
}

dependencies {
//    implementation(project(":lib:kcp:addzero-kcp-annotations"))
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:${org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION}")
}


