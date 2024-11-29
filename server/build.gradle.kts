plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    application

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}

group = "com.omasyo.gatherspace"
version = "1.0.0"
application {
    mainClass.set("com.omasyo.gatherspace.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {


    implementation(projects.shared.common)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.sse)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.resources)
    implementation(libs.kotlinx.datetime)
    implementation(libs.postgresql)
    implementation(libs.h2)
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback)
    implementation(libs.sqldelight.coroutines)
    implementation(libs.sqldelight.jdbc.driver)
    implementation(libs.hikari)
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.omasyo.gatherspace.database")
            dialect(libs.sqldelight.postgresql.dialect)
        }
    }
}
