package com.hailavirtual.ui.screens.students.chooseclass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ChooseClassScreen(
    viewModel: ChooseClassScreenViewModel = hiltViewModel(),
    onAddClick: (String) -> Unit = {} // primeste classId valid -> nav in parent: navController.navigate("student_home/$id")
) {
    val classId = viewModel.classId
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    Box(
        modifier = Modifier
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
        // Continut centrat
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Introduceti ID-ul clasei din care faceti parte",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Textbox rotunjit, semi-transparent
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    value = classId,
                    onValueChange = { viewModel.onClassIdChange(it) },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    placeholder = { Text("") },
                    enabled = !isLoading
                )
            }

            if (error != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = error,
                    color = Color(0xFFFFE082),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            // --- BUTON SUB TEXTBOX ---
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.tryEnter { schoolClass ->
                        onAddClick(schoolClass.id) // trimitem doar daca exista clasa
                    }
                },
                enabled = classId.isNotBlank() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text(text = "Intra in clasa")
                }
            }
            // ------------------------------
        }

        // Buton plus in dreapta jos (acelasi comportament cu cel de sus)
        FloatingActionButton(
            onClick = {
                viewModel.tryEnter { schoolClass ->
                    onAddClick(schoolClass.id)
                }
            },
            shape = CircleShape,
            containerColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            // Material3 FloatingActionButton nu are "enabled"; folosim loading state vizual
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Adauga",
                    tint = Color(0xFF3C0F84)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChooseClassScreenPreview() {
    MaterialTheme { /* Preview simplu */ }
}
