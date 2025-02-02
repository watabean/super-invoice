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
    val invoiceRepository = InvoiceRepository()
    val invoiceService = InvoiceService(invoiceRepository)
    val userRepository = UserRepository()
    val userService = UserService(userRepository)
    configureRouting(userService, invoiceService)
}
