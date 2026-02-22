import io.qameta.allure.Allure
import io.qameta.allure.Description
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Severity
import io.qameta.allure.SeverityLevel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.concurrent.TimeUnit

@Epic("ДЗ 6: Сетевой слой")
@Feature("MockWebServer API")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiMockTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: OkHttpClient

    @BeforeAll
    fun setupAll() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @AfterAll
    fun tearDownAll() {
        mockWebServer.shutdown()
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("GET запрос: Получение списка задач")
    fun testGetTasks() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""[{"id": 1, "title": "Test Task"}]""")
            .addHeader("Content-Type", "application/json")

        mockWebServer.enqueue(mockResponse)

        val url = mockWebServer.url("/tasks").toString()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()

        assertEquals(200, response.code)
        val body = response.body?.string()
        assertTrue(body?.contains("Test Task") == true)
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("POST запрос: Создание задачи")
    fun testCreateTask() {
        val mockResponse = MockResponse().setResponseCode(201)
        mockWebServer.enqueue(mockResponse)

        val url = mockWebServer.url("/tasks").toString()
        val jsonBody = """{"title": "New Task"}""".toRequestBody()
        
        val request = Request.Builder()
            .url(url)
            .post(jsonBody)
            .build()

        val response = client.newCall(request).execute()
        assertEquals(201, response.code)
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("DELETE запрос: Удаление задачи")
    fun testDeleteTask() {
        val mockResponse = MockResponse().setResponseCode(204)
        mockWebServer.enqueue(mockResponse)

        val url = mockWebServer.url("/tasks/1").toString()
        val request = Request.Builder().url(url).delete().build()

        val response = client.newCall(request).execute()
        assertEquals(204, response.code)
    }
}
