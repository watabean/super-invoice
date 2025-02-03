package com.upsider

import com.upsider.repositories.InvoiceRepository
import com.upsider.repositories.UserRepository
import com.upsider.routes.configureRouting
import com.upsider.services.InvoiceService
import com.upsider.services.UserService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    connectToDatabase()
    val invoiceRepository = InvoiceRepository()
    val userRepository = UserRepository()
    val invoiceService = InvoiceService(invoiceRepository, userRepository)
    val userService = UserService(userRepository)
    configureRouting(userService, invoiceService)
}

fun Application.connectToDatabase() {
    val dbConfig = environment.config.config("ktor.database")

    Database.connect(
        url = dbConfig.property("url").getString(),
        driver = dbConfig.property("driver").getString(),
        user = dbConfig.property("user").getString(),
        password = dbConfig.property("password").getString()
    )
}