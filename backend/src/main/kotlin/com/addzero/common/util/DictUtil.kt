//package com.addzero.common.util
//
//import cn.hutool.core.util.ClassUtil
//import com.addzero.web.infra.jimmer.enum.BaseEnum
//
//object DictUtil {
//    fun getAllEnum(): List<BaseEnum?> {
//        val scanPackageBySuper = ClassUtil.scanPackageBySuper("com.addzero", BaseEnum::class.java)
//        val toList = scanPackageBySuper.map {
//            val klass = it as Class<BaseEnum>
////            val newInstance = klass.enumConstants
//            val newInstance1 = klass.newInstance()
//            newInstance1
//        }.toList()
//        return toList
//    }
//    fun getEnumMap(): Map<String?, BaseEnum?> {
//        val associate = getAllEnum().associate { it?.dictCode to it }
//        return associate
//
//    }
//    fun getEnumByDictCode(string: String): BaseEnum? {
//        val enumMap = getEnumMap()
//        val get = enumMap.get(string)
//        return get
//    }
//
//}
