plugins {
    id("com.google.devtools.ksp")
    id("ksp4jdbc")
}

ksp {
    //建议检查生成的sql后放到flyway的规范目录(以下为resource资源目录的相对目录)
    arg("sqlSavePath", "db/autoddl")
    arg("dbType", "pg") //可选项仅有mysql oracle pg dm h2
    arg("idType", "bigint")
    arg("id", "id")
    arg("createBy", "create_by")
    arg("updateBy", "update_by")
    arg("createTime", "create_time")
    arg("updateTime", "update_time")

    arg("metaJsonSavePath", "db/autoddl/meta")
    arg("metaJsonSaveName", "jimmer_ddlcontext.json")


}












