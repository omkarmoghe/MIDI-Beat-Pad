package com.omkarmoghe.midibeatpad.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import com.omkarmoghe.midibeatpad.R

class SettingsFragment: PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.preferences)
    }
}