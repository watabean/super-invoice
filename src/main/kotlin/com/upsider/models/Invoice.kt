package com.upsider.models

import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDate

data class Invoice(
    val id: Int,
    val userId: Int,
    val issueDate: LocalDate,
    val paymentAmount: BigDecimal,
    val fee: BigDecimal,
    val feeRate: BigDecimal,
    val taxAmount: BigDecimal,
    val taxRate: BigDecimal,
    val totalAmount: BigDecimal,
    val paymentDueDate: LocalDate,
    val createdAt: Long,
    val updatedAt: Long
)

data class InvoiceRequest(
    val userId: Int,
    val issueDate: LocalDate,
    val paymentAmount: BigDecimal,
    val paymentDueDate: LocalDate
)

@Serializable
data class InvoiceResponse(
    val companyName: String,
    val issueDate: String,
    val paymentAmount: Long
)

fun Invoice.toResponse(): InvoiceResponse {
    return InvoiceResponse(
        "Your Company Name",
        issueDate.toString(),
        paymentAmount.toLong()
    )
}