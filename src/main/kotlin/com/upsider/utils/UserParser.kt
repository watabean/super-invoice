package com.upsider.utils

import com.upsider.models.LoginRequest
import com.upsider.models.UserRequest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun parseUserRequest(json: String): UserRequest {
    val jsonObject = Json.parseToJsonElement(json).jsonObject

    val companyName = jsonObject["companyName"]?.jsonPrimitive?.content
        ?: throw IllegalArgumentException("companyName is required.")

    val name = jsonObject["name"]?.jsonPrimitive?.content
        ?: throw IllegalArgumentException("name is required.")

    val email = jsonObject["email"]?.jsonPrimitive?.content
        ?: throw IllegalArgumentException("email is required.")

    val password = jsonObject["password"]?.jsonPrimitive?.content
        ?: throw IllegalArgumentException("password is required.")

    if (companyName.isBlank()) {
        throw IllegalArgumentException("companyName cannot be blank.")
    }

    if (name.isBlank()) {
        throw IllegalArgumentException("name cannot be blank.")
    }

    if (!email.contains("@")) {
        throw IllegalArgumentException("email must be a valid email address.")
    }

    if (password.length < 8) {
        throw IllegalArgumentException("password must be at least 8 characters long.")
    }

    return UserRequest(companyName, name, email, password)
}

fun parseLoginRequest(json: String): LoginRequest {
    val jsonObject = Json.parseToJsonElement(json).jsonObject

    val email = jsonObject["email"]?.jsonPrimitive?.content
        ?: throw IllegalArgumentException("email is required.")

    val password = jsonObject["password"]?.jsonPrimitive?.content
        ?: throw IllegalArgumentException("password is required.")

    return LoginRequest(email, password)
}
