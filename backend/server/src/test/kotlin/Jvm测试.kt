import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.babyfish.jimmer.Draft
import org.babyfish.jimmer.runtime.DraftSpi
import org.junit.jupiter.api.Test
import site.addzero.common.util.excel.ExcelUtil
import site.addzero.model.entity.SysUser
import site.addzero.model.entity.SysUserDraft
import site.addzero.web.infra.jackson.toJson
import site.addzero.web.infra.jimmer.toMap
import weatherutil.WeatherUtil
import kotlin.jvm.java

//fun main() =

fun main() {

}

fun log(message: Any?) {
    println("[${Thread.currentThread().name}] $message")
}

class Jvm测试 {


    @Test
    fun `test11`() {
        val sysUser = SysUser {
        }
        //todo 上面假设是数据库查完了 我想经过aop切面做一些后置工作

       //todo 假设走到了   val obj= pjp.proceed()
        if (obj is xxxx) {
            // todo  这里可能直接返回实体对象
        }else if (obj is xxxxx) {
           //todo 这里可能是dto捏的对象  :View
        }
    }

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
