package com.addzero.kmp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.addzero.kmp.api.ApiProvider.fileApi
import io.ktor.client.request.forms.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


sealed class FileTaskState {
    object Idle : FileTaskState()
    data class Progress(val percent: Float) : FileTaskState()
    data class Success(val result: String) : FileTaskState()
    data class Error(val message: String) : FileTaskState()
}

@KoinViewModel
class FileViewModel() : ViewModel() {
    var taskState by mutableStateOf<FileTaskState>(FileTaskState.Idle)
        private set
    var currentKey: String? = null
        private set
    private var pollingJob: Job? = null

    private fun upload(file: MultiPartFormDataContent) {
        viewModelScope.launch {
            try {
                val key = fileApi.upload(file) // 返回redis key
                currentKey = key
                pollProgress(key)
            } catch (e: Exception) {
                taskState = FileTaskState.Error(e.message ?: "上传失败")
            }
        }
    }

    private fun download(fileId: String) {
        viewModelScope.launch {
            try {
                val key = fileApi.download(fileId) // 伪代码: 假设有download接口返回redis key
                currentKey = key
                pollProgress(key)
            } catch (e: Exception) {
                taskState = FileTaskState.Error(e.message ?: "下载失败")
            }
        }
    }

    private fun pollProgress(key: String) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (true) {
                try {
                    val res = fileApi.queryProgress(key) // 移除type参数，只用key

                    val progress = res.progress
                    if (progress >= 1f) {
                        taskState = FileTaskState.Success(key)
                        break
                    } else {
                        taskState = FileTaskState.Progress(progress)
                    }
                } catch (e: Exception) {
                    taskState = FileTaskState.Error(e.message ?: "进度查询失败")
                    break
                }
                delay(1000)
            }
        }
    }

    override fun onCleared() {
        pollingJob?.cancel()
        super.onCleared()
    }
}

