package com.hailavirtual.ui.screens.teachers.addlesson

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldColors

object AddLessonScreen {

    @Composable
    operator fun invoke(
        onChooseSubstancesClick: () -> Unit = {},
        onChooseMaterialsClick: () -> Unit = {},
        onChooseRecipeClick: () -> Unit = {},
        onCreateClick: () -> Unit = {}
    ) {
        var title by rememberSaveable { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF000B3F),
                            Color(0xFF2C0074),
                            Color(0xFFCF026F)
                        )
                    )
                )
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Creeeaza lectia!",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 32.dp)
                )

                // TextField titlu lectie
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Adauga titlul lectiei",
                            color = Color.White.copy(alpha = 0.55f)
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0x55FFFFFF),
                        textColor = Color.White,
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Selectoare
                SelectorField(
                    label = "Alege substantele",
                    backgroundColor = Color(0xFF2A009D),
                    onClick = onChooseSubstancesClick
                )

                Spacer(modifier = Modifier.height(16.dp))

                SelectorField(
                    label = "Alege materialele",
                    backgroundColor = Color(0xFFB00063),
                    onClick = onChooseMaterialsClick
                )

                Spacer(modifier = Modifier.height(16.dp))

                SelectorField(
                    label = "Alege reteta",
                    backgroundColor = Color(0xFF2A009D),
                    onClick = onChooseRecipeClick
                )

                Spacer(modifier = Modifier.weight(1f))

                GradientButton(
                    text = "Creeeaza!",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    onClick = onCreateClick
                )
            }
        }
    }

    @Composable
    private fun TextFieldDefaults.textFieldColors(
        containerColor: Color,
        textColor: Color,
        cursorColor: Color,
        focusedIndicatorColor: Color,
        unfocusedIndicatorColor: Color,
        disabledIndicatorColor: Color
    ): TextFieldColors {
        return TextFieldDefaults.colors();
    }
}

@Composable
private fun SelectorField(
    label: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
private fun GradientButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // buton rotund cu gradient, ca in design
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(50),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFF006A),
                                Color(0xFF5C00FF)
                            )
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddLessonScreenPreview() {
    MaterialTheme {
        AddLessonScreen()
    }
}
