package com.addzero.kmp.constant

object Promts {
    const val PROMT_HIS = """
                你需要对用户提出的问题进行判断，如果上下文表示的含义需要用到历史记录，返回true，否则返回false。
                当用户提出的问题无法根据内容进行判断你也不清楚时，默认返回false。
                内容如下:
                {question}
                
                """
    const val PROMT_GRAPH: String = """
            你是一个通用的知识图谱抽取助手,你需要将我输入的文本转为List<Node> nodes 和 List<Line>  lines和List<SPO> spos这些数据结构,分别是实体节点,一个是关系边,一个是实体属性及其值的三元组。
            实体的分类定义: 包括但不限于人物,地点,组织机构,事件,物品等等,最好能列出子分类,如地点可以分为城市,县区,乡镇,村庄等等
            关系的定义:能够作为数据库建表语句的属性值( 如姓名,年龄,身高,体重,爱好等等),relation尽量是实体与实体之间的关系,而不是实体与属性之间的关系
            SPO三元组的定义:S即Subject,即实体的名称或别名,P即Predicate,O即Object,即实体的属性值或别名,还有一个属性context是对该三元组SPO的上下文边界描述,如果原文中没有体现上下文边界信息,请自行总结 .
            注意事项:实体的属性(即SPO出现过的知识,不必形成实体和边,不要将本该作为实体的属性值作为实体。)
            nodes,lines,spos最终用java实体GraphPO包装类返回;
            以下是我输入的文本
            {question}
            """

    val JSON_PATTERN_PROMPT = """
      期望最终返回的结果,即: 结构化的json数据格式如下,最终结果移除开头```json,和结尾```没有偏差 
       您的响应应该是JSON格式。不包括任何解释(不要出现//注释)，只提供符合RFC8259的JSON响应，遵循此格式，没有偏差。不要在响应中包含markdown代码块。从输出中删除``json标记。这是您的输出必须遵循的JSON模式实例：
        """

    val DEFAULT_SYSTEM: String =
        "你是一个友好的AI,请提供有效简短的回答.但是涉及代码回答," + "请根据我的上下文精准回答并给出思考过程," + "不要直接复制粘贴我的代码."


    val DBASIMPLE_JSON_PATTERN_PROMPT = """
        {question}
        ----------------
        结果中不要出现以下基本字段
         `id` varchar(64) not null ,
        `create_by` varchar(255) not null comment '创建者',
        `update_by` varchar(255) null comment '更新者',
        `create_time` datetime not null default current_timestamp comment '创建时间',
        `update_time` datetime null default current_timestamp on update current_timestamp comment '更新时间',
        ------------
        期望json格式如下:
   {
  "tableName": "",
  "tableEnglishName": "",
  "dbType": "",
  "dbName": "",
  "fields": [
    {
      "javaType": "",
      "fieldName": "",
      "fieldChineseName": ""
    }
  ]
} 
""".trimIndent()
    val DBASIMPLE_JSON_PATTERN_PROMPT_CHINESE = """
        你是一个数据提取助手
       """

}