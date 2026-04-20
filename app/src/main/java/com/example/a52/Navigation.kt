package com.example.a52.ui.theme

import androidx.compose.runtime.Composable



import com.example.a52.ReworkViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


sealed class Screen(val route: String) {
    data object List : Screen("list")
    data object Detail : Screen("detail")
}

@Composable
fun Navigation(navController: NavHostController = rememberNavController(),
               viewModel: ReworkViewModel = viewModel()
) {
    NavHost (navController, startDestination = Screen.List.route) {
        composable(Screen.List.route) {
            FirstScreen(viewModel, navController)
        }
        composable(
            Screen.Detail.route,
        ) { backStackEntry ->
            AddNoteScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}