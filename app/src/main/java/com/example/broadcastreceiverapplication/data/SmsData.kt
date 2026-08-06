package com.example.broadcastreceiverapplication.data

data class SmsData(
    val sender: String,
    val body: String,
    val timestamp: Long
)
