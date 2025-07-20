import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.timer

fun main() {
    val scheduler = TaskScheduler()

    // 5秒后执行一次
    val scheduleOnce = scheduler.scheduleOnce(5) {
        println("延迟5秒执行一次: ${Date()}")
    }

    // 每2秒执行一次
    val timer1 = scheduler.scheduleAtFixedRate(2) {
        println("每2秒执行一次: ${Date()}")
    }

    // 每天特定时间执行
//    val timer2 = scheduler.scheduleDaily(14, 30) {
//        println("每天14:30执行: ${Date()}")
//    }

    // 等待一段时间后取消
    Thread.sleep(100000)
//    timer1.cancel()

    // 或者取消所有任务
    scheduler.cancelAll()
}

class TaskScheduler {
    private val timers = mutableListOf<Timer>()
    private val tasks = mutableListOf<TimerTask>()

    /**
     * 延迟执行一次
     */
    fun scheduleOnce(delaySeconds: Long, action: () -> Unit): Timer {
        val timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                action()
                timer.cancel() // 执行完就取消
            }
        }
        timer.schedule(task, delaySeconds * 1000)
        timers.add(timer)
        tasks.add(task)
        return timer
    }

    /**
     * 固定间隔重复执行
     */
    fun scheduleAtFixedRate(intervalSeconds: Long, action: () -> Unit): Timer {
        return fixedRateTimer(
            initialDelay = 0,
            period = intervalSeconds * 1000
        ) {
            action()
        }.also { timers.add(it) }
    }

    /**
     * 在指定时间执行任务
     */
    fun scheduleDaily(hour: Int, minute: Int, action: () -> Unit): Timer {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        return timer(
            startAt = calendar.time,
            period = 24 * 60 * 60 * 1000
        ) {
            action()
        }.also { timers.add(it) }
    }

    /**
     * 取消所有定时任务
     */
    fun cancelAll() {
        timers.forEach { it.cancel() }
        timers.clear()
        tasks.forEach { it.cancel() }
        tasks.clear()
    }
}