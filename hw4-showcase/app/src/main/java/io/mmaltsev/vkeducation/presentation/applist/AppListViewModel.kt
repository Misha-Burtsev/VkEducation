package io.mmaltsev.vkeducation.presentation.applist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mmaltsev.vkeducation.domain.model.CatalogApp
import io.mmaltsev.vkeducation.domain.repository.CatalogRepository
import io.mmaltsev.vkeducation.domain.usecase.LoadCatalogRatingsUseCase
import io.mmaltsev.vkeducation.domain.usecase.RatingLoadResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CatalogUiState {
    data object Loading : CatalogUiState
    data class Success(
        val apps: List<CatalogApp>,
        val ratings: Map<String, Float> = emptyMap(),
    ) : CatalogUiState
    data object Error : CatalogUiState
}

class AppListViewModel(
    private val repository: CatalogRepository,
    private val loadCatalogRatings: LoadCatalogRatingsUseCase,
) : ViewModel() {
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
                loadRatings(apps)
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
        loadCatalogRatings(apps).collect { result ->
            val mode = when (result) {
                is RatingLoadResult.Parallel -> {
                    _uiState.value = CatalogUiState.Success(apps, result.ratings)
                    "Parallel"
                }
                is RatingLoadResult.Sequential -> "Sequential"
            }
            Log.i("CatalogRatings", "$mode: ${apps.size} apps in ${result.durationMillis} ms")
        }
    }
}
