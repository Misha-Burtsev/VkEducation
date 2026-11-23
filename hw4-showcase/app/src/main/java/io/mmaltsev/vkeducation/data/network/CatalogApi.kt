package io.mmaltsev.vkeducation.data.network

import com.google.gson.JsonObject
import kotlinx.coroutines.delay
import retrofit2.http.GET
import retrofit2.http.Path

data class CatalogAppDto(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val iconUrl: String,
)

interface CatalogService {
    @GET("catalog")
    suspend fun getCatalog(): List<CatalogAppDto>

    @GET("{id}")
    suspend fun getAppById(@Path("id") id: String): JsonObject
}

class CatalogApi(service: CatalogService) : CatalogService by service {
    // The simulator gives each request the same latency and generates a fresh rating.
    @Suppress("UNUSED_PARAMETER")
    suspend fun getRating(id: String): Float {
        delay(1_500)
        return (30..50).random() / 10f
    }
}
