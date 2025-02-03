package groovy

@Grab('io.github.http-builder-ng:http-builder-ng-core:1.0.4')

import groovyx.net.http.HttpBuilder
import static groovyx.net.http.ContentTypes.JSON

def BASE_URL = "http://localhost:8080"
def EMAIL = "john.doe@example.com"
def PASSWORD = "securepassword"

// APIリクエスト用の関数
def post(String path, Map body) {
    HttpBuilder.configure {
        request.uri = BASE_URL + path
        request.contentType = JSON
    }.post {
        request.body = body
    }
}

def get(String path, String token) {
    HttpBuilder.configure {
        request.uri = BASE_URL + path
        request.headers['Authorization'] = "Bearer $token"
    }.get()
}

// 1. ユーザー作成
println "Creating user..."
post("/users", [
        companyName: "Example Corp",
        name: "John Doe",
        email: EMAIL,
        password: PASSWORD
])

// 2. ログイン & JWT取得
println "Logging in..."
def loginResponse = post("/login", [email: EMAIL, password: PASSWORD])
def token = loginResponse.token

if (!token) {
    println "Failed to get token"
    System.exit(1)
}
println "JWT Token: $token"

// 3. 請求書データを作成
def invoices = [
        [issueDate: "2025-02-01", paymentAmount: 10000.50, paymentDueDate: "2025-02-28"],
        [issueDate: "2025-02-10", paymentAmount: 5000.00, paymentDueDate: "2025-03-10"],
        [issueDate: "2025-03-01", paymentAmount: 12000.75, paymentDueDate: "2025-03-30"]
]

println "Creating invoices..."
invoices.each { invoice ->
    post("/invoices", invoice).each { println it }
}

// 4. 各日付範囲で請求書取得 & 結果確認
def testCases = [
        [start: "2025-02-01", end: "2025-02-28", expected: 2], // 2月のデータ
        [start: "2025-03-01", end: "2025-03-30", expected: 1], // 3月のデータ
        [start: "2025-01-01", end: "2025-01-31", expected: 0]  // 1月のデータ（ないはず）
]

testCases.each { testCase ->
    def response = get("/invoices?start_date=${testCase.start}&end_date=${testCase.end}", token)
    println "Fetching invoices from ${testCase.start} to ${testCase.end}..."
    println "Response: $response"

    def count = response.size()
    assert count == testCase.expected : "Expected ${testCase.expected}, but got ${count}"
    println "✅ Test passed for ${testCase.start} - ${testCase.end} (found ${count} invoices)"
}
