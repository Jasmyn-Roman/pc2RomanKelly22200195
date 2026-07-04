package com.example.pc2romankelly22200195.data.remote

import com.example.pc2romankelly22200195.dat.model.ConversionModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

object FirestoreManager {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun saveConversion(
        uid: String, amount: Double,
        fromCurrency: String, toCurrency: String, result: Double
    ): Result<Unit> {
        return try {
            val date = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
            val data = hashMapOf(
                "uid" to uid,
                "amount" to amount,
                "fromCurrency" to fromCurrency,
                "toCurrency" to toCurrency,
                "result" to result,
                "date" to date
            )
            firestore.collection("conversions").add(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getConversions(uid: String): Result<List<ConversionModel>> {
        return try {
            val snapshot = firestore.collection("conversions")
                .whereEqualTo("uid", uid)
                .get().await()
            val list = snapshot.documents.mapNotNull { it.toObject(ConversionModel::class.java) }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}