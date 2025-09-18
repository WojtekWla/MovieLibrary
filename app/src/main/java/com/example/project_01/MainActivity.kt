package com.example.project_01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project_01.ui.theme.Project_01Theme
import com.example.project_01.ui.view.AddView
import com.example.project_01.ui.view.EditView
import com.example.project_01.ui.view.ListOfFilms
import com.example.project_01.ui.view.ViewScreen
import com.example.project_01.viewModel.AddFilmViewModel
import com.example.project_01.viewModel.EditFilmViewModel
import com.example.project_01.viewModel.FilmViewModel
import com.example.project_01.viewModel.ListViewModel
import kotlin.collections.listOf


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Project_01Theme {
                Screen()
            }
        }
    }
}

object Destinations {
    val argId = "id"

    val listDestination    = "list"
    val viewDestination    = "view/{$argId}"
    val editDestination    = "edit/{$argId}"
    val addFilmDestination = "addFilm"

    fun getRouteForView(id: Int): String {
        return viewDestination.replace("{$argId}", id.toString())
    }

    fun getRouteForEdit(id: Int): String {
        return editDestination.replace("{$argId}", id.toString())
    }
}


@Composable
fun Screen() {
    val navController = rememberNavController()
    NavHost(
        navController,
        Destinations.listDestination
    ) {
        composable (Destinations.listDestination) {
            val vm: ListViewModel = viewModel()
            LaunchedEffect(Unit) {
                vm.loadData()
            }

            ListOfFilms(
                listViewModel = vm,
                {
                    navController.navigate(Destinations.getRouteForView(it))
                },
                {
                    navController.navigate(Destinations.addFilmDestination)
                }
            )
        }

        composable(
            Destinations.viewDestination,
            arguments = listOf(
                navArgument (Destinations.argId) {
                    type = NavType.IntType
                }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt(Destinations.argId) ?: -1
            val vm: FilmViewModel = viewModel()

            LaunchedEffect(id) {
                vm.loadData(id)
            }

            val film = vm
            if (film.state != null) {
                ViewScreen(
                    film,
                    {navController.navigate(Destinations.getRouteForEdit(id))},
                    {navController.popBackStack()}
                )
            }

        }

        composable (Destinations.addFilmDestination) {
            val vm: AddFilmViewModel = viewModel()
            LaunchedEffect (Unit){
                vm.loadData()
            }
            AddView (
                vm
            ) { navController.popBackStack() }
        }

        composable (
            Destinations.editDestination,
            arguments = listOf(
                navArgument (Destinations.argId) {
                    type = NavType.IntType
                }
            )
        ) { navBackStackEntry ->

            val id = navBackStackEntry.arguments?.getInt(Destinations.argId) ?: -1
            val vm: EditFilmViewModel = viewModel()

            LaunchedEffect(id) {
                vm.loadData(id)
            }

            val film = vm.state
            if (film != null) {
                EditView (
                    vm
                ) { navController.navigate(Destinations.listDestination) }
            }

        }
    }
}