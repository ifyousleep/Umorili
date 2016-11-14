package com.ifyou.umorili.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.ListPreference
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.View
import com.ifyou.umorili.R
import com.ifyou.umorili.SettingsActivity
import com.ifyou.umorili.appContext
import com.ifyou.umorili.utils.setNightMode

class PreferenceFragment : PreferenceFragmentCompat() {

    private lateinit var theme: ListPreference
    private lateinit var font: ListPreference
    private lateinit var count: ListPreference
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        val PREF_THEME = "pref_theme"
        val PREF_FONT = "pref_font"
        val PREF_COUNT = "pref_count"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = preferenceManager.sharedPreferences
        sharedPreferences.registerOnSharedPreferenceChangeListener(changeListener)
        theme = findPreference(PREF_THEME) as ListPreference
        theme.summary = theme.entry
        font = findPreference(PREF_FONT) as ListPreference
        font.summary = font.entry
        count = findPreference(PREF_COUNT) as ListPreference
        count.summary = count.entry
    }

    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when (key) {
            PREF_THEME -> {
                theme.summary = theme.entry
                setNightMode(appContext)
                if (activity is SettingsActivity) (activity as SettingsActivity).recreate()
            }
            PREF_FONT -> {
                font.summary = font.entry
                if (activity is SettingsActivity) (activity as SettingsActivity).recreate()
            }
            PREF_COUNT -> {
                count.summary = count.entry
                if (activity is SettingsActivity) (activity as SettingsActivity).recreate()
            }
        }
    }
}