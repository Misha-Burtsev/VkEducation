package io.mmaltsev.vkeducation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mmaltsev.vkeducation.data.repository.LocalAppsRepository
import io.mmaltsev.vkeducation.domain.repository.AppsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAppsRepository(repository: LocalAppsRepository): AppsRepository
}
