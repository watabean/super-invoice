package com.upsider.repositories

import com.upsider.models.User
import com.upsider.models.UserRequest

class UserRepository {
    private val users = mutableListOf<User>()
    private var userIdCounter = 1

    fun save(request: UserRequest): User {
        val user = User(
            userIdCounter++,
            request.companyName,
            request.name,
            request.email,
            request.password,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )
        users.add(user)
        return user
    }

    fun findByEmail(email: String): User? {
        return users.find { it.email == email }
    }
}
