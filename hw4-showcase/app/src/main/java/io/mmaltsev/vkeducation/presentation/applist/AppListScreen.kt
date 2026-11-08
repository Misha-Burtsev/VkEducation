package io.mmaltsev.vkeducation.presentation.applist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.mmaltsev.vkeducation.domain.model.CatalogApp

@Composable
fun AppListScreen(uiState: CatalogUiState, onRetry: () -> Unit, onAppClick: (CatalogApp) -> Unit, modifier: Modifier = Modifier) {
    when (uiState) {
        CatalogUiState.Loading -> Box(modifier, contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        CatalogUiState.Error -> Box(modifier, contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Не удалось загрузить каталог")
                Button(onClick = onRetry) { Text("Повторить") }
            }
        }
        is CatalogUiState.Success -> if (uiState.apps.isEmpty()) {
            Box(modifier, contentAlignment = Alignment.Center) { Text("Каталог пуст") }
        } else {
            LazyColumn(modifier) {
                items(uiState.apps, key = { it.id }) { app ->
                    Row(Modifier.fillMaxWidth().clickable { onAppClick(app) }.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(model = app.iconUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)))
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(app.name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(app.description, maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodySmall)
                            Text(app.category, color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
