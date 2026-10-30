package io.mmaltsev.vkeducation.data.mapper

import io.mmaltsev.vkeducation.data.dto.AppDetailsDto
import io.mmaltsev.vkeducation.data.dto.AppDto
import io.mmaltsev.vkeducation.domain.model.App
import io.mmaltsev.vkeducation.domain.model.AppDetails
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

    private fun mapCategory(value: String): Category = when (value) {
        "app" -> Category.APP
        "game" -> Category.GAME
        else -> error("Unknown category: $value")
    }
}
