package com.upsider.repositories

import com.upsider.models.Invoice
import java.time.LocalDate

class InvoiceRepository {
    private val invoices = mutableListOf<Invoice>()

    fun save(invoice: Invoice): Invoice {
        invoices.add(invoice)
        return invoice
    }

    fun findInvoicesBetween(startDate: LocalDate?, endDate: LocalDate?): List<Invoice> {
        return invoices.filter {
            (startDate == null || it.paymentDueDate >= startDate) &&
                    (endDate == null || it.paymentDueDate <= endDate)
        }
    }
}
