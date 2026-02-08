package com.example.week1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.example.week1.view.*
import com.example.week1.ui.theme.Week1Theme

import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.week1.navigation.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week1.viewmodel.TaskViewModel

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Week1Theme {
                val navController = rememberNavController()
                val taskViewModel: TaskViewModel = viewModel()

                AppNavigation(navController, taskViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: TaskViewModel
) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_HOME
    ) {
        composable(ROUTE_HOME) {
            HomeScreen(
                taskViewModel = viewModel,
                onNavigateToCalendar = { navController.navigate(ROUTE_CALENDAR) }
            )
        }

        composable(ROUTE_CALENDAR) {
            CalendarScreen(
                taskViewModel = viewModel,
                onNavigateToHome = {
                    navController.popBackStack()
                    //navController.navigate(ROUTE_HOME)
                }
            )
        }
    }
}