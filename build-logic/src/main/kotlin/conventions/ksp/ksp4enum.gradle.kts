plugins {
    id("com.google.devtools.ksp")
}

ksp {
    // 枚举生成配置（生成到 shared 编译目录）
    arg("enumOutputPackage", "site.addzero.generated.enums")
    // 字典枚举处理器配置
    arg("dictTableName", "sys_dict")
    arg("dictIdColumn", "id")
    arg("dictCodeColumn", "dict_code")
    arg("dictNameColumn", "dict_name")
    arg("dictItemTableName", "sys_dict_item")
    arg("dictItemForeignKeyColumn", "dict_id")
    arg("dictItemCodeColumn", "item_value")
    arg("dictItemNameColumn", "item_text")
}
