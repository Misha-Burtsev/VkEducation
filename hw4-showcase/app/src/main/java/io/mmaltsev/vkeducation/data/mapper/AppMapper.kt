package io.mmaltsev.vkeducation.data.mapper

import io.mmaltsev.vkeducation.data.dto.AppDetailsDto
import io.mmaltsev.vkeducation.data.dto.AppDto
import io.mmaltsev.vkeducation.data.network.CatalogAppDto
import io.mmaltsev.vkeducation.domain.model.App
import io.mmaltsev.vkeducation.domain.model.AppDetails
import io.mmaltsev.vkeducation.domain.model.CatalogApp
import io.mmaltsev.vkeducation.domain.model.Category

class AppMapper {
    fun toDomain(dto: AppDto): App = App(
        name = dto.name,
        developer = dto.developer,
        category = mapCategory(dto.category),
        iconUrl = dto.iconUrl,
    )

    fun toDomain(dto: AppDetailsDto): AppDetails = AppDetails(
        name = dto.name,
        developer = dto.developer,
        category = mapCategory(dto.category),
        ageRating = dto.ageRating,
        size = dto.size,
        iconUrl = dto.iconUrl,
        screenshotUrlList = dto.screenshotUrlList.toList(),
        description = dto.description,
    )

    fun toDomain(dto: CatalogAppDto): CatalogApp = CatalogApp(
        id = dto.id,
        name = dto.name,
        description = dto.description,
        category = dto.category,
        iconUrl = dto.iconUrl,
    )

    fun toDto(app: App): AppDto = AppDto(
        name = app.name,
        developer = app.developer,
        category = mapCategory(app.category),
        iconUrl = app.iconUrl,
    )

    fun toDto(details: AppDetails): AppDetailsDto = AppDetailsDto(
        name = details.name,
        developer = details.developer,
        category = mapCategory(details.category),
        ageRating = details.ageRating,
        size = details.size,
        iconUrl = details.iconUrl,
        screenshotUrlList = details.screenshotUrlList.toList(),
        description = details.description,
    )

    fun toDto(app: CatalogApp): CatalogAppDto = CatalogAppDto(
        id = app.id,
        name = app.name,
        description = app.description,
        category = app.category,
        iconUrl = app.iconUrl,
    )

    private fun mapCategory(value: Category): String = when (value) {
        Category.APP -> "app"
        Category.GAME -> "game"
    }

    private fun mapCategory(value: String): Category = when (value) {
        "app" -> Category.APP
        "game" -> Category.GAME
        else -> error("Unknown category: $value")
    }
}
