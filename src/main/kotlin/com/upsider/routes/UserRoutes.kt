package com.upsider.routes

import com.upsider.models.toResponse
import com.upsider.services.UserService
import com.upsider.utils.parseLoginRequest
import com.upsider.utils.parseUserRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(userService: UserService) {
    route("/users") {
        post("/register") {
            try {
                val json = call.receive<String>()
                val request = parseUserRequest(json)
                val user = userService.createUser(request)
                call.respond(HttpStatusCode.Created, user.toResponse())
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid registration request"))
            }
        }

        post("/login") {
            try {
                val json = call.receive<String>()
                val request = parseLoginRequest(json)
                val token = userService.authenticate(request.email, request.password)
                if (token != null) {
                    call.respond(HttpStatusCode.OK, mapOf("token" to token))
                } else {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid login request"))
            }
        }
    }
}
