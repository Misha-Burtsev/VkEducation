package io.mmaltsev.vkeducation.data.repository

import io.mmaltsev.vkeducation.data.network.CatalogApi
import io.mmaltsev.vkeducation.domain.model.CatalogApp

class CatalogRepository(private val api: CatalogApi) {
    suspend fun getCatalog(): List<CatalogApp> = api.getCatalog().map {
        CatalogApp(it.id, it.name, it.description, it.category, it.iconUrl)
    }
}
