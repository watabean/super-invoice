package com.upsider.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.upsider.models.User
import com.upsider.models.UserRequest
import com.upsider.repositories.UserRepository
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class UserService(private val userRepository: UserRepository) {
    fun createUser(request: UserRequest) {
        // emailの登録事前チェック
        userRepository.findByEmail(request.email)?.let {
            throw IllegalArgumentException("This email has been already registered")
        }
        userRepository.save(request)
    }

    fun authenticate(email: String, password: String): String? {
        val user = userRepository.findByEmail(email)
        return if (user != null && user.password == password) generateJwtToken(user.id) else null
    }


    private fun generateJwtToken(userId: Int): String {
        val secret = "test"
        val issuer = "UPSIDER"
        val audience = "super-invoice-api"
        val expirationTime = Date.from(Instant.now().plus(1, ChronoUnit.HOURS))

        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject(userId.toString())
            .withClaim("userId", userId)
            .withExpiresAt(expirationTime)
            .sign(Algorithm.HMAC256(secret))
    }
}