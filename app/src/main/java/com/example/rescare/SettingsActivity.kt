package com.example.rescare

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsActivity : AppCompatActivity() {

    private val languages = mapOf(
        R.id.languageSystem to "",
        R.id.languageEnglish to "en",
        R.id.languageSetswana to "tn",
        R.id.languageZulu to "zu",
        R.id.languageAfrikaans to "af"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings)

        findViewById<LinearLayout>(
            R.id.backButton
        ).setOnClickListener {

            finish()
        }

        val darkModeSwitch = findViewById<MaterialSwitch>(R.id.darkModeSwitch)
        darkModeSwitch.isChecked = AppSettings.isDarkMode(this)
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            AppSettings.setDarkMode(this, isChecked)
        }

        val languageGroup = findViewById<RadioGroup>(R.id.languageGroup)
        val currentLanguage = AppSettings.languageTag()
        languageGroup.check(
            languages.entries.firstOrNull { it.value == currentLanguage }?.key
                ?: R.id.languageSystem
        )
        languageGroup.setOnCheckedChangeListener { _, checkedId ->
            AppSettings.setLanguage(languages.getValue(checkedId))
        }
    }
}
