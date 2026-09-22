package com.example.rescare

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login)

        emailEditText =
            findViewById(R.id.emailEditText)

        passwordEditText =
            findViewById(R.id.passwordEditText)

        findViewById<TextView>(
            R.id.signInButton
        ).setOnClickListener {
            login()
        }

        findViewById<TextView>(
            R.id.createAccount
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }

        findViewById<TextView>(
            R.id.forgotPassword
        ).setOnClickListener {

            Toast.makeText(
                this,
                "Please contact residence administration.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun login() {

        val email =
            emailEditText.text.toString().trim()

        val password =
            passwordEditText.text.toString().trim()

        if (email.isEmpty()) {
            emailEditText.error =
                "Enter your email"
            return
        }

        if (password.isEmpty()) {
            passwordEditText.error =
                "Enter your password"
            return
        }

        val preferences =
            getSharedPreferences(
                "ResCare",
                MODE_PRIVATE
            )

        val savedEmail =
            preferences.getString("email", "")

        val savedPassword =
            preferences.getString("password", "")

        if (
            email == savedEmail &&
            password == savedPassword
        ) {

            Toast.makeText(
                this,
                "Login successful",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )

            finish()

        } else {

            Toast.makeText(
                this,
                "Incorrect email or password",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}