package com.addzero.web.modules.sys.ai

import cn.hutool.ai.model.deepseek.DeepSeekConfig
import cn.hutool.ai.model.deepseek.DeepSeekService
import cn.hutool.ai.model.doubao.DoubaoCommon
import cn.hutool.ai.model.doubao.DoubaoService
import com.addzero.kmp.core.ext.now
import com.addzero.kmp.entity.VisionRequest
import com.addzero.kmp.entity.sys.ai.*
import com.addzero.kmp.exp.BizException
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.tool.ToolCallback
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/ai")
class AiController(
    private val methodToolCallbackProvider: MethodToolCallbackProvider,

    private val deepSeekService: DeepSeekService,
    private val doubaoService: DoubaoService,
    private val deepSeekConfig: DeepSeekConfig,
//    @Value("\${doubao.key}") private val doubaoKey: String,

    private val chatClient: ChatClient
) {

    // 内存存储常用提示词（生产环境应使用数据库）
    private val prompts = mutableMapOf<String, AiPrompt>()

    init {
        // 初始化常用提示词
        initBuiltInPrompts()
    }

    /**
     * 🚀 初始化内置常用提示词
     */
    private fun initBuiltInPrompts() {
        val builtInPrompts = listOf(
            AiPrompt(
                id = "code_review",
                title = "代码审查助手",
                content = "请帮我审查以下代码，重点关注：\n1. 代码质量和最佳实践\n2. 潜在的bug和安全问题\n3. 性能优化建议\n4. 代码可读性和维护性\n\n代码：\n{code}",
                category = "编程开发",
                tags = listOf("代码审查", "编程", "质量"),
                isBuiltIn = true
            ),
            AiPrompt(
                id = "sql_optimizer",
                title = "SQL优化专家",
                content = "请帮我优化以下SQL查询，提供：\n1. 性能优化建议\n2. 索引建议\n3. 查询重写方案\n4. 执行计划分析\n\nSQL：\n{sql}",
                category = "数据库",
                tags = listOf("SQL", "优化", "数据库"),
                isBuiltIn = true
            ),
            AiPrompt(
                id = "api_designer",
                title = "API设计师",
                content = "请帮我设计RESTful API，包括：\n1. 资源路径设计\n2. HTTP方法选择\n3. 请求/响应格式\n4. 错误处理机制\n5. 状态码定义\n\n需求描述：\n{requirement}",
                category = "编程开发",
                tags = listOf("API", "设计", "RESTful"),
                isBuiltIn = true
            ),
            AiPrompt(
                id = "bug_analyzer",
                title = "Bug分析专家",
                content = "请帮我分析以下bug：\n1. 问题根因分析\n2. 复现步骤梳理\n3. 修复方案建议\n4. 预防措施\n\n错误信息：\n{error}\n\n相关代码：\n{code}",
                category = "问题解决",
                tags = listOf("bug", "调试", "分析"),
                isBuiltIn = true
            ),
            AiPrompt(
                id = "tech_writer",
                title = "技术文档写手",
                content = "请帮我编写技术文档，要求：\n1. 结构清晰，层次分明\n2. 语言简洁，易于理解\n3. 包含代码示例\n4. 添加注意事项和最佳实践\n\n文档主题：\n{topic}",
                category = "文档写作",
                tags = listOf("文档", "技术写作", "说明"),
                isBuiltIn = true
            ),
            AiPrompt(
                id = "architecture_advisor",
                title = "架构设计顾问",
                content = "请帮我设计系统架构，考虑：\n1. 业务需求和技术约束\n2. 可扩展性和性能\n3. 安全性和可靠性\n4. 技术选型建议\n5. 部署和运维方案\n\n项目需求：\n{requirement}",
                category = "系统设计",
                tags = listOf("架构", "设计", "系统"),
                isBuiltIn = true
            ),
            AiPrompt(
                id = "test_generator",
                title = "测试用例生成器",
                content = "请为以下功能生成测试用例：\n1. 正常流程测试\n2. 边界条件测试\n3. 异常情况测试\n4. 性能测试场景\n5. 安全测试要点\n\n功能描述：\n{feature}",
                category = "测试",
                tags = listOf("测试", "用例", "质量保证"),
                isBuiltIn = true
            ),
            AiPrompt(
                id = "performance_tuner",
                title = "性能调优专家",
                content = "请帮我分析性能问题并提供优化方案：\n1. 性能瓶颈识别\n2. 监控指标分析\n3. 优化策略建议\n4. 实施步骤规划\n\n性能数据：\n{metrics}\n\n系统信息：\n{system}",
                category = "性能优化",
                tags = listOf("性能", "调优", "监控"),
                isBuiltIn = true
            ),
            AiPrompt(
                id = "security_auditor",
                title = "安全审计员",
                content = "请对以下系统进行安全审计：\n1. 安全漏洞识别\n2. 风险等级评估\n3. 修复建议\n4. 安全最佳实践\n5. 合规性检查\n\n系统描述：\n{system}\n\n代码片段：\n{code}",
                category = "安全",
                tags = listOf("安全", "审计", "漏洞"),
                isBuiltIn = true
            ),
            AiPrompt(
                id = "data_analyst",
                title = "数据分析师",
                content = "请帮我分析以下数据：\n1. 数据趋势分析\n2. 异常值检测\n3. 相关性分析\n4. 预测建议\n5. 可视化方案\n\n数据描述：\n{data}\n\n分析目标：\n{goal}",
                category = "数据分析",
                tags = listOf("数据", "分析", "统计"),
                isBuiltIn = true
            )
        )

        // 将内置提示词添加到存储中
        builtInPrompts.forEach { prompt ->
            prompts[prompt.id] = prompt
        }
    }

    /**
     * 对话
     * @param [promt]
     * @return [DeepSeekChatResponse]
     */
    @GetMapping("/ask")
    fun ask(promt: String): String {
//        log.info("promt: $promt")
//        val chat = deepSeekService.chat(promt).parseObject<DeepSeekChatResponse>()
//        val joinToString = chat.choices?.map { it.message }?.joinToString(System.lineSeparator())
//        log.info("chat response: ${joinToString.toNotBlankStr()}")


        val call = chatClient.prompt().user { u: ChatClient.PromptUserSpec ->
            u.text(promt)
//                .params(map)
        }.call()

        val content = call.content()
        return content!!


//        return chat
    }

    /**
     * deepseek余额查询
     * @return [Unit]
     */
    @GetMapping("getDeepSeekBalance")
    fun getDeepSeekBalance(): String? {
        val balance: String? = deepSeekService.balance()
        return balance
    }

    @PostMapping("/chatVision")
    fun chatVision(@RequestBody visionRequest: VisionRequest): String? {
        val (promt, images) = visionRequest
        val chatVision = doubaoService.chatVision(promt, images, DoubaoCommon.DoubaoVision.HIGH.detail)
        return chatVision
    }

    @PostMapping("/genVideo")
    fun genVideo(@RequestBody visionRequest: VisionRequest): String? {

        val (promt, images) = visionRequest

        if (images.size > 1) {
            throw BizException("暂不支持批量处理")
        }
        val videoTasks = doubaoService.videoTasks(
            promt, images[0]
        )
        return videoTasks

    }

    @GetMapping("/getAiVideoProgres")
    fun getAiVideoProgres(taskkId: String): Unit {

        //查询视频生成任务信息
        val videoTasksInfo: String? = doubaoService.getVideoTasksInfo(taskkId)
    }

    // ==================== 提示词管理 API ====================

    /**
     * 📋 获取所有常用提示词
     *
     * @param category 分类筛选（可选）
     * @return 提示词列表
     */
    @GetMapping("/prompts")
    fun getPrompts(category: String?): List<AiPrompt> {
        return if (category.isNullOrBlank()) {
            prompts.values.sortedBy { it.category }
        } else {
            prompts.values.filter { it.category == category }.sortedBy { it.title }
        }
    }

    /**
     * 🔧 获取所有MCP工具描述
     *
     * @return 工具描述列表
     */
    @GetMapping("/tools")
    fun getTools(): List<String> {
        val tools = methodToolCallbackProvider.toolCallbacks
        return tools.map { it.toolDefinition.description() }
    }

    /**
     * 🔍 根据ID获取提示词
     *
     * @param id 提示词ID
     * @return 提示词详情
     */
    @GetMapping("/prompts/{id}")
    fun getPromptById(id: String): AiPrompt? {
        return prompts[id]
    }

    /**
     * 💾 保存新的提示词
     *
     * @param request 提示词保存请求
     * @return 保存的提示词
     */
    @PostMapping("/prompts")
    fun savePrompt(@RequestBody request: SavePromptRequest): AiPrompt {
        val id = "custom_${System.currentTimeMillis()}"
        val prompt = AiPrompt(
            id = id,
            title = request.title,
            content = request.content,
            category = request.category,
            tags = request.tags,
            isBuiltIn = false
        )

        prompts[id] = prompt
        return prompt
    }

    /**
     * ✏️ 更新提示词
     *
     * @param id 提示词ID
     * @param request 更新请求
     * @return 更新后的提示词
     */
    @PostMapping("/prompts/{id}")
    fun updatePrompt(id: String, @RequestBody request: SavePromptRequest): AiPrompt? {
        val existingPrompt = prompts[id] ?: return null

        // 内置提示词不允许修改
        if (existingPrompt.isBuiltIn) {
            throw BizException("内置提示词不允许修改")
        }

        val updatedPrompt = existingPrompt.copy(
            title = request.title,
            content = request.content,
            category = request.category,
            tags = request.tags,
            updatedAt = now
        )

        prompts[id] = updatedPrompt
        return updatedPrompt
    }

    /**
     * 🗑️ 删除提示词
     *
     * @param id 提示词ID
     * @return 删除结果
     */
    @PostMapping("/prompts/{id}/delete")
    fun deletePrompt(id: String): Map<String, String> {
        val prompt = prompts[id] ?: return mapOf("success" to "false", "message" to "提示词不存在")

        // 内置提示词不允许删除
        if (prompt.isBuiltIn) {
            throw BizException("内置提示词不允许删除")
        }

        prompts.remove(id)
        return mapOf("success" to "true", "message" to "删除成功")
    }

    /**
     * 🏷️ 获取所有分类
     *
     * @return 分类列表
     */
    @GetMapping("/prompts/categories")
    fun getCategories(): List<String> {
        return prompts.values.map { it.category }.distinct().sorted()
    }

    /**
     * 🔖 获取所有标签
     *
     * @return 标签列表
     */
    @GetMapping("/prompts/tags")
    fun getTags(): List<String> {
        return prompts.values.flatMap { it.tags }.distinct().sorted()
    }

    /**
     * 🔍 搜索提示词
     *
     * @param keyword 关键词
     * @return 搜索结果
     */
    @GetMapping("/prompts/search")
    fun searchPrompts(keyword: String): List<AiPrompt> {
        if (keyword.isBlank()) return emptyList()

        return prompts.values.filter { prompt ->
            prompt.title.contains(keyword, ignoreCase = true) ||
                    prompt.content.contains(keyword, ignoreCase = true) ||
                    prompt.category.contains(keyword, ignoreCase = true) ||
                    prompt.tags.any { it.contains(keyword, ignoreCase = true) }
        }.sortedBy { it.title }
    }

    /**
     * 🎯 使用提示词进行对话
     *
     * @param id 提示词ID
     * @param variables 变量替换映射
     * @return AI回复
     */
    @PostMapping("/prompts/{id}/chat")
    fun chatWithPrompt(id: String, @RequestBody variables: Map<String, String>): String {
        val prompt = prompts[id] ?: throw BizException("提示词不存在")

        // 替换提示词中的变量
        var processedContent = prompt.content
        variables.forEach { (key, value) ->
            processedContent = processedContent.replace("{$key}", value)
        }

        // 调用AI进行对话
        val call = chatClient.prompt().user { u: ChatClient.PromptUserSpec ->
            u.text(processedContent)
        }.call()

        return call.content() ?: "AI回复异常"
    }

    /**
     * 🎯 使用提示词进行对话（新版本，使用共享数据类）
     *
     * @param request 聊天请求
     * @return AI回复
     */
    @PostMapping("/prompts/chat")
    fun chatWithPromptV2(@RequestBody request: ChatWithPromptRequest): String {
        val prompt = prompts[request.promptId] ?: throw BizException("提示词不存在")

        // 替换提示词中的变量
        var processedContent = prompt.content
        request.variables.forEach { (key, value) ->
            processedContent = processedContent.replace("{$key}", value)
        }

        // 调用AI进行对话
        val call = chatClient.prompt().user { u: ChatClient.PromptUserSpec ->
            u.text(processedContent)
        }.call()

        return call.content() ?: "AI回复异常"
    }

    /**
     * 📊 获取提示词统计信息
     *
     * @return 统计信息
     */
    @GetMapping("/prompts/statistics")
    fun getPromptStatistics(): PromptStatistics {
        val allPrompts = prompts.values
        val builtInCount = allPrompts.count { it.isBuiltIn }
        val customCount = allPrompts.count { !it.isBuiltIn }
        val categoryCount = allPrompts.groupBy { it.category }
            .mapValues { it.value.size }

        return PromptStatistics(
            totalCount = allPrompts.size,
            builtInCount = builtInCount,
            customCount = customCount,
            categoryCount = categoryCount
        )
    }

    /**
     * 🔍 高级搜索提示词（使用共享数据类）
     *
     * @param request 搜索请求
     * @return 搜索结果
     */
    @PostMapping("/prompts/search")
    fun searchPromptsV2(@RequestBody request: SearchPromptRequest): List<AiPrompt> {
        var results = prompts.values.toList()

        // 关键词搜索
        request.keyword?.let { keyword ->
            if (keyword.isNotBlank()) {
                results = results.filter { prompt ->
                    prompt.title.contains(keyword, ignoreCase = true) ||
                            prompt.content.contains(keyword, ignoreCase = true) ||
                            prompt.category.contains(keyword, ignoreCase = true) ||
                            prompt.tags.any { it.contains(keyword, ignoreCase = true) }
                }
            }
        }

        // 分类筛选
        request.category?.let { category ->
            if (category.isNotBlank()) {
                results = results.filter { it.category == category }
            }
        }

        // 标签筛选
        if (request.tags.isNotEmpty()) {
            results = results.filter { prompt ->
                request.tags.any { tag -> prompt.tags.contains(tag) }
            }
        }

        return results.sortedBy { it.title }
    }

}
