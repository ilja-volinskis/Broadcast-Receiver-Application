package com.example.broadcastreceiverapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.broadcastreceiverapplication.ui.MainScreen
import com.example.broadcastreceiverapplication.ui.theme.BroadcastReceiverApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BroadcastReceiverApplicationTheme(darkTheme = true) {
                MainScreen()
            }
        }
    }
}
