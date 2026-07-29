package com.example.broadcastreceiverapplication.ui.sms

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random


@Composable
fun SenderElement(
    name: String,
    modifier: Modifier = Modifier
) {
    val color = remember(name) { randomGoodColor() }

    val delayOffset = remember(name) { Random.nextInt(10000) }

    val infiniteTransition = rememberInfiniteTransition(label = "blobAnimation")

    val offsetYFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, delayMillis = delayOffset, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(5500, delayMillis = delayOffset, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, delayMillis = delayOffset, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .padding(8.dp)
            .offset(y = offsetYFloat.dp)
            .scale(scale)
            .rotate(rotation)
            .background(color, shape = RoundedCornerShape(20))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name)
    }
}

// Generate acceptable color for dark background and white text
fun randomGoodColor(): Color {
    var red: Float = Random.nextFloat()
    var green: Float = Random.nextFloat()
    val blue: Float = Random.nextFloat()

    // Lower influence of green and red to exclude bright yellows and such
    val factor = 1F - red * green
    red *= factor
    green *= factor

    return Color(red, green, blue)
}

//@Composable
//fun SenderElement(
//    name: String,
//    modifier: Modifier = Modifier
//) {
//    val color = remember(name) {
//        Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
//    }
//    Box(
//        modifier = modifier
//            .padding(16.dp)
//            .background(color)
//            .fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(text = name)
//    }
//}
