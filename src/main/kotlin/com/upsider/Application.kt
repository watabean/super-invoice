package com.upsider

import com.upsider.routes.configureRouting
import com.upsider.services.InvoiceService
import com.upsider.services.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val invoiceService = InvoiceService()
    val userService = UserService()
    configureRouting(invoiceService, userService)
}
