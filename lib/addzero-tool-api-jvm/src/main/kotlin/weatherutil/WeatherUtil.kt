package weatherutil

import cn.hutool.http.HttpUtil
import com.alibaba.fastjson2.JSON
import weatherutil.Assist.isFutureDate
import weatherutil.Assist.splitDateAndWeekday

/**
 * 2345天气查询接口
 *
 * @author zjarlin
 * @since 2023/04/09
 */
object WeatherUtil {
    /**
     * 洛阳天气查询
     *
     * @param year  一年
     * @param month 月 入参
     * @return [List]<[WeatherData]>
     * @author zjarlin
     * @since 2023/04/10
     */
    fun queryWeather(year: String, month: String, areaId: String, areaType: String): MutableList<WeatherData?> {
        require(!isFutureDate(year.toInt(), month.toInt())) { "不能查询未来的天气!" }

        val url = "https://tianqi.2345.com/Pc/GetHistory"
        var areaId = areaId
        var areaType = areaType
        val cookie =
            "positionCityID=71778; positionCityPinyin=luolong; lastProvinceId=20; lastCityId=57073; Hm_lvt_a3f2879f6b3620a363bec646b7a8bcdd=1681045373; lastCountyId=57073; lastTownId=-1; lastTownTime=1681045444; lastCountyPinyin=luoyang; lastAreaName=æ´›é˜³; Hm_lpvt_a3f2879f6b3620a363bec646b7a8bcdd=1681045482; lastCountyTime=1681045481"
        val referer = "https://tianqi.2345.com/wea_history/57073.htm"

        val result = HttpUtil.createGet(url)
            .header("Accept", "application/json, text/javascript, */*; q=0.01")
            .header("Accept-Language", "zh-CN,zh;q=0.9")
            .header("Referer", referer)
            .header("Cookie", cookie)
            .header(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36"
            )
            .header("X-Requested-With", "XMLHttpRequest")
            .header("sec-ch-ua", "\"Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111\"")
            .header("sec-ch-ua-mobile", "?0")
            .header("sec-ch-ua-platform", "\"macOS\"")
            .form("areaInfo[areaId]", areaId)
            .form("areaInfo[areaType]", areaType)
            .form("date[year]", year)
            .form("date[month]", month)
            .execute().body()

        val jsonObject = JSON.parseObject(result)
        val data = jsonObject.getString("data")
        val parse = WeatherParser.parseHtml(data)

        parse.forEach {
            it.apply {
                val date1 = it?.date ?: ""
                val splitDateAndWeekday = date1.splitDateAndWeekday()
                this?.date = splitDateAndWeekday.first
                this?.week = splitDateAndWeekday.second
                this?.areaId = areaId
                this?.areaType = areaType
            }
        }
        return parse
    }

}