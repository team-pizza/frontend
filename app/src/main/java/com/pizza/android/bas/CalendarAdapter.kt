package com.pizza.android.bas

import android.Manifest
import android.content.pm.PackageManager
import android.provider.CalendarContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.time.Duration
import java.util.*

class CalendarAdapter(mainActivity: MainActivity) {
    private val mainActivity = mainActivity
    private var callback: ((List<Event>)->Unit)? = null
    private var start: Date? = null
    private var span: Double? = null

    private val READ_PERMISSION_REQUEST = 9901

    /**
     * Queries the user's default calendar for all events within [start, start+duration].
     * callback is then called when the data is ready.
     */
    fun queryCalendarEvents(start: Date, span: Double, callback: (List<Event>)->Unit) {
        // Function has already been called and is awaiting result, don't overwrite it
        if(this.callback != null || this.start!=null || this.span!=null) return

        this.callback = callback
        this.start = start
        this.span = span

        if(ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainActivity, arrayOf(Manifest.permission.READ_CALENDAR), READ_PERMISSION_REQUEST)
        } else {
            performQuery()
        }
    }

    private fun performQuery() {

    }

    fun handlePermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == READ_PERMISSION_REQUEST) {
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                performQuery()
            } else {
                // Permission was denied. Nothing we can do now. Reset callback.
                callback = null
                start = null
                span = null
            }
        }
    }
}

data class Event(val start: Date, val duration: Duration)