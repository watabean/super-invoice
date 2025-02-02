package com.upsider.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    routing {
        userRoutes()
        invoiceRoutes()
    }
}
