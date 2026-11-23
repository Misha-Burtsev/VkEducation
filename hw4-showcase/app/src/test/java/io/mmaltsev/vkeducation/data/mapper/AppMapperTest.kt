package io.mmaltsev.vkeducation.data.mapper

import io.mmaltsev.vkeducation.data.dto.AppDetailsDto
import io.mmaltsev.vkeducation.data.dto.AppDto
import io.mmaltsev.vkeducation.data.network.CatalogAppDto
import io.mmaltsev.vkeducation.domain.model.App
import io.mmaltsev.vkeducation.domain.model.AppDetails
import io.mmaltsev.vkeducation.domain.model.CatalogApp
import io.mmaltsev.vkeducation.domain.model.Category
import org.junit.Assert.assertEquals
import org.junit.Test

class AppMapperTest {
    private val mapper = AppMapper()
    private val appDto = AppDto("Notes", "Studio", "app", "notes.png")
    private val app = App("Notes", "Studio", Category.APP, "notes.png")
    private val detailsDto = AppDetailsDto(
        "Quest", "Studio", "game", 12, 42.5f, "quest.png", listOf("first", "second"), "Description",
    )
    private val details = AppDetails(
        "Quest", "Studio", Category.GAME, 12, 42.5f, "quest.png", listOf("first", "second"), "Description",
    )
    private val catalogDto = CatalogAppDto("notes-1", "Notes", "Description", "Производительность", "notes.png")
    private val catalogApp = CatalogApp("notes-1", "Notes", "Description", "Производительность", "notes.png")

    @Test
    fun appDtoMapsAllFields() {
        assertEquals(app, mapper.toDomain(appDto))
    }

    @Test
    fun gameDtoMapsToGameCategory() {
        assertEquals(app.copy(category = Category.GAME), mapper.toDomain(appDto.copy(category = "game")))
    }

    @Test
    fun detailsDtoMapsAllFields() {
        assertEquals(details, mapper.toDomain(detailsDto))
    }

    @Test(expected = IllegalStateException::class)
    fun detailsDtoRejectsUnknownCategory() {
        mapper.toDomain(detailsDto.copy(category = "unknown"))
    }

    @Test
    fun catalogDtoPreservesServerCategoryAndId() {
        assertEquals(catalogApp, mapper.toDomain(catalogDto))
    }

    @Test
    fun domainAppMapsAllFields() {
        assertEquals(appDto, mapper.toDto(app))
    }

    @Test
    fun domainGameUsesGameCategoryInDto() {
        assertEquals(appDto.copy(category = "game"), mapper.toDto(app.copy(category = Category.GAME)))
    }

    @Test
    fun domainDetailsMapsAllFields() {
        assertEquals(detailsDto, mapper.toDto(details))
    }

    @Test
    fun detailsDtoKeepsIndependentCopyOfScreenshots() {
        val screenshots = details.screenshotUrlList.toMutableList()
        val dto = mapper.toDto(details.copy(screenshotUrlList = screenshots))

        screenshots.clear()

        assertEquals(detailsDto.screenshotUrlList, dto.screenshotUrlList)
    }

    @Test
    fun domainCatalogItemPreservesServerCategoryAndId() {
        assertEquals(catalogDto, mapper.toDto(catalogApp))
    }
}
