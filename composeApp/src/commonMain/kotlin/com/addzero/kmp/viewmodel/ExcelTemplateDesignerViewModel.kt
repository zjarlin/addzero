package com.addzero.kmp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.addzero.kmp.ext.api
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import org.koin.android.annotation.KoinViewModel

/**
 * Excel模板设计器ViewModel
 * 专门为Excel模板填充设计简单的数据结构
 */
@KoinViewModel
class ExcelTemplateDesignerViewModel : ViewModel() {
    
    /**
     * 字段数据类
     */
    data class FieldItem(
        val id: String = generateId(),
        var key: String = "",
        var value: String = "",
        var type: FieldType = FieldType.STRING
    ) {
        companion object {
            private var idCounter = 0
            fun generateId(): String = "field_${++idCounter}"
        }
    }
    
    /**
     * 字段类型
     */
    enum class FieldType {
        STRING,   // 字符串
        NUMBER    // 数字
    }
    
    /**
     * Excel模板
     */
    data class ExcelTemplate(
        val id: String = generateTemplateId(),
        val name: String,
        val fileName: String,
        val isCommon: Boolean = false,
        val jsonTemplateId: String? = null, // 关联的JSON模板ID
        val fileSize: String = "未知", // 文件大小
        val uploadTime: Long = Clock.System.now().toEpochMilliseconds() // 上传时间
    ) {
        companion object {
            private var templateIdCounter = 0
            fun generateTemplateId(): String = "template_${++templateIdCounter}"
        }
    }

    /**
     * 元数据提取项
     */
    data class MetadataExtractionItem(
        val id: String = generateExtractionId(),
        val excelTemplate: ExcelTemplate,
        val extractionTime: Long = Clock.System.now().toEpochMilliseconds(),
        val status: ExtractionStatus = ExtractionStatus.PENDING
    ) {
        companion object {
            private var extractionIdCounter = 0
            fun generateExtractionId(): String = "extraction_${++extractionIdCounter}"
        }
    }

    /**
     * 提取状态
     */
    enum class ExtractionStatus {
        PENDING,    // 待处理
        PROCESSING, // 处理中
        COMPLETED,  // 已完成
        FAILED      // 失败
    }

    /**
     * JSON模板数据类
     */
    data class JsonTemplate(
        val id: String = generateJsonTemplateId(),
        val name: String,
        val jsonContent: String,
        val oneDimensionFields: List<FieldItem>,
        val twoDimensionFields: List<FieldItem>,
        val createTime: Long =  Clock.System.now().toEpochMilliseconds()
    ) {
        companion object {
            private var jsonTemplateIdCounter = 0
            fun generateJsonTemplateId(): String = "json_template_${++jsonTemplateIdCounter}"
        }
    }
    
    // 一维区域字段列表 (vo: Map<String, Any>)
    val oneDimensionFields = mutableStateListOf<FieldItem>()
    
    // 二维区域字段列表 (dtos: List<Map<String, Any>>)
    val twoDimensionFields = mutableStateListOf<FieldItem>()
    
    // 生成的JSON预览
    var jsonPreview by mutableStateOf("{}")
        private set
    
    // 上传的Excel模板
    val excelTemplates = mutableStateListOf<ExcelTemplate>()
    
    // 常用模板
    val commonTemplates = mutableStateListOf<ExcelTemplate>()

    // JSON模板列表
    val jsonTemplates = mutableStateListOf<JsonTemplate>()

    // 当前选中的JSON模板
    var selectedJsonTemplate by mutableStateOf<JsonTemplate?>(null)
        private set

    // 元数据提取购物车
    val metadataExtractionCart = mutableStateListOf<MetadataExtractionItem>()

    // 可选择的Excel文件列表（用于元数据提取）
    val availableExcelFiles = mutableStateListOf<ExcelTemplate>()

    // 错误信息
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    init {
        // 初始化示例数据
        addOneDimensionField("一维区域", "")
        addTwoDimensionField("温度", "111")
        addTwoDimensionField("湿度", "222")
        updateJsonPreview()
    }
    
    /**
     * 添加一维字段
     */
    fun addOneDimensionField(key: String = "新字段", value: String = "") {
        oneDimensionFields.add(FieldItem(key = key, value = value))
        updateJsonPreview()
    }
    
    /**
     * 添加二维字段
     */
    fun addTwoDimensionField(key: String = "新字段", value: String = "") {
        twoDimensionFields.add(FieldItem(key = key, value = value))
        updateJsonPreview()
    }
    
    /**
     * 更新一维字段
     */
    fun updateOneDimensionField(field: FieldItem, key: String? = null, value: String? = null, type: FieldType? = null) {
        key?.let { field.key = it }
        value?.let { field.value = it }
        type?.let { field.type = it }
        updateJsonPreview()
    }
    
    /**
     * 更新二维字段
     */
    fun updateTwoDimensionField(field: FieldItem, key: String? = null, value: String? = null, type: FieldType? = null) {
        key?.let { field.key = it }
        value?.let { field.value = it }
        type?.let { field.type = it }
        updateJsonPreview()
    }
    
    /**
     * 删除一维字段
     */
    fun deleteOneDimensionField(field: FieldItem) {
        oneDimensionFields.remove(field)
        updateJsonPreview()
    }
    
    /**
     * 删除二维字段
     */
    fun deleteTwoDimensionField(field: FieldItem) {
        twoDimensionFields.remove(field)
        updateJsonPreview()
    }
    
    /**
     * 生成vo数据 (Map<String, Any>)
     */
    fun generateVoData(): Map<String, Any> {
        val voMap = mutableMapOf<String, Any>()
        
        // 添加一维区域
        val oneDimensionMap = mutableMapOf<String, Any>()
        oneDimensionFields.forEach { field ->
            if (field.key.isNotBlank()) {
                oneDimensionMap[field.key] = convertValue(field.value, field.type)
            }
        }
        voMap["一维区域"] = oneDimensionMap
        
        // 添加二维区域
        val twoDimensionList = mutableListOf<Map<String, Any>>()
        if (twoDimensionFields.isNotEmpty()) {
            // 生成两行示例数据
            repeat(2) { index ->
                val rowMap = mutableMapOf<String, Any>()
                twoDimensionFields.forEach { field ->
                    if (field.key.isNotBlank()) {
                        val value = if (index == 0) field.value else "${field.value}${index + 1}"
                        rowMap[field.key] = convertValue(value, field.type)
                    }
                }
                if (rowMap.isNotEmpty()) {
                    twoDimensionList.add(rowMap)
                }
            }
        }
        voMap["二维区域"] = twoDimensionList
        
        return voMap
    }
    
    /**
     * 生成dtos数据 (List<Map<String, Any>>)
     */
    fun generateDtosData(): List<Map<String, Any>> {
        val dtosList = mutableListOf<Map<String, Any>>()
        
        // 生成两行示例数据
        repeat(2) { index ->
            val rowMap = mutableMapOf<String, Any>()
            twoDimensionFields.forEach { field ->
                if (field.key.isNotBlank()) {
                    val value = if (index == 0) field.value else "${field.value}${index + 1}"
                    rowMap[field.key] = convertValue(value, field.type)
                }
            }
            if (rowMap.isNotEmpty()) {
                dtosList.add(rowMap)
            }
        }
        
        return dtosList
    }
    
    /**
     * 转换值类型
     */
    private fun convertValue(value: String, type: FieldType): Any {
        return when (type) {
            FieldType.STRING -> value
            FieldType.NUMBER -> value.toDoubleOrNull() ?: value
        }
    }
    
    /**
     * 更新JSON预览
     */
    private fun updateJsonPreview() {
        try {
            val voData = generateVoData()
            // 使用简单的JSON字符串构建，避免序列化问题
            jsonPreview = buildJsonString(voData)
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = "JSON生成错误: ${e.message}"
        }
    }

    /**
     * 手动构建JSON字符串
     */
    private fun buildJsonString(data: Map<String, Any>): String {
        val sb = StringBuilder()
        sb.append("{\n")

        data.entries.forEachIndexed { index, (key, value) ->
            sb.append("  \"$key\": ")
            when (value) {
                is Map<*, *> -> {
                    sb.append("{\n")
                    val mapEntries = value.entries.toList()
                    mapEntries.forEachIndexed { mapIndex, (mapKey, mapValue) ->
                        sb.append("    \"$mapKey\": ")
                        sb.append(formatValue(mapValue))
                        if (mapIndex < mapEntries.size - 1) sb.append(",")
                        sb.append("\n")
                    }
                    sb.append("  }")
                }
                is List<*> -> {
                    sb.append("[\n")
                    value.forEachIndexed { listIndex, item ->
                        if (item is Map<*, *>) {
                            sb.append("    {\n")
                            val itemEntries = item.entries.toList()
                            itemEntries.forEachIndexed { itemIndex, (itemKey, itemValue) ->
                                sb.append("      \"$itemKey\": ")
                                sb.append(formatValue(itemValue))
                                if (itemIndex < itemEntries.size - 1) sb.append(",")
                                sb.append("\n")
                            }
                            sb.append("    }")
                        } else {
                            sb.append("    ${formatValue(item)}")
                        }
                        if (listIndex < value.size - 1) sb.append(",")
                        sb.append("\n")
                    }
                    sb.append("  ]")
                }
                else -> sb.append(formatValue(value))
            }
            if (index < data.size - 1) sb.append(",")
            sb.append("\n")
        }

        sb.append("}")
        return sb.toString()
    }

    /**
     * 格式化值
     */
    private fun formatValue(value: Any?): String {
        return when (value) {
            is String -> "\"$value\""
            is Number -> value.toString()
            is Boolean -> value.toString()
            null -> "null"
            else -> "\"$value\""
        }
    }
    
    /**
     * 上传Excel模板
     */
    fun uploadExcelTemplate(fileName: String, onUpload: (String) -> Unit) {
        val template = ExcelTemplate(
            name = fileName.substringBeforeLast("."),
            fileName = fileName
        )
        excelTemplates.add(template)
        onUpload(fileName)
    }
    
    /**
     * 保存为常用模板
     */
    fun saveAsCommonTemplate(template: ExcelTemplate) {
        val commonTemplate = template.copy(isCommon = true)
        commonTemplates.add(commonTemplate)
        excelTemplates.remove(template)
    }
    
    /**
     * 删除模板
     */
    fun deleteTemplate(template: ExcelTemplate) {
        excelTemplates.remove(template)
        commonTemplates.remove(template)
    }
    
    /**
     * 清空所有字段
     */
    fun clearAll() {
        oneDimensionFields.clear()
        twoDimensionFields.clear()
        addOneDimensionField("一维区域", "")
        addTwoDimensionField("温度", "111")
        addTwoDimensionField("湿度", "222")
        updateJsonPreview()
    }
    
    /**
     * 导出数据供Excel填充使用
     */
    fun exportForExcelFill(): Pair<Map<String, Any>, List<Map<String, Any>>> {
        val vo = generateVoData()
        val dtos = generateDtosData()
        return Pair(vo, dtos)
    }

    /**
     * 保存当前设计为JSON模板
     */
    fun saveAsJsonTemplate(templateName: String) {
        if (templateName.isBlank()) {
            errorMessage = "模板名称不能为空"
            return
        }

        val jsonTemplate = JsonTemplate(
            name = templateName,
            jsonContent = jsonPreview,
            oneDimensionFields = oneDimensionFields.toList(),
            twoDimensionFields = twoDimensionFields.toList()
        )

        jsonTemplates.add(jsonTemplate)
        selectedJsonTemplate = jsonTemplate
        errorMessage = null
    }

    /**
     * 加载JSON模板
     */
    fun loadJsonTemplate(template: JsonTemplate) {
        oneDimensionFields.clear()
        twoDimensionFields.clear()

        oneDimensionFields.addAll(template.oneDimensionFields)
        twoDimensionFields.addAll(template.twoDimensionFields)

        selectedJsonTemplate = template
        updateJsonPreview()
    }

    /**
     * 删除JSON模板
     */
    fun deleteJsonTemplate(template: JsonTemplate) {
        jsonTemplates.remove(template)
        if (selectedJsonTemplate == template) {
            selectedJsonTemplate = null
        }

        // 解除Excel模板的关联
        excelTemplates.forEach { excelTemplate ->
            if (excelTemplate.jsonTemplateId == template.id) {
                val updatedTemplate = excelTemplate.copy(jsonTemplateId = null)
                val index = excelTemplates.indexOf(excelTemplate)
                excelTemplates[index] = updatedTemplate
            }
        }

        commonTemplates.forEach { excelTemplate ->
            if (excelTemplate.jsonTemplateId == template.id) {
                val updatedTemplate = excelTemplate.copy(jsonTemplateId = null)
                val index = commonTemplates.indexOf(excelTemplate)
                commonTemplates[index] = updatedTemplate
            }
        }
    }

    /**
     * 将JSON模板与Excel模板绑定
     */
    fun bindJsonTemplateToExcel(jsonTemplate: JsonTemplate, excelTemplate: ExcelTemplate) {
        val updatedExcelTemplate = excelTemplate.copy(jsonTemplateId = jsonTemplate.id)

        // 更新Excel模板列表中的模板
        val excelIndex = excelTemplates.indexOf(excelTemplate)
        if (excelIndex >= 0) {
            excelTemplates[excelIndex] = updatedExcelTemplate
        }

        val commonIndex = commonTemplates.indexOf(excelTemplate)
        if (commonIndex >= 0) {
            commonTemplates[commonIndex] = updatedExcelTemplate
        }
    }

    /**
     * 获取与Excel模板绑定的JSON模板
     */
    fun getJsonTemplateForExcel(excelTemplate: ExcelTemplate): JsonTemplate? {
        return excelTemplate.jsonTemplateId?.let { jsonTemplateId ->
            jsonTemplates.find { it.id == jsonTemplateId }
        }
    }

    /**
     * 复制JSON到剪贴板（模拟）
     */
    fun copyJsonToClipboard(): String {
        return jsonPreview
    }

    /**
     * 添加Excel文件到元数据提取购物车
     */
    fun addToExtractionCart(excelTemplate: ExcelTemplate) {
        // 检查是否已经在购物车中
        val existingItem = metadataExtractionCart.find { it.excelTemplate.id == excelTemplate.id }
        if (existingItem == null) {
            val extractionItem = MetadataExtractionItem(excelTemplate = excelTemplate)
            metadataExtractionCart.add(extractionItem)
        }
    }

    /**
     * 从元数据提取购物车中移除
     */
    fun removeFromExtractionCart(item: MetadataExtractionItem) {
        metadataExtractionCart.remove(item)
    }

    /**
     * 清空元数据提取购物车
     */
    fun clearExtractionCart() {
        metadataExtractionCart.clear()
    }

    /**
     * 开始元数据提取
     */
    fun startMetadataExtraction() {
        metadataExtractionCart.forEach { item ->
            // 更新状态为处理中
            val index = metadataExtractionCart.indexOf(item)
            if (index >= 0) {
                metadataExtractionCart[index] = item.copy(status = ExtractionStatus.PROCESSING)
            }
        }

        // TODO: 实际的元数据提取逻辑
        // 模拟处理完成（实际项目中应该使用viewModelScope）
        try {
            // 模拟异步处理
            api {
                delay(2000)
                metadataExtractionCart.forEachIndexed { index, item ->
                    if (index < metadataExtractionCart.size) {
                        metadataExtractionCart[index] = item.copy(status = ExtractionStatus.COMPLETED)
                    }
                }

            }
        } catch (e: Exception) {
            errorMessage = "元数据提取失败: ${e.message}"
        }
    }

    /**
     * 添加可选择的Excel文件
     */
    fun addAvailableExcelFile(fileName: String, fileSize: String = "未知") {
        val template = ExcelTemplate(
            name = fileName.substringBeforeLast("."),
            fileName = fileName,
            fileSize = fileSize
        )
        availableExcelFiles.add(template)
    }

    /**
     * 移除可选择的Excel文件
     */
    fun removeAvailableExcelFile(template: ExcelTemplate) {
        availableExcelFiles.remove(template)
        // 同时从购物车中移除
        metadataExtractionCart.removeAll { it.excelTemplate.id == template.id }
    }

    /**
     * 检查Excel文件是否在购物车中
     */
    fun isInExtractionCart(excelTemplate: ExcelTemplate): Boolean {
        return metadataExtractionCart.any { it.excelTemplate.id == excelTemplate.id }
    }
}
