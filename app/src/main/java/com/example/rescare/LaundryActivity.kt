package com.example.rescare

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity

class LaundryActivity : AppCompatActivity() {

    private var selectedDate = "Sun 23 Aug"
    private var selectedTime = ""
    private var selectedMachine = ""

    private var selectedDateView: TextView? = null
    private var selectedTimeView: TextView? = null
    private var selectedMachineView: TextView? = null

    private lateinit var summaryDate: TextView
    private lateinit var summaryTime: TextView
    private lateinit var summaryMachine: TextView
    private lateinit var bookingText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.laundry)

        summaryDate = findViewById(R.id.summaryDate)
        summaryTime = findViewById(R.id.summaryTime)
        summaryMachine = findViewById(R.id.summaryMachine)
        bookingText = findViewById(R.id.bookingText)

        selectedDateView = findViewById(R.id.dateToday)

        setupDates()
        setupTimes()
        setupMachines()
        setupConfirmButton()
    }

    private fun setupDates() {

        val dates = mapOf(
            R.id.dateToday to "Sun 23 Aug",
            R.id.date24 to "Mon 24 Aug",
            R.id.date25 to "Tue 25 Aug",
            R.id.date26 to "Wed 26 Aug",
            R.id.date27 to "Thu 27 Aug",
            R.id.date28 to "Fri 28 Aug"
        )

        for ((id, date) in dates) {

            val dateView = findViewById<TextView>(id)

            dateView.setOnClickListener {

                    selectedDateView?.let { previousView ->
                        if (previousView != dateView) {
                            previousView.setBackgroundResource(R.drawable.card_background)
                            previousView.setTextColor(
                                ContextCompat.getColor(this, R.color.text_primary)
                            )
                        }
                    }

                    dateView.setBackgroundResource(R.drawable.date_selected)
                    dateView.setTextColor(android.graphics.Color.WHITE)
                    selectedDateView = dateView

                    selectedDate = date

                    summaryDate.text =
                        "Date                              $selectedDate"
                }
        }
    }

    private fun setupTimes() {

        val times = listOf(
            R.id.time0809,
            R.id.time0910,
            R.id.time1011,
            R.id.time1213,
            R.id.time1314,
            R.id.time1415,
            R.id.time1516,
            R.id.time1718,
            R.id.time1819,
            R.id.time1920
        )

        for (id in times) {

            val timeView = findViewById<TextView>(id)

            timeView.setOnClickListener {

                    selectedTimeView?.let { previousView ->
                        if (previousView != timeView) {
                            previousView.setBackgroundResource(R.drawable.card_background)
                            previousView.setTextColor(
                                ContextCompat.getColor(this, R.color.text_primary)
                            )
                        }
                    }

                    timeView.setBackgroundResource(R.drawable.date_selected)
                    timeView.setTextColor(
                        ContextCompat.getColor(this, R.color.green_text)
                    )
                    selectedTimeView = timeView

                    selectedTime =
                        timeView.text
                            .toString()
                            .substringBefore("\n")

                    summaryTime.text =
                        "Time                              $selectedTime"
                }
        }
    }

    private fun setupMachines() {

        val machines = mapOf(
            R.id.washer1 to "Washer 1",
            R.id.washer2 to "Washer 2",
            R.id.washer3 to "Washer 3",
            R.id.dryer1 to "Dryer 1",
            R.id.dryer2 to "Dryer 2"
        )

        for ((id, machine) in machines) {

            val machineView = findViewById<TextView>(id)

            machineView.setOnClickListener {

                    selectedMachineView?.let { previousView ->
                        if (previousView != machineView) {
                            previousView.setBackgroundResource(R.drawable.card_background)
                            previousView.setTextColor(
                                ContextCompat.getColor(this, R.color.text_primary)
                            )
                        }
                    }

                    machineView.setBackgroundResource(R.drawable.date_selected)
                    machineView.setTextColor(
                        ContextCompat.getColor(this, R.color.green_text)
                    )
                    selectedMachineView = machineView

                    selectedMachine = machine

                    summaryMachine.text =
                        "Machine                         $selectedMachine"
                }
        }
    }

    private fun setupConfirmButton() {

        findViewById<Button>(
            R.id.confirmBooking
        ).setOnClickListener {

            if (selectedTime.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please select a time slot",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (selectedMachine.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please select a machine",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            bookingText.text =
                "$selectedDate\n$selectedTime • $selectedMachine"

            Toast.makeText(
                this,
                "Laundry booking confirmed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}