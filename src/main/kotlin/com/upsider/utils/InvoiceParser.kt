package com.upsider.utils

import com.upsider.models.InvoiceRequest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeParseException

fun parseInvoiceRequest(json: String): InvoiceRequest {
    val jsonObject = Json.parseToJsonElement(json).jsonObject

    val issueDate = jsonObject["issueDate"]?.jsonPrimitive?.content?.let {
        try {
            LocalDate.parse(it)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid issueDate format. Expected YYYY-MM-DD.")
        }
    } ?: throw IllegalArgumentException("issueDate is required.")

    val paymentAmount = jsonObject["paymentAmount"]?.jsonPrimitive?.content?.let {
        try {
            BigDecimal(it)
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid paymentAmount format. Must be a valid decimal number.")
        }
    } ?: throw IllegalArgumentException("paymentAmount is required.")

    val paymentDueDate = jsonObject["paymentDueDate"]?.jsonPrimitive?.content?.let {
        try {
            LocalDate.parse(it)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid paymentDueDate format. Expected YYYY-MM-DD.")
        }
    } ?: throw IllegalArgumentException("paymentDueDate is required.")

    if (issueDate.isAfter(paymentDueDate)) {
        throw IllegalArgumentException("issueDate must be before or equal to paymentDueDate.")
    }

    return InvoiceRequest(issueDate, paymentAmount, paymentDueDate)
}
