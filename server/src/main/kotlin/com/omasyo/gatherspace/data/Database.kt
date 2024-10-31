package com.omasyo.gatherspace.data

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.omasyo.gatherspace.database.Database
import com.zaxxer.hikari.HikariDataSource

fun createDatabase(username: String, password: String): Database {
    val driver = HikariDataSource().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        this.username = username
        this.password = password
    }.asJdbcDriver()
    Database.Schema.create(driver)
    return Database(driver)
}