plugins {
    id("kmp-ksp")
}


kotlin{
    sourceSets{
        jvmMain.dependencies {
            // JDBC相关依赖
            implementation(libs.postgresql.driver) // PostgreSQL驱动
            implementation(libs.mysql.connector.java) // MySQL驱动

        }
    }
}
