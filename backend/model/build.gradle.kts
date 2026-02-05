plugins {
//    id("kotlin-convention")
    alias(libs.plugins.site.addzero.jvm.kotlin. convention)
//    alias(libs.plugins.site)
//   site-addzero-jvm-kotlin-convention
    id("site.addzero.buildlogic.ksp.ksp4jdbc")
    id("site.addzero.buildlogic.ksp.ksp4iso")
    id("site.addzero.buildlogic.ksp.ksp4projectdir")
    id("site.addzero.buildlogic.ksp.ksp4autoddl")
}
dependencies {
    ksp(libs.jimmer.ksp)
//    ksp(projects.lib.ksp.common.addzeroKspEasycodeJimmer)
    ksp(libs.addzero.jdbc2entity.processor)
    ksp(libs.addzero.entity2iso.processor)
    ksp(libs.addzero.entity2form.processor)
    ksp(libs.addzero.entity2mcp.processor)
    implementation(libs.addzero.entity2form.core)



    implementation(libs.jimmer.sql.kotlin)
    //easy-excel
    implementation(libs.fastexcel)
    //实体表单核心注解
    implementation(projects.shared)
    implementation(libs.hutool.all)
}
ksp {
    arg("modelPackageName", "site.addzero.model.entity")
}
