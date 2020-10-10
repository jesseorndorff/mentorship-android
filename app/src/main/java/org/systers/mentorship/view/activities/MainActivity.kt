package org.systers.mentorship.view.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.systers.mentorship.R
import org.systers.mentorship.utils.GpsTracker
import org.systers.mentorship.utils.PreferenceManager
import org.systers.mentorship.view.fragments.*

/**
 * This activity has the bottom navigation which allows the user to switch between fragments
 */
class MainActivity : BaseActivity() {

    private var atHome = true

    private val TOAST_DURATION = 4000
    private var mLastPress: Long = 0
    private lateinit var exitToast: Toast
    var current_lattude: String? = null
    var current_longitude : String?=null
    private val preferenceManager: PreferenceManager = PreferenceManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        try {
            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (savedInstanceState == null) {
            showHomeFragment()
        } else {
            atHome = savedInstanceState.getBoolean("atHome")
        }
    }

    override fun onResume() {
        super.onResume()
        getLocation()

    }

    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        replaceFragment(R.id.contentFrame, HomeFragment.newInstance(),
                                R.string.fragment_title_home)
                        atHome = true
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_profile -> {
                        replaceFragment(R.id.contentFrame, ProfileFragment.newInstance(),
                                R.string.fragment_title_profile)
                        atHome = false
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_relation -> {
                        replaceFragment(R.id.contentFrame, RelationPagerFragment.newInstance(),
                                R.string.fragment_title_relation)
                        atHome = false
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_members -> {

                        val fragment: Fragment = MembersFragment.newInstance(current_lattude!! , current_longitude!!)

                        replaceFragment(R.id.contentFrame, fragment,
                                R.string.fragment_title_members)
                        atHome = false
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_requests -> {
                        replaceFragment(R.id.contentFrame, RequestsFragment.newInstance(),
                                R.string.fragment_title_requests)
                        atHome = false
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.menu_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }

    private fun showHomeFragment() {
        atHome = true
        bottomNavigation.selectedItemId = R.id.navigation_home
        replaceFragment(R.id.contentFrame, HomeFragment.newInstance(), R.string.fragment_title_home)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("atHome", atHome)
    }

    private fun showToast() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mLastPress > TOAST_DURATION) {
            exitToast = Toast.makeText(baseContext, getString(R.string.exit_toast), Toast.LENGTH_LONG)
            exitToast.show()
            mLastPress = currentTime
        } else {
            exitToast.cancel() // Hide toast on App exit
            super.onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (!atHome) {
            showHomeFragment()
        } else {
            showToast()
        }
    }

    fun getLocation() {
        val gpsTracker = GpsTracker(this@MainActivity)
        if (gpsTracker.canGetLocation()) {
            val latitude: Double = gpsTracker.latitude
            val longitude: Double = gpsTracker.longitude
            current_lattude = latitude.toString()
          current_longitude =(longitude.toString())

            Log.e("Latitude", current_lattude)
            Log.e("Longitude", current_longitude)

        } else {
            gpsTracker.showSettingsAlert()
        }
    }
}
