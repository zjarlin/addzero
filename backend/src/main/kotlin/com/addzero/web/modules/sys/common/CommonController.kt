package com.addzero.web.modules.sys.common

import com.addzero.common.consts.sql
import com.addzero.kmp.api.CommonApi
import com.addzero.kmp.entity.low_table.*
import com.addzero.kmp.exp.BizException
import com.addzero.kmp.jdbc.meta.jdbcMetadata
import com.addzero.kmp.kt_util.isBlank
import com.addzero.kmp.util.TypeMapper
import com.addzero.kmp.util.TypeMapper.toLowCamelCase
import com.addzero.web.infra.entityMap
import com.addzero.web.infra.jackson.toJson
import com.addzero.web.infra.jimmer.adv_search.queryPage
import org.babyfish.jimmer.ImmutableObjects
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.web.bind.annotation.*
import kotlin.reflect.KClass

@RestController
class CommonController (private val jdbcClient: JdbcClient): CommonApi {

    @PostMapping("/common/getTable")
    override suspend fun getTableData(@RequestBody commonTableDaTaInputDTO: CommonTableDaTaInputDTO): String {
        val tableName = commonTableDaTaInputDTO.tableName
        if (tableName.isBlank()) {
            throw BizException("Table name is blank")
        }
        commonTableDaTaInputDTO.keyword
        val stateSorts = commonTableDaTaInputDTO.stateSorts
        val stateSearchForms = commonTableDaTaInputDTO.stateSearchForms
        val pageNo = commonTableDaTaInputDTO.pageNo
        val pageSize = commonTableDaTaInputDTO.pageSize
        val associateBy = stateSearchForms.groupBy { it.logicType }
        val andSearchCondition = associateBy[EnumLogicOperator.AND]
        val orSearchCondition = associateBy[EnumLogicOperator.OR]
        val klass = entityMap[tableName]
        val klass1 = klass as KClass<Any>
        val queryPage = queryPage(
            sortStats = stateSorts,
            andStateSearchConditions = andSearchCondition?.toMutableSet(),
            orStateSearchConditions = orSearchCondition?.toMutableSet(),
            entityClass = klass1,
            stateVos = mutableSetOf(),
            pageNo = pageNo,
            pageSize = pageSize,
        )
        val toJson = queryPage.toJson()
//        val decodeFromString = json.decodeFromString<SpecPageResult<Map<String, JsonElement?>>>( toJsonByKtx )
        return toJson
    }

    @PostMapping("/common/exportTable")
    override suspend fun export(@RequestBody exportParam: ExportParam): Boolean {
        return true
    }

    @GetMapping("/common/getColumns")
    override suspend fun getColumns(@RequestParam tableName: String): List<StateColumnMetadata> {

        //todo 获取登录态字段配置拿到Map<key,Meta后>对StateColumnMetadata进行扩充元数据

        val filter = jdbcMetadata.filter { it.tableName == tableName }


        val flatMap = filter.flatMap {
            val columns = it.columns
            columns.map {
                val mapToKotlinType = TypeMapper.mapToKotlinType(it)
                val stateColumnMetadata = StateColumnMetadata(
                    key = it.columnName.toLowCamelCase(),
                    title = it.remarks,
                    jdbcType = it.columnType,
                    javaType = mapToKotlinType,
//                    widthRatio = 1f,
//                    alignment = EnumColumnAlignment.CENTER,
//                    sortable = true,
//                    searchable = true,
//                    visible = true
                )
                stateColumnMetadata
                //todo 根据元数据配置补充元数据

            }
        }
        return flatMap
    }

//    override suspend fun edit(tableSaveOrUpdateDTO: TableSaveOrUpdateDTO): Boolean {
//        TODO("Not yet implemented")
//    }

    @PostMapping("/common/edit")
    override suspend fun edit(@RequestBody tableSaveOrUpdateDTO: TableSaveOrUpdateDTO): Boolean {
        val tableName = tableSaveOrUpdateDTO.tableName
        val toJson = tableSaveOrUpdateDTO.toJson()
        val klass1 = entityMap[tableName] as KClass<Any>
        val fromString = ImmutableObjects.fromString<Any>(klass1.java, toJson)
        val save = sql.save(fromString)
        val rowAffected = save.isRowAffected
        return rowAffected
    }


    @GetMapping("/common/checkExist")
    override suspend fun checkExist(tableName: String, column: String, value: String): Boolean {
        val trimIndent = """
           SELECT EXISTS(
    SELECT 1 FROM $tableName WHERE $column = '$value'
); 
        """.trimIndent()
        val query = jdbcClient.sql(trimIndent).query(Boolean::class.java)
        val single = query.single()
        return single
    }
}
