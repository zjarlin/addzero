package com.addzero.kmp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.addzero.kmp.core.network.json.json
import kotlinx.serialization.json.*
import org.koin.android.annotation.KoinViewModel

/**
 * JSON设计器ViewModel
 * 管理JSON结构的构建和状态，支持双向编辑
 */
@KoinViewModel
class JsonDesignerViewModel : ViewModel() {
    
    /**
     * JSON元素类型
     */
    enum class JsonElementType {
        OBJECT,     // 对象 {}
        ARRAY,      // 数组 []
        STRING,     // 字符串
        NUMBER,     // 数字
        BOOLEAN,    // 布尔值
        NULL        // 空值
    }
    
    /**
     * JSON元素数据类
     */
    data class JsonElement(
        val id: String = generateId(),
        var type: JsonElementType = JsonElementType.OBJECT,
        var key: String = "",
        var value: String = "",
        val children: MutableList<JsonElement> = mutableStateListOf(),
        var isExpanded: Boolean = true,
        var parent: JsonElement? = null
    ) {
        companion object {
            private var idCounter = 0
            fun generateId(): String = "element_${++idCounter}"
        }
    }
    
    /**
     * Excel模板数据类
     */
    data class ExcelTemplate(
        val id: String = generateTemplateId(),
        val name: String,
        val fileName: String,
        val isCommon: Boolean = false,
//        val uploadTime: Long = System.currentTimeMillis()
    ) {
        companion object {
            private var templateIdCounter = 0
            fun generateTemplateId(): String = "template_${++templateIdCounter}"
        }
    }
    
    // 根JSON元素
    var rootElement by mutableStateOf(JsonElement(type = JsonElementType.OBJECT))
        private set
    
    // 当前选中的元素
    var selectedElement by mutableStateOf<JsonElement?>(null)
        private set
    
    // JSON预览文本
    var jsonPreview by mutableStateOf("{}")
        private set
    
    // 用户编辑的JSON文本
    var editableJsonText by mutableStateOf("{}")
        private set
    
    // 是否正在从编辑器同步到树
    private var isSyncingFromEditor = false
    
    // 上传的Excel文件列表
    val uploadedExcelFiles = mutableStateListOf<ExcelTemplate>()
    
    // 常用模板列表
    val commonTemplates = mutableStateListOf<ExcelTemplate>()
    
    // 是否显示JSON预览
    var showJsonPreview by mutableStateOf(true)
        private set
    
    // 错误信息
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    init {
        updateJsonPreview()
    }
    
    /**
     * 添加对象元素
     */
    fun addObject(parent: JsonElement? = null) {
        val targetParent = parent ?: rootElement
        val newElement = JsonElement(
            type = JsonElementType.OBJECT,
            key = if (targetParent.type == JsonElementType.ARRAY) "" else "newObject",
            parent = targetParent
        )
        targetParent.children.add(newElement)
        updateJsonPreview()
    }
    
    /**
     * 添加数组元素
     */
    fun addArray(parent: JsonElement? = null) {
        val targetParent = parent ?: rootElement
        val newElement = JsonElement(
            type = JsonElementType.ARRAY,
            key = if (targetParent.type == JsonElementType.ARRAY) "" else "newArray",
            parent = targetParent
        )
        targetParent.children.add(newElement)
        updateJsonPreview()
    }
    
    /**
     * 添加字符串元素
     */
    fun addString(parent: JsonElement? = null, key: String = "newString", value: String = "") {
        val targetParent = parent ?: rootElement
        val newElement = JsonElement(
            type = JsonElementType.STRING,
            key = if (targetParent.type == JsonElementType.ARRAY) "" else key,
            value = value,
            parent = targetParent
        )
        targetParent.children.add(newElement)
        updateJsonPreview()
    }
    
    /**
     * 添加数字元素
     */
    fun addNumber(parent: JsonElement? = null, key: String = "newNumber", value: String = "0") {
        val targetParent = parent ?: rootElement
        val newElement = JsonElement(
            type = JsonElementType.NUMBER,
            key = if (targetParent.type == JsonElementType.ARRAY) "" else key,
            value = value,
            parent = targetParent
        )
        targetParent.children.add(newElement)
        updateJsonPreview()
    }
    
    /**
     * 删除元素
     */
    fun deleteElement(element: JsonElement) {
        element.parent?.children?.remove(element)
        if (selectedElement == element) {
            selectedElement = null
        }
        updateJsonPreview()
    }
    
    /**
     * 选择元素
     */
    fun selectElement(element: JsonElement) {
        selectedElement = element
    }
    
    /**
     * 更新元素
     */
    fun updateElement(element: JsonElement, key: String? = null, value: String? = null, type: JsonElementType? = null) {
        key?.let { element.key = it }
        value?.let { element.value = it }
        type?.let { element.type = it }
        updateJsonPreview()
    }
    
    /**
     * 切换元素展开状态
     */
    fun toggleElementExpansion(element: JsonElement) {
        element.isExpanded = !element.isExpanded
    }
    
    /**
     * 更新JSON预览
     */
    private fun updateJsonPreview() {
        if (isSyncingFromEditor) return
        
        try {
            val jsonObject = buildJsonElement(rootElement)
            val prettyJson = json.encodeToString(jsonObject)

            jsonPreview = prettyJson
            editableJsonText = prettyJson
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = "JSON构建错误: ${e.message}"
        }
    }
    
    /**
     * 从编辑器更新JSON
     */
    fun updateJsonFromEditor(jsonText: String) {
        editableJsonText = jsonText
        try {
            isSyncingFromEditor = true
            val jsonElement = Json.parseToJsonElement(jsonText)
            rootElement = parseJsonElement(jsonElement, "root")
            selectedElement = null
            jsonPreview = json.encodeToString(jsonElement)
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = "JSON解析错误: ${e.message}"
        } finally {
            isSyncingFromEditor = false
        }
    }
    
    /**
     * 上传Excel文件
     */
    fun uploadExcelFile(fileName: String, onUpload: (String) -> Unit) {
        // TODO: 实际的文件上传逻辑
        val template = ExcelTemplate(
            name = fileName.substringBeforeLast("."),
            fileName = fileName
        )
        uploadedExcelFiles.add(template)
        onUpload(fileName)
    }
    
    /**
     * 保存为常用模板
     */
    fun saveAsCommonTemplate(template: ExcelTemplate) {
        val commonTemplate = template.copy(isCommon = true)
        commonTemplates.add(commonTemplate)
        uploadedExcelFiles.remove(template)
    }
    
    /**
     * 删除模板
     */
    fun deleteTemplate(template: ExcelTemplate) {
        uploadedExcelFiles.remove(template)
        commonTemplates.remove(template)
    }
    
    /**
     * 清空所有数据
     */
    fun clearAll() {
        rootElement = JsonElement(type = JsonElementType.OBJECT)
        selectedElement = null
        updateJsonPreview()
    }
    
    /**
     * 切换JSON预览显示
     */
    fun toggleJsonPreview() {
        showJsonPreview = !showJsonPreview
    }

    /**
     * 构建JSON元素
     */
    private fun buildJsonElement(element: JsonElement): kotlinx.serialization.json.JsonElement {
        return when (element.type) {
            JsonElementType.OBJECT -> {
                val map = mutableMapOf<String, kotlinx.serialization.json.JsonElement>()
                element.children.forEach { child ->
                    if (child.key.isNotBlank()) {
                        map[child.key] = buildJsonElement(child)
                    }
                }
                JsonObject(map)
            }
            JsonElementType.ARRAY -> {
                val list = element.children.map { buildJsonElement(it) }
                JsonArray(list)
            }
            JsonElementType.STRING -> JsonPrimitive(element.value)
            JsonElementType.NUMBER -> {
                try {
                    if (element.value.contains(".")) {
                        JsonPrimitive(element.value.toDouble())
                    } else {
                        JsonPrimitive(element.value.toLong())
                    }
                } catch (e: NumberFormatException) {
                    JsonPrimitive(element.value)
                }
            }
            JsonElementType.BOOLEAN -> JsonPrimitive(element.value.toBooleanStrictOrNull() ?: false)
            JsonElementType.NULL -> JsonNull
        }
    }

    /**
     * 解析JSON元素
     */
    private fun parseJsonElement(jsonElement: kotlinx.serialization.json.JsonElement, key: String): JsonElement {
        return when (jsonElement) {
            is JsonObject -> {
                val element = JsonElement(type = JsonElementType.OBJECT, key = key)
                jsonElement.forEach { (k, v) ->
                    val child = parseJsonElement(v, k)
                    child.parent = element
                    element.children.add(child)
                }
                element
            }
            is JsonArray -> {
                val element = JsonElement(type = JsonElementType.ARRAY, key = key)
                jsonElement.forEachIndexed { index, v ->
                    val child = parseJsonElement(v, "")
                    child.parent = element
                    element.children.add(child)
                }
                element
            }
            is JsonPrimitive -> {
                when {
                    jsonElement.isString -> JsonElement(
                        type = JsonElementType.STRING,
                        key = key,
                        value = jsonElement.content
                    )
                    jsonElement.content.toBooleanStrictOrNull() != null -> JsonElement(
                        type = JsonElementType.BOOLEAN,
                        key = key,
                        value = jsonElement.content
                    )
                    else -> JsonElement(
                        type = JsonElementType.NUMBER,
                        key = key,
                        value = jsonElement.content
                    )
                }
            }
            JsonNull -> JsonElement(type = JsonElementType.NULL, key = key, value = "null")
        }
    }
}
