package com.addzero.web.jdbc.metadata

enum class DbEnum(val key: String) {
    MYSQL("mysql"),
    H2("h2"),
    MYSQL5("mysql5"),
    ORACLE("oracle"),
    POSTGRESQL("pg"),
    DM("dm"),
    TiDB("tidb"),
    SQLSERVER("sqlserver");
}
