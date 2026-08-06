package com.example.broadcastreceiverapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.example.broadcastreceiverapplication.data.SmsData
import com.example.broadcastreceiverapplication.data.SmsRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: SmsRepository

    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        messages
            ?.groupBy { it.originatingAddress.orEmpty() }
            ?.forEach { (sender, messages) ->
                val body = messages.joinToString("") { it.messageBody.orEmpty() }
                val timestamp = messages.firstOrNull()?.timestampMillis ?: System.currentTimeMillis()
                val smsData = SmsData(
                    sender = sender,
                    body = body,
                    timestamp = timestamp
                )
                repository.addMessage(smsData)
            }
    }

}