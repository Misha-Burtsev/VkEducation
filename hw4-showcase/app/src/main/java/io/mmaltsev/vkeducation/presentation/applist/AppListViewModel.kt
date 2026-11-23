package io.mmaltsev.vkeducation.presentation.applist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mmaltsev.vkeducation.data.repository.CatalogRepository
import io.mmaltsev.vkeducation.domain.model.CatalogApp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

sealed interface CatalogUiState {
    data object Loading : CatalogUiState
    data class Success(
        val apps: List<CatalogApp>,
        val ratings: Map<String, Float> = emptyMap(),
    ) : CatalogUiState
    data object Error : CatalogUiState
}

class AppListViewModel(private val repository: CatalogRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<CatalogUiState>(CatalogUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var loading = false

    init { load() }

    fun load() {
        if (loading) return
        loading = true
        _uiState.value = CatalogUiState.Loading
        viewModelScope.launch {
            try {
                val apps = repository.getCatalog()
                _uiState.value = CatalogUiState.Success(apps)
                if (apps.isNotEmpty()) {
                    loadRatings(apps)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = CatalogUiState.Error
            } finally {
                loading = false
            }
        }
    }

    private suspend fun loadRatings(apps: List<CatalogApp>) {
        val ids = apps.map { it.id }

        // Show ratings as soon as the faster run completes.
        val parallelRatings: Map<String, Float>
        val parallelMillis = measureTimeMillis {
            parallelRatings = repository.loadRatingsInParallel(ids)
        }
        _uiState.value = CatalogUiState.Success(apps, parallelRatings)
        Log.i("CatalogRatings", "Parallel: ${ids.size} apps in ${parallelMillis} ms")

        val sequentialMillis = measureTimeMillis {
            repository.loadRatingsSequentially(ids)
        }
        Log.i("CatalogRatings", "Sequential: ${ids.size} apps in ${sequentialMillis} ms")
    }
}
