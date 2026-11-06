package com.example.rescare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val signedIn = UserProfile.currentUser() != null

        startActivity(
            Intent(
                this,
                if (signedIn) HomeActivity::class.java else LoginActivity::class.java
            )
        )

        finish()
    }
}
