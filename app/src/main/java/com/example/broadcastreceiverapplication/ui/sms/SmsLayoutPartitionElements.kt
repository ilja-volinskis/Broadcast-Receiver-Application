package com.example.broadcastreceiverapplication.ui.sms

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.example.broadcastreceiverapplication.data.SmsData
import kotlin.math.abs


enum class PartitionLayout {
    RightDown, Spiral, BoxyRightDown, BoxyLeftDown
}

@Composable
fun PartitionContent(
    messages: List<SmsData>,
    onSenderClick: (String) -> Unit,
    sharedScope: SharedTransitionScope,
    animatedScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    initialLayout: PartitionLayout = PartitionLayout.BoxyRightDown
) {
    val counts = messages.groupingBy { it.sender }
        .eachCount()
        .toList()
        .sortedByDescending { it.second }
    val total = counts.sumOf { it.second }

    var layout by remember { mutableStateOf(initialLayout) }
    val layouts = remember { PartitionLayout.entries }

    var dragAccum by remember { mutableFloatStateOf(0f) }
    var hasFlippedThisGesture by remember { mutableStateOf(false) }
    val swipeThreshold = 120f

    val swipeModifier = modifier.pointerInput(Unit) {
        detectHorizontalDragGestures(
            onDragStart = {
                dragAccum = 0f
                hasFlippedThisGesture = false
            },
            onDragEnd = {
                dragAccum = 0f
                hasFlippedThisGesture = false
            },
            onDragCancel = {
                dragAccum = 0f
                hasFlippedThisGesture = false
            },
            onHorizontalDrag = { change, dragAmount ->
                change.consume()
                dragAccum += dragAmount
                if (!hasFlippedThisGesture && abs(dragAccum) >= swipeThreshold) {
                    val currentIndex = layouts.indexOf(layout)
                    val delta = if (dragAccum > 0) -1 else 1
                    val nextIndex = (currentIndex + delta + layouts.size) % layouts.size
                    layout = layouts[nextIndex]
                    hasFlippedThisGesture = true
                }
            }
        )
    }

    Crossfade(
        targetState = layout,
        animationSpec = tween(250),
        modifier = swipeModifier
    ) { currentLayout ->
        when (currentLayout) {
            PartitionLayout.RightDown ->
                PartitionColumn(counts, 0, total, onSenderClick, sharedScope, animatedScope)
            PartitionLayout.Spiral ->
                PartitionSpiral(counts, 0, total, 0, onSenderClick, sharedScope, animatedScope)
            PartitionLayout.BoxyRightDown ->
                PartitionBoxyRightDown(counts, 0, total, onSenderClick, sharedScope, animatedScope)
            PartitionLayout.BoxyLeftDown ->
                PartitionBoxyLeftDown(counts, 0, total, 0, onSenderClick, sharedScope, animatedScope)
        }
    }
}


@Composable
fun PartitionBoxyLeftDown(
    list: List<Pair<String, Int>>,
    index: Int,
    innerCount: Int,
    depth: Int,
    onSenderClick: (String) -> Unit,
    sharedScope: SharedTransitionScope,
    animatedScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    if (index >= list.size || innerCount <= 0) return
    val element = list[index]
    val count = element.second
    val otherCount = innerCount - count

    val countAnim = remember(element.first) { Animatable(0f) }
    val otherCountAnim = remember(index) { Animatable(0f) }
    LaunchedEffect(count) { countAnim.animateTo(count.toFloat(), tween(400, easing = EaseInOutCubic)) }
    LaunchedEffect(otherCount) { otherCountAnim.animateTo(otherCount.toFloat(), tween(400, easing = EaseInOutCubic)) }

    val itemWeight = countAnim.value.coerceAtLeast(0.0001f)
    val remainderWeight = otherCountAnim.value.coerceAtLeast(0.0001f)

    val itemFirst = depth % 2 == 0

    BoxWithConstraints(modifier.fillMaxSize()) {
        val splitHorizontally = remember(element.first) { maxWidth >= maxHeight }

        if (splitHorizontally) {
            Row(Modifier.fillMaxSize()) {
                if (itemFirst) {
                    SenderElement(
                        sender = element.first,
                        onClick = { onSenderClick(element.first) },
                        sharedScope = sharedScope,
                        animatedScope = animatedScope,
                        modifier = Modifier.weight(itemWeight).fillMaxSize()
                    )
                    if (otherCount > 0) {
                        PartitionBoxyLeftDown(
                            list, index + 1, otherCount, depth + 1,
                            onSenderClick, sharedScope, animatedScope,
                            modifier = Modifier.weight(remainderWeight)
                        )
                    }
                } else {
                    if (otherCount > 0) {
                        PartitionBoxyLeftDown(
                            list, index + 1, otherCount, depth + 1,
                            onSenderClick, sharedScope, animatedScope,
                            modifier = Modifier.weight(remainderWeight)
                        )
                    }
                    SenderElement(
                        sender = element.first,
                        onClick = { onSenderClick(element.first) },
                        sharedScope = sharedScope,
                        animatedScope = animatedScope,
                        modifier = Modifier.weight(itemWeight).fillMaxSize()
                    )
                }
            }
        } else {
            Column(Modifier.fillMaxSize()) {
                if (itemFirst) {
                    SenderElement(
                        sender = element.first,
                        onClick = { onSenderClick(element.first) },
                        sharedScope = sharedScope,
                        animatedScope = animatedScope,
                        modifier = Modifier.weight(itemWeight).fillMaxSize()
                    )
                    if (otherCount > 0) {
                        PartitionBoxyLeftDown(
                            list, index + 1, otherCount, depth + 1,
                            onSenderClick, sharedScope, animatedScope,
                            modifier = Modifier.weight(remainderWeight)
                        )
                    }
                } else {
                    if (otherCount > 0) {
                        PartitionBoxyLeftDown(
                            list, index + 1, otherCount, depth + 1,
                            onSenderClick, sharedScope, animatedScope,
                            modifier = Modifier.weight(remainderWeight)
                        )
                    }
                    SenderElement(
                        sender = element.first,
                        onClick = { onSenderClick(element.first) },
                        sharedScope = sharedScope,
                        animatedScope = animatedScope,
                        modifier = Modifier.weight(itemWeight).fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun PartitionBoxyRightDown(
    list: List<Pair<String, Int>>,
    index: Int,
    innerCount: Int,
    onSenderClick: (String) -> Unit,
    sharedScope: SharedTransitionScope,
    animatedScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    if (index >= list.size || innerCount <= 0) return
    val element = list[index]
    val count = element.second
    val otherCount = innerCount - count

    val countAnim = remember(element.first) { Animatable(0f) }
    val otherCountAnim = remember(index) { Animatable(0f) }
    LaunchedEffect(count) { countAnim.animateTo(count.toFloat(), tween(400, easing = EaseInOutCubic)) }
    LaunchedEffect(otherCount) { otherCountAnim.animateTo(otherCount.toFloat(), tween(400, easing = EaseInOutCubic)) }

    val itemWeight = countAnim.value.coerceAtLeast(0.0001f)
    val remainderWeight = otherCountAnim.value.coerceAtLeast(0.0001f)

    BoxWithConstraints(modifier.fillMaxSize()) {
        // Split the longer axis, so elements are closer to squares
        val splitHorizontally = remember(element.first) { maxWidth >= maxHeight }

        if (splitHorizontally) {
            Row(Modifier.fillMaxSize()) {
                SenderElement(
                    sender = element.first,
                    onClick = { onSenderClick(element.first) },
                    sharedScope = sharedScope,
                    animatedScope = animatedScope,
                    modifier = Modifier.weight(itemWeight).fillMaxSize()
                )
                if (otherCount > 0) {
                    PartitionBoxyRightDown(
                        list, index + 1, otherCount,
                        onSenderClick, sharedScope, animatedScope,
                        modifier = Modifier.weight(remainderWeight)
                    )
                }
            }
        } else {
            Column(Modifier.fillMaxSize()) {
                SenderElement(
                    sender = element.first,
                    onClick = { onSenderClick(element.first) },
                    sharedScope = sharedScope,
                    animatedScope = animatedScope,
                    modifier = Modifier.weight(itemWeight).fillMaxSize()
                )
                if (otherCount > 0) {
                    PartitionBoxyRightDown(
                        list, index + 1, otherCount,
                        onSenderClick, sharedScope, animatedScope,
                        modifier = Modifier.weight(remainderWeight)
                    )
                }
            }
        }
    }
}


@Composable
fun PartitionSpiral(
    list: List<Pair<String, Int>>,
    index: Int,
    innerCount: Int,
    side: Int,
    onSenderClick: (String) -> Unit,
    sharedScope: SharedTransitionScope,
    animatedScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    if (index >= list.size || innerCount <= 0) return
    val element = list[index]
    val count = element.second
    val otherCount = innerCount - count

    val countAnim = remember(element.first) { Animatable(0f) }
    val otherCountAnim = remember(index) { Animatable(0f) }
    LaunchedEffect(count) { countAnim.animateTo(count.toFloat(), tween(400, easing = EaseInOutCubic)) }
    LaunchedEffect(otherCount) { otherCountAnim.animateTo(otherCount.toFloat(), tween(400, easing = EaseInOutCubic)) }

    val itemWeight = countAnim.value.coerceAtLeast(0.0001f)
    val remainderWeight = otherCountAnim.value.coerceAtLeast(0.0001f)

    when (side) {
        0 -> Row(modifier.fillMaxSize()) { // left
            SenderElement(
                sender = element.first,
                onClick = { onSenderClick(element.first) },
                sharedScope = sharedScope,
                animatedScope = animatedScope,
                modifier = Modifier.weight(itemWeight).fillMaxSize()
            )
            if (otherCount > 0) {
                PartitionSpiral(
                    list, index + 1, otherCount, (side + 1) % 4,
                    onSenderClick, sharedScope, animatedScope,
                    modifier = Modifier.weight(remainderWeight)
                )
            }
        }
        1 -> Column(modifier.fillMaxSize()) { // top
            SenderElement(
                sender = element.first,
                onClick = { onSenderClick(element.first) },
                sharedScope = sharedScope,
                animatedScope = animatedScope,
                modifier = Modifier.weight(itemWeight).fillMaxSize()
            )
            if (otherCount > 0) {
                PartitionSpiral(
                    list, index + 1, otherCount, (side + 1) % 4,
                    onSenderClick, sharedScope, animatedScope,
                    modifier = Modifier.weight(remainderWeight)
                )
            }
        }
        2 -> Row(modifier.fillMaxSize()) { // right
            if (otherCount > 0) {
                PartitionSpiral(
                    list, index + 1, otherCount, (side + 1) % 4,
                    onSenderClick, sharedScope, animatedScope,
                    modifier = Modifier.weight(remainderWeight)
                )
            }
            SenderElement(
                sender = element.first,
                onClick = { onSenderClick(element.first) },
                sharedScope = sharedScope,
                animatedScope = animatedScope,
                modifier = Modifier.weight(itemWeight).fillMaxSize()
            )
        }
        else -> Column(modifier.fillMaxSize()) { // bottom
            if (otherCount > 0) {
                PartitionSpiral(
                    list, index + 1, otherCount, (side + 1) % 4,
                    onSenderClick, sharedScope, animatedScope,
                    modifier = Modifier.weight(remainderWeight)
                )
            }
            SenderElement(
                sender = element.first,
                onClick = { onSenderClick(element.first) },
                sharedScope = sharedScope,
                animatedScope = animatedScope,
                modifier = Modifier.weight(itemWeight).fillMaxSize()
            )
        }
    }
}


@Composable
fun PartitionRow(
    list: List<Pair<String, Int>>,
    index: Int,
    innerCount: Int,
    onSenderClick: (String) -> Unit,
    sharedScope: SharedTransitionScope,
    animatedScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    if (index >= list.size || innerCount <= 0) return
    val element = list[index]
    val count = element.second
    val otherCount = innerCount - count

    val countAnim = remember(element.first) { Animatable(0f) }
    val otherCountAnim = remember(index) { Animatable(0f) }

    LaunchedEffect(count) {
        countAnim.animateTo(count.toFloat(), tween(400))
    }
    LaunchedEffect(otherCount) {
        otherCountAnim.animateTo(otherCount.toFloat(), tween(400))
    }

    Row(
        modifier = modifier.fillMaxHeight(),
        horizontalArrangement = Arrangement.Start
    ) {
        SenderElement(
            sender = element.first,
            onClick = { onSenderClick(element.first) },
            sharedScope = sharedScope,
            animatedScope = animatedScope,
            modifier = Modifier
                .weight(countAnim.value.coerceAtLeast(0.0001f))
                .fillMaxWidth()
        )
        if (otherCount > 0) {
            PartitionColumn(
                list, index + 1, otherCount,
                onSenderClick, sharedScope, animatedScope,
                modifier = Modifier.weight(otherCountAnim.value.coerceAtLeast(0.0001f))
            )
        }
    }
}

@Composable
fun PartitionColumn(
    list: List<Pair<String, Int>>,
    index: Int,
    innerCount: Int,
    onSenderClick: (String) -> Unit,
    sharedScope: SharedTransitionScope,
    animatedScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    if (index >= list.size || innerCount <= 0) return
    val element = list[index]
    val count = element.second
    val otherCount = innerCount - count

    val countAnim = remember(element.first) { Animatable(0f) }
    val otherCountAnim = remember(index) { Animatable(0f) }

    LaunchedEffect(count) {
        countAnim.animateTo(count.toFloat(), tween(400))
    }
    LaunchedEffect(otherCount) {
        otherCountAnim.animateTo(otherCount.toFloat(), tween(400))
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        SenderElement(
            sender = element.first,
            onClick = { onSenderClick(element.first) },
            sharedScope = sharedScope,
            animatedScope = animatedScope,
            modifier = Modifier
                .weight(countAnim.value.coerceAtLeast(0.0001f))
                .fillMaxWidth()
        )
        if (otherCount > 0) {
            PartitionRow(
                list, index + 1, otherCount,
                onSenderClick, sharedScope, animatedScope,
                modifier = Modifier.weight(otherCountAnim.value.coerceAtLeast(0.0001f))
            )
        }
    }
}