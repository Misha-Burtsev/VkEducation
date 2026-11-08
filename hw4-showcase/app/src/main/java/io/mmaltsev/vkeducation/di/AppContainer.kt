package io.mmaltsev.vkeducation.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.mmaltsev.vkeducation.data.network.CatalogApi
import io.mmaltsev.vkeducation.data.repository.CatalogRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.mmaltsev.vkeducation.data.mapper.AppMapper
import io.mmaltsev.vkeducation.data.repository.LocalAppsRepository
import io.mmaltsev.vkeducation.data.source.LocalAppsDataSource
import io.mmaltsev.vkeducation.domain.repository.AppsRepository
import io.mmaltsev.vkeducation.presentation.appdetails.AppDetailsViewModel
import io.mmaltsev.vkeducation.presentation.applist.AppListViewModel

/** Точка сборки зависимостей: UI получает ViewModel, а не реализацию Repository. */
class AppContainer {
    private val repository: AppsRepository = LocalAppsRepository(LocalAppsDataSource(), AppMapper())

    private val catalogApi = Retrofit.Builder()
        .baseUrl("http://185.103.109.134/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CatalogApi::class.java)
    private val catalogRepository = CatalogRepository(catalogApi)

    val viewModelFactory = viewModelFactory {
        initializer { AppListViewModel(catalogRepository) }
        initializer { AppDetailsViewModel(repository) }
    }
}
