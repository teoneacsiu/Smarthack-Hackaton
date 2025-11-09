package com.hailavirtual.ui.screens.students.home

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hailavirtual.data.model.Lesson

@Composable
fun StudentHomeScreen(
    viewModel: StudentHomeScreenViewModel = hiltViewModel(),
    onAddClick: () -> Unit = {},
    onLessonClick: (Lesson) -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    // presupunem ca viewModel.lessons e un state (mutableStateOf/lista observabila)
    val lessons = viewModel.lessons

    Scaffold(
        topBar = { HomeTopBar(onSettingsClick) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
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

                if (lessons.isEmpty()) {
                    EmptyLessonsHint()
                } else {
                    lessons.forEach { lesson ->
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
}

@Composable
private fun LessonCard(
    lesson: Lesson,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(avatarColorFor(lesson.id)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "\uD83D\uDD2C", fontSize = 28.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.name.ifBlank { "Lectie fara nume" },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF222222)
                )
                val expCount = lesson.experimentIds.size
                Text(
                    text = if (expCount == 1) "1 experiment" else "$expCount experimente",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

/**
 * Genereaza o culoare stabila pe baza id-ului lectiei (fallback daca nu ai culoare in model).
 */
private fun avatarColorFor(id: String): Color {
    if (id.isBlank()) return Color(0xFF8E24AA)
    val hash = id.fold(0) { acc, c -> (31 * acc + c.code) }
    // paleta scurta, contrast decent pe fundal deschis
    val palette = listOf(
        0xFF3F51B5, 0xFF009688, 0xFF8E24AA, 0xFFFF7043, 0xFF7CB342, 0xFF5C6BC0
    )
    val colorInt = palette[kotlin.math.abs(hash) % palette.size]
    return Color(colorInt)
}

@Composable
private fun EmptyLessonsHint() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0x22FFFFFF)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Nu exista lectii pentru clasa selectata.",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Dupa ce profesorul adauga lectii, ele vor aparea aici.",
                color = Color(0xFFE0E0E0),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun HomeTopBar(
    onSettingsClick: () -> Unit
) {
    Surface(color = Color(0xFFF5F5F5), shadowElevation = 4.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Outlined.Person, contentDescription = "Profil")
            }

            Text(
                text = "Nume APP",
                fontSize = 22.sp,
                color = Color(0xFF3C0F84),
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = onSettingsClick,
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
