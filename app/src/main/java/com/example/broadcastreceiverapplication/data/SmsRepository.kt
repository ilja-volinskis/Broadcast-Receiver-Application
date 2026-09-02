package com.example.broadcastreceiverapplication.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface SmsRepository {
    val messages: StateFlow<List<SmsData>>
    fun addMessage(sms: SmsData)
}

@Singleton
class SmsRepositoryImpl @Inject constructor() : SmsRepository {

    private val _messages = MutableStateFlow<List<SmsData>>(emptyList())
    override val messages: StateFlow<List<SmsData>> = _messages

    override fun addMessage(sms: SmsData) {
        _messages.value += sms
    }
}