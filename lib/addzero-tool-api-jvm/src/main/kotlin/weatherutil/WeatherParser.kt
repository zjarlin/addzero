package weatherutil

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.stream.Collectors

object WeatherParser {
    fun parseHtml(html: String?): MutableList<WeatherData?> {
        val doc = Jsoup.parse(html!!)
        val table = doc.select("table.history-table")
        // 解析历史天气数据表格
        val rows = table.select("tr")
        // 去掉表头行
        rows.removeAt(0)
        // 解析每一行数据，构造 WeatherData 对象
        return rows.stream().map<WeatherData?> { row: Element? ->
            val cells = row!!.select("td")
            val date = cells.get(0).text()
            val highTemp = cells.get(1).text().replace("°", "").toInt()
            val lowTemp = cells.get(2).text().replace("°", "").toInt()
            val amCondition = cells.get(3).text()
            val pmCondition = cells.get(3).text()
            val wind = cells.get(4).text()
            val span = cells.get(5).select("span").text()
            val aqi = if (span == "-") 0 else span.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0].toInt()
            val weatherData = WeatherData(
                date = date,
                highTemp = highTemp,
                lowTemp = lowTemp,
                amCondition = amCondition,
                pmCondition = pmCondition,
                wind = wind,
                aqi = aqi
            )
            weatherData
        }.collect(Collectors.toList())
    }
}
