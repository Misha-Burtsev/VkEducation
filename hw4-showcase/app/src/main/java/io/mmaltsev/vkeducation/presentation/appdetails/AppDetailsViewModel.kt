package io.mmaltsev.vkeducation.presentation.appdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.mmaltsev.vkeducation.domain.model.App
import io.mmaltsev.vkeducation.domain.model.AppDetails
import io.mmaltsev.vkeducation.domain.repository.AppsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface InstallState {
    data object NotInstalled : InstallState
    data class Installing(val progress: Int) : InstallState
    data object Installed : InstallState
}

data class AppDetailsUiState(
    val appDetails: AppDetails,
    val showcaseApps: List<App>,
    val descriptionExpanded: Boolean = false,
    val installState: InstallState = InstallState.NotInstalled,
)

class AppDetailsViewModel(repository: AppsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(
        AppDetailsUiState(repository.getAppDetails(), repository.getApps())
    )
    val uiState = _uiState.asStateFlow()

    private var installJob: Job? = null

    fun onInstallClick() {
        when (_uiState.value.installState) {
            InstallState.NotInstalled -> {
                _uiState.update { it.copy(installState = InstallState.Installing(0)) }
                installJob = viewModelScope.launch { emulateInstall() }
            }
            is InstallState.Installing -> {
                installJob?.cancel()
                installJob = null
                _uiState.update { it.copy(installState = InstallState.NotInstalled) }
            }
            InstallState.Installed -> Unit
        }
    }

    private suspend fun emulateInstall() {
        for (progress in 5..100 step 5) {
            delay(150)
            _uiState.update { it.copy(installState = InstallState.Installing(progress)) }
        }
        _uiState.update { it.copy(installState = InstallState.Installed) }
        installJob = null
    }

    fun expandDescription() {
        _uiState.update { it.copy(descriptionExpanded = true) }
    }
}
