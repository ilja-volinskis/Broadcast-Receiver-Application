package com.example.broadcastreceiverapplication.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

object AuthManager {

    private val auth: FirebaseAuth = Firebase.auth

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun ensureSignedIn(): String {
        val existing = auth.currentUser
        if (existing != null) return existing.uid

        val result = auth.signInAnonymously().await()
        return result.user?.uid
            ?: throw IllegalStateException("Anonymous sign-in succeeded but returned no user")
    }
}
