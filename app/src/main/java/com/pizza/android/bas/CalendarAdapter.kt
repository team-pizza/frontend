package com.pizza.android.bas

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.provider.CalendarContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.time.Duration
import java.util.*

class CalendarAdapter(mainActivity: MainActivity) {
    private val mainActivity = mainActivity
    private var callback: ((List<Event>)->Unit)? = null
    private var backgroundThread: Thread? = null
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

    /**
     * Queries the user's calendars on a background thread for all event instances within the time frame specified.
     *
     * Permission to read from the user's calendars must be granted before this function is called!
     */
    private fun performQuery() {
        backgroundThread = Thread {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)

            val start = this.start ?: Date()
            val span = this.span ?: 0
            val callback = this.callback ?: {}

            val projection = arrayOf(CalendarContract.Instances.BEGIN,  CalendarContract.Instances.END)

            val cr = mainActivity.contentResolver

            val uriBuilder = Uri.parse(CalendarContract.Instances.CONTENT_URI.toString()).buildUpon()
            ContentUris.appendId(uriBuilder, Long.MIN_VALUE)
            ContentUris.appendId(uriBuilder, Long.MAX_VALUE)

            var result = MutableList(0) { Event(Date(), 0.0) }

            try {
                val cursor = cr.query(uriBuilder.build(), projection, null, null, null)
                while(cursor != null && cursor.moveToNext()) {
                    val begin = cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.BEGIN)).toLong()
                    val end = cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.END)).toLong()

                    val startDate = Date(begin)
                    val dateString = startDate.toString()
                    val durationInMilliseconds = end - begin
                    val durationInMinutes = durationInMilliseconds / (1000*60)



                }
            } catch (e: Exception) {


            }



            // Execute the callback from the main thread
            val mainHandler = Handler(mainActivity.mainLooper)
            mainHandler.post {
                callback(result)
            }
        }
        backgroundThread?.start()
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

data class Event(val start: Date, val duration: Double)