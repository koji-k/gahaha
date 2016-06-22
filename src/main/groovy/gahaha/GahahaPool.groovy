package gahaha

import groovy.sql.Sql

class GahahaPool {
    static Sql sql
    static defaultJdbcUrl = "jdbc:h2:mem:gahaha"
    static defaultJdbcDriverClassName = "org.h2.Driver"

    static connection() {
        connection(defaultJdbcUrl, defaultJdbcDriverClassName)
    }

    static connection(String jdbcUrl, String jdbcDriverClassName) {
        sql = Sql.newInstance(jdbcUrl, jdbcDriverClassName)
        sql
    }
}