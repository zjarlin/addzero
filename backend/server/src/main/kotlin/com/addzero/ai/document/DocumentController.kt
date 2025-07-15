package com.addzero.ai.document

import com.addzero.ai.util.ai.ai_abs_builder.AiUtil
import com.addzero.kmp.entity.ai.EmbDTO
import com.addzero.web.infra.jackson.toJson
import io.swagger.v3.oas.annotations.Operation
import org.springframework.ai.document.Document
import org.springframework.ai.reader.tika.TikaDocumentReader
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.core.io.InputStreamResource
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController

@RequestMapping("/document")
class DocumentController(
    private val vectorStore: VectorStore,
) {

    /**
     * 嵌入文件
     *
     * @param file 待嵌入的文件
     * @return 是否成功
     */
    @PostMapping("embedding")
    @Operation(summary = "嵌入文件", description = "Embed a file into the vector store")
    fun embedding(@RequestPart file: MultipartFile): MutableList<String>? {
        // 从IO流中读取文件
        val tikaDocumentReader = TikaDocumentReader(InputStreamResource(file.inputStream))
        // 将文本内容划分成更小的块
        val splitDocuments = AiUtil.getTikaSplitContent(tikaDocumentReader)
        // 存入向量数据库，这个过程会自动调用embeddingModel,将文本变成向量再存入。
        splitDocuments.stream().forEach {
            val metadata = it.metadata
            metadata.put("filename", file.originalFilename)
//            metadata.put("fid", file.originalFilename)
        }
        vectorStore.add(splitDocuments)
        val toList = splitDocuments.stream().map { it.id }.toList()
        return toList
    }

    /**
     * 嵌入文本
     *
     * @return 是否成功
     */
    @PostMapping("embeddingText")
    @Operation(summary = "嵌入文本", description = "Embed a text into the vector store")
    fun embeddingText(@RequestBody embDTO: EmbDTO): MutableList<String>? {
        val document = Document(embDTO.content, embDTO.metadata)
        val list = listOf(document)
        vectorStore.add(list)
        val distinct = list.stream().map { it.id }.toList()
        return distinct
    }

    /**
     * 查询向量数据库
     *
     * @param query 用户的提问
     * @return 匹配到的文档
     */
    @GetMapping("query")
    @Operation(summary = "查询向量库", description = "Retrieve documents similar to a query")
    fun query(@RequestParam query: String): String {
        // Retrieve documents similar to a query
//        val b = FilterExpressionBuilder()
        val TOP_K = 5
        val SIMILARITY_THRESHOLD = 0.85
        val request = SearchRequest.builder().query(query).topK(TOP_K).similarityThreshold(SIMILARITY_THRESHOLD).build()
        val documents = vectorStore.similaritySearch(request)
        return documents?.toJson() ?: ""

    }
}
