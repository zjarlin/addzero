
plugins {
    id("kotlin-convention")
    id("ksp4jdbc")
    id("ksp4iso")
    id("ksp4projectdir")
    id("ksp4autoddl")
}

dependencies {
    ksp(libs.jimmer.ksp)

    ksp(libs.addzero.jdbc2entity.processor)
    ksp(libs.addzero.entity2iso.processor)
    ksp(libs.addzero.entity2form.processor)
    ksp(libs.addzero.entity2mcp.processor)
    implementation(libs.jimmer.sql.kotlin)
    //easy-excel
    implementation(libs.fastexcel)
    //实体表单核心注解
    implementation(libs.addzero.entity2form.core)
    implementation(projects.shared)
    implementation(libs.hutool.all)
}
ksp {

    arg("modelPackageName", "site.addzero.model.entity")
}
