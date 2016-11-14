package com.ifyou.umorili.utils

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatDelegate
import android.support.v7.preference.PreferenceManager
import android.util.TypedValue
import com.ifyou.umorili.R

fun setNightMode(context: Context?) {
    val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val theme = sharedPreferences.getString("pref_theme", "0")
    when (theme.toInt()) {
        0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
        3 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}

fun isNight(context: Context?): Boolean {
    val outValue = TypedValue()
    context?.theme?.resolveAttribute(R.attr.metaThemeName, outValue, true)
    val isNight = "night" == outValue.string
    return isNight
}