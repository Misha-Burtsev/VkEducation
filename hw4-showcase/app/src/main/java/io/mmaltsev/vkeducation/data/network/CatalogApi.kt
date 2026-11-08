package io.mmaltsev.vkeducation.data.network

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Path

data class CatalogAppDto(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val iconUrl: String,
)

interface CatalogApi {
    @GET("catalog")
    suspend fun getCatalog(): List<CatalogAppDto>

    @GET("{id}")
    suspend fun getAppById(@Path("id") id: String): JsonObject
}
