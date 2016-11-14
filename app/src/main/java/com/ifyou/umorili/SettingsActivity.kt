package com.ifyou.umorili

import android.annotation.SuppressLint
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.ifyou.umorili.fragments.PreferenceFragment
import com.ifyou.umorili.utils.isNight
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            if (!isNight(this)) {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decor.systemUiVisibility = 0
            }
        }

        setContentView(R.layout.activity_settings)

        toolbar.title = getString(R.string.settings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, PreferenceFragment()).commit()
    }

    @SuppressLint("PrivateResource")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
