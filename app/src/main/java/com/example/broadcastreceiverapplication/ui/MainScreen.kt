package com.example.broadcastreceiverapplication.ui

import android.Manifest
import androidx.compose.runtime.Composable
import com.example.broadcastreceiverapplication.ui.permission.PermissionScreen
import com.example.broadcastreceiverapplication.ui.sms.SmsScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {
    val smsPermissionState: PermissionState = rememberPermissionState(Manifest.permission.RECEIVE_SMS)
    val hasPermission = smsPermissionState.status.isGranted

    if(hasPermission) {
        SmsScreen()
    }else {
        PermissionScreen(smsPermissionState::launchPermissionRequest)
    }
}