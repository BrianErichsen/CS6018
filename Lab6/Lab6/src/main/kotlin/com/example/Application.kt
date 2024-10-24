package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction



fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

object DBConfig {
    val database by lazy { Database.connect("jdbc:h2:mem:test;MODE=MYSQL;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")}

    fun init() {
        transaction(database) {
            SchemaUtils.create(Post)
        }
    }
}

fun Application.module() {
    configureSerialization()
    DBConfig.init()
    configureResources()
}

