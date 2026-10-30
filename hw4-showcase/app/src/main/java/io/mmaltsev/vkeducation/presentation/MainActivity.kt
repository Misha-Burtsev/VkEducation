package io.mmaltsev.vkeducation.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.mmaltsev.vkeducation.di.AppContainer
import io.mmaltsev.vkeducation.presentation.appdetails.AppDetailsScreen
import io.mmaltsev.vkeducation.presentation.appdetails.AppDetailsViewModel
import io.mmaltsev.vkeducation.presentation.applist.AppListScreen
import io.mmaltsev.vkeducation.presentation.applist.AppListViewModel
import io.mmaltsev.vkeducation.presentation.theme.VkEducationTheme

private const val APP_LIST_ROUTE = "app_list"
private const val APP_DETAILS_ROUTE = "app_details"

class MainActivity : ComponentActivity() {
    private val container = AppContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VkEducationTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = APP_LIST_ROUTE,
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                ) {
                    composable(APP_LIST_ROUTE) {
                        val viewModel: AppListViewModel = viewModel(factory = container.viewModelFactory)
                        val apps by viewModel.apps.collectAsStateWithLifecycle()
                        AppListScreen(
                            apps = apps,
                            onAppClick = { navController.navigate(APP_DETAILS_ROUTE) },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    composable(APP_DETAILS_ROUTE) {
                        val viewModel: AppDetailsViewModel = viewModel(factory = container.viewModelFactory)
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        AppDetailsScreen(
                            uiState = uiState,
                            onReadMoreClick = viewModel::expandDescription,
                            onBackClick = { navController.popBackStack() },
                            onShowcaseAppClick = {
                                // Возвращаемся к уже открытому списку, а не кладём новый в стек.
                                navController.navigate(APP_LIST_ROUTE) {
                                    popUpTo(APP_LIST_ROUTE) { inclusive = true }
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}
