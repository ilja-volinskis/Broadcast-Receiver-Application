package com.example.broadcastreceiverapplication.data

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface SmsRepository {
    val messages: StateFlow<List<SmsData>>
    suspend fun addMessage(sms: SmsData)
}

@Singleton
class SmsRepositoryImpl @Inject constructor(
    @param:ApplicationScope private val scope: CoroutineScope
) : SmsRepository {

    private val _messages = MutableStateFlow<List<SmsData>>(emptyList())
    override val messages: StateFlow<List<SmsData>> = _messages

    init {
        scope.launch {
            val uid = AuthManager.ensureSignedIn()
            val ref = Firebase.database.reference.child("users").child(uid).child("messages")
            ref.observeFlow().collect { snapshot ->
                _messages.value = snapshot.children.mapNotNull { it.getValue(SmsData::class.java) }
            }
        }
    }

    override suspend fun addMessage(sms: SmsData) {
        val uid = AuthManager.ensureSignedIn()
        Firebase.database.reference.child("users").child(uid).child("messages").push().setValue(sms).await()
    }
}

private fun DatabaseReference.observeFlow(): Flow<DataSnapshot> = callbackFlow {
    val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) { trySend(snapshot) }
        override fun onCancelled(error: DatabaseError) { close(error.toException()) }
    }
    addValueEventListener(listener)
    awaitClose { removeEventListener(listener) }
}