package com.upsider.repositories

import com.upsider.models.User
import com.upsider.models.UserRequest
import com.upsider.tables.UsersTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

class UserRepository {

    fun save(request: UserRequest): User {
        return transaction {
            val id = UsersTable.insert {
                it[companyName] = request.companyName
                it[name] = request.name
                it[email] = request.email
                it[password] = BCrypt.hashpw(request.password, BCrypt.gensalt())
            } get UsersTable.id

            UsersTable.selectAll().where { UsersTable.id eq id }
                .map {
                    User(
                        id = it[UsersTable.id],
                        companyName = it[UsersTable.companyName],
                        name = it[UsersTable.name],
                        email = it[UsersTable.email],
                        password = it[UsersTable.password],
                        createdAt = it[UsersTable.createdAt].toEpochMilli(),
                        updatedAt = it[UsersTable.updatedAt].toEpochMilli()
                    )
                }
                .single()
        }
    }

    fun findByEmail(email: String): User? {
        return transaction {
            UsersTable.selectAll().where { UsersTable.email eq email }
                .map {
                    User(
                        id = it[UsersTable.id],
                        companyName = it[UsersTable.companyName],
                        name = it[UsersTable.name],
                        email = it[UsersTable.email],
                        password = it[UsersTable.password],
                        createdAt = it[UsersTable.createdAt].toEpochMilli(),
                        updatedAt = it[UsersTable.updatedAt].toEpochMilli()
                    )
                }
                .singleOrNull()
        }
    }

    fun findById(id: Int): User? {
        return transaction {
            UsersTable.selectAll().where { UsersTable.id eq id }
                .map {
                    User(
                        id = it[UsersTable.id],
                        companyName = it[UsersTable.companyName],
                        name = it[UsersTable.name],
                        email = it[UsersTable.email],
                        password = it[UsersTable.password],
                        createdAt = it[UsersTable.createdAt].toEpochMilli(),
                        updatedAt = it[UsersTable.updatedAt].toEpochMilli()
                    )
                }
                .singleOrNull()
        }
    }
}
