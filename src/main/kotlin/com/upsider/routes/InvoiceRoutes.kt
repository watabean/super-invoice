package com.upsider.routes

import com.upsider.models.InvoiceRequest
import com.upsider.services.InvoiceService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDate

fun Route.invoiceRoutes() {
    val invoiceService by inject<InvoiceService>()


    route("/invoices") {
        post {
            val request = call.receive<InvoiceRequest>()
            val invoice = invoiceService.createInvoice(request)
            call.respond(HttpStatusCode.Created, invoice)
        }

        get {
            val startDate = call.request.queryParameters["start_date"]?.let { LocalDate.parse(it) }
            val endDate = call.request.queryParameters["end_date"]?.let { LocalDate.parse(it) }
            val invoices = invoiceService.getInvoices(startDate, endDate)
            call.respond(HttpStatusCode.OK, invoices)
        }
    }
}