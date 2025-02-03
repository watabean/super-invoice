package com.upsider.routes

import com.upsider.services.UserService
import com.upsider.utils.parseLoginRequest
import com.upsider.utils.parseUserRequest
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Route.userRoutes(userService: UserService) {
    val logger = LoggerFactory.getLogger(this::class.java.name)

    route("/users") {
        post {
            try {
                val json = call.receive<String>()
                val request = parseUserRequest(json)
                userService.createUser(request)
                call.respond(HttpStatusCode.Created)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                logger.error("Unexpected error during user registration", e)
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Internal server error"))
            }
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
            logger.error("Unexpected error during user login", e)
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Internal server error"))
        }
    }
}
