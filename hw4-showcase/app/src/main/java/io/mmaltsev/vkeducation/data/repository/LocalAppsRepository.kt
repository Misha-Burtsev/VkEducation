package io.mmaltsev.vkeducation.data.repository

import io.mmaltsev.vkeducation.data.mapper.AppMapper
import io.mmaltsev.vkeducation.data.source.LocalAppsDataSource
import io.mmaltsev.vkeducation.domain.model.App
import io.mmaltsev.vkeducation.domain.model.AppDetails
import io.mmaltsev.vkeducation.domain.repository.AppsRepository

class LocalAppsRepository(
    private val source: LocalAppsDataSource,
    private val mapper: AppMapper,
) : AppsRepository {
    override fun getApps(): List<App> = source.getApps().map(mapper::toDomain)
    override fun getAppDetails(): AppDetails = mapper.toDomain(source.getAppDetails())
}
