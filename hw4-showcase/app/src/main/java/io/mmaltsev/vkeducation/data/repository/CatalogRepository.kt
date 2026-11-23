package io.mmaltsev.vkeducation.data.repository

import io.mmaltsev.vkeducation.data.network.CatalogApi
import io.mmaltsev.vkeducation.domain.model.CatalogApp
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class CatalogRepository(private val api: CatalogApi) {
    suspend fun getCatalog(): List<CatalogApp> = api.getCatalog().map {
        CatalogApp(it.id, it.name, it.description, it.category, it.iconUrl)
    }

    suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> = buildMap {
        for (id in ids) {
            put(id, api.getRating(id))
        }
    }

    suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> = coroutineScope {
        ids.map { id -> async { id to api.getRating(id) } }.awaitAll().toMap()
    }
}
