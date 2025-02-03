package com.upsider.routes

import com.upsider.models.toResponseList
import com.upsider.services.InvoiceService
import com.upsider.utils.parseInvoiceRequest
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeParseException

fun Route.invoiceRoutes(invoiceService: InvoiceService) {
    val logger = LoggerFactory.getLogger(this::class.java.name)

    authenticate("auth-jwt") {
        route("/invoices") {
            post {
                try {
                    val json = call.receive<String>()
                    val request = parseInvoiceRequest(json)
                    invoiceService.createInvoice(request)
                    call.respond(HttpStatusCode.Created)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                } catch (e: Exception) {
                    logger.error("Unexpected error during invoice registration", e)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Internal server error"))
                }
            }

            get {
                try {
                    val startDate = call.request.queryParameters["start_date"]?.let { LocalDate.parse(it) }
                    val endDate = call.request.queryParameters["end_date"]?.let { LocalDate.parse(it) }

                    if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "start_date must be before end_date"))
                        return@get
                    }

                    val invoices = invoiceService.getInvoices(startDate, endDate)
                    call.respond(HttpStatusCode.OK, invoices.toResponseList())
                } catch (e: DateTimeParseException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid date format"))
                } catch (e: Exception) {
                    logger.error("Unexpected error during get invoices", e)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Internal server error"))
                }
            }
        }
    }
}
