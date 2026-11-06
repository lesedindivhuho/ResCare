package com.example.rescare

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

object BottomNav {

    enum class Tab(val navId: Int, val screen: Class<out Activity>) {
        HOME(R.id.navHome, HomeActivity::class.java),
        LAUNDRY(R.id.navLaundry, LaundryActivity::class.java),
        MAINTENANCE(R.id.navMaintenance, MaintenanceActivity::class.java),
        STAFF(R.id.navStaff, StaffDirectoryActivity::class.java),
        EMERGENCY(R.id.navEmergency, EmergencyActivity::class.java)
    }

    // Home stays at the bottom of the back stack and at most one other tab sits on top of it,
    // so the system back button always returns to Home.
    fun setup(activity: Activity, current: Tab) {

        for (tab in Tab.entries) {

            val item = activity.findViewById<LinearLayout>(tab.navId)
            val selected = tab == current
            val color = ContextCompat.getColor(
                activity,
                if (selected) R.color.green else R.color.text_muted
            )

            val icon = item.getChildAt(0) as TextView
            val label = item.getChildAt(1) as TextView
            icon.setTextColor(color)
            label.setTextColor(color)
            label.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)

            item.setOnClickListener {

                if (selected) {
                    return@setOnClickListener
                }

                open(activity, current, tab)
            }
        }
    }

    fun open(activity: Activity, current: Tab, target: Tab) {

        if (target != Tab.HOME) {
            activity.startActivity(Intent(activity, target.screen))
        }

        if (current != Tab.HOME) {
            activity.finish()
        }

        @Suppress("DEPRECATION")
        activity.overridePendingTransition(0, 0)
    }
}
