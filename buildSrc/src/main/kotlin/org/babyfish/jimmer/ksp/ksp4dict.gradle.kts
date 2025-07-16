plugins {
    id("com.google.devtools.ksp")
    id("ksp4jdbc")
}
ksp {
    arg("enumOutputPackage", "com.addzero.kmp.generated.enums")
    // 字典表配置（小驼峰命名）
    arg("dictTableName", "sys_dict")
    arg("dictIdColumn", "id")
    arg("dictCodeColumn", "dict_code")
    arg("dictNameColumn", "dict_name")
    arg("dictItemTableName", "sys_dict_item")
    arg("dictItemForeignKeyColumn", "dict_id")
    arg("dictItemCodeColumn", "item_value")
    arg("dictItemNameColumn", "item_text")

}
