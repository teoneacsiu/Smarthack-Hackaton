package com.hailavirtual.ui.nav

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.hailavirtual.R
import com.hailavirtual.data.model.UserRole
import com.hailavirtual.ui.auth.LoginEvent
import com.hailavirtual.ui.auth.LoginScreen
import com.hailavirtual.ui.auth.LoginViewModel
import com.hailavirtual.ui.screens.admin.main.MainScreen
import com.hailavirtual.ui.screens.school.manageteacher.ManageScreen
import com.hailavirtual.ui.screens.teachers.home.HomeScreen

@Composable
fun Navigation(startDestination: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    var backPressedOnce by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isOnHomeRoot =
        currentRoute == Routes.ADMIN_HOME ||
                currentRoute == Routes.SCHOOL_HOME ||
                currentRoute == Routes.TEACHER_HOME

    // back de 2 ori pentru ieisre din home
    BackHandler(enabled = isOnHomeRoot) {
        if (backPressedOnce) {
            (context as? Activity)?.moveTaskToBack(true)
        } else {
            backPressedOnce = true
            Toast.makeText(context, R.string.app_name, Toast.LENGTH_SHORT).show()
            scope.launch {
                delay(2000)
                backPressedOnce = false
            }
        }
    }

    NavHost(navController, startDestination = startDestination) {
        composable(Routes.ADMIN_HOME) { MainScreen() }

        composable(Routes.TEACHER_HOME) { HomeScreen() }

        composable(Routes.SCHOOL_HOME) { ManageScreen() }

        composable(Routes.CHOOSE_CLASS) { SimpleTabBody("Home") }

        composable(Routes.CUSTOM_EXPR) { SimpleTabBody("Home") }

        composable(Routes.LESSON)  { SimpleTabBody("Post") }

        // LOGIN
        composable(Routes.START) {
            val loginVm: LoginViewModel = hiltViewModel()
            val state by loginVm.state.collectAsStateWithLifecycle()
            val nav = navController

            LaunchedEffect(state.success, state.role) {
                if (state.success && state.role != null) {
                    val dest = when (state.role) {
                        UserRole.ADMIN -> Routes.ADMIN_HOME
                        UserRole.SCHOOL -> Routes.SCHOOL_HOME
                        UserRole.TEACHER -> Routes.TEACHER_HOME
                        null -> TODO()
                    }
                    nav.navigate(dest) {
                        popUpTo(Routes.START) { inclusive = true }
                    }
                    loginVm.onEvent(LoginEvent.SuccessConsumed)
                }
            }

            LoginScreen(
                vm = loginVm,
                onLoggedIn = { role ->
                    val targetRoute = when (role) {
                        UserRole.ADMIN -> Routes.ADMIN_HOME
                        UserRole.SCHOOL -> Routes.SCHOOL_HOME
                        UserRole.TEACHER -> Routes.TEACHER_HOME
                    }
                    navController.navigate(targetRoute) {
                        popUpTo(Routes.START) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
private fun SimpleTabBody(label: String) {
    Text(text = label, color = Color.White, modifier = Modifier.padding(16.dp))
}