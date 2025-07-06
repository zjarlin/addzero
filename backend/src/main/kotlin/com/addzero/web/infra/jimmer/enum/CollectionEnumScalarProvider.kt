//package com.addzero.web.infra.jimmer
//
//import cn.hutool.core.util.TypeUtil
//import com.addzero.common.kt_util.bitValue
//import org.babyfish.jimmer.sql.runtime.ScalarProvider
//import org.springframework.stereotype.Component
//
//
//@Component
//class CollectionEnumScalarProvider<T : Enum<T>> : ScalarProvider<List<T>, Int> {
//
//    override fun toScalar(sqlValue: Int): List<T>? {
//        val typeArgument = TypeUtil.getTypeArgument(this::class.java, 0)
//
//        return emptyList()
////        val enumValues =     enumValues<T>()
////        return  enumValues .filter { (sqlValue and it.bitValue) != 0 }
//    }
//
//    override fun toSql(scalarValue: List<T>): Int? {
//        val fold = scalarValue.fold(0) { mask, enum ->
//            mask or enum.bitValue  // 叠加所有标志位
//        }
//        return fold
//    }
//
//
//}
