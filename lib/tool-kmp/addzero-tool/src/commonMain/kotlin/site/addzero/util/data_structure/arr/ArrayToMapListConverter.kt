package site.addzero.util.data_structure.arr

import site.addzero.str.add


object ArrayToMapListConverter {
    fun convertArrayToListOfMaps(array: Array<Array<String>>): List<Map<String, String>> {
        val listOfMaps: List<Map<String, String>> = ArrayList()

        // 假设第一行是列标题
        val headers = array[0]

        // 遍历除标题外的每一行
        for (i in 1 until array.size) {
            val rowMap: MutableMap<String, String> = HashMap()
            val row = array[i]

            // 遍历每一列，使用headers作为键，row中的元素作为值
            for (j in row.indices) {
                rowMap[headers[j]] = row[j]
            }

            // 将创建的Map添加到列表中
            listOfMaps.add(rowMap)
        }

        return listOfMaps
    }

    fun main(args: Array<String>) {
        // 示例二维数组
        val array = arrayOf(
            arrayOf("ID", "Name", "Age", "City"),
            arrayOf("1", "Alice", "30", "New York"),
            arrayOf("2", "Bob", "25", "Los Angeles"),
            arrayOf("3", "Charlie", "35", "Chicago")
        )

        // 转换为List<Map>
        val listOfMaps = convertArrayToListOfMaps(array)

        // 打印结果
        for (row in listOfMaps) {
            println(row)
        }
    }
}
