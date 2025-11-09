package com.hailavirtual.ui.screens.students.customexp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hailavirtual.ui.PromptViewModel
import kotlinx.coroutines.launch
import com.hailavirtual.data.model.GeminiResult

@Composable
fun CustomExpScreen(
    viewModel: CustomExpScreenViewModel = hiltViewModel(),
    promptViewModel: PromptViewModel = hiltViewModel()
) {
    val isChatVisible by viewModel.isChatVisible.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val promptState by promptViewModel.state.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    var userInput by remember { mutableStateOf("") }

    val prefix = "Write the answer for the following prompt in romanian, " +
            "u talk to child from 10 to 15 years so make the text understandable, short and brief." +
            "Don't answer in markdown, use plain text because you're writing in a chat dialog," +
            "the text doesn't support any type of styling, also don't forget you talk to a 13 y.o." +
            "about chemistry. if the following prompt isn't about chemistry you just tell politely" +
            "that you don't have permission to respond to this. "

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            CustomExpTopBar(onChatClick = { viewModel.toggleChat() })
            // Screen content placeholder
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Substante
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = { viewModel.toggleSubstancesDropdown() },
                        modifier = Modifier.width(160.dp)
                    ) {
                        Text("Substante")
                    }

                    DropdownMenu(
                        expanded = viewModel.isSubstancesExpanded,
                        onDismissRequest = { viewModel.toggleSubstancesDropdown() },
                        modifier = Modifier.width(160.dp)
                    ) {
                        if (viewModel.isLoading) {
                            DropdownMenuItem(
                                text = { Text("Se incarca...") },
                                onClick = {}
                            )
                        } else {
                            viewModel.substances.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item.name) },
                                    onClick = { viewModel.selectSubstance(item) }
                                )
                            }
                        }
                    }
                }

                // Echipament
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = { viewModel.toggleEquipmentDropdown() },
                        modifier = Modifier.width(160.dp)
                    ) {
                        Text("Echipament")
                    }

                    DropdownMenu(
                        expanded = viewModel.isEquipmentExpanded,
                        onDismissRequest = { viewModel.toggleEquipmentDropdown() },
                        modifier = Modifier.width(160.dp)
                    ) {
                        if (viewModel.isLoading) {
                            DropdownMenuItem(
                                text = { Text("Se incarca...") },
                                onClick = {}
                            )
                        } else {
                            viewModel.equipments.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item.name) },
                                    onClick = { viewModel.selectEquipment(item) }
                                )
                            }
                        }
                    }
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF7F7F7)),
                contentAlignment = Alignment.Center
            ) {
                Text("Custom Experience Content", color = Color.Gray)
            }
        }

        if (isChatVisible) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .width(320.dp)
                    .height(420.dp),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Dialog TopBar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF3C0F84))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            viewModel.toggleChat()
                            viewModel.clearChat()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Close Chat",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "Chatbot",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Chat Messages
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(messages) { message ->
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
                            ) {
                                Surface(
                                    color = if (message.isUser) Color(0xFFD1C4E9) else Color(0xFFF5F5F5),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = message.text,
                                        modifier = Modifier.padding(10.dp),
                                        color = Color.Black
                                    )
                                }
                            }
                        }

                        // AI response from PromptViewModel
                        if (promptState !is GeminiResult.Waiting) {
                            val aiText = when (val result = promptState) {
                                is GeminiResult.Completed -> result.text
                                is GeminiResult.Error -> "Error: ${result.message}"
                                else -> ""
                            }
                            if (aiText.isNotEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Surface(
                                            color = Color(0xFFF5F5F5),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text(
                                                text = aiText,
                                                modifier = Modifier.padding(10.dp),
                                                color = Color.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Message Input
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = userInput,
                            onValueChange = { userInput = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Type a message...") }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val text = userInput.trim()
                                if (text.isNotEmpty()) {
                                    // adauga mesajul cu textul fotografiat
                                    viewModel.addMessage(ChatMessage(text, true))
                                    // curata imediat UI-ul
                                    userInput = ""

                                    val sendText = "$prefix$text"
                                    coroutineScope.launch {
                                        // foloseste snapshot-ul, nu state-ul mutabil
                                        promptViewModel.run(sendText)
                                        Log.d("AI RESPONSE", sendText)
                                    }
                                }
                            }
                        ) {
                            Text("Send")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomExpTopBar(onChatClick: () -> Unit) {
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
                    contentDescription = "Profile"
                )
            }

            Text(
                text = "Custom Experience",
                fontSize = 22.sp,
                color = Color(0xFF3C0F84),
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = onChatClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Chat,
                    contentDescription = "Chatbot",
                    tint = Color(0xFF3C0F84)
                )
            }
        }
    }
}
