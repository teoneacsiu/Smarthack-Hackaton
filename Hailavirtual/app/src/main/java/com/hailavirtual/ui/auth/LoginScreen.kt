package com.hailavirtual.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hailavirtual.data.model.UserRole
import com.hailavirtual.R

@Composable
fun LoginScreen(
    vm: LoginViewModel = hiltViewModel(),
    onLoggedIn: (UserRole) -> Unit = {},
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(state.success, state.role) {
        if (state.success && state.role != null) {
            onLoggedIn(state.role!!)
            vm.onEvent(LoginEvent.SuccessConsumed)
        }
    }

    // Fundal identic cu StartScreen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF001B4E), // albastru închis sus
                        Color(0xFF3A005F),
                        Color(0xFFCF026F)  // magenta jos
                    )
                )
            )
            .padding(horizontal = 32.dp, vertical = 48.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Titlu
            Text(
                text = "Profesor",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            // Placeholder pentru imagine
            Image(
                painter = painterResource(R.drawable.course),
                contentDescription = "login photo",
                modifier = Modifier.size(150.dp).clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(40.dp))

            // Câmp Username
            OutlinedTextField(
                value = state.email,
                onValueChange = { vm.onEvent(LoginEvent.EmailChanged(it)) },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(Modifier.height(16.dp))

            // Câmp Parolă
            OutlinedTextField(
                value = state.password,
                onValueChange = { vm.onEvent(LoginEvent.PasswordChanged(it)) },
                label = { Text("Parolă") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(Modifier.height(36.dp))

            // Butonul mare rotunjit
            Button(
                onClick = { vm.onEvent(LoginEvent.Submit) },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(50)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.9f),
                    contentColor = Color(0xFFCF026F)
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color(0xFFCF026F),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Arată clasele",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }

            if (state.error != null) {
                Spacer(Modifier.height(16.dp))
                Snackbar { Text(state.error!!) }
            }
        }
    }
}
