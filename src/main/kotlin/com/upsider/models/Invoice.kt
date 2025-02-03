package com.upsider.models

import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDate

data class InvoiceForInsert(
    val userId: Int,
    val issueDate: LocalDate, // 発行日
    val paymentAmount: BigDecimal,
    val fee: BigDecimal,
    val feeRate: BigDecimal,
    val taxAmount: BigDecimal,
    val taxRate: BigDecimal,
    val totalAmount: BigDecimal,
    val paymentDueDate: LocalDate, // 支払期日
)

data class InvoiceRequest(
    val issueDate: LocalDate,
    val paymentAmount: BigDecimal,
    val paymentDueDate: LocalDate
)

data class InvoiceWithUserInfo(
    val name: String,
    val userId: Int,
    val companyName: String,
    val issueDate: LocalDate, // 発行日
    val paymentAmount: BigDecimal,
    val fee: BigDecimal,
    val feeRate: BigDecimal,
    val taxAmount: BigDecimal,
    val taxRate: BigDecimal,
    val totalAmount: BigDecimal,
    val paymentDueDate: LocalDate, // 支払期日
)

@Serializable
data class InvoiceResponse(
    val userName: String,
    val companyName: String,
    val issueDate: String, // 発行日
    val paymentAmount: String,
    val fee: String,
    val feeRate: String,
    val taxAmount: String,
    val taxRate: String,
    val totalAmount: String,
    val paymentDueDate: String, // 支払期日
)

fun InvoiceWithUserInfo.toResponse(): InvoiceResponse {
    return InvoiceResponse(
        userName = this.name,
        companyName = this.companyName,
        issueDate = this.issueDate.toString(),
        paymentDueDate = this.paymentDueDate.toString(),
        paymentAmount = this.paymentAmount.toString(),
        fee = this.fee.toString(),
        feeRate = this.feeRate.toString(),
        taxAmount = this.taxAmount.toString(),
        taxRate = this.taxRate.toString(),
        totalAmount = this.totalAmount.toString()
    )
}


@Serializable
data class InvoiceListResponse(
    val invoices: List<InvoiceResponse>
)

fun List<InvoiceWithUserInfo>.toResponseList(): InvoiceListResponse {
    return InvoiceListResponse(this.map { it.toResponse() })
}
