package com.example.rescare

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// Per-user data lives under users/{uid}/<collection> so the Firestore rules can limit
// each resident to their own documents.
object UserRecords {

    const val BOOKINGS = "bookings"
    const val MAINTENANCE = "maintenance"

    private fun collection(name: String): CollectionReference? {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null

        return FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection(name)
    }

    fun add(
        activity: Activity,
        name: String,
        data: Map<String, Any>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val target = collection(name)
        if (target == null) {
            onFailure(IllegalStateException(activity.getString(R.string.sign_in_again)))
            return
        }

        target.add(data + ("createdAt" to FieldValue.serverTimestamp()))
            .addOnSuccessListener(activity) { onSuccess() }
            .addOnFailureListener(activity) { onFailure(it) }
    }

    fun loadRecent(
        activity: Activity,
        name: String,
        limit: Long,
        onResult: (List<DocumentSnapshot>) -> Unit
    ) {
        val target = collection(name)
        if (target == null) {
            onResult(emptyList())
            return
        }

        target.orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .addOnSuccessListener(activity) { onResult(it.documents) }
            .addOnFailureListener(activity) { onResult(emptyList()) }
    }
}
