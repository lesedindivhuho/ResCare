package com.example.rescare

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val firstName =
            findViewById<EditText>(R.id.firstName)

        val surname =
            findViewById<EditText>(R.id.surname)

        val email =
            findViewById<EditText>(R.id.email)

        val phone =
            findViewById<EditText>(R.id.phone)

        val room =
            findViewById<EditText>(R.id.roomNumber)

        val building =
            findViewById<EditText>(R.id.building)

        val password =
            findViewById<EditText>(R.id.password)

        findViewById<TextView>(
            R.id.createAccountButton
        ).setOnClickListener {

            if (
                firstName.text.toString().trim().isEmpty() ||
                surname.text.toString().trim().isEmpty() ||
                email.text.toString().trim().isEmpty() ||
                phone.text.toString().trim().isEmpty() ||
                room.text.toString().trim().isEmpty() ||
                building.text.toString().trim().isEmpty() ||
                password.text.toString().trim().isEmpty()
            ) {

                Toast.makeText(
                    this,
                    getString(R.string.complete_all_fields),
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (password.text.toString().length < 6) {
                password.error = getString(R.string.password_too_short)
                return@setOnClickListener
            }

            val createButton = it
            createButton.isEnabled = false

            val auth = FirebaseAuth.getInstance()

            auth.createUserWithEmailAndPassword(
                email.text.toString().trim(),
                password.text.toString()
            ).addOnSuccessListener { result ->

                val profile = hashMapOf(
                    "firstName" to firstName.text.toString().trim(),
                    "surname" to surname.text.toString().trim(),
                    "email" to email.text.toString().trim(),
                    "phone" to phone.text.toString().trim(),
                    "room" to room.text.toString().trim(),
                    "building" to building.text.toString().trim(),
                    "provider" to "password",
                    "createdAt" to FieldValue.serverTimestamp()
                )

                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(result.user!!.uid)
                    .set(profile)
                    .addOnSuccessListener {

                        Toast.makeText(
                            this,
                            getString(R.string.account_created),
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(
                            Intent(
                                this,
                                HomeActivity::class.java
                            )
                        )

                        finishAffinity()
                    }
                    .addOnFailureListener { e ->

                        createButton.isEnabled = true

                        Toast.makeText(
                            this,
                            getString(R.string.profile_save_failed, e.message),
                            Toast.LENGTH_LONG
                        ).show()
                    }

            }.addOnFailureListener { e ->

                createButton.isEnabled = true

                Toast.makeText(
                    this,
                    getString(R.string.registration_failed, e.message),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        findViewById<LinearLayout>(
            R.id.backToSignIn
        ).setOnClickListener {

            finish()
        }
    }
}