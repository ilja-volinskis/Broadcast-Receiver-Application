package com.example.broadcastreceiverapplication.ui.sms

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.TextAutoSizeDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.broadcastreceiverapplication.data.SmsData
import kotlin.random.Random


@Composable
fun SenderElement(
    sender: String,
    onClick: () -> Unit,
    sharedScope: SharedTransitionScope,
    animatedScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    val color = remember(sender) { randomGoodColor(sender.hashCode()) }

    val delayOffset = remember(sender) { Random.nextInt(10000) }

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

    val shape = RoundedCornerShape(20)
    with(sharedScope) {
        Box(
            modifier = modifier
                .padding(8.dp)
                .offset(y = offsetYFloat.dp)
                .scale(scale)
                .rotate(rotation)
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = sender),
                    animatedVisibilityScope = animatedScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                )
//                .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                .clip(shape)
                .background(color, shape)
                .fillMaxSize()
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            val textColor = MaterialTheme.colorScheme.onBackground
            BasicText(
                text = sender,
                autoSize = TextAutoSize.StepBased(minFontSize = 6.sp, maxFontSize = 24.sp),
                maxLines = 1,
                color = { textColor },
                modifier = Modifier.padding(horizontal = 4.dp)
            )

        }
    }
}

@Composable
fun SenderMessages(
    sender: String,
    messages: List<SmsData>,
    onBack: () -> Unit,
    sharedScope: SharedTransitionScope,
    animatedScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBack()
    }

    val color = remember(sender) { randomGoodColor(sender.hashCode()) }

    val shape = RoundedCornerShape(20)
    with(sharedScope) {
        Box(
            modifier = modifier
                .padding(8.dp)
                .fillMaxSize()
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = sender),
                    animatedVisibilityScope = animatedScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                )
//                .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                .clip(shape)
                .background(color, shape)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            MessagesContent(sender, messages)
        }
    }
}

// Generate acceptable color for dark background and white text
fun randomGoodColor(seed: Int): Color {
    val rnd = Random(seed)
    var red: Float = rnd.nextFloat()
    var green: Float = rnd.nextFloat()
    val blue: Float = rnd.nextFloat()

    // Lower influence of green and red to exclude bright yellows and such
    val factor = 1F - red * green
    red *= factor
    green *= factor

    return Color(red, green, blue)
}
