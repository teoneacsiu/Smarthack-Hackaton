package com.hailavirtual.ui.screens.teachers.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun TeacherLessonsScreen(
    viewModel: TeacherLessonsScreenViewModel = hiltViewModel(),
    onAddLessonClick: () -> Unit = {},        // aici tratezi "adauga lectie"
    onLessonClick: (TeacherLessonUi) -> Unit = {} // optional, click pe card
) {
    Scaffold(
        topBar = { LessonsTopBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddLessonClick,
                shape = CircleShape,
                containerColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Adauga lectie",
                    tint = Color(0xFF3C0F84)
                )
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF020024),
                            Color(0xFF090979),
                            Color(0xFFcf0072)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp)
            ) {
                Text(
                    text = "Lectiile tale:",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                viewModel.lessons.forEach { lesson ->
                    LessonCard(
                        lesson = lesson,
                        onClick = { onLessonClick(lesson) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun LessonCard(
    lesson: TeacherLessonUi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9F9F9)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // cerc colorat cu icon (placeholder emoji, poti pune drawable-ul tau)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(lesson.color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\uD83D\uDD2C",
                    fontSize = 28.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = lesson.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF222222)
                )
                Text(
                    text = lesson.subtitle,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
private fun LessonsTopBar() {
    Surface(
        color = Color(0xFFF5F5F5),
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            // avatar stanga
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profil"
                )
            }

            // titlu centru
            Text(
                text = "Nume APP",
                fontSize = 22.sp,
                color = Color(0xFF3C0F84),
                fontWeight = FontWeight.Bold
            )

            // setari dreapta
            IconButton(
                onClick = { /* TODO: settings */ },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Setari",
                    tint = Color(0xFF3C0F84)
                )
            }
        }
    }
}
