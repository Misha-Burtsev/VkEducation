package io.mmaltsev.vkeducation.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.mmaltsev.vkeducation.data.mapper.AppMapper
import io.mmaltsev.vkeducation.data.network.CatalogApi
import io.mmaltsev.vkeducation.data.network.CatalogService
import io.mmaltsev.vkeducation.data.repository.LocalAppsRepository
import io.mmaltsev.vkeducation.data.repository.NetworkCatalogRepository
import io.mmaltsev.vkeducation.data.source.LocalAppsDataSource
import io.mmaltsev.vkeducation.domain.repository.AppsRepository
import io.mmaltsev.vkeducation.domain.usecase.LoadCatalogRatingsUseCase
import io.mmaltsev.vkeducation.presentation.appdetails.AppDetailsViewModel
import io.mmaltsev.vkeducation.presentation.applist.AppListViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Точка сборки зависимостей: UI получает ViewModel, а не реализацию Repository. */
class AppContainer {
    private val mapper = AppMapper()
    private val repository: AppsRepository = LocalAppsRepository(LocalAppsDataSource(), mapper)

    private val catalogService = Retrofit.Builder()
        .baseUrl("http://185.103.109.134/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CatalogService::class.java)
    private val catalogRepository = NetworkCatalogRepository(CatalogApi(catalogService), mapper)

    val viewModelFactory = viewModelFactory {
        initializer { AppListViewModel(catalogRepository, LoadCatalogRatingsUseCase(catalogRepository)) }
        initializer { AppDetailsViewModel(repository) }
    }
}
