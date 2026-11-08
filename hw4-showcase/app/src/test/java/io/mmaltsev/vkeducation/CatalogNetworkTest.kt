package io.mmaltsev.vkeducation

import io.mmaltsev.vkeducation.data.network.CatalogApi
import io.mmaltsev.vkeducation.data.repository.CatalogRepository
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CatalogNetworkTest {
    @Test
    fun catalogIsMappedAndDetailsRequestUsesId() = runBlocking {
        val server = MockWebServer()
        server.start()
        try {
            val api = Retrofit.Builder().baseUrl(server.url("/"))
                .addConverterFactory(GsonConverterFactory.create()).build().create(CatalogApi::class.java)
            server.enqueue(MockResponse().setBody("""[{"id":"abc","name":"QuickNote","description":"Notes","category":"Производительность","iconUrl":"https://example.com/icon"}]"""))
            val app = CatalogRepository(api).getCatalog().single()
            assertEquals("abc", app.id)
            assertEquals("QuickNote", app.name)
            assertEquals("Notes", app.description)
            assertEquals("Производительность", app.category)
            assertEquals("https://example.com/icon", app.iconUrl)
            assertEquals("/catalog", server.takeRequest().path)
            server.enqueue(MockResponse().setBody("""{"id":"abc","extra":"value"}"""))
            assertEquals("value", api.getAppById("abc").get("extra").asString)
            assertEquals("/abc", server.takeRequest().path)
            server.enqueue(MockResponse().setResponseCode(404))
            try {
                api.getAppById("missing")
                fail("Expected HTTP error")
            } catch (e: HttpException) {
                assertEquals(404, e.code())
            }
        } finally {
            server.shutdown()
        }
    }
}
