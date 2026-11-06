package com.example.rescare

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.ChipGroup

class MaintenanceActivity : AppCompatActivity() {

    companion object {
        // Firestore stores the key so requests still show in the right language after switching.
        val CATEGORIES = linkedMapOf(
            "plumbing" to R.string.category_plumbing,
            "electrical" to R.string.category_electrical,
            "furniture" to R.string.category_furniture,
            "internet" to R.string.category_internet,
            "other" to R.string.category_other
        )
    }

    private val chipCategories = mapOf(
        R.id.categoryPlumbing to "plumbing",
        R.id.categoryElectrical to "electrical",
        R.id.categoryFurniture to "furniture",
        R.id.categoryInternet to "internet",
        R.id.categoryOther to "other"
    )

    private lateinit var requestsContainer: LinearLayout
    private lateinit var noRequestsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.maintenance)

        BottomNav.setup(this, BottomNav.Tab.MAINTENANCE)

        requestsContainer = findViewById(R.id.requestsContainer)
        noRequestsText = findViewById(R.id.noRequestsText)

        val categoryGroup = findViewById<ChipGroup>(R.id.categoryGroup)
        val description = findViewById<EditText>(R.id.issueDescription)

        findViewById<TextView>(
            R.id.submitRequestButton
        ).setOnClickListener {

            val text = description.text.toString().trim()
            if (text.isEmpty()) {
                description.error = getString(R.string.describe_problem_error)
                return@setOnClickListener
            }

            val submitButton = it
            submitButton.isEnabled = false

            val request = mapOf(
                "category" to (chipCategories[categoryGroup.checkedChipId] ?: "other"),
                "description" to text,
                "status" to "open"
            )

            UserRecords.add(
                this,
                UserRecords.MAINTENANCE,
                request,
                onSuccess = {

                    submitButton.isEnabled = true
                    description.text.clear()

                    Toast.makeText(
                        this,
                        getString(R.string.request_submitted),
                        Toast.LENGTH_SHORT
                    ).show()

                    loadMyRequests()
                },
                onFailure = { e ->

                    submitButton.isEnabled = true

                    Toast.makeText(
                        this,
                        getString(R.string.request_failed, e.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }

        loadMyRequests()
    }

    private fun loadMyRequests() {

        UserRecords.loadRecent(this, UserRecords.MAINTENANCE, 10) { requests ->

            requestsContainer.removeAllViews()
            requestsContainer.addView(noRequestsText)

            noRequestsText.visibility = if (requests.isEmpty()) View.VISIBLE else View.GONE

            for (request in requests) {
                ActivityRows.addMaintenanceRequest(requestsContainer, request)
            }
        }
    }
}
