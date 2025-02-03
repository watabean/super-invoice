package com.upsider.services;

import com.upsider.models.*
import com.upsider.repositories.InvoiceRepository
import com.upsider.repositories.UserRepository
import java.math.BigDecimal
import java.time.LocalDate

class InvoiceService(private val invoiceRepository: InvoiceRepository, private val userRepository: UserRepository) {
    fun createInvoice(request: InvoiceRequest) {
        // ユーザの存在チェック
        if (userRepository.findById(request.userId) == null) {
            throw IllegalArgumentException("This user ID not found")
        }

        val fee = request.paymentAmount.multiply(BigDecimal("0.04"))
        val taxAmount = fee.multiply(BigDecimal("0.10"))
        val totalAmount = request.paymentAmount.add(fee).add(taxAmount)
        val invoice = InvoiceForInsert(
            userId = request.userId,
            issueDate = request.issueDate,
            paymentAmount = request.paymentAmount,
            fee = fee,
            feeRate = BigDecimal("4.00"),
            taxAmount = taxAmount,
            taxRate = BigDecimal("10.00"),
            totalAmount = totalAmount,
            paymentDueDate = request.paymentDueDate
        )
        invoiceRepository.save(invoice)
    }

    fun getInvoices(startDate: LocalDate?, endDate: LocalDate?): List<InvoiceWithUserInfo> {
        return invoiceRepository.findInvoicesBetween(startDate, endDate)
    }
}