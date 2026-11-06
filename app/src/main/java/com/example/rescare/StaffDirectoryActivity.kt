package com.example.rescare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class StaffDirectoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.staff)

        BottomNav.setup(this, BottomNav.Tab.STAFF)
    }
}
