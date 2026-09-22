package com.example.rescare

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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
                    "Please complete all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            getSharedPreferences(
                "ResCare",
                MODE_PRIVATE
            ).edit()
                .putString(
                    "firstName",
                    firstName.text.toString()
                )
                .putString(
                    "surname",
                    surname.text.toString()
                )
                .putString(
                    "email",
                    email.text.toString()
                )
                .putString(
                    "phone",
                    phone.text.toString()
                )
                .putString(
                    "room",
                    room.text.toString()
                )
                .putString(
                    "building",
                    building.text.toString()
                )
                .putString(
                    "password",
                    password.text.toString()
                )
                .apply()

            Toast.makeText(
                this,
                "Account created successfully",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finish()
        }

        findViewById<LinearLayout>(
            R.id.backToSignIn
        ).setOnClickListener {

            finish()
        }
    }
}