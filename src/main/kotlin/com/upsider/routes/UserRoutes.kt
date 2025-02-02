package com.upsider.routes

import com.upsider.models.LoginRequest
import com.upsider.models.UserRequest
import com.upsider.services.UserService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes(userService: UserService) {
    route("/users") {
        post("/register") {
            val request = call.receive<UserRequest>()
            val user = userService.createUser(request)
            call.respond(HttpStatusCode.Created, user)
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            val token = userService.authenticate(request.email, request.password)
            if (token != null) {
                call.respond(HttpStatusCode.OK, mapOf("token" to token))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }
    }
}