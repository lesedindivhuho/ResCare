package com.example.rescare

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

object UserProfile {

    fun currentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun document(user: FirebaseUser): DocumentReference =
        FirebaseFirestore.getInstance().collection("users").document(user.uid)

    fun fullName(profile: DocumentSnapshot?, user: FirebaseUser): String {
        val name = listOf(profile?.getString("firstName"), profile?.getString("surname"))
            .filterNot { it.isNullOrBlank() }
            .joinToString(" ")

        return name.ifBlank { user.displayName.orEmpty() }
    }

    fun initials(name: String): String =
        name.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifBlank { "?" }

    fun signedInWithGoogle(user: FirebaseUser): Boolean =
        user.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }
}
