package com.example.rescare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class EmergencyActivity : AppCompatActivity() {

    private data class Contact(val nameRes: Int, val number: String, val icon: String)

    private val contacts = listOf(
        Contact(R.string.emergency_police, "10111", "✚"),
        Contact(R.string.emergency_ambulance_fire, "10177", "✚"),
        Contact(R.string.emergency_mobile, "112", "✆"),
        Contact(R.string.emergency_res_security, "0765433454", "⛨"),
        Contact(R.string.emergency_gbv, "0800428428", "☏")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.emergency)

        BottomNav.setup(this, BottomNav.Tab.EMERGENCY)

        val container = findViewById<LinearLayout>(R.id.emergencyContainer)

        for (contact in contacts) {

            val row = ActivityRows.add(
                container,
                contact.icon,
                getString(contact.nameRes),
                contact.number,
                getString(R.string.call)
            )

            row.findViewById<TextView>(R.id.rowIcon).apply {
                setBackgroundResource(R.drawable.bg_red_icon)
                setTextColor(ContextCompat.getColor(this@EmergencyActivity, R.color.red_text))
            }

            row.setOnClickListener {

                // ACTION_DIAL opens the dialer with the number filled in, so no call permission is needed.
                startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:${contact.number}")
                    )
                )
            }
        }
    }
}
