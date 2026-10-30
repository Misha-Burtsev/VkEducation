package io.mmaltsev.vkeducation

import io.mmaltsev.vkeducation.data.dto.AppDto
import io.mmaltsev.vkeducation.data.dto.AppDetailsDto
import io.mmaltsev.vkeducation.data.mapper.AppMapper
import io.mmaltsev.vkeducation.data.repository.LocalAppsRepository
import io.mmaltsev.vkeducation.data.source.LocalAppsDataSource
import io.mmaltsev.vkeducation.domain.model.App
import io.mmaltsev.vkeducation.domain.model.AppDetails
import io.mmaltsev.vkeducation.domain.model.Category
import io.mmaltsev.vkeducation.domain.repository.AppsRepository
import io.mmaltsev.vkeducation.presentation.applist.AppListViewModel
import io.mmaltsev.vkeducation.presentation.appdetails.AppDetailsViewModel
import org.junit.Assert.*
import org.junit.Test

class ArchitectureTest {
    private val mapper = AppMapper()

    @Test
    fun appDtoIsMappedWithBothCategories() {
        for ((raw, category) in listOf("app" to Category.APP, "game" to Category.GAME)) {
            assertEquals(
                App("Name", "Developer", category, "icon"),
                mapper.toDomain(AppDto("Name", "Developer", raw, "icon")),
            )
        }
    }

    @Test
    fun detailsDtoIsMappedWithoutLosingFieldsOrSharingMutableScreenshots() {
        val screenshots = mutableListOf("first", "second")
        val actual = mapper.toDomain(
            AppDetailsDto("Game", "Studio", "game", 16, 42.5f, "icon", screenshots, "Description")
        )
        screenshots.clear()
        assertEquals(
            AppDetails("Game", "Studio", Category.GAME, 16, 42.5f, "icon", listOf("first", "second"), "Description"),
            actual,
        )
    }

    @Test(expected = IllegalStateException::class)
    fun invalidCategoryIsNotSilentlyPresentedAsAnApp() {
        mapper.toDomain(AppDto("Name", "Developer", "unknown", "icon"))
    }

    @Test
    fun repositoryReturnsTheExistingCatalogAndDetailsAsDomainModels() {
        val repository: AppsRepository = LocalAppsRepository(LocalAppsDataSource(), mapper)
        val apps = repository.getApps()
        assertEquals(9, apps.size)
        assertEquals("QuickNote", apps.first().name)
        assertEquals(3, apps.count { it.category == Category.GAME })
        val details = repository.getAppDetails()
        assertEquals("VK Play", details.developer)
        assertEquals(Category.GAME, details.category)
        assertEquals(4, details.screenshotUrlList.size)
    }

    @Test
    fun viewModelsUseInjectedRepositoryAndExpansionPreservesContent() {
        val apps = listOf(App("Test", "Author", Category.APP, "test-icon"))
        val details = AppDetails("Test", "Author", Category.APP, 0, 1f, "test-icon", emptyList(), "Text")
        val repository = object : AppsRepository {
            override fun getApps() = apps
            override fun getAppDetails() = details
        }
        assertEquals(apps, AppListViewModel(repository).apps.value)
        val viewModel = AppDetailsViewModel(repository)
        val initial = viewModel.uiState.value
        assertEquals(details, initial.appDetails)
        assertEquals(apps, initial.showcaseApps)
        assertFalse(initial.descriptionExpanded)
        viewModel.expandDescription()
        viewModel.expandDescription()
        assertEquals(initial.copy(descriptionExpanded = true), viewModel.uiState.value)
    }
}
