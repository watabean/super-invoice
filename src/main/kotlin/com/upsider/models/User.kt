package com.upsider.models

import kotlinx.serialization.Serializable

data class User(
    val id: Int,
    val companyName: String,
    val name: String,
    val email: String,
    val password: String,
    val createdAt: Long,
    val updatedAt: Long
)

@Serializable
data class UserResponse(
    val id: Int,
    val companyName: String,
    val name: String,
    val email: String,
)

data class UserRequest(
    val companyName: String,
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)
