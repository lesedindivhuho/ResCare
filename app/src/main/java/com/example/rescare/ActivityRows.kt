package com.example.rescare

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.firestore.DocumentSnapshot

object ActivityRows {

    fun add(
        container: LinearLayout,
        icon: String,
        title: String,
        subtitle: String,
        status: String
    ): View {
        val row = LayoutInflater.from(container.context)
            .inflate(R.layout.item_activity, container, false)

        row.findViewById<TextView>(R.id.rowIcon).text = icon
        row.findViewById<TextView>(R.id.rowTitle).text = title
        row.findViewById<TextView>(R.id.rowSubtitle).text = subtitle
        row.findViewById<TextView>(R.id.rowStatus).text = status

        container.addView(row)
        return row
    }

    fun addBooking(container: LinearLayout, booking: DocumentSnapshot) {
        val context = container.context
        add(
            container,
            "▣",
            "${booking.getString("machine").orEmpty()} • ${booking.getString("time").orEmpty()}",
            booking.getString("date").orEmpty(),
            context.getString(R.string.booking_status_confirmed)
        )
    }

    fun addMaintenanceRequest(container: LinearLayout, request: DocumentSnapshot) {
        val context = container.context
        val categoryRes = MaintenanceActivity.CATEGORIES[request.getString("category")]
            ?: R.string.category_other
        val status = when (request.getString("status")) {
            "open" -> context.getString(R.string.status_open)
            else -> request.getString("status").orEmpty()
        }

        add(
            container,
            "⚒",
            context.getString(categoryRes),
            request.getString("description").orEmpty(),
            status
        )
    }
}
