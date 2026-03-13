package com.example.soccmel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soccmel.ui.theme.DeepOceanBlue
import com.example.soccmel.ui.theme.MidnightBlue
import kotlin.random.Random

@Composable
fun BolognaBackground(
    content: @Composable () -> Unit
) {
    // Un blu scuro profondo per lo sfondo
    val darkSkyGradient = Brush.verticalGradient(
        colors = listOf(
            MidnightBlue,
            DeepOceanBlue,
            MidnightBlue
        )
    )

    Box(modifier = Modifier.fillMaxSize().background(darkSkyGradient)) {
        val randomTortellini = remember {
            List(25) {
                TortelliniData(
                    x = Random.nextFloat(),
                    y = Random.nextFloat(),
                    rotation = Random.nextFloat() * 360f,
                    size = Random.nextInt(38, 56)
                )
            }
        }

        randomTortellini.forEach { tortellino ->
            Text(
                text = "🥟",
                fontSize = tortellino.size.sp,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(
                        x = (tortellino.x * 400).dp,
                        y = (tortellino.y * 800).dp
                    )
                    .rotate(tortellino.rotation)
                    .alpha(0.25f) // Si vedono bene ma mantengono un'atmosfera elegante
            )
        }

        content()
    }
}

data class TortelliniData(
    val x: Float,
    val y: Float,
    val rotation: Float,
    val size: Int
)
