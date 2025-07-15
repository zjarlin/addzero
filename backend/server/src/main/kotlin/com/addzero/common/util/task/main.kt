package com.addzero.common.util.task

fun main() {
    // 创建调度器
    val scheduler = TaskScheduler()

    // 加载配置
    val configs = TaskConfigLoader.loadFromJson("tasks.json")

    // 启动所有任务
    scheduler.loadAndStartTasks(configs)

    // 运行一段时间
    Thread.sleep(1000000)

    // 停止特定任务
    scheduler.stopTask("task1")

    // 或者停止所有任务
    scheduler.stopAll()
}