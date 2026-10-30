package io.mmaltsev.vkeducation.presentation.appdetails

import androidx.lifecycle.ViewModel
import io.mmaltsev.vkeducation.domain.model.App
import io.mmaltsev.vkeducation.domain.model.AppDetails
import io.mmaltsev.vkeducation.domain.repository.AppsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppDetailsUiState(
    val appDetails: AppDetails,
    val showcaseApps: List<App>,
    val descriptionExpanded: Boolean = false,
)

class AppDetailsViewModel(repository: AppsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(
        AppDetailsUiState(repository.getAppDetails(), repository.getApps())
    )
    val uiState = _uiState.asStateFlow()

    fun expandDescription() {
        _uiState.update { it.copy(descriptionExpanded = true) }
    }
}
