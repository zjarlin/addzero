package com.addzero.kmp.entity.low_table

data class ExportParam(
    val enumExportType: EnumExportType = EnumExportType.XLSX,
    val ids: Set<Any>
)
