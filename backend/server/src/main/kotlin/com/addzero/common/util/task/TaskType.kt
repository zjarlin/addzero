package com.addzero.common.util.task

import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.timer

/**
 * 任务类型枚举
 */
enum class TaskType {
    ONCE,           // 执行一次
    FIXED_RATE,     // 固定间隔重复执行
    DAILY          // 每天定时执行
}

/**
 * 任务配置数据类
 */
data class TaskConfig(
    val id: String,                 // 任务ID
    val name: String,               // 任务名称
    val type: TaskType,             // 任务类型
    val command: String,            // 要执行的命令

    // 以下字段仅用于定时任务
    val delaySeconds: Long = 0,     // 延迟时间（秒）

    // 以下字段仅用于固定间隔重复执行
    val intervalSeconds: Long = 0,   // 间隔时间（秒）

    // 以下字段仅用于每日定时执行
    val hour: Int = 0,              // 每天执行的小时（24小时制）
    val minute: Int = 0,            // 每天执行的分钟
    val enabled: Boolean = true     // 是否启用
)

/**
 * 任务调度器
 */
class TaskScheduler {
    private val timers = mutableMapOf<String, Timer>()
    private val tasks = mutableMapOf<String, TimerTask>()

    /**
     * 从配置文件加载并启动任务
     */
    fun loadAndStartTasks(configs: List<TaskConfig>) {
        configs.filter { it.enabled }.forEach { config ->
            startTask(config)
        }
    }

    /**
     * 启动单个任务
     */
    private fun startTask(config: TaskConfig) {
        println("Starting task: ${config.name} (${config.id})")

        when (config.type) {
            TaskType.ONCE -> scheduleOnce(config)
            TaskType.FIXED_RATE -> scheduleAtFixedRate(config)
            TaskType.DAILY -> scheduleDaily(config)
        }
    }

    private fun scheduleOnce(config: TaskConfig) {
        val timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                executeCommand(config)
                timer.cancel()
                timers.remove(config.id)
                tasks.remove(config.id)
            }
        }
        timer.schedule(task, config.delaySeconds * 1000)
        timers[config.id] = timer
        tasks[config.id] = task
    }

    private fun scheduleAtFixedRate(config: TaskConfig) {
        val timer = fixedRateTimer(
            initialDelay = config.delaySeconds * 1000,
            period = config.intervalSeconds * 1000
        ) {
            executeCommand(config)
        }
        timers[config.id] = timer
    }

    private fun scheduleDaily(config: TaskConfig) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, config.hour)
            set(Calendar.MINUTE, config.minute)
            set(Calendar.SECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        val timer = timer(
            startAt = calendar.time,
            period = 24 * 60 * 60 * 1000
        ) {
            executeCommand(config)
        }
        timers[config.id] = timer
    }

    /**
     * 执行命令
     */
    private fun executeCommand(config: TaskConfig) {
        println("Executing task ${config.name} (${config.id}) at ${Date()}")
        try {
            // 这里执行实际的命令
            when {
                config.command.startsWith("http") -> {
                    // 执行HTTP请求
                    println("Executing HTTP request: ${config.command}")
                }

                else -> {
                    // 执行本地命令
                    println("Executing command: ${config.command}")
                }
            }
        } catch (e: Exception) {
            println("Error executing task ${config.name}: ${e.message}")
        }
    }

    /**
     * 停止指定任务
     */
    fun stopTask(taskId: String) {
        timers[taskId]?.cancel()
        tasks[taskId]?.cancel()
        timers.remove(taskId)
        tasks.remove(taskId)
    }

    /**
     * 停止所有任务
     */
    fun stopAll() {
        timers.values.forEach { it.cancel() }
        tasks.values.forEach { it.cancel() }
        timers.clear()
        tasks.clear()
    }
}

/**
 * 配置加载器
 */
object TaskConfigLoader {
    /**
     * 从JSON文件加载配置
     */
    fun loadFromJson(jsonPath: String): List<TaskConfig> {
        // 这里实现从JSON文件加载配置的逻辑
        // 示例配置
        return listOf(
            TaskConfig(
                id = "task1",
                name = "一次性任务",
                type = TaskType.ONCE,
                command = "echo 'Hello once'",
                delaySeconds = 5
            ),
            TaskConfig(
                id = "task2",
                name = "定期任务",
                type = TaskType.FIXED_RATE,
                command = "echo 'Hello every 60 seconds'",
                intervalSeconds = 2
            ),
            TaskConfig(
                id = "task3",
                name = "每日任务",
                type = TaskType.DAILY,
                command = "echo 'Good morning'",
                hour = 2,
                minute = 30
            )
        )
    }
}