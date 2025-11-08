package com.hailavirtual.ui.screens.students.lesson

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentLessonsScreen(
    viewModel: StudentLessonsScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lecții") }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Butoanele principale
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Substanțe
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = { viewModel.toggleSubstancesDropdown() },
                        modifier = Modifier.width(160.dp)
                    ) {
                        Text("Substanțe")
                    }

                    DropdownMenu(
                        expanded = viewModel.isSubstancesExpanded,
                        onDismissRequest = { viewModel.toggleSubstancesDropdown() },
                        modifier = Modifier.width(160.dp)
                    ) {
                        if (viewModel.isLoading) {
                            DropdownMenuItem(
                                text = { Text("Se încarcă...") },
                                onClick = {}
                            )
                        } else {
                            viewModel.substances.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item.name ?: "Fără nume") },
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
                                text = { Text("Se încarcă...") },
                                onClick = {}
                            )
                        } else {
                            viewModel.equipments.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item.name ?: "Fără nume") },
                                    onClick = { viewModel.selectEquipment(item) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Rezultate selecții
            viewModel.selectedSubstance?.let {
                Text("Ai selectat: ${it.name}", style = MaterialTheme.typography.bodyLarge)
            }

            viewModel.selectedEquipment?.let {
                Text("Echipament: ${it.name}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}