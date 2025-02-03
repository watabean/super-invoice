import com.upsider.models.InvoiceListResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import java.util.*

fun main() = runBlocking {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    val baseUrl = "http://localhost:8080"
    val email = "user_${UUID.randomUUID()}@example.com"

    // ユーザー作成
    println("Creating user...")
    val userResponse = client.post("$baseUrl/users") {
        contentType(ContentType.Application.Json)
        setBody(
            mapOf(
                "companyName" to "Example Corp",
                "name" to "John Doe",
                "email" to email,
                "password" to "securepassword"
            )
        )
    }
    println("User Response: ${userResponse.status}")

    // ログインしてJWT取得
    println("Logging in...")
    val loginResponse = client.post("$baseUrl/login") {
        contentType(ContentType.Application.Json)
        setBody(mapOf("email" to email, "password" to "securepassword"))
    }
    val token = loginResponse.body<Map<String, String>>()["token"]
    println("JWT Token: $token")

    if (token.isNullOrEmpty()) {
        println("Failed to get token")
        return@runBlocking
    }

    // ✅ 請求書データを3件作成
    val invoices = listOf(
        "2025-02-01" to "5000",
        "2025-02-05" to "7500",
        "2025-02-10" to "12000"
    )

    invoices.forEach { (date, amount) ->
        println("Creating invoice for $date...")
        val invoiceResponse = client.post("$baseUrl/invoices") {
            header("Authorization", "Bearer $token")
            setBody(
                mapOf(
                    "issueDate" to date,
                    "paymentAmount" to amount,
                    "paymentDueDate" to "2025-02-28"
                )
            )
            contentType(ContentType.Application.Json)
        }
        println("Invoice Response: ${invoiceResponse.status}")
    }

    // ✅ パラメータごとの件数確認
    val testCases = listOf(
        "2025-02-01" to "2025-02-05", // 2件のはず（2/01, 2/05）
        "2025-02-01" to "2025-02-10", // 3件のはず（2/01, 2/05, 2/10）
        "2025-02-06" to "2025-02-10"  // 1件のはず（2/10）
    )

    testCases.forEach { (start, end) ->
        println("Fetching invoices from $start to $end...")
        val response = client.get("$baseUrl/invoices?start_date=$start&end_date=$end") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
        }
        println("Invoices ($start - $end): ${response.status} ${response.body<InvoiceListResponse>()}")
    }

    println("Fetching all invoices (no date filter)...")
    val response = client.get("$baseUrl/invoices") {
        header("Authorization", "Bearer $token")
    }
    println("All Invoices: ${response.status} ${response.bodyAsText()}")
}
