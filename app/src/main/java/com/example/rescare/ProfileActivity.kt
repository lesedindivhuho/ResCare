package com.example.rescare

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.SetOptions

class ProfileActivity : AppCompatActivity() {

    private val fields = linkedMapOf(
        R.id.profileFirstName to "firstName",
        R.id.profileSurname to "surname",
        R.id.profilePhone to "phone",
        R.id.profileRoom to "room",
        R.id.profileBuilding to "building"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.profile)

        val user = UserProfile.currentUser()
        if (user == null) {
            finish()
            return
        }

        val userDoc = UserProfile.document(user)
        val firstName = findViewById<EditText>(R.id.profileFirstName)

        findViewById<LinearLayout>(
            R.id.backButton
        ).setOnClickListener {

            finish()
        }

        findViewById<TextView>(R.id.profileEmail).text = user.email.orEmpty()
        findViewById<TextView>(R.id.profileSignInMethod).setText(
            if (UserProfile.signedInWithGoogle(user)) R.string.signed_in_with_google
            else R.string.signed_in_with_email
        )
        showName(UserProfile.fullName(null, user))

        userDoc.get()
            .addOnSuccessListener(this) { profile ->

                for ((id, key) in fields) {
                    findViewById<EditText>(id).setText(profile.getString(key).orEmpty())
                }

                showName(UserProfile.fullName(profile, user))
            }
            .addOnFailureListener(this) { e ->

                Toast.makeText(
                    this,
                    getString(R.string.profile_load_failed, e.message),
                    Toast.LENGTH_LONG
                ).show()
            }

        findViewById<TextView>(
            R.id.saveProfileButton
        ).setOnClickListener {

            if (firstName.text.toString().trim().isEmpty()) {
                firstName.error = getString(R.string.first_name_required)
                return@setOnClickListener
            }

            val saveButton = it
            saveButton.isEnabled = false

            val updates = fields.entries.associate { (id, key) ->
                key to findViewById<EditText>(id).text.toString().trim()
            } + ("email" to user.email.orEmpty())

            // Merge so fields this screen doesn't edit (provider, createdAt) are kept.
            userDoc.set(updates, SetOptions.merge())
                .addOnSuccessListener(this) {

                    Toast.makeText(
                        this,
                        getString(R.string.profile_saved),
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }
                .addOnFailureListener(this) { e ->

                    saveButton.isEnabled = true

                    Toast.makeText(
                        this,
                        getString(R.string.profile_save_failed, e.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    private fun showName(name: String) {
        findViewById<TextView>(R.id.profileName).text = name
        findViewById<TextView>(R.id.profileInitials).text = UserProfile.initials(name)
    }
}
