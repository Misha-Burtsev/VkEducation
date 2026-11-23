package io.mmaltsev.vkeducation.data.repository

import com.google.gson.JsonObject
import io.mmaltsev.vkeducation.data.mapper.AppMapper
import io.mmaltsev.vkeducation.data.network.CatalogApi
import io.mmaltsev.vkeducation.data.network.CatalogAppDto
import io.mmaltsev.vkeducation.data.network.CatalogService
import io.mmaltsev.vkeducation.domain.model.CatalogApp
import java.io.IOException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkCatalogRepositoryTest {
    private val first = CatalogAppDto("notes-1", "Notes", "Description", "Productivity", "notes.png")
    private val second = CatalogAppDto("quest-1", "Quest", "Adventure", "Games", "quest.png")

    @Test
    fun catalogReturnsDomainModelsInServiceOrder() = runBlocking {
        val repository = repository { listOf(second, first) }

        assertEquals(
            listOf(
                CatalogApp("quest-1", "Quest", "Adventure", "Games", "quest.png"),
                CatalogApp("notes-1", "Notes", "Description", "Productivity", "notes.png"),
            ),
            repository.getCatalog(),
        )
    }

    @Test
    fun emptyCatalogReturnsEmptyList() = runBlocking {
        assertTrue(repository { emptyList() }.getCatalog().isEmpty())
    }

    @Test
    fun catalogPropagatesServiceFailure() {
        val failure = IOException("Service unavailable")
        val repository = repository { throw failure }

        val actual = assertThrows(IOException::class.java) {
            runBlocking { repository.getCatalog() }
        }

        assertSame(failure, actual)
    }

    @Test
    fun sequentialRatingsAreKeyedByRequestedIds() = runBlocking {
        val ids = listOf(first.id, second.id)

        val ratings = repository().loadRatingsSequentially(ids)

        assertEquals(ids.toSet(), ratings.keys)
        assertTrue(ratings.values.all { it in 3f..5f })
    }

    @Test
    fun parallelRatingsAreKeyedByRequestedIds() = runBlocking {
        val ids = listOf(second.id, first.id)

        val ratings = repository().loadRatingsInParallel(ids)

        assertEquals(ids.toSet(), ratings.keys)
        assertTrue(ratings.values.all { it in 3f..5f })
    }

    @Test
    fun emptyRatingRequestsReturnEmptyMaps() = runBlocking {
        val repository = repository()

        assertTrue(repository.loadRatingsSequentially(emptyList()).isEmpty())
        assertTrue(repository.loadRatingsInParallel(emptyList()).isEmpty())
    }

    private fun repository(
        fetchCatalog: suspend () -> List<CatalogAppDto> = { error("Unexpected catalog request") },
    ): NetworkCatalogRepository {
        val service = object : CatalogService {
            override suspend fun getCatalog() = fetchCatalog()
            override suspend fun getAppById(id: String): JsonObject = error("Unexpected details request")
        }
        return NetworkCatalogRepository(CatalogApi(service), AppMapper())
    }
}
