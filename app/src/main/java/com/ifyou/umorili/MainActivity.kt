package com.ifyou.umorili

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.ifyou.umorili.adapters.DrawerAdapter
import com.ifyou.umorili.fragments.MainFragment
import com.ifyou.umorili.utils.ClickListener
import com.ifyou.umorili.utils.RecyclerTouchListener
import com.ifyou.umorili.utils.isNight
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private var isBlackTheme = "0"
    private var isFont = "14"
    lateinit private var drawerToggle: ActionBarDrawerToggle
    private var currentType = 0
    val STORAGE_PERMISSION = 1
    private var handler: Handler = Handler {
        handleMessage(it)
    }
    lateinit var sharedPreferences: SharedPreferences

    private fun handleMessage(msg: Message): Boolean {
        if (msg.what == 5)
            recreate()
        return true
    }

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

        setContentView(R.layout.activity_main)

        val TITLES = resources.getStringArray(R.array.nav_actions)
        val ICONS = resources.obtainTypedArray(R.array.nav_imgs)
        val indentICONS = (0..ICONS.length() - 1).map { ICONS.getDrawable(it) }
        ICONS.recycle()

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val count = sharedPreferences.getString("pref_count", "20").toInt()
        isBlackTheme = sharedPreferences.getString("pref_theme", "0")
        isFont = sharedPreferences.getString("pref_font", "14")


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance("bash.im", "bash", count))
                    .commit()
        } else {
            currentType = savedInstanceState.getInt("current", 0)
        }

        toolbar.title = TITLES[currentType]
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        drawerToggle = ActionBarDrawerToggle(this, drawer_layout, R.string.drawer_open, R.string.drawer_close)
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        drawerRecyclerView.setHasFixedSize(true)
        val adapter = DrawerAdapter(TITLES, indentICONS)
        drawerRecyclerView.adapter = adapter
        drawerRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter.selected_item = currentType
        drawerRecyclerView.addOnItemTouchListener(RecyclerTouchListener(this, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                toolbar.title = TITLES[position]
                adapter.selected_item = position
                drawerRecyclerView.adapter.notifyDataSetChanged()
                drawer_layout.closeDrawer(GravityCompat.START)
                setFragment(position, count)
                currentType = position
            }
        }
        ))

        if (!hasStoragePermission())
            AlertDialog.Builder(this)
                    .setMessage(getString(R.string.permission))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, {
                        dialogInterface, _ ->
                        dialogInterface.dismiss()
                        requestStoragePermission()
                    })
                    .show()
    }

    private fun requestStoragePermission() = ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION)
    private fun hasStoragePermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            toast(R.string.thanks)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("current", currentType)
        super.onSaveInstanceState(outState)
    }

    fun setFragment(item: Int, count: Int) {
        showAppBar()
        when (item) {
            0 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance("bash.im", "bash", count))
                        .commit()
            }
            1 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance("bash.im", "abyss", count))
                        .commit()
            }
            2 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance("zadolba.li", "zadolbali", count))
                        .commit()
            }
            3 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance("anekdot.ru", "new anekdot", count))
                        .commit()
            }
            4 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance("anekdot.ru", "new story", count))
                        .commit()
            }
            5 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance("anekdot.ru", "new aforizm", count))
                        .commit()
            }
            6 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance("anekdot.ru", "new stihi", count))
                        .commit()
            }
            7 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance("ideer.ru", "ideer", count))
                        .commit()
            }
            8 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance("det.org.ru", "Deti", count))
                        .commit()
            }
            9 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance("xkcdb.com", "XKCDB", count))
                        .commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.up -> {
                val f = supportFragmentManager.findFragmentById(R.id.container)
                if (f is MainFragment)
                    f.scrollTop()
            }
            R.id.about -> {
                AlertDialog.Builder(this)
                        .setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.ifyou))
                        .show()
                return true
            }
            R.id.settings -> {
                startActivity(Intent(this@MainActivity, SettingsActivity ::class.java))
                return true
            }
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        val onIsBlack = sharedPreferences.getString("pref_theme", "0")
        val onIsFont = sharedPreferences.getString("pref_font", "14")
        if (onIsBlack != isBlackTheme || onIsFont != isFont) {
            isBlackTheme = onIsBlack
            isFont = onIsFont
            val msg = handler.obtainMessage()
            msg.what = 5
            handler.sendMessage(msg)
        }
    }

    fun showAppBar() {
        appBarLayout.setExpanded(true, true)
    }
}
