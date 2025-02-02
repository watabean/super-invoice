package com.upsider.services

import com.upsider.models.User
import com.upsider.models.UserRequest
import com.upsider.repositories.UserRepository

class UserService(private val userRepository: UserRepository) {
    fun createUser(request: UserRequest): User {
        return userRepository.save(request)
    }

    fun authenticate(email: String, password: String): String? {
        val user = userRepository.findByEmail(email)
        return if (user != null && user.password == password) "sample-jwt-token" else null
    }
}