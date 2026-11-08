package io.mmaltsev.vkeducation.presentation.applist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mmaltsev.vkeducation.data.repository.CatalogRepository
import io.mmaltsev.vkeducation.domain.model.CatalogApp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CatalogUiState {
    data object Loading : CatalogUiState
    data class Success(val apps: List<CatalogApp>) : CatalogUiState
    data object Error : CatalogUiState
}

class AppListViewModel(private val repository: CatalogRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<CatalogUiState>(CatalogUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var loading = false

    init { load() }

    fun load() {
        if (_uiState.value is CatalogUiState.Loading && loading) return
        loading = true
        _uiState.value = CatalogUiState.Loading
        viewModelScope.launch {
            try {
                _uiState.value = CatalogUiState.Success(repository.getCatalog())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = CatalogUiState.Error
            } finally {
                loading = false
            }
        }
    }

}
