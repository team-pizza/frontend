package com.pizza.android.bas

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import java.util.*
import android.view.MenuItem
import com.pizza.android.bas.networking.EventList
import com.pizza.android.bas.networking.NetworkAdapter

class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener, HelpFragment.OnFragmentInteractionListener, ScheduleFragment.OnFragmentInteractionListener, GroupListFragment.OnFragmentInteractionListener, HeatmapFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var googleAuth: GoogleAuth
    private lateinit var calendarAdapter: CalendarAdapter
    lateinit var networkAdapter: NetworkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.groupListFragment, R.id.scheduleFragment, R.id.helpFragment
            ), drawerLayout
        )

        googleAuth = GoogleAuth(this)
        calendarAdapter = CalendarAdapter(this)
        networkAdapter = NetworkAdapter(this)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        calendarAdapter.handlePermissionResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            googleAuth.handleActivityResult(requestCode, data)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_logout){
            findNavController(R.id.action_logout).navigate(R.id.action_helpFragment_to_loginFragment)
            return true
        }

        return false

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun requestGoogleSignIn(callback: (identity: UserIdentity)->Unit) {
        googleAuth.requestSignIn(callback)
    }

    fun queryCalendarEvents(start: Date, span: Long, callback: (EventList)->Unit) {
        calendarAdapter.queryCalendarEvents(start, span, callback)
    }

    inline fun <reified BDY, reified RES> postToBackend(path: String, body: BDY, crossinline callback: (RES?)->Unit) {
        networkAdapter.post<BDY, RES>(path, body, callback)
    }

    fun getUserIdentity(): UserIdentity? {
        return googleAuth.getIdentity()
    }
}
