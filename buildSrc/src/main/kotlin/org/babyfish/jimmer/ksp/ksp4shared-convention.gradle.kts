plugins {
    id("com.google.devtools.ksp")
}
ksp {
//    arg("generatedsrc.dir", "src/commonMain/kotlin")
//    arg("project.dir", rootProject.projectDir.absolutePath)
    // 或者使用更精确的路径获取方式


    // JDBC元数据处理器配置
    arg("jdbcUrl", "jdbc:postgresql://localhost:15432/postgres")
    arg("jdbcUsername", "postgres")
    arg("jdbcPassword", "postgres")
    arg("jdbcSchema", "public")
    arg("jdbcDriver", "org.postgresql.Driver")
    arg("outputPackage", "com.addzero.kmp.jdbc.meta")
    // 可选：指定要包含的表（逗号分隔）
    // arg("includeTables", "user_info,product")
    // 可选：指定要排除的表（逗号分隔）
    // arg("excludeTables", "flyway_schema_history")

    arg("excludeTables", "flyway_schema_history,vector_store")

    // 字典枚举处理器配置
    arg("dictTableName", "sys_dict")
    arg("dictIdColumn", "id")
    arg("dictCodeColumn", "dict_code")
    arg("dictNameColumn", "dict_name")
    arg("dictItemTableName", "sys_dict_item")
    arg("dictItemForeignKeyColumn", "dict_id")
    arg("dictItemCodeColumn", "item_value")
    arg("dictItemNameColumn", "item_text")
    arg("enumOutputPackage", "com.addzero.kmp.generated.enums")


}
