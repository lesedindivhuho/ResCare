package com.example.rescare

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.ConfigurationCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LaundryActivity : AppCompatActivity() {

    private data class Machine(
        val id: Int,
        val icon: String,
        val name: String,
        val status: String,
        val inUse: Boolean = false
    )

    private var selectedDate = ""
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

        summaryTime.text = getString(R.string.summary_time, getString(R.string.select_a_slot))
        summaryMachine.text = getString(R.string.summary_machine, getString(R.string.select_a_machine))

        selectedDateView = findViewById(R.id.dateToday)

        setupDates()
        setupTimes()
        setupMachines()
        setupConfirmButton()
    }

    private fun setupDates() {

        val locale = ConfigurationCompat.getLocales(resources.configuration)[0]
            ?: Locale.getDefault()
        val dayName = SimpleDateFormat("EEE", locale)
        val dayNumber = SimpleDateFormat("d", locale)
        val monthName = SimpleDateFormat("MMM", locale)
        val summaryFormat = SimpleDateFormat("EEE d MMM", locale)

        val dateIds = listOf(
            R.id.dateToday,
            R.id.date24,
            R.id.date25,
            R.id.date26,
            R.id.date27,
            R.id.date28
        )

        val calendar = Calendar.getInstance()

        dateIds.forEachIndexed { index, id ->

            val date = calendar.time
            val dateView = findViewById<TextView>(id)
            val label = summaryFormat.format(date)

            val topLine = if (index == 0) getString(R.string.today) else dayName.format(date)
            dateView.text = "$topLine\n${dayNumber.format(date)}\n${monthName.format(date)}"

            if (index == 0) {
                selectedDate = label
            }

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
                    dateView.setTextColor(
                        ContextCompat.getColor(this, R.color.green_text)
                    )
                    selectedDateView = dateView

                    selectedDate = label

                    summaryDate.text = getString(R.string.summary_date, selectedDate)
                }

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        summaryDate.text = getString(R.string.summary_date, selectedDate)
    }

    private fun setupTimes() {

        val times = linkedMapOf(
            R.id.time0809 to "08:00 – 09:00",
            R.id.time0910 to "09:00 – 10:00",
            R.id.time1011 to "10:00 – 11:00",
            R.id.time1112 to "11:00 – 12:00",
            R.id.time1213 to "12:00 – 13:00",
            R.id.time1314 to "13:00 – 14:00",
            R.id.time1415 to "14:00 – 15:00",
            R.id.time1516 to "15:00 – 16:00",
            R.id.time1617 to "16:00 – 17:00",
            R.id.time1718 to "17:00 – 18:00",
            R.id.time1819 to "18:00 – 19:00",
            R.id.time1920 to "19:00 – 20:00"
        )

        val bookedTimes = setOf(R.id.time1112, R.id.time1617)

        for ((id, time) in times) {

            val timeView = findViewById<TextView>(id)
            val booked = id in bookedTimes

            timeView.text = "$time\n" + getString(
                if (booked) R.string.slot_booked else R.string.slot_available
            )

            if (booked) {
                continue
            }

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

                    selectedTime = time

                    summaryTime.text = getString(R.string.summary_time, selectedTime)
                }
        }
    }

    private fun setupMachines() {

        val washer = getString(R.string.machine_washer)
        val dryer = getString(R.string.machine_dryer)
        val inUse = getString(R.string.machine_in_use)

        val machines = listOf(
            Machine(R.id.washer1, "▣", getString(R.string.washer_number, 1), washer),
            Machine(R.id.washer2, "▣", getString(R.string.washer_number, 2), washer),
            Machine(R.id.washer3, "▣", getString(R.string.washer_number, 3), washer),
            Machine(R.id.washer4, "▣", getString(R.string.washer_number, 4), inUse, inUse = true),
            Machine(R.id.dryer1, "♨", getString(R.string.dryer_number, 1), dryer),
            Machine(R.id.dryer2, "♨", getString(R.string.dryer_number, 2), dryer)
        )

        for (machine in machines) {

            val machineView = findViewById<TextView>(machine.id)

            machineView.text = "${machine.icon}  ${machine.name}\n     ${machine.status}"

            if (machine.inUse) {
                continue
            }

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

                    selectedMachine = machine.name

                    summaryMachine.text = getString(R.string.summary_machine, selectedMachine)
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
                    getString(R.string.toast_select_slot),
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (selectedMachine.isEmpty()) {

                Toast.makeText(
                    this,
                    getString(R.string.toast_select_machine),
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            bookingText.text =
                "$selectedDate\n$selectedTime • $selectedMachine"

            Toast.makeText(
                this,
                getString(R.string.booking_confirmed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
