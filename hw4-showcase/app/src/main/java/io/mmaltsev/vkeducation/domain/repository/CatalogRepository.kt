package io.mmaltsev.vkeducation.domain.repository

import io.mmaltsev.vkeducation.domain.model.CatalogApp

interface CatalogRepository {
    suspend fun getCatalog(): List<CatalogApp>
    suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float>
    suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float>
}
