package io.mmaltsev.vkeducation.presentation.applist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.mmaltsev.vkeducation.domain.model.App

@Composable
fun AppListScreen(
    apps: List<App>,
    onAppClick: (App) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        items(apps) { app ->
            AppItem(
                app = app,
                onClick = { onAppClick(app) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
