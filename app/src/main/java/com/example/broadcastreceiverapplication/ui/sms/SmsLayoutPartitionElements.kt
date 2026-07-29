package com.example.broadcastreceiverapplication.ui.sms

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.broadcastreceiverapplication.data.SmsData


@Composable
fun PartitionContent(messages: List<SmsData>) {
    val counts = messages.groupingBy { it.sender }
        .eachCount()
        .toList()
        .sortedByDescending { it.second }
    val total = counts.sumOf { it.second }
    PartitionColumn(counts, 0, total)
}

@Composable
fun PartitionRow(
    list: List<Pair<String, Int>>,
    index: Int,
    innerCount: Int,
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
            name = element.first,
            modifier = Modifier
                .weight(countAnim.value.coerceAtLeast(0.0001f))
                .fillMaxWidth()
        )
        if (otherCount > 0) {
            PartitionColumn(
                list,
                index + 1,
                otherCount,
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
            name = element.first,
            modifier = Modifier
                .weight(countAnim.value.coerceAtLeast(0.0001f))
                .fillMaxWidth()
        )
        if (otherCount > 0) {
            PartitionRow(
                list,
                index + 1,
                otherCount,
                modifier = Modifier.weight(otherCountAnim.value.coerceAtLeast(0.0001f))
            )
        }
    }
}