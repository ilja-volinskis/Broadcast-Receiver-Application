package com.example.broadcastreceiverapplication.ui.sms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.broadcastreceiverapplication.R
import com.example.broadcastreceiverapplication.data.SmsData
import java.text.DateFormat
import java.util.Date

@Composable
fun SmsScreen(
    modifier: Modifier = Modifier,
    viewModel: SmsViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.listening_for_incoming_sms),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun MessagesContent(
    messages: List<SmsData>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (messages.isEmpty()) {
            Text(stringResource(R.string.no_messages_received_yet))
        } else {
            LazyColumn {
                items(messages.reversed()) {
                    SmsItem(it)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun SmsItem(
    message: SmsData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp)
    ) {
        Text(
            text = message.sender,
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = message.body,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = DateFormat.getDateTimeInstance().format(Date(message.timestamp)),
            style = MaterialTheme.typography.labelSmall
        )
    }
}
