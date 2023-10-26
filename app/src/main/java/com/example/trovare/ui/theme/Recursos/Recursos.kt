package com.example.trovare.ui.theme.Recursos

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Divisor(modifier: Modifier = Modifier){
    Divider(
        modifier = modifier
            .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
        color = Color.White
    )
}