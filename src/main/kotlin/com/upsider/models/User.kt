package com.upsider.models

data class User(
    val id: Int,
    val companyName: String,
    val name: String,
    val email: String,
    val password: String,
    val createdAt: Long,
    val updatedAt: Long
)

data class UserRequest(val companyName: String, val name: String, val email: String, val password: String)

data class LoginRequest(val email: String, val password: String)
