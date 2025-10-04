plugins {
    id("com.google.devtools.ksp")
}

ksp {
    arg("backendServerSourceDir", "/Users/zjarlin/IdeaProjects/addzero/backend/server/src/main/kotlin")
    arg("backendServerBuildDir", "/Users/zjarlin/IdeaProjects/addzero/backend/server/build/generated/ksp/main/kotlin")
    arg("backendServerResourceDir", "/Users/zjarlin/IdeaProjects/addzero/backend/server/src/main/resources")
    arg("backendModelSourceDir", "/Users/zjarlin/IdeaProjects/addzero/backend/model/src/main/kotlin")
    arg("backendModelBuildDir", "/Users/zjarlin/IdeaProjects/addzero/backend/model/build/generated/ksp/main/kotlin")
    arg("backendModelResourceDir", "/Users/zjarlin/IdeaProjects/addzero/backend/model/src/main/resources")
    arg("sharedSourceDir", "/Users/zjarlin/IdeaProjects/addzero/shared/src/commonMain/kotlin")
    arg("sharedBuildDir", "/Users/zjarlin/IdeaProjects/addzero/shared/build/generated/ksp/metadata/commonMain/kotlin")
    arg("composeAppSourceDir", "/Users/zjarlin/IdeaProjects/addzero/composeApp/src/commonMain/kotlin")
    arg("composeAppBuildDir", "/Users/zjarlin/IdeaProjects/addzero/composeApp/build/generated/ksp/metadata/commonMain/kotlin")
    arg("sharedComposeSourceDir", "/Users/zjarlin/IdeaProjects/addzero/shared-compose/src/commonMain/kotlin")
    arg("sharedComposeBuildDir", "/Users/zjarlin/IdeaProjects/addzero/shared-compose/build/generated/ksp/metadata/commonMain/kotlin")
    arg("enumOutputPackage", "site.addzero.generated.enums")
    arg("dictTableName", "sys_dict")
    arg("dictIdColumn", "id")
    arg("dictCodeColumn", "dict_code")
    arg("dictNameColumn", "dict_name")
    arg("dictItemTableName", "sys_dict_item")
    arg("dictItemForeignKeyColumn", "dict_id")
    arg("dictItemCodeColumn", "item_value")
    arg("dictItemNameColumn", "item_text")
}
