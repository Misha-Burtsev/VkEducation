package io.mmaltsev.vkeducation.presentation.appdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.mmaltsev.vkeducation.R
import io.mmaltsev.vkeducation.domain.model.App
import io.mmaltsev.vkeducation.presentation.applist.AppItem

private const val APPS_PER_PAGE = 3

@Composable
fun Showcase(
    apps: List<App>,
    onAppClick: (App) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pages = apps.chunked(APPS_PER_PAGE)

    Column(modifier) {
        Text(
            text = stringResource(R.string.app_details_showcase),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(8.dp))
        HorizontalPager(
            state = rememberPagerState { pages.size },
            contentPadding = PaddingValues(end = 32.dp),
            modifier = Modifier.fillMaxWidth(),
        ) { pageIndex ->
            Column {
                pages[pageIndex].forEach { app ->
                    AppItem(
                        app = app,
                        onClick = { onAppClick(app) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
