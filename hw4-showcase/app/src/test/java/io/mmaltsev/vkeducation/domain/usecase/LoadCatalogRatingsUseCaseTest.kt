package io.mmaltsev.vkeducation.domain.usecase

import io.mmaltsev.vkeducation.domain.model.CatalogApp
import io.mmaltsev.vkeducation.domain.repository.CatalogRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class LoadCatalogRatingsUseCaseTest {
    private val apps = listOf(
        CatalogApp("notes-2", "Notes", "Shared notes", "Productivity", "shared.png"),
        CatalogApp("notes-1", "Notes", "Private notes", "Productivity", "private.png"),
    )
    private val repository = FakeCatalogRepository()
    private val useCase = LoadCatalogRatingsUseCase(repository)

    @Test
    fun emptyCatalogSkipsBothRequests() = runBlocking {
        assertTrue(useCase(emptyList()).toList().isEmpty())
        assertTrue(repository.calls.isEmpty())
    }

    @Test
    fun bothRequestsReceiveCatalogIdsInOrder() = runBlocking {
        useCase(apps).toList()

        val ids = listOf("notes-2", "notes-1")
        assertEquals(listOf("parallel" to ids, "sequential" to ids), repository.calls)
    }

    @Test
    fun emitsParallelRatingsBeforeSequentialResult() = runBlocking {
        val results = useCase(apps).toList()

        assertEquals(2, results.size)
        assertEquals(repository.parallelRatings, (results[0] as RatingLoadResult.Parallel).ratings)
        assertTrue(results[1] is RatingLoadResult.Sequential)
    }

    @Test
    fun stoppingCollectionAfterFirstResultSkipsSequentialRequest() = runBlocking {
        assertTrue(useCase(apps).first() is RatingLoadResult.Parallel)
        assertEquals(listOf("parallel"), repository.calls.map { it.first })
    }

    @Test
    fun parallelFailurePreventsSequentialRequest() {
        val failure = IllegalStateException("Parallel request failed")
        repository.parallelFailure = failure

        val actual = assertThrows(IllegalStateException::class.java) {
            runBlocking { useCase(apps).toList() }
        }

        assertSame(failure, actual)
        assertEquals(listOf("parallel"), repository.calls.map { it.first })
    }

    @Test
    fun sequentialFailurePropagatesAfterParallelResult() {
        val failure = IllegalStateException("Sequential request failed")
        repository.sequentialFailure = failure
        val results = mutableListOf<RatingLoadResult>()

        val actual = assertThrows(IllegalStateException::class.java) {
            runBlocking { useCase(apps).toList(results) }
        }

        assertSame(failure, actual)
        assertTrue(results.single() is RatingLoadResult.Parallel)
    }

    private class FakeCatalogRepository : CatalogRepository {
        val calls = mutableListOf<Pair<String, List<String>>>()
        val parallelRatings = mapOf("notes-2" to 4.5f, "notes-1" to 4.2f)
        var parallelFailure: IllegalStateException? = null
        var sequentialFailure: IllegalStateException? = null

        override suspend fun getCatalog(): List<CatalogApp> = error("Unexpected catalog request")

        override suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> {
            calls += "parallel" to ids
            parallelFailure?.let { throw it }
            return parallelRatings
        }

        override suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> {
            calls += "sequential" to ids
            sequentialFailure?.let { throw it }
            return emptyMap()
        }
    }
}
