package io.mmaltsev.vkeducation.domain.usecase

import io.mmaltsev.vkeducation.domain.model.CatalogApp
import io.mmaltsev.vkeducation.domain.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis

sealed interface RatingLoadResult {
    val durationMillis: Long

    data class Parallel(
        val ratings: Map<String, Float>,
        override val durationMillis: Long,
    ) : RatingLoadResult

    data class Sequential(override val durationMillis: Long) : RatingLoadResult
}

class LoadCatalogRatingsUseCase(private val repository: CatalogRepository) {
    operator fun invoke(apps: List<CatalogApp>): Flow<RatingLoadResult> = flow {
        if (apps.isEmpty()) return@flow
        val ids = apps.map(CatalogApp::id)

        val ratings: Map<String, Float>
        val parallelMillis = measureTimeMillis {
            ratings = repository.loadRatingsInParallel(ids)
        }
        emit(RatingLoadResult.Parallel(ratings, parallelMillis))

        val sequentialMillis = measureTimeMillis {
            repository.loadRatingsSequentially(ids)
        }
        emit(RatingLoadResult.Sequential(sequentialMillis))
    }
}
