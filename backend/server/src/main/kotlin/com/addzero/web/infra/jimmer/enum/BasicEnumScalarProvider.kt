//package com.addzero.web.infra.jimmer.enum
//import com.addzero.common.util.DictUtil
//import com.addzero.web.infra.jackson.toJsonByKtx
//import com.alibaba.fastjson2.JSON
//import org.babyfish.jimmer.sql.runtime.ScalarProvider
//import org.springframework.stereotype.Component
//
//@Component
//class BasicEnumScalarProvider: ScalarProvider<BaseEnum, String> {
//    override fun toScalar(sqlValue: String): BaseEnum? {
//        val parseObject = JSON.parseObject(sqlValue)
//        val dictCode = parseObject.getString("dictCode")
//        val enumByDictCode = DictUtil.getEnumByDictCode(dictCode)
//        return enumByDictCode
//    }
//
//    override fun toSql(scalarValue: BaseEnum): String? {
//        val dictCode = scalarValue.dictCode
//        val enumByDictCode = DictUtil.getEnumByDictCode(dictCode)
//        val toJsonByKtx = enumByDictCode?.toJsonByKtx()
//        return toJsonByKtx
//    }
//
//}
