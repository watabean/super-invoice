package com.upsider.services;

import com.upsider.models.Invoice
import com.upsider.models.InvoiceRequest
import com.upsider.repositories.InvoiceRepository
import java.math.BigDecimal
import java.time.LocalDate

class InvoiceService(private val invoiceRepository: InvoiceRepository) {
    fun createInvoice(request: InvoiceRequest): Invoice {
        val fee = request.paymentAmount.multiply(BigDecimal("0.04"))
        val taxAmount = fee.multiply(BigDecimal("0.10"))
        val totalAmount = request.paymentAmount.add(fee).add(taxAmount)
        val invoice = Invoice(
            id = 1,
            userId = request.userId,
            issueDate = request.issueDate,
            paymentAmount = request.paymentAmount,
            fee = fee,
            feeRate = BigDecimal("4.00"),
            taxAmount = taxAmount,
            taxRate = BigDecimal("10.00"),
            totalAmount = totalAmount,
            paymentDueDate = request.paymentDueDate,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        return invoiceRepository.save(invoice)
    }

    fun getInvoices(startDate: LocalDate?, endDate: LocalDate?): List<Invoice> {
        return invoiceRepository.findInvoicesBetween(startDate, endDate)
    }
}