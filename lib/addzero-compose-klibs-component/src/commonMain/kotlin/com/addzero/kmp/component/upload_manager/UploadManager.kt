@file:OptIn(ExperimentalTime::class)

package com.addzero.kmp.component.upload_manager

import androidx.compose.runtime.mutableStateMapOf
import com.addzero.kmp.component.file_picker.USE_MOCK_FILE_UPLOAD
import com.addzero.kmp.component.file_picker.mockQueryProgress
import io.ktor.client.request.forms.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

/**
 * 上传任务状态
 */
enum class UploadTaskStatus {
    PENDING,    // 等待上传
    UPLOADING,  // 上传中
    QUERYING,   // 查询进度中
    COMPLETED,  // 上传完成
    FAILED      // 上传失败
}

/**
 * 上传任务数据类
 */
@Serializable
data class UploadTask(
    val id: String,                                    // 任务唯一ID
    val fileName: String,                              // 文件名
    val fileSize: Long?,                              // 文件大小
    val redisKey: String? = null,                     // 上传后获得的redisKey
    val fileUrl: String? = null,                      // 最终文件URL
    val progress: Float = 0f,                         // 上传进度 0-1
    val status: UploadTaskStatus = UploadTaskStatus.PENDING,
    val errorMessage: String? = null,                 // 错误信息
) {
    // 计算上传速度等辅助属性可以在这里添加
    val isActive: Boolean
        get() = status in listOf(
            UploadTaskStatus.PENDING,
            UploadTaskStatus.UPLOADING,
            UploadTaskStatus.QUERYING
        )
    val isCompleted: Boolean get() = status == UploadTaskStatus.COMPLETED
    val isFailed: Boolean get() = status == UploadTaskStatus.FAILED
}

/**
 * 全局上传管理器
 * 类似浏览器下载管理器，统一管理所有上传任务
 */
class UploadManager {
    // 所有上传任务
    private val _tasks = mutableStateMapOf<String, UploadTask>()
    val tasks: Map<String, UploadTask> = _tasks

    // 活跃任务列表（正在进行的任务）
    val activeTasks: List<UploadTask>
        get() = _tasks.values.filter { it.isActive }

    // 已完成任务列表
    val completedTasks: List<UploadTask>
        get() = _tasks.values.filter { it.isCompleted }

    // 失败任务列表
    val failedTasks: List<UploadTask>
        get() = _tasks.values.filter { it.isFailed }

    // 协程作用域
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // 任务执行Job映射
    private val taskJobs = mutableMapOf<String, Job>()

    /**
     * 添加上传任务
     */
    fun addTask(
        fileName: String,
        fileSize: Long?,
        content: MultiPartFormDataContent
    ): String {
        val taskId = generateTaskId()
        val task = UploadTask(
            id = taskId,
            fileName = fileName,
            fileSize = fileSize
        )

        _tasks[taskId] = task

        // 启动上传任务
        startUploadTask(taskId, content)

        return taskId
    }

    /**
     * 启动上传任务
     */
    private fun startUploadTask(taskId: String, content: MultiPartFormDataContent) {
        taskJobs[taskId] = scope.launch {
            try {
                // 更新状态为上传中
                updateTaskStatus(taskId, UploadTaskStatus.UPLOADING)

                // 1. 上传文件获取redisKey
                val redisKey = if (USE_MOCK_FILE_UPLOAD) {
                    mockUploadFile(content)
                } else {
                    ""
//                    fileApi.upload(content)
                }

                // 更新redisKey
                updateTask(taskId) { it.copy(redisKey = redisKey, status = UploadTaskStatus.QUERYING) }

                // 2. 查询上传进度
                queryUploadProgress(taskId, redisKey)

            } catch (e: Exception) {
                updateTask(taskId) {
                    it.copy(
                        status = UploadTaskStatus.FAILED,
                        errorMessage = e.message ?: "上传失败"
                    )
                }
            }
        }
    }

    /**
     * 查询上传进度
     */
    private suspend fun queryUploadProgress(taskId: String, redisKey: String) {
        while (true) {
            try {
                if (USE_MOCK_FILE_UPLOAD) {
                    val response = mockQueryProgress(redisKey)

                    // 更新进度
                    updateTask(taskId) { task ->
                        task.copy(
                            progress = response.progress,
                            fileUrl = response.fileUrl.takeIf { it.isNotEmpty() }
                        )
                    }

                    // 检查是否完成
                    if (response.progress >= 1f) {
                        updateTask(taskId) {
                            it.copy(
                                status = UploadTaskStatus.COMPLETED,
                            )
                        }
                        break
                    }
                } else {
                    // 真实API调用 - 需要根据实际API响应结构调整
//                    val response = fileApi.queryProgress(redisKey)
                    // TODO: 根据实际API响应结构更新这部分代码
                    // 暂时使用模拟逻辑
                    updateTask(taskId) { task ->
                        task.copy(
                            progress = 1f, // 临时设为完成
                            status = UploadTaskStatus.COMPLETED,
                        )
                    }
                    break
                }

                delay(1000) // 每秒查询一次

            } catch (e: Exception) {
                updateTask(taskId) {
                    it.copy(
                        status = UploadTaskStatus.FAILED,
                        errorMessage = e.message ?: "进度查询失败"
                    )
                }
                break
            }
        }
    }

    /**
     * 取消任务
     */
    fun cancelTask(taskId: String) {
        taskJobs[taskId]?.cancel()
        taskJobs.remove(taskId)
        _tasks.remove(taskId)
    }

    /**
     * 重试任务
     */
    fun retryTask(taskId: String, content: MultiPartFormDataContent) {
        cancelTask(taskId)
        val task = _tasks[taskId] ?: return

        val newTask = task.copy(
            status = UploadTaskStatus.PENDING,
            progress = 0f,
            errorMessage = null,
            redisKey = null,
            fileUrl = null
        )
        _tasks[taskId] = newTask

        startUploadTask(taskId, content)
    }

    /**
     * 清除已完成的任务
     */
    fun clearCompletedTasks() {
        val completedTaskIds = _tasks.values.filter { it.isCompleted }.map { it.id }
        completedTaskIds.forEach { _tasks.remove(it) }
    }

    /**
     * 清除失败的任务
     */
    fun clearFailedTasks() {
        val failedTaskIds = _tasks.values.filter { it.isFailed }.map { it.id }
        failedTaskIds.forEach { _tasks.remove(it) }
    }

    // 辅助方法
    private fun generateTaskId(): String {
        return "upload_${111}_${(0..9999).random()}"
    }

    private fun updateTaskStatus(taskId: String, status: UploadTaskStatus) {
        updateTask(taskId) { it.copy(status = status) }
    }

    private fun updateTask(taskId: String, update: (UploadTask) -> UploadTask) {
        _tasks[taskId]?.let { task ->
            _tasks[taskId] = update(task)
        }
    }

    /**
     * 模拟文件上传API
     */
    private suspend fun mockUploadFile(content: MultiPartFormDataContent): String {
        delay(500) // 模拟网络延迟
        return "file_upload_${111}"
    }
}

/**
 * 全局上传管理器实例
 */
object GlobalUploadManager {
    val instance = UploadManager()
}
