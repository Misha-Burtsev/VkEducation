package io.mmaltsev.vkeducation.data.repository

import io.mmaltsev.vkeducation.data.mapper.AppMapper
import io.mmaltsev.vkeducation.data.network.CatalogApi
import io.mmaltsev.vkeducation.domain.model.CatalogApp
import io.mmaltsev.vkeducation.domain.repository.CatalogRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class NetworkCatalogRepository(
    private val api: CatalogApi,
    private val mapper: AppMapper,
) : CatalogRepository {
    override suspend fun getCatalog(): List<CatalogApp> = api.getCatalog().map(mapper::toDomain)

    override suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> = buildMap {
        for (id in ids) {
            put(id, api.getRating(id))
        }
    }

    override suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> = coroutineScope {
        ids.map { id -> async { id to api.getRating(id) } }.awaitAll().toMap()
    }
}
