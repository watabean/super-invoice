package com.upsider.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.JavaInstantColumnType
import org.jetbrains.exposed.sql.javatime.date
import java.time.Instant

fun Table.instant(name: String): Column<Instant> =
    registerColumn(name, JavaInstantColumnType())

object InvoicesTable : Table("invoices") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val issueDate = date("issue_date")
    val paymentAmount = decimal("payment_amount", 15, 2)
    val fee = decimal("fee", 15, 2)
    val feeRate = decimal("fee_rate", 5, 2)
    val taxAmount = decimal("tax_amount", 15, 2)
    val taxRate = decimal("tax_rate", 5, 2)
    val totalAmount = decimal("total_amount", 15, 2)
    val paymentDueDate = date("payment_due_date")
    val createdAt = instant("created_at")
    val updatedAt = instant("updated_at")
}

object UsersTable : Table("users") {
    val id = integer("id").autoIncrement()
    val companyName = varchar("company_name", 255)
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val createdAt = instant("created_at")
    val updatedAt = instant("updated_at")
}