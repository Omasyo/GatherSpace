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