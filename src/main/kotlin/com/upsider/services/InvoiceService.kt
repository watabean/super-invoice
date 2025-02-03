package com.upsider.services

import com.upsider.models.InvoiceForInsert
import com.upsider.models.InvoiceRequest
import com.upsider.models.InvoiceWithUserInfo
import com.upsider.repositories.InvoiceRepository
import com.upsider.repositories.UserRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class InvoiceService(
    private val invoiceRepository: InvoiceRepository,
    private val userRepository: UserRepository
) {
    companion object {
        private val FEE_RATE = BigDecimal("0.04")
        private val TAX_RATE = BigDecimal("0.10")
    }

    fun createInvoice(request: InvoiceRequest, userId: Int) {
        // ユーザの存在チェック
        userRepository.findById(userId) ?: throw IllegalArgumentException("This user ID not found")

        // 手数料
        val fee = calculateFee(request.paymentAmount, FEE_RATE)
        // 消費税（手数料）
        val taxAmount = calculateTax(fee, TAX_RATE)
        // 請求額 = 支払金額 + 手数料 + 消費税（手数料）
        val totalAmount = request.paymentAmount.add(fee).add(taxAmount)

        val invoice = InvoiceForInsert(
            userId = userId,
            issueDate = request.issueDate,
            paymentAmount = request.paymentAmount,
            fee = fee,
            feeRate = FEE_RATE.multiply(BigDecimal(100)),
            taxAmount = taxAmount,
            taxRate = TAX_RATE.multiply(BigDecimal(100)),
            totalAmount = totalAmount,
            paymentDueDate = request.paymentDueDate
        )
        invoiceRepository.save(invoice)
    }

    fun getInvoices(userId: Int, startDate: LocalDate?, endDate: LocalDate?): List<InvoiceWithUserInfo> {
        return invoiceRepository.findInvoicesBetween(userId, startDate, endDate)
    }

    private fun calculateFee(baseAmount: BigDecimal, feeRate: BigDecimal): BigDecimal {
        // 手数料は小数点以下切り上げ
        return baseAmount.multiply(feeRate).setScale(0, RoundingMode.CEILING)
    }

    private fun calculateTax(amount: BigDecimal, taxRate: BigDecimal): BigDecimal {
        // 税額は小数点以下切り捨て
        return amount.multiply(taxRate).setScale(0, RoundingMode.DOWN)
    }
}
