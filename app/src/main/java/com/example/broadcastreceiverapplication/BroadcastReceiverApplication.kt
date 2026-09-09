package com.example.broadcastreceiverapplication

import android.app.Application
import com.example.broadcastreceiverapplication.data.ApplicationScope
import com.example.broadcastreceiverapplication.data.AuthManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class BroadcastReceiverApplication : Application() {
    @Inject
    @ApplicationScope
    lateinit var appScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        appScope.launch { AuthManager.ensureSignedIn() }
    }
}