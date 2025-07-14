package com.addzero.ai.mcp

import com.addzero.ai.util.ai.ai_abs_builder.AiUtil
import com.addzero.web.modules.sys_dict.entity.SysDict
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class AiDictService {
    @Tool(description = "结构化输入并添加字典")
    fun doisajdoi( input: String): Unit {

        AiUtil.Companion.ask(input, "","", SysDict::class.java)
//        input.toStru


    }
}