package com.upsider.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.upsider.models.User
import com.upsider.models.UserRequest
import com.upsider.repositories.UserRepository
import org.mindrot.jbcrypt.BCrypt
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
        return if (user != null && BCrypt.checkpw(password, user.password)) generateJwtToken(user.id) else null
    }


    private fun generateJwtToken(userId: Int): String {
        val jwtConfig = Config.jwt()
        val expirationTime = Date(System.currentTimeMillis() + jwtConfig.expiration)

        return JWT.create()
            .withIssuer(jwtConfig.issuer)
            .withAudience(jwtConfig.audience)
            .withSubject(userId.toString())
            .withClaim("userId", userId)
            .withExpiresAt(expirationTime)
            .sign(Algorithm.HMAC256(jwtConfig.secret))
    }
}