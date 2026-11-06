package com.example.rescare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class HomeActivity : AppCompatActivity() {

    private lateinit var recentActivityContainer: LinearLayout
    private lateinit var noRecentActivityText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.home)

        BottomNav.setup(this, BottomNav.Tab.HOME)

        recentActivityContainer = findViewById(R.id.recentActivityContainer)
        noRecentActivityText = findViewById(R.id.noRecentActivityText)

        findViewById<TextView>(R.id.greetingText).text = getString(greetingForTimeOfDay())

        findViewById<TextView>(
            R.id.settingsButton
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        }

        findViewById<LinearLayout>(
            R.id.residenceCard
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
                )
            )
        }

        val quickActions = mapOf(
            R.id.quickLaundry to BottomNav.Tab.LAUNDRY,
            R.id.quickMaintenance to BottomNav.Tab.MAINTENANCE,
            R.id.quickStaff to BottomNav.Tab.STAFF,
            R.id.quickEmergency to BottomNav.Tab.EMERGENCY
        )

        for ((id, tab) in quickActions) {

            findViewById<LinearLayout>(id).setOnClickListener {

                BottomNav.open(this, BottomNav.Tab.HOME, tab)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        loadProfile()
        loadRecentActivity()
    }

    private fun greetingForTimeOfDay(): Int {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        return when {
            hour < 12 -> R.string.good_morning
            hour < 17 -> R.string.good_afternoon
            else -> R.string.good_evening
        }
    }

    private fun loadProfile() {

        val user = FirebaseAuth.getInstance().currentUser ?: return
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val residenceText = findViewById<TextView>(R.id.residenceText)

        user.displayName
            ?.substringBefore(" ")
            ?.takeIf { it.isNotBlank() }
            ?.let { welcomeText.text = getString(R.string.welcome_back_name, it) }

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener(this) { profile ->

                val firstName = profile.getString("firstName").orEmpty()
                if (firstName.isNotBlank()) {
                    welcomeText.text = getString(R.string.welcome_back_name, firstName)
                }

                val room = profile.getString("room").orEmpty()
                val building = profile.getString("building").orEmpty()
                residenceText.text =
                    if (room.isNotBlank() && building.isNotBlank()) {
                        getString(R.string.room_building, room, building)
                    } else {
                        getString(R.string.room_not_set)
                    }
            }
    }

    private fun loadRecentActivity() {

        UserRecords.loadRecent(this, UserRecords.BOOKINGS, 4) { bookings ->

            UserRecords.loadRecent(this, UserRecords.MAINTENANCE, 4) { requests ->

                showRecentActivity(bookings, requests)
            }
        }
    }

    private fun showRecentActivity(
        bookings: List<DocumentSnapshot>,
        requests: List<DocumentSnapshot>
    ) {
        recentActivityContainer.removeAllViews()
        recentActivityContainer.addView(noRecentActivityText)

        // Writes that haven't reached the server yet have no timestamp, so treat them as newest.
        val items = (bookings.map { it to true } + requests.map { it to false })
            .sortedByDescending { (document, _) ->
                document.getTimestamp("createdAt") ?: Timestamp.now()
            }
            .take(4)

        noRecentActivityText.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE

        for ((document, isBooking) in items) {

            if (isBooking) {
                ActivityRows.addBooking(recentActivityContainer, document)
            } else {
                ActivityRows.addMaintenanceRequest(recentActivityContainer, document)
            }
        }
    }
}
