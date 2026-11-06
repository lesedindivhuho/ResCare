package com.example.rescare

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private val languages = mapOf(
        R.id.languageSystem to "",
        R.id.languageEnglish to "en",
        R.id.languageSetswana to "tn",
        R.id.languageZulu to "zu",
        R.id.languageAfrikaans to "af"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings)

        findViewById<LinearLayout>(
            R.id.backButton
        ).setOnClickListener {

            finish()
        }

        findViewById<LinearLayout>(
            R.id.editProfileButton
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
                )
            )
        }

        findViewById<TextView>(
            R.id.signOutButton
        ).setOnClickListener {

            confirmSignOut()
        }

        val darkModeSwitch = findViewById<MaterialSwitch>(R.id.darkModeSwitch)
        darkModeSwitch.isChecked = AppSettings.isDarkMode(this)
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            AppSettings.setDarkMode(this, isChecked)
        }

        val languageGroup = findViewById<RadioGroup>(R.id.languageGroup)
        val currentLanguage = AppSettings.languageTag()
        languageGroup.check(
            languages.entries.firstOrNull { it.value == currentLanguage }?.key
                ?: R.id.languageSystem
        )
        languageGroup.setOnCheckedChangeListener { _, checkedId ->
            AppSettings.setLanguage(languages.getValue(checkedId))
        }
    }

    override fun onResume() {
        super.onResume()

        loadAccount()
    }

    private fun loadAccount() {

        val user = UserProfile.currentUser() ?: return
        val nameText = findViewById<TextView>(R.id.accountName)
        val emailText = findViewById<TextView>(R.id.accountEmail)
        val initialsText = findViewById<TextView>(R.id.accountInitials)

        fun show(name: String) {
            nameText.text = name
            initialsText.text = UserProfile.initials(name)
        }

        emailText.text = user.email.orEmpty()
        show(UserProfile.fullName(null, user))

        UserProfile.document(user)
            .get()
            .addOnSuccessListener(this) { profile ->
                show(UserProfile.fullName(profile, user))
            }
    }

    private fun confirmSignOut() {

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.sign_out_confirm_title)
            .setMessage(R.string.sign_out_confirm_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.sign_out) { _, _ -> signOut() }
            .show()
    }

    private fun signOut() {

        FirebaseAuth.getInstance().signOut()

        // Without this, Google silently picks the same account on the next sign-in.
        GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()

        startActivity(
            Intent(
                this,
                LoginActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }
}
