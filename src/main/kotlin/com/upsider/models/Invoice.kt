package com.upsider.models

import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDate

data class Invoice(
    val id: Int,
    val userId: Int,
    val issueDate: LocalDate, // 発行日
    val paymentAmount: BigDecimal,
    val fee: BigDecimal,
    val feeRate: BigDecimal,
    val taxAmount: BigDecimal,
    val taxRate: BigDecimal,
    val totalAmount: BigDecimal,
    val paymentDueDate: LocalDate, // 支払期日
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
    val paymentDueDate: String,
    val paymentAmount: Long
)

fun Invoice.toResponse(): InvoiceResponse {
    return InvoiceResponse(
        "Your Company Name",
        issueDate.toString(),
        paymentDueDate.toString(),
        paymentAmount.toLong()
    )
}

@Serializable
data class InvoiceListResponse(
    val invoices: List<InvoiceResponse>
)

fun List<Invoice>.toResponseList(): InvoiceListResponse {
    return InvoiceListResponse(this.map { it.toResponse() })
}
