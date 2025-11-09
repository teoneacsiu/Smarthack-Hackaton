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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.hailavirtual.R
import com.hailavirtual.data.model.UserRole
import com.hailavirtual.ui.auth.LoginEvent
import com.hailavirtual.ui.auth.LoginScreen
import com.hailavirtual.ui.auth.LoginViewModel
import com.hailavirtual.ui.screens.admin.main.MainScreen
import com.hailavirtual.ui.screens.school.manageteacher.ManageScreen
import com.hailavirtual.ui.screens.start.StartScreen
import com.hailavirtual.ui.screens.students.chooseclass.ChooseClassScreen
import com.hailavirtual.ui.screens.students.endlesson.EndLessonScreen
import com.hailavirtual.ui.screens.students.home.StudentHomeScreen
import com.hailavirtual.ui.screens.students.lesson.StudentLessonsScreen
import com.hailavirtual.ui.screens.teachers.addlesson.AddLessonScreen
import com.hailavirtual.ui.screens.teachers.lesson.TeacherLessonsScreen
import com.hailavirtual.ui.screens.teachers.schoolclass.SchoolClassScreen


@Composable
fun Navigation(startDestination: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    var backPressedOnce by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isOnHomeRoot =
        currentRoute == Route.AdminHome.route ||
                currentRoute == Route.SchoolHome.route ||
                currentRoute == Route.TeacherHome.route

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

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // START
        composable(Route.Start.route) {
            StartScreen(
                onProfesorClick = { navController.navigate(Route.Login.route) },
                onElevClick = { navController.navigate(Route.ChooseClass.route) },
                onAdminClick = { navController.navigate(Route.Login.route) }
            )
        }

        // LOGIN – redirectioneaza in functie de rol
        composable(Route.Login.route) {
            val vm: LoginViewModel = hiltViewModel()
            LoginScreen(
                vm = vm,
                onLoggedIn = { role ->
                    when (role) {
                        UserRole.ADMIN -> navController.navigate(Route.AdminHome.route) {
                            popUpTo(Route.Start.route) { inclusive = true }
                        }

                        UserRole.TEACHER -> navController.navigate(Route.TeacherHome.route) {
                            popUpTo(Route.Start.route) { inclusive = true }
                        }

                        UserRole.SCHOOL -> navController.navigate(Route.SchoolHome.route) {
                            popUpTo(Route.Start.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // ADMIN HOME (MainScreen placeholder in proiect)
        composable(Route.AdminHome.route) { MainScreen() }

        // SCHOOL HOME (Manage teachers)
        composable(Route.SchoolHome.route) { ManageScreen() }

        // STUDENT FLOW
        composable(Route.ChooseClass.route) {
            ChooseClassScreen(
                onAddClick = { classId ->
                    // optional: encode daca ai spatii/caractere speciale
                    // val encoded = Uri.encode(classId)
                    // navController.navigate("student_home/$encoded")

                    navController.navigate("student_home/$classId") {
                        // optional: nu mai intoarce la ChooseClass la back
                        popUpTo(Route.Start.route) { inclusive = false }
                    }
                }
            )
        }
        composable(
            route = Route.StudentHome.route, // "student_home/{classId}"
            arguments = listOf(
                navArgument("classId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Daca vrei sa-l vezi aici:
            // val classId = backStackEntry.arguments?.getString("classId") ?: ""

            // ViewModel-ul StudentHomeScreen citeste "classId" din SavedStateHandle
            StudentHomeScreen(
                onAddClick = { navController.navigate(Route.StudentLessons.route) },
                onLessonClick = { lessonId ->
                    navController.navigate("student_lessons/$lessonId")
                }
            )
        }
        composable(
            route = "student_lessons/{lessonId}",
            arguments = listOf(
                navArgument("lessonId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
            StudentLessonsScreen(lessonId = lessonId)
        }

        composable(Route.StudentEndLesson.route) {
            EndLessonScreen(
                lessonTitle = "Lectia 1",
                onRepeatExperimentClick = { /* TODO */ },
                onViewBrevierClick = { /* TODO */ },
                onViewTestClick = { /* TODO */ }
            )
        }
        // Momentan CustomExpScreen nu este @Composable (clasa goala)
        composable(Route.StudentCustomExp.route) {
            // TODO: transforma in @Composable real
        }

        // TEACHER FLOW
        composable(Route.TeacherHome.route) {
            TeacherLessonsScreen(
                onAddLessonClick = { navController.navigate(Route.TeacherAddLesson.route) },
                onLessonClick = { /* TODO: detalii lectie */ }
            )
        }
        composable(Route.TeacherLessons.route) {
            TeacherLessonsScreen(
                onAddLessonClick = { navController.navigate(Route.TeacherAddLesson.route) }
            )
        }
        composable(Route.TeacherAddLesson.route) {
            AddLessonScreen(
                onChooseSubstancesClick = { /* TODO */ },
                onChooseMaterialsClick = { /* TODO */ },
                onChooseRecipeClick = { /* TODO */ },
                onCreateClick = { navController.popBackStack() }
            )
        }
        composable(Route.TeacherClasses.route) {
            // SchoolClassScreen cere o lista in params -> mutati in ViewModel
            SchoolClassScreen(
                classes = emptyList(),
                onClassClick = { /* TODO */ },
                onSettingsClick = { /* TODO */ }
            )
        }

        // UTILITATI
        composable(Route.ManageTeachers.route) { ManageScreen() }
    }
}

@Composable
private fun SimpleTabBody(label: String) {
    Text(text = label, color = Color.White, modifier = Modifier.padding(16.dp))
}