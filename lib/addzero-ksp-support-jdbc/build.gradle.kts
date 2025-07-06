plugins {
    id("kmp-ksp")
}


kotlin{
    sourceSets{
        jvmMain.dependencies {
            // JDBC相关依赖
            implementation("org.postgresql:postgresql:+") // PostgreSQL驱动
            implementation("mysql:mysql-connector-java:+") // MySQL驱动

        }
    }
}
