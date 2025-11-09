package com.hailavirtual.ui.nav

sealed class Route(val route: String) {
    // Flow general / auth
    data object Start : Route("start")
    data object Login : Route("login")

    // Role homes
    data object AdminHome : Route("admin_home")
    data object TeacherHome : Route("teacher_home")
    data object SchoolHome : Route("school_home")

    // Student flow
    data object ChooseClass : Route("student_choose_class")
    data object StudentHome : Route("student_home/{classId}")
    data object StudentLessons : Route("student_lessons/{lessonId}") {
        // funcție helper pentru navigare ușoară
        fun createRoute(lessonId: String) = "student_lessons/$lessonId"
    }
    data object StudentEndLesson : Route("student_end_lesson")
    data object StudentCustomExp : Route("student_custom_exp")

    // Teacher flow
    data object TeacherLessons : Route("teacher_lessons")
    data object TeacherAddLesson : Route("teacher_add_lesson")
    data object TeacherClasses : Route("teacher_classes")

    // School/Admin utilities
    data object ManageTeachers : Route("manage_teachers")
}