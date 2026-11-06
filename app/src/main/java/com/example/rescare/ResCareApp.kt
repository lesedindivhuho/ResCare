package com.example.rescare

import android.app.Application

class ResCareApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppSettings.applyTheme(this)
    }
}
