package io.mmaltsev.vkeducation.presentation.applist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.mmaltsev.vkeducation.presentation.theme.VkEducationTheme

@Composable
fun AppListScreen(
    onAppClick: (App) -> Unit,
    modifier: Modifier = Modifier,
) {
    val apps = remember { getApps() }

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

@Preview
@Composable
private fun Preview() {
    VkEducationTheme {
        AppListScreen(
            onAppClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
