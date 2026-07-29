package com.example.broadcastreceiverapplication.ui.sms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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

    Scaffold {
        innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            PartitionContent(messages)
        }
    }
}


