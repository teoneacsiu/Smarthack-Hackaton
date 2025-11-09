package com.hailavirtual.ui.screens.students.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hailavirtual.data.model.Substance
import kotlin.math.roundToInt
import com.hailavirtual.R

data class DraggableItem(
    val id: Int,
    val label: String,
    val drawableRes: Int,
    var offsetX: Float = 0f,
    var offsetY: Float = 0f
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentLessonsScreen(
    lessonId: String,
    viewModel: StudentLessonsScreenViewModel = hiltViewModel()
) {

    LaunchedEffect(lessonId) {
        viewModel.loadDataForLesson(lessonId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lectii") }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ---------- TOP FILTER BAR (dropdowns) ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(8.dp))

            // ---------- SMALL SUMMARY ROW ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Substanta: ${viewModel.selectedSubstance?.name ?: "-"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Echipament: ${viewModel.selectedEquipment?.name ?: "-"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ---------- DASHBOARD AREA (separate from dropdowns) ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)              // ocupa tot spatiul ramas
                    .background(Color(0xFFEFEFEF))
                    .padding(16.dp)
            ) {
                DraggableSubstanceDashboard(
                    selectedSubstances = viewModel.dashboardSubstances
                )
            }
        }
    }
}

/**
 * Dashboard-ul in care incarcam poza cu eprubeta pentru substanta selectata
 * si o putem muta cu degetul.
 */
@Composable
fun DraggableSubstanceDashboard(
    selectedSubstances: List<Substance>
) {
    val items = remember { mutableStateListOf<DraggableItem>() }

    // Add a new draggable item every time a new substance is added
    LaunchedEffect(selectedSubstances.size) {
        if (selectedSubstances.isNotEmpty()) {
            val last = selectedSubstances.last()
            val label = last.name
            val drawableRes = mapSubstanceToDrawable(last)

            val nextId = (items.maxOfOrNull { it.id } ?: 0) + 1
            items.add(
                DraggableItem(
                    id = nextId,
                    label = label,
                    drawableRes = drawableRes,
                    offsetX = 0f,
                    offsetY = 0f
                )
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (items.isEmpty()) {
            Text(
                text = "Selecteaza o substanta pentru a vedea eprubeta.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Center)
            )
            return
        }

        items.forEach { item ->
            key(item.id) {
                var offsetX by remember { mutableStateOf(item.offsetX) }
                var offsetY by remember { mutableStateOf(item.offsetY) }

                // keep DraggableItem’s offsets in sync with local state
                LaunchedEffect(offsetX, offsetY) {
                    item.offsetX = offsetX
                    item.offsetY = offsetY
                }

                Image(
                    painter = painterResource(id = item.drawableRes),
                    contentDescription = "Eprubeta ${item.label}",
                    modifier = Modifier
                        .size(width = 120.dp, height = 220.dp)
                        .align(Alignment.Center) // start in center
                        .offset {
                            IntOffset(
                                offsetX.roundToInt(),
                                offsetY.roundToInt()
                            )
                        }
                        .pointerInput(item.id) {
                            detectDragGestures(
                                onDragEnd = {
                                    // when drag ends, try to merge
                                    mergeIfOverlap(items, item)
                                }
                            ) { change, dragAmount ->
                                change.consume()
                                offsetX += dragAmount.x
                                offsetY += dragAmount.y
                            }
                        }
                )
            }
        }
    }
}

fun mapSubstanceToDrawable(substance: Substance): Int {
    return when (substance.name.lowercase().trim()) {
        "hcl" -> R.drawable.substanta2
        "turnesol" -> R.drawable.substanta1
        "water", "apa" -> R.drawable.ic_launcher_foreground
        else -> R.drawable.ic_launcher_foreground
    }
}

private fun mergeIfOverlap(
    items: MutableList<DraggableItem>,
    moved: DraggableItem
) {
    val threshold = 120f // how close centers must be to merge

    val other = items.firstOrNull { it.id != moved.id && distance(it, moved) < threshold }
    if (other != null) {
        // create merged item
        val newLabel = "${moved.label} + ${other.label}"

        val newDrawable = mapMergeToDrawable(moved.label, other.label)

        val newItem = DraggableItem(
            id = (items.maxOfOrNull { it.id } ?: 0) + 1,
            label = newLabel,
            drawableRes = newDrawable,
            offsetX = (moved.offsetX + other.offsetX) / 2f,
            offsetY = (moved.offsetY + other.offsetY) / 2f
        )

        // remove old 2 and add the merged one
        items.removeAll { it.id == moved.id || it.id == other.id }
        items.add(newItem)
    }
}

private fun distance(a: DraggableItem, b: DraggableItem): Float {
    val dx = a.offsetX - b.offsetX
    val dy = a.offsetY - b.offsetY
    return kotlin.math.sqrt(dx * dx + dy * dy)
}

fun mapMergeToDrawable(label1: String, label2: String): Int {
    val a = label1.lowercase().trim()
    val b = label2.lowercase().trim()
    val set = setOf(a, b)

    return when (set) {
        // example: HCl + NaOH -> “salt water” test tube
        setOf("hcl", "turnesol") -> R.drawable.substanta3

        // add your own combinations here
        // setOf("hcl", "water") -> R.drawable.test_tube_hcl_diluted
        // setOf("naoh", "water") -> R.drawable.test_tube_naoh_diluted

        else -> R.drawable.ic_launcher_foreground // generic mixed solution
    }
}

