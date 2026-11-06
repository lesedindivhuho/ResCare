package com.example.rescare

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        findViewById<TextView>(R.id.signInButton).setOnClickListener {
            login()
        }

        // Google Sign-In button
        findViewById<com.google.android.gms.common.SignInButton>(R.id.googleSignInButton).setOnClickListener {
            signInWithGoogle()
        }

        findViewById<TextView>(R.id.createAccount).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        findViewById<TextView>(R.id.forgotPassword).setOnClickListener {
            Toast.makeText(this, getString(R.string.contact_admin), Toast.LENGTH_LONG).show()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, getString(R.string.google_sign_in_failed, e.message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val idToken = account.idToken
        if (idToken == null) {
            Toast.makeText(this, getString(R.string.google_no_token), Toast.LENGTH_SHORT).show()
            return
        }

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener(this) { result ->
                ensureGoogleProfile(result.user!!.uid, account)
            }
            .addOnFailureListener(this) { e ->
                Toast.makeText(this, getString(R.string.auth_failed, e.message), Toast.LENGTH_LONG).show()
            }
    }

    private fun ensureGoogleProfile(uid: String, account: GoogleSignInAccount) {
        val userDoc = FirebaseFirestore.getInstance().collection("users").document(uid)

        userDoc.get()
            .addOnSuccessListener(this) { snapshot ->
                if (snapshot.exists()) {
                    goToHome(getString(R.string.google_login_successful))
                    return@addOnSuccessListener
                }

                val profile = hashMapOf(
                    "firstName" to (account.givenName ?: ""),
                    "surname" to (account.familyName ?: ""),
                    "email" to (account.email ?: ""),
                    "phone" to "",
                    "room" to "",
                    "building" to "",
                    "provider" to "google",
                    "createdAt" to FieldValue.serverTimestamp()
                )

                userDoc.set(profile)
                    .addOnSuccessListener(this) { goToHome(getString(R.string.google_login_successful)) }
                    .addOnFailureListener(this) { e ->
                        Toast.makeText(this, getString(R.string.profile_save_failed, e.message), Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener(this) { e ->
                Toast.makeText(this, getString(R.string.profile_load_failed, e.message), Toast.LENGTH_LONG).show()
            }
    }

    private fun goToHome(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun login() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString()

        if (email.isEmpty()) {
            emailEditText.error = getString(R.string.error_enter_email)
            return
        }

        if (password.isEmpty()) {
            passwordEditText.error = getString(R.string.error_enter_password)
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(this) { goToHome(getString(R.string.login_successful)) }
            .addOnFailureListener(this) {
                Toast.makeText(this, getString(R.string.login_incorrect), Toast.LENGTH_SHORT).show()
            }
    }
}