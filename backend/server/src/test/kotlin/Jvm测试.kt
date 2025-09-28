import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import site.addzero.common.util.excel.ExcelUtil
import site.addzero.web.infra.jackson.toJson
import site.addzero.web.infra.jimmer.toMap
import weatherutil.WeatherUtil

//fun main() =

fun main() {

}

fun log(message: Any?) {
    println("[${Thread.currentThread().name}] $message")
}

class Jvm测试 {

    @Test
    fun test111() {
        runBlocking<Unit> {
            val channel = Channel<String>()
            launch {
                channel.send("A1")
                channel.send("A2")
                log("A done")
            }
            launch {
                channel.send("B1")
                log("B done")
            }
            launch {
                repeat(3) {
                    val x = channel.receive()
                    log(x)
                }
            }
        }
    }


    @Test
    fun test1() {
//        val message = System.getenv("SERVER_HOST")
        val message = System.getenv("user.dir")
        val getenv = System.getenv("user.home")
        println(message)

    }


    @Test
    fun test2() {

        val flatMap = (7..12).flatMap {
            val queryWeather = WeatherUtil.queryWeather(
                "2023", it.toString(), "57073", "2"
            )
            queryWeather
        }
        val flatMap1 = (1..4).flatMap {
            val queryWeather = WeatherUtil.queryWeather(
                "2024", it.toString(), "57073", "2"
            )
            queryWeather

        }
        val data = flatMap + flatMap1


        val toMap = data.map { it.toMap() }
        ExcelUtil.exportMapList(toMap, "/Users/zjarlin/Desktop/outtt/112.xlsx")

    }

}
