package com.omasyo.gatherspace.data

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.zaxxer.hikari.HikariDataSource

val database = run {
    val driver = HikariDataSource().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        username = "postgres"
        password = "pass"
    }.asJdbcDriver()
    Database.Schema.create(driver)
    Database(driver)
}

fun createDatabase(username: String, password: String): Database {
    val driver = HikariDataSource().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        this.username = username
        this.password = password
    }.asJdbcDriver()
    Database.Schema.create(driver)
    return Database(driver)
}