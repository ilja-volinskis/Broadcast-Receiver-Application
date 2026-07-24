package com.example.broadcastreceiverapplication.ui.sms

import androidx.lifecycle.ViewModel
import com.example.broadcastreceiverapplication.data.SmsData
import com.example.broadcastreceiverapplication.data.SmsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SmsViewModel @Inject constructor(
    val repository: SmsRepository
) : ViewModel() {

    val messages: StateFlow<List<SmsData>> = repository.messages
}