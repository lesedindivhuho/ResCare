package com.example.rescare

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StaffDirectoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.staff)

        findViewById<TextView>(
            R.id.laundryNav
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LaundryActivity::class.java
                )
            )
        }
    }
}