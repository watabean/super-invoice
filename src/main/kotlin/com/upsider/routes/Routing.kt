package com.upsider.routes

import com.upsider.services.InvoiceService
import com.upsider.services.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureRouting(userService: UserService, invoiceService: InvoiceService) {
    routing {
        userRoutes(userService)
        invoiceRoutes(invoiceService)
    }
}
