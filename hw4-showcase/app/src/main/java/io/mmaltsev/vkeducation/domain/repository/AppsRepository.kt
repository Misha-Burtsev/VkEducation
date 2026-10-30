package io.mmaltsev.vkeducation.domain.repository

import io.mmaltsev.vkeducation.domain.model.App
import io.mmaltsev.vkeducation.domain.model.AppDetails

interface AppsRepository {
    fun getApps(): List<App>
    fun getAppDetails(): AppDetails
}
