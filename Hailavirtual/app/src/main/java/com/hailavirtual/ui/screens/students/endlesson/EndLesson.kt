package com.hailavirtual.ui.screens.students.endlesson

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AssignmentTurnedIn
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun EndLessonScreen(
    viewModel: EndLessonViewModel = hiltViewModel(),
    lessonTitle: String = "Lectia 1",
    onRepeatExperimentClick: () -> Unit = {},
    onViewBrevierClick: () -> Unit = {},
    onViewTestClick: () -> Unit = {}
) {
    Scaffold(
        topBar = { EndLessonTopBar(lessonTitle) },
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
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Rand 1: doua carduri
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    EndLessonActionCard(
                        title = "Refa experimentul",
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        modifier = Modifier.weight(1f),
                        onClick = onRepeatExperimentClick
                    )

                    EndLessonActionCard(
                        title = "Vezi breviarul",
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Description,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        modifier = Modifier.weight(1f),
                        onClick = onViewBrevierClick
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Rand 2: card centrat
                EndLessonActionCard(
                    title = "Vizualizeaza testul",
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.AssignmentTurnedIn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.6f),
                    onClick = onViewTestClick
                )
            }
        }
    }
}

@Composable
private fun EndLessonActionCard(
    title: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 0.dp,
        shadowElevation = 8.dp,
        border = BorderStroke(2.dp, Color.White),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFcf0072),
                            Color(0xFF3C0F84)
                        )
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun EndLessonTopBar(
    lessonTitle: String
) {
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
                text = lessonTitle,
                fontSize = 22.sp,
                color = Color(0xFF3C0F84),
                fontWeight = FontWeight.Bold
            )

            // icon setari
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
