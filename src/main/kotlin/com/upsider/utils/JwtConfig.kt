import io.ktor.server.application.*

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val expiration: Long // ミリ秒単位
)

object Config {
    private lateinit var jwtConfig: JwtConfig

    fun load(environment: ApplicationEnvironment) {
        val config = environment.config

        jwtConfig = JwtConfig(
            secret = config.property("jwt.secret").getString(),
            issuer = config.property("jwt.issuer").getString(),
            audience = config.property("jwt.audience").getString(),
            expiration = config.property("jwt.expiration").getString().toLong() * 1000L // 秒 → ミリ秒
        )
    }

    fun jwt(): JwtConfig = jwtConfig
}
