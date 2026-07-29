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

//    private val _messages = MutableStateFlow<List<SmsData>>(emptyList())
    private val _messages = MutableStateFlow<List<SmsData>>(listOf(
        SmsData("+37112345678", "Text text", 13785623),
        SmsData("+37112345678", "Text text2", 237856223),
        SmsData("+37112345678", "Text text3", 337856223),
        SmsData("+37112345699", "Text text4", 437856223),
        SmsData("+37112345699", "Text text5", 537856223),
        SmsData("+37112345679", "Text text6", 637856253),
        SmsData("+37112345679", "Text text7", 737856253),
        SmsData("+37112345679", "Text text8", 837856253),
    ))
    override val messages: StateFlow<List<SmsData>> = _messages

    override fun addMessage(sms: SmsData) {
        _messages.value += sms
    }


}