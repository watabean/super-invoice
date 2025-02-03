package com.upsider.repositories

import com.upsider.models.InvoiceForInsert
import com.upsider.models.InvoiceWithUserInfo
import com.upsider.tables.InvoicesTable
import com.upsider.tables.UsersTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

class InvoiceRepository {

    fun save(invoice: InvoiceForInsert) {
        transaction {
            InvoicesTable.insert {
                it[userId] = invoice.userId
                it[issueDate] = invoice.issueDate
                it[paymentAmount] = invoice.paymentAmount
                it[fee] = invoice.fee
                it[feeRate] = invoice.feeRate
                it[taxAmount] = invoice.taxAmount
                it[taxRate] = invoice.taxRate
                it[totalAmount] = invoice.totalAmount
                it[paymentDueDate] = invoice.paymentDueDate
            } get InvoicesTable.id
        }
    }

    fun findInvoicesBetween(userId: Int, startDate: LocalDate?, endDate: LocalDate?): List<InvoiceWithUserInfo> {
        return transaction {
            (InvoicesTable innerJoin UsersTable)
                .selectAll()
                .where {
                    (startDate?.let { InvoicesTable.paymentDueDate greaterEq it } ?: Op.TRUE) and
                            (endDate?.let { InvoicesTable.paymentDueDate lessEq it } ?: Op.TRUE)
                }
                .map {
                    InvoiceWithUserInfo(
                        userId = it[InvoicesTable.userId],
                        name = it[UsersTable.name],
                        companyName = it[UsersTable.companyName],
                        issueDate = it[InvoicesTable.issueDate],
                        paymentAmount = it[InvoicesTable.paymentAmount],
                        fee = it[InvoicesTable.fee],
                        feeRate = it[InvoicesTable.feeRate],
                        taxAmount = it[InvoicesTable.taxAmount],
                        taxRate = it[InvoicesTable.taxRate],
                        totalAmount = it[InvoicesTable.totalAmount],
                        paymentDueDate = it[InvoicesTable.paymentDueDate],
                    )
                }
        }
    }

}
