package com.example.broadcastreceiverapplication.ui.sms

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SmsScreen(
    modifier: Modifier = Modifier,
    viewModel: SmsViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    var selectedSender by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SharedTransitionLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = selectedSender,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                }
            ) { target ->
                Scaffold { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (target == null) {
                            PartitionContent(
                                messages = messages,
                                onSenderClick = { selectedSender = it },
                                sharedScope = this@SharedTransitionLayout,
                                animatedScope = this@AnimatedContent
                            )
                        } else {
                            SenderMessages(
                                sender = target,
                                messages = messages.filter { it.sender == target },
                                onBack = { selectedSender = null },
                                sharedScope = this@SharedTransitionLayout,
                                animatedScope = this@AnimatedContent
                            )
                        }
                    }
                }
            }
        }
    }
}


