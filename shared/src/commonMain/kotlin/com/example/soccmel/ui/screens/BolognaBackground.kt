package com.example.soccmel.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.soccmel.ui.theme.DeepOceanBlue
import com.example.soccmel.ui.theme.MidnightBlue
import org.jetbrains.compose.resources.painterResource
import com.example.soccmel.shared.generated.resources.Res
import com.example.soccmel.shared.generated.resources.ic_tortellino
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
            List(20) {
                TortelliniData(
                    x = Random.nextFloat(),
                    y = Random.nextFloat(),
                    rotation = Random.nextFloat() * 360f,
                    size = Random.nextInt(40, 70)
                )
            }
        }

        randomTortellini.forEach { tortellino ->
            Image(
                painter = painterResource(Res.drawable.ic_tortellino),
                contentDescription = null,
                modifier = Modifier
                    .size(tortellino.size.dp)
                    .offset(
                        x = (tortellino.x * 400).dp,
                        y = (tortellino.y * 800).dp
                    )
                    .rotate(tortellino.rotation)
                    .alpha(0.15f)
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
