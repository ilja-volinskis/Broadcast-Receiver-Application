package com.example.broadcastreceiverapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.example.broadcastreceiverapplication.data.ApplicationScope
import com.example.broadcastreceiverapplication.data.SmsData
import com.example.broadcastreceiverapplication.data.SmsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    @Inject lateinit var repository: SmsRepository
    @Inject @ApplicationScope
    lateinit var scope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent) ?: return
        val pendingResult = goAsync()

        scope.launch {
            try {
                messages.groupBy { it.originatingAddress.orEmpty() }.forEach { (sender, msgs) ->
                    val body = msgs.joinToString("") { it.messageBody.orEmpty() }
                    val timestamp = msgs.firstOrNull()?.timestampMillis ?: System.currentTimeMillis()
                    repository.addMessage(SmsData(sender, body, timestamp))
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}