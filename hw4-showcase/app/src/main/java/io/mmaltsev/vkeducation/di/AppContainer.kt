package io.mmaltsev.vkeducation.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.mmaltsev.vkeducation.data.mapper.AppMapper
import io.mmaltsev.vkeducation.data.repository.LocalAppsRepository
import io.mmaltsev.vkeducation.data.source.LocalAppsDataSource
import io.mmaltsev.vkeducation.domain.repository.AppsRepository
import io.mmaltsev.vkeducation.presentation.appdetails.AppDetailsViewModel
import io.mmaltsev.vkeducation.presentation.applist.AppListViewModel

/** Точка сборки зависимостей: UI получает ViewModel, а не реализацию Repository. */
class AppContainer {
    private val repository: AppsRepository = LocalAppsRepository(LocalAppsDataSource(), AppMapper())

    val viewModelFactory = viewModelFactory {
        initializer { AppListViewModel(repository) }
        initializer { AppDetailsViewModel(repository) }
    }
}
