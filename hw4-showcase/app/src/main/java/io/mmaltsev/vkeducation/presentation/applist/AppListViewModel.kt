package io.mmaltsev.vkeducation.presentation.applist

import androidx.lifecycle.ViewModel
import io.mmaltsev.vkeducation.domain.model.App
import io.mmaltsev.vkeducation.domain.repository.AppsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppListViewModel(repository: AppsRepository) : ViewModel() {
    private val _apps = MutableStateFlow(repository.getApps())
    val apps: StateFlow<List<App>> = _apps.asStateFlow()
}
