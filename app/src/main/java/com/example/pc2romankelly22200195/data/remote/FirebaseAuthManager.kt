package com.example.pc2romankelly22200195.data.remote

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

object FirebaseAuthManager {
    private val auth = FirebaseAuth.getInstance()

    suspend fun loginUser(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() { auth.signOut() }
    fun getCurrentUid(): String? = auth.currentUser?.uid
    fun isLoggedIn(): Boolean = auth.currentUser != null
}