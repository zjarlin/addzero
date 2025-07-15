package com.addzero.addzero_common.excel

import cn.idev.excel.EasyExcel
import org.junit.jupiter.api.Test
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class 填充excel模板 {
    @Test
    fun test() {
        val filePath = "/Users/zjarlin/Documents/11111管网施工日记2024-11.xlsx"

        // 读取所有sheet页
        val workbook = EasyExcel.read(filePath)
        val doReadSync = workbook.sheet(0).doReadSync<Map<String, Any>>()
        println(doReadSync)

    }
}
