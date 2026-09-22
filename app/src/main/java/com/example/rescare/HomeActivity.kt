package com.example.rescare

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.home)

        findViewById<LinearLayout>(
            R.id.laundryButton
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LaundryActivity::class.java
                )
            )
        }

        findViewById<LinearLayout>(
            R.id.staffButton
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    StaffDirectoryActivity::class.java
                )
            )
        }
    }
}