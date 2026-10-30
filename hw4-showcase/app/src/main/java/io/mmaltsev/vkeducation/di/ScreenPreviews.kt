package io.mmaltsev.vkeducation.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.mmaltsev.vkeducation.data.mapper.AppMapper
import io.mmaltsev.vkeducation.data.repository.LocalAppsRepository
import io.mmaltsev.vkeducation.data.source.LocalAppsDataSource
import io.mmaltsev.vkeducation.presentation.appdetails.AppDetailsScreen
import io.mmaltsev.vkeducation.presentation.appdetails.AppDetailsUiState
import io.mmaltsev.vkeducation.presentation.applist.AppListScreen
import io.mmaltsev.vkeducation.presentation.theme.VkEducationTheme

private val previewRepository = LocalAppsRepository(LocalAppsDataSource(), AppMapper())

@Preview(showBackground = true)
@Composable
private fun AppListPreview() {
    VkEducationTheme {
        AppListScreen(apps = previewRepository.getApps(), onAppClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun AppDetailsPreview() {
    VkEducationTheme {
        AppDetailsScreen(
            uiState = AppDetailsUiState(
                appDetails = previewRepository.getAppDetails(),
                showcaseApps = previewRepository.getApps(),
            ),
            onReadMoreClick = {},
            onBackClick = {},
            onShowcaseAppClick = {},
        )
    }
}
