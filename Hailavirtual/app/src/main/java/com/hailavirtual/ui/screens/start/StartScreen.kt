package com.hailavirtual.ui.screens.start

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun StartScreen(
    viewModel: StartScreenViewModel = hiltViewModel(),
    onProfesorClick: () -> Unit = {},
    onElevClick: () -> Unit = {},
    onAdminClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF001B4E),
                        Color(0xFF3A005F),
                        Color(0xFFCF026F)
                    )
                )
            )
            .padding(horizontal = 24.dp, vertical = 32.dp),
        contentAlignment = Alignment.Center // 👈 centreaza tot continutul
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // 👈 centreaza pe verticala
        ) {
            Text(
                text = "Bun venit in\nlaboratorul nostru\ndigital!",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoleCard(
                    title = "Profesor",
                    emoji = "👨‍🏫",
                    backgroundColor = Color(0xFFDD006A),
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onProfesorClick(); onProfesorClick() }
                )
                RoleCard(
                    title = "Elev",
                    emoji = "📚",
                    backgroundColor = Color(0xFF8C0AA7),
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onElevClick(); onElevClick() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Alege rolul care te reprezinta si hai sa incepem explorarea stiintei!",
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )

            viewModel.selectedRole?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Rol selectat: $it",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}



@Composable
private fun RoleCard(
    title: String,
    emoji: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .aspectRatio(0.9f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 42.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
