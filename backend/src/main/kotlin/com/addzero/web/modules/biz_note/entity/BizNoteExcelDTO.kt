//package com.addzero.web.modules.second_brain.note;
//
//import cn.idev.excel.annotation.ExcelProperty
//
//
//fun BizNote.toExcelDTO(): BizNoteExcelDTO {
//    var entity = BizNoteExcelDTO()
//
//    entity.title = this.title
//
//
//    entity.content = this.content
//
//
//    entity.jdbcType = this.jdbcType
//
//
//    entity.parentId = this.parent?.title
//
//    return entity
//}
//
//public open class BizNoteExcelDTO {
//
//    @ExcelProperty("标题")
//    var title: String? = null
//
//
//    @ExcelProperty("内容")
//    var content: String? = null
//
//
//    @ExcelProperty("类型")
//    var jdbcType: String? = null
//
//
//    @ExcelProperty("上级")
//    var parentId: String? = null
//
//    fun toEntity(): BizNote {
//        val that = this
//
//        return BizNote {
//            title = that.title?:""
//            content = that.content?:""
//            jdbcType = that.jdbcType?:""
//
//
////            parentId = bizNoteExcelDTO.parentId.toString()?:""
//
//        }
//    }
//}
